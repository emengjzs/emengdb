/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by emengjzs on 2016/9/12.
 */
public class PrimitiveWritable extends WritableFile implements DataOutput {

    private WritableFile writableFile;

    public PrimitiveWritable(WritableFile writableFile) {
        this.writableFile = writableFile;
    }


    public void write(byte b) throws IOException {
        writableFile.write(b);
    }

    @Override
    public void write(int b) throws IOException {
        writableFile.write((byte) b);
    }

    public void write(byte b[]) throws IOException {
        writableFile.write(b, 0, b.length);
    }

    public void write(byte b[], int off, int len) throws IOException {
        writableFile.write(b, off, len);
    }

    public void writeBoolean(boolean val) throws IOException {
        writableFile.write((byte) (val ? 1 : 0));
    }

    @Override
    public void writeByte(int val) throws IOException {
        writableFile.write(val);
    }

    @Override
    public void writeShort(int val) throws IOException {
        writableFile.write ((byte) (val >>> 8));
        writableFile.write ((byte) (val      ));
    }

    @Override
    public void writeChar(int val) throws IOException {
        writableFile.write ((byte) (val >>> 8));
        writableFile.write ((byte) (val      ));
    }

    @Override
    public void writeInt(int val) throws IOException {
        writableFile.write( (byte) (val >>> 24));
        writableFile.write( (byte) (val >>> 16));
        writableFile.write( (byte) (val >>>  8));
        writableFile.write( (byte) (val       ));
    }

    @Override
    public void writeLong(long v) throws IOException {
        byte writeBuffer[] = new byte[8];
        writeBuffer[1] = (byte)(v >>> 48);
        writeBuffer[2] = (byte)(v >>> 40);
        writeBuffer[3] = (byte)(v >>> 32);
        writeBuffer[4] = (byte)(v >>> 24);
        writeBuffer[5] = (byte)(v >>> 16);
        writeBuffer[6] = (byte)(v >>>  8);
        writeBuffer[7] = (byte)(v       );
        writableFile.write(writeBuffer, 0, 8);
    }

    @Override
    public void writeFloat(float val) throws IOException {
        writeInt(Float.floatToIntBits(val));
    }

    @Override
    public void writeDouble(double val) throws IOException {
        writeLong(Double.doubleToLongBits(val));
    }

    @Override
    public void writeBytes(String s) throws IOException {
        int len = s.length();
        for (int i = 0 ; i < len ; i++) {
            writableFile.write((byte)s.charAt(i));
        }
    }

    @Override
    public void writeChars(String s) throws IOException {
        int len = s.length();
        byte b[] = new byte[16];
        int turns = len >>> 3;
        int i = 0, v = 0, j = 0;
        while (turns -- > 0) {
            for (j = 0; j < 16 ;) {
                v = s.charAt(i ++);
                b[j ++] = (byte) ((v >>> 8) & 0xFF);
                b[j ++] = (byte) ( v  & 0xFF);
            }
            writableFile.write(b);
        }
        for (j = 0; i < len; i ++) {
            v = s.charAt(i);
            b[j ++] = (byte) ((v >>> 8) & 0xFF);
            b[j ++] = (byte) ( v  & 0xFF);
        }
        writableFile.write(b, 0, j);
    }

    @Override
    public void writeUTF(String s) throws IOException {
        throw new IOException(new UnsupportedOperationException());
    }

    @Override
    public void sync() throws IOException {
        writableFile.sync();
    }
}
