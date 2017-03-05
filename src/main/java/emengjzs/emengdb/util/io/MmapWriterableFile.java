/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import emengjzs.emengdb.util.byt.Slice;
import emengjzs.emengdb.util.byt.SliceByteStreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by emengjzs on 2016/10/11.
 */
public class MmapWriterableFile extends WritableFile {

    public static final int MAX_MMAP_SIZE = Integer.MAX_VALUE;

    private RandomAccessFile rw;
    private FileChannel fileChannel;
    private MappedByteBuffer mmapBuffer;
    private int mapSize = 1 << (10 - 1);
    private long fileOffset;
    private boolean isLastMapSync = true;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private UmmapWorker unMapWorker;

    private SliceByteStreamHandler sliceWriteHandler;

    public enum Flag {
        APPEND,
        TRUNCATE,

    }


    public MmapWriterableFile(String fileName, Flag flag) throws IOException {
        if (flag == Flag.APPEND)
            init(fileName, -1);
        else {
            init(fileName, 0);
        }
    }

    public MmapWriterableFile(String fileName, long offset) throws IOException {
        init(fileName, offset);
    }

    private void init(String fileName, long offset) throws IOException {
        this.fileOffset = offset;
        rw = new RandomAccessFile(fileName, "rw");
        fileChannel = rw.getChannel();
        offset = offset == -1 ? rw.length() : offset;
        mmapBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileOffset, 0);
        unMapWorker = new UmmapWorker();
        sliceWriteHandler = ByteStream.newSliceWriteHandler(this);
    }



    @Override
    public void sync() throws IOException {
        if (! isLastMapSync) {
            // this.rw.getFD().sync();
            isLastMapSync = true;
        }
        mmapBuffer.force();
    }


    private void unmapCurrentMap() {
        // mmapBuffer.force();
        // asume gc will collect this.
        fileOffset += mmapBuffer.capacity();
        isLastMapSync = true;
        currentHandler.unMap(mmapBuffer);
        mmapBuffer = null;
    }




    private void resizeMap() throws IOException {
        if (mapSize < MAX_MMAP_SIZE) {
            mapSize <<= 1;
        }
        // resize the file
        this.rw.setLength(fileOffset + mapSize);
        mmapBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileOffset, mapSize);
    }

    @Override
    public void write(byte b) throws IOException {
        if (mmapBuffer.remaining() <= 0) {
            unmapCurrentMap();
            resizeMap();
        }
        mmapBuffer.put(b);
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        int left = len;
        int offset = off;
        while (left > 0) {
            if (mmapBuffer.remaining() <= 0) {
                unmapCurrentMap();
                resizeMap();
            }
            int writable = left < mmapBuffer.remaining() ? left : mmapBuffer.remaining();
            mmapBuffer.put(b, offset, writable);
            left -= writable;
            offset += writable;
        }
    }

    @Override
    @Deprecated
    public void write(int b) throws IOException {
        write((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }


    @Override
    public void write(Slice data) throws IOException {
        data.serialize(sliceWriteHandler);
    }

    @Override
    public void close() throws IOException {
        // sync();
        int unused = mmapBuffer.remaining();
        changeHandler(true);
        unmapCurrentMap();
        rw.setLength(fileOffset - unused);
        fileChannel.close();
        rw.close();
        unMapWorker.close();
    }

    @Override
    public void flush() throws IOException {

    }


    interface UnmapHandler {
        void unMap(ByteBuffer buffer);
    }

    private UnmapHandler asyncHandler = (buffer -> unMapWorker.unMapAsync(buffer));
    private UnmapHandler syncHandler = (buffer -> unMapWorker.unMapSync(buffer));
    private UnmapHandler currentHandler = asyncHandler;


    private void changeHandler(boolean sync) {
        currentHandler = sync ? syncHandler : asyncHandler;
    }


}
