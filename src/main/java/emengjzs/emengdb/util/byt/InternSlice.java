/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */
package emengjzs.emengdb.util.byt;

import java.util.Arrays;

public class InternSlice extends Slice {
    private byte[] bytes;
    private int start;
    private int length;

    InternSlice(byte[] bytes, int start) {
        this(bytes, start, bytes.length - start);
    }

    InternSlice(byte[] bytes, int start, int length) {
        this.bytes = bytes;
        this.start = bytes.length <= start ? bytes.length : start;
        this.length = bytes.length - this.start < length ? bytes.length - this.start : length;
    }

    @Override
    public byte get(int i) {
        if (i >= length || i < 0) {
            throw new IndexOutOfBoundsException();
        }
        return bytes[start + i];
    }

    @Override
    public byte get0(int i) {
        return bytes[start + i];
    }

    @Override
    public Slice subSlice(int start, int length) {
        return new InternSlice(bytes, this.start + start, length);
    }

    @Override
    public String toString() {
        return new String(bytes, start, length);
    }

    @Override
    public boolean isEmpty() {
        return this.length == 0;
    }

    @Override
    public int length() {
        return this.length;
    }

    public int start() {
        return  start;
    }

    public byte[] array() {
        return bytes;
    }

    public byte[] toBytes() {
        return Arrays.copyOfRange(bytes, start, start + length);
    }

    public void serialize(SliceByteStreamHandler handler) {
        handler.handle(bytes, start, length);
    }

}
