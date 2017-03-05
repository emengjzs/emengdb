/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */
package emengjzs.emengdb.util.byt;

import emengjzs.emengdb.db.ByteConsumer;

import java.util.Iterator;

public abstract class Slice {

    public static Slice from(byte[] bytes) {
        return new WrapSlice(bytes);
    }

    public static Slice from(byte[] bytes, int start, int length) {
        return new InternSlice(bytes, start, length);
    }

    public static Slice from(String s) {
        return new WrapSlice(s);
    }

    public static Slice from(Slice s) {
        return s;
    }

    public static CompositeSlice compose(Slice ...slices) {
        return new CompositeSlice(slices);
    }

    abstract public byte get(int i);

    abstract protected byte get0(int i);

    abstract public int length();

    abstract public int start();

    abstract public byte[] array();

    abstract public byte[] toBytes();

    abstract public Slice subSlice(int start, int length);

    abstract public void serialize(SliceByteStreamHandler handler);


    public Slice subSlice(int start) {
        return subSlice(start, length() - start);
    }

    abstract public String toString();

    public boolean isEmpty() {
        return length() == 0;
    }

    public int compareTo(Slice o) {
        int minLen = o.length() < length() ? length() : o.length();
        for (int i = 0; i < minLen; i++) {
            if (get0(i) - o.get0(i) != 0) {
                return (0xFF & get0(i)) - (0xFF & o.get0(i));
            }
        }
        return length() - o.length();
    }

    public boolean equals(Object o) {
        return o instanceof Slice && compareTo((Slice) o) == 0;
    }

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
            return get0(i);
        }
    }

    public void forEach(ByteConsumer action) {
        forEachInRange(0, length(), action);
    }

    public void forEachInRange(int i, ByteConsumer consumer) {
        forEachInRange(i, length(), consumer);
    }

    public void forEachInRange(int i, int length, ByteConsumer consumer) {
        for (int s = i; s < length; s ++) {
            consumer.accept(get0(s));
        }
    }

    public String toByteString() {
        StringBuilder stringBuilder = new StringBuilder(length() * 3);
        forEach((b) -> {
            stringBuilder.append(Integer.toHexString(b & 0xFF)).append(" ");
        });
        return stringBuilder.toString();
    }

}
