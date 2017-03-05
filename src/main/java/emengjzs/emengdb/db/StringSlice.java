/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/12/22.
 *
 * Optimize for String
 */
public class StringSlice implements CharSequence, Comparable<StringSlice>, java.io.Serializable {

    private String str;


    public StringSlice(String str) {
        this.str = str;
    }



    public byte get(int i) {
        return (byte) (str.charAt(i >>> 1) >>> (8 & ((~(i & 0x01 )) << 3)));
    }

    public boolean isEmpty() {
        return str.isEmpty();
    }



    @Override
    public int length() {
        return str.length();
    }

    @Override
    public char charAt(int index) {
        return str.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return str.subSequence(start, end);
    }

    public String toString() {
        return this.str;
    }


    @Override
    public int compareTo(StringSlice o) {
        return this.str.compareTo(o.str);
    }

    public byte[] toBytes() {
        return str.getBytes();
    }

}
