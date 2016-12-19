/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util;

/**
 * Created by emengjzs on 2016/8/30.
 */
public class Converter {

    // reduce data copy
    public static int writeL(int int_, byte[] bs, int start) {
        for (int i = 0; i < 4; i ++) {
            bs[start ++ ] = (byte) (0xFF & (int_ >> ((i) << 3)));
        }
        return start;
    }


    public static int writeB(int int_, byte[] bs, int start) {
        for (int i = 0; i < 4; i ++) {
            bs[start ++ ] = (byte) (0xFF & (int_ >> ((3 - i) << 3)));
        }
        return start;
    }

    public static int writeL(long long_, byte[] bs, int start) {
        for (int i = 0; i < 8; i ++) {
            bs[start ++ ] = (byte) (0xFF & (long_ >> ((i) << 3)));
        }
        return start;
    }


    public static int writeB(long long_, byte[] bs, int start) {

        for (int i = 0; i < 8; i ++) {
            bs[start ++ ] = (byte) (0xFF & (long_ >> ((7 - i) << 3)));
        }
        return start;
    }

    // little endian
    public static byte[] convertL(int int_) {
        byte[] bs = new byte[4];
        for (int i = 0; i < 4; i ++) {
            bs[i] = (byte) (0xFF & (int_ >> ((i) << 3)));
        }
        return bs;
    }

    // big endian
    public static byte[] convertB(int int_) {
        byte[] bs = new byte[4];
        for (int i = 0; i < 4; i ++) {
            bs[i] = (byte) (0xFF & (int_ >> ((3 - i) << 3)));
        }
        return bs;
    }

    public static byte[] convertB(long long_) {
        byte[] bs = new byte[8];
        for (int i = 0; i < 8; i ++) {
            bs[i] = (byte) (0xFF & (long_ >> ((7 - i) << 3)));
        }
        return bs;
    }
}
