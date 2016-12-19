/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/30.
 */

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;

/**
 * A byte viewer, share the same byte array.
 */
public class Slice implements Iterable<Byte>, Comparable<Slice> {
    public final static Charset UTF8_CHARSET = Charset.forName("utf8");
    private byte[] bytes;
    private int start;
    private int length;

    public Slice(String s) {
        bytes = s.getBytes(UTF8_CHARSET);
        start = 0;
        length = bytes.length;
    }

    public Slice() {
        bytes = new byte[0];
        start = 0;
        length = 0;
    }

    public Slice(Slice s, boolean copy) {
        this.bytes = copy ? s.toBytes() : s.bytes;
        this.start = s.start;
        this.length = s.length;
    }

    public Slice(byte[] bytes) {
        this.bytes = bytes;
        start = 0;
        length = bytes.length;
    }

    public Slice(byte[] bytes, int start) {
        this.bytes = bytes;
        this.start = bytes.length <= start ? bytes.length : start;
        this.length = bytes.length - (this.start + 1);
    }

    public Slice(byte[] bytes, int start, int length) {
        this.bytes = bytes;
        this.start = bytes.length <= start ? bytes.length : start;
        this.length = bytes.length - this.start < length ? bytes.length - this.start : length;
    }

    public byte get(int i) {
        if (i >= length || i < 0) {
            throw new IndexOutOfBoundsException();
        }
        return bytes[start + i];
    }

    public Slice getSubSlice(int start, int length) {
        return new Slice(bytes, this.start + start, length);
    }

    public Slice getDependentSlice(int start, int length) {
        return new Slice(Arrays.copyOfRange(bytes, this.start + start, this.start + start + length));
    }

    public boolean isEmpty() {
        return this.length == 0;
    }

    public int getLength() {
        return this.length;
    }

    public String toString() {
        return new String(bytes, start, length, UTF8_CHARSET);
    }

    public String toByteString() {
        StringBuilder stringBuilder = new StringBuilder(length * 3);
        forEach((b) -> {
            stringBuilder.append(Integer.toHexString(b & 0xFF)).append(" ");
        });
        return stringBuilder.toString();
    }

    public int getStart() {
        return  start;
    }

    public byte[] array() {
        return bytes;
    }

    public byte[] toBytes() {
        return Arrays.copyOfRange(bytes, start, start + length);
    }

    public void outPut(OutputStream out) throws IOException {
        out.write(bytes, this.start, this.length);
    }

    public void outPut(ByteBuffer out) throws IOException {
        out.put(bytes, this.start, this.length);
    }

    public void outPut(ByteBuffer out, int offset, int length) throws IOException {
        out.put(bytes, this.start + offset, length);
    }


    public void clear() {
        bytes = new byte[0];
        start = 0;
        length = 0;
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Itr();
    }

    public ByteBuffer toByteBuffer(int start) {
        return ByteBuffer.wrap(bytes, this.start + start, this.length - start);
    }

    public ByteBuffer toByteBuffer(int start, int length) {
        return ByteBuffer.wrap(bytes, this.start + start, length);
    }

    public ByteBuffer toByteBuffer() {
        return ByteBuffer.wrap(bytes, start, length);
    }

    @Override
    public int compareTo(Slice o) {
        int minLen = o.length < this.length ? length : o.length;
        for (int i = 0; i < minLen; i++) {
            if (get(i) - o.get(i) != 0) {
                return (0xFF & get(i)) - (0xFF & o.get(i));
            }
        }
        return this.length - o.length;
    }


    private class Itr implements Iterator<Byte> {
        int i = start;

        Itr() {

        }

        @Override
        public boolean hasNext() {
            return i < start + length;
        }

        @Override
        public Byte next() {
            return bytes[i++];
        }
    }




    public void forEach(ByteConsumer action) {
        forEachInRange(0, length, action);
    }


    public void forEachInRange(int i, ByteConsumer consumer) {
        forEachInRange(i, length, consumer);
    }

    public void forEachInRange(int i, int length, ByteConsumer consumer) {
        for (int s = i + this.start, e = start + length; s < e; s++) {
            consumer.accept(bytes[s]);
        }
    }

}
