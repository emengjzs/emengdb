/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import com.google.common.base.Preconditions;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import emengjzs.emengdb.api.EmengDB;
import emengjzs.emengdb.log.LogWriter;
import emengjzs.emengdb.util.byt.Slice;
import emengjzs.emengdb.util.io.MmapWriterableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by emengjzs on 2016/12/25.
 */
public class EmengdbImpt implements EmengDB {
    // debug logger
    private static Logger LOGGER = LoggerFactory.getLogger(EmengdbImpt.class);

    private static int MAX_GROUP_SIZE = 1 << 20; // 2MB

    private MemTable table;
    private LogWriter logWriter;

    private Queue<WriteTask> writersQueue;
    private Lock writeTaskMutex;
    private Condition readyForWriteTask;
    private ByteArrayDataOutput tempGroupWriteBatch;

    public EmengdbImpt() throws IOException {
        table = new MemTable(new InternalKeyComparator());
        writersQueue = new ConcurrentLinkedQueue<>();
        writeTaskMutex = new ReentrantLock();
        readyForWriteTask = writeTaskMutex.newCondition();
        tempGroupWriteBatch = ByteStreams.newDataOutput();
        logWriter = new LogWriter(new MmapWriterableFile("emengdb.log", 0));
    }

    @Override
    public Slice get(Slice key) {
        return table.get(new LookupKey(key, table.getSeqNum())).value;
    }

    @Override
    public void put(Slice key, Slice value) {
        put(key, ValueType.VALUE, value.toBytes());
    }

    @Override
    public void del(Slice key) {
        put(key, ValueType.DELETE, new byte[0]);
    }


    private void put(Slice key, ValueType type, byte[] value) {
        long seqBase = table.getSeqNum(), seqNum = seqBase;

        table.add(seqNum ++, type, key.toBytes(), value);

        table.addSeqNum(seqNum - seqBase);
    }

    public OperationResult write(WriteBatch writeBatch) {
        Preconditions.checkNotNull(writeBatch);
        WriteTask writeTask = new WriteTask(writeBatch);
        writersQueue.offer(writeTask);
        try {
            writeTaskMutex.lock();
            // while the task is not done by others and not on the first
            while ( (!writeTask.done) && writersQueue.peek() != writeTask) {
                readyForWriteTask.await();
            }
            if (writeTask.done) {
                return writeTask.result;
            }
            else {
                Preconditions.checkArgument(writeTask == writersQueue.poll());
                // Preconditions.checkArgument(tempGroupWriteBatch.getDataCount() == 0
                //   , "tempGroupWriteBatch should be empty between each write!");
                for (Iterator<WriteTask> itr = writersQueue.iterator();
                     (! writeTask.isOverMaxSize()) && itr.hasNext(); ){
                    WriteTask task = itr.next();
                    // tempGroupWriteBatch.add(task.writeBatch);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("Write Thread should not be interrupted!");
            LOGGER.error("Data: " + writeTask.writeBatch.contents.toString());
        } finally {
            writeTaskMutex.unlock();
        }

        // temporally unlock the mutex
        // doWrite(tempGroupWriteBatch);
        return null;
    }

    private void appendWriteBatch(WriteBatch writeBatch) {

    }


    private void doWrite(WriteBatch writeBatch) {

    }

    /**
     * Encode as:
     * +----------+----------+---------------+------------+---------------+--------------+-----
     * |   seq    |   count  |   key1.length | key1.value | value2.length | value2.value | ...
     * +----------+----------+---------------+------------+---------------+--------------+-----
     * |    8     |     4    |       4       | key1.length|       4       | value2.length|
     * -----------------------------------------------------------------------------------
     *
     * @param writeBatch the contents to be encoded
     * @return Slice byte array;
     */
    private Slice encodeWriteBatch(WriteBatch writeBatch) {
        tempGroupWriteBatch.writeLong(table.getSeqNum());
        tempGroupWriteBatch.writeInt(writeBatch.getDataCount());
        writeBatch.contents.forEach(kv -> {
            tempGroupWriteBatch.writeInt(kv.getKey().length);
            tempGroupWriteBatch.write(kv.getKey());
            tempGroupWriteBatch.writeInt(kv.getValue().length);
            tempGroupWriteBatch.write(kv.getValue());
        });
        // return new Slice(tempGroupWriteBatch.toByteArray());
        return null;
    }

    class WriteTask {



        final WriteBatch writeBatch;
        boolean done;
        OperationResult result;
        WriteTask(WriteBatch writeBatch) {
            this.writeBatch = writeBatch;
            this.done = false;
        }

        boolean isOverMaxSize() {
            return writeBatch.getDataSize() > MAX_GROUP_SIZE || writeBatch.getDataSize() < 0;
        }

    }

    class OperationResult {

    }

}
