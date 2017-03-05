/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */
package emengjzs.emengdb.util.byt;

public class CompositeSlice extends Slice {

    private final Slice[] slices;
    private final int length;

    CompositeSlice(Slice ...slices) {
        this.slices = slices;
        int length = 0;
        for (Slice s: slices) {
            length += s.length();
        }
        this.length = length;
    }

    @Override
    public byte get(int i) {
        if (i >= length || i < 0) {
            throw new IndexOutOfBoundsException();
        }
        return get0(i);
    }

    @Override
    protected byte get0(int i) {
        int j = 0;
        while (j < slices.length && i >= slices[j].length()) {
            i -= slices[j ++].length();
        }
        return slices[j].get0(i);
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public byte[] array() {
       return toBytes();
    }

    public int start() {
        return  0;
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[length];
        int i = 0;
        for (Slice s: slices) {
            System.arraycopy(s.array(), 0, bytes, i, s.length());
            i += s.length();
        }
        return bytes;
    }

    @Override
    public Slice subSlice(int start, int length) {
        if (start < 0 || start > this.length) {
            return new WrapSlice();
        }
        int j = 0;
        while (start >= slices[j].length()) {
            start -= slices[j ++].length();
        }
        // slices[j] is the begin of the subSlice.
        // if the subSlice is within slices[j], just return the subSlice of slices[j]
        if (slices[j].length() - start >= length) {
            return slices[j].subSlice(start, length);
        }
        // the subSlice is cross multiple slices, find the range
        int i = j;
        length -= slices[++ i].length() - start;
        while (i < slices.length && length > slices[i].length()) {
            length -= slices[i ++].length();
        }
        // [j ~ i] or [j ~ slices.length - 1]
        i = i < slices.length ? i : slices.length - 1;
        Slice[] subSlices = new Slice[i - j + 1];
        subSlices[0] = slices[j].subSlice(start);
        for (int k = 1; k < subSlices.length - 1; ++ k) {
            subSlices[k] = slices[j + k];
        }
        subSlices[subSlices.length - 1] = slices[i].subSlice(0, length);
        return new CompositeSlice(subSlices);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(length);
        for (Slice s : slices) {
            sb.append(s.toString());
        }
        return sb.toString();
    }

    public void serialize(SliceByteStreamHandler handler) {
        for (Slice s: slices) {
            s.serialize(handler);
        }
    }
}
