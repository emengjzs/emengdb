/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import emengjzs.emengdb.db.Slice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static emengjzs.emengdb.log.LogFileException.Type.RECORD_DATA_ERRPR;

/**
 * Created by emengjzs on 2016/9/3.
 */

/**
 * Need external synchronization to be thread safe
 */
public class LogReader extends LogFormat {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private LogErrorListener logErrorListener;

    private RandomAccessFile randomAccessFile;
    private long initialOffset;
    private ByteBuffer bf;

    LogReader(RandomAccessFile file, long initialOffset) {
        this.randomAccessFile = file;
        this.initialOffset = initialOffset;
        bf = ByteBuffer.allocate(K_BLOCK_SIZE);
        seekToInitBlock();
    }

    private void seekToInitBlock() {
        long inBlockOffSet = initialOffset % K_BLOCK_SIZE;
        long blockOffSet = initialOffset - inBlockOffSet;
        try {
            randomAccessFile.seek(blockOffSet);
            readNextBlock(bf);
        } catch (IOException e) {
            log.error("InitialOffset overlap.");
        }
    }



    Slice readNextData() throws LogFileException {
        ByteArrayOutputStream byteBuilder = new ByteArrayOutputStream(64);
        RecordType type;
        Slice slice;
        boolean inReading = false;

        while (true) {
            type = readNextRecord(bf, byteBuilder);

            switch (type) {
                case FIRST_TYPE: {
                    if ( ! inReading) {
                        inReading = true;
                    }
                    else {
                        throw new LogFileException(LogFileException.Type.RECORD_DATA_ERRPR);
                    }
                    break;
                }

                case FULL_TYPE: {
                    if (! inReading) {
                        inReading = true;
                        return new Slice(byteBuilder.toByteArray());
                    }
                    else {
                        throw new LogFileException(LogFileException.Type.RECORD_DATA_ERRPR);
                    }
                }

                case MIDDLE_TYPE: {
                    if (! inReading) {
                        throw new LogFileException(LogFileException.Type.RECORD_DATA_ERRPR);
                    }
                    break;
                }

                case LAST_TYPE: {
                    if (! inReading) {
                        throw new LogFileException(LogFileException.Type.RECORD_DATA_ERRPR);
                    }
                    return new Slice(byteBuilder.toByteArray());
                }

                default: {
                    throw new LogFileException(LogFileException.Type.UNKNOWN_RECORD_TYPE_ERROR);
                }

            }

        }


    }


    private RecordType readNextRecord(ByteBuffer bf, ByteArrayOutputStream byteBuilder) throws LogFileException {
        try {
            // skip short dummy zero
            if (bf.remaining() < K_HEADER_SIZE && (! readNextBlock(bf))) {
                return RecordType.EOF;
            }
            // CRC omit
            int crc = bf.getInt();
            int length = Short.toUnsignedInt(bf.getShort());
            RecordType type = RecordType.of(bf.get());

            if (bf.remaining() < length) {
                throw new LogFileException(RECORD_DATA_ERRPR);
            }

             /*
            if (log.isDebugEnabled()) {
                log.debug("[LOG FILE] Read: {}, {} - [{}]",
                        type.toString(),
                        length,
                        new Slice(bf.array(), bf.arrayOffset() + bf.position(), length).toByteString());
            }
            */
            for (int i = 0; i < length; i ++) {
                byteBuilder.write(bf.get());
            }
            // Slice record = new Slice(bf.array(), bf.arrayOffset() + bf.position(), length);
            // byteBuilder.writeUTF8(bf.array(), bf.arrayOffset() + bf.position(), length);



            if (crc != 0x12345678) {
                throw new LogFileException(LogFileException.Type.CRC_NOT_MATCH_ERROR);
            }


            return type;

        } catch (IOException e) {
            e.printStackTrace();
            throw new LogFileException(LogFileException.Type.UNKNOWN_IO_ERROR, e);
        }

    }

    /**
     * read the next full block data from the physical file to the buffer
     *
     * @param bf
     */
    private boolean readNextBlock(ByteBuffer bf) throws IOException {
        FileChannel channel = randomAccessFile.getChannel();

        bf.clear();
        // writeUTF8 to buffer
        int read = channel.read(bf);
        // set up to read from buffer
        bf.flip();
        return read > 0;
    }

}
