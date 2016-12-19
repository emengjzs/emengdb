/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import emengjzs.emengdb.db.Slice;
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
    private RandomAccessFile rw;
    private FileChannel fileChannel;
    private MappedByteBuffer mmapBuffer;
    private int mapSize = 1 << (16 - 1);
    private long fileOffset;
    private boolean isLastMapSync = true;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public MmapWriterableFile(String fileName, long offset) throws IOException {

        this.fileOffset = offset;
        rw = new RandomAccessFile(fileName, "rw");
        fileChannel = rw.getChannel();
        mmapBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, fileOffset, 0);
    }

    @Override
    public void sync() throws IOException {
        if (! isLastMapSync) {
            // this.rw.getFD().sync();
            isLastMapSync = true;
        }
        mmapBuffer.force();
    }

    @Override
    public void write(Slice data) throws IOException {
        write(data.array(), data.getStart(), data.getLength());
    }

    private void unmapCurrentMap() {
        // mmapBuffer.force();
        // asume gc will collect this.
        fileOffset += mmapBuffer.capacity();
        isLastMapSync = true;
        unmapMmaped0(mmapBuffer);
        mmapBuffer = null;
    }

    private void unmapMmaped0(ByteBuffer buffer) {

        if (buffer instanceof sun.nio.ch.DirectBuffer) {
            log.debug("Clean up ! {}",  buffer.capacity());
            sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
            if (cleaner != null) cleaner.clean();
            log.debug("Clean up down !");
        }

    }


    private void resizeMap() throws IOException {
        int MAX_MAP_SIZE = 1 << 24;
        if (mapSize < MAX_MAP_SIZE) {
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
    public void write(int b) throws IOException {
        write((byte) b);
    }

    @Override
    public void write(byte[] b) throws IOException {
        this.write(b, 0, b.length);
    }

    @Override
    public void close() throws IOException {
        // sync();
        int unused = mmapBuffer.remaining();
        unmapCurrentMap();
        rw.setLength(fileOffset - unused);
        fileChannel.close();
        rw.close();
    }

    @Override
    public void flush() throws IOException {

    }
}
