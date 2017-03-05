/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.byt;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A byte viewer, share the same byte array.
 */
public class WrapSlice extends Slice implements Iterable<Byte>, Comparable<Slice> {
    final static Charset UTF8_CHARSET = Charset.forName("utf8");
    private byte[] bytes;

    WrapSlice() {
        bytes = new byte[0];
    }

    WrapSlice(String s) {
        bytes = s.getBytes(UTF8_CHARSET);
    }

    WrapSlice(byte[] bytes) {
        this.bytes = bytes;
    }

    public byte get(int i) {
        if (i >= bytes.length || i < 0) {
            throw new IndexOutOfBoundsException();
        }
        return bytes[i];
    }

    protected byte get0(int i) {
        assert i < bytes.length && i >= 0;
        return bytes[i];
    }

    /**
     * @param start the begin index of sub-slice
     * @param length the length of sub-slice
     * @return Return a shared sub-slice of the slice
     */
    public Slice subSlice(int start, int length) {
        return new InternSlice(bytes, start, length > bytes.length ? bytes.length : length);
    }

    public int length() {
        return bytes.length;
    }

    public String toString() {
        return new String(bytes, UTF8_CHARSET);
    }

    public int start() {
        return  0;
    }

    public byte[] array() {
        return bytes;
    }

    public byte[] toBytes() {
        return Arrays.copyOf(bytes, bytes.length);
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<Byte> {
        int i = 0;

        @Override
        public boolean hasNext() {
            return i < length();
        }

        @Override
        public Byte next() {
            return get(i);
        }
    }


    public void serialize(SliceByteStreamHandler handler) {
        handler.handle(bytes, 0, bytes.length);
    }
}
