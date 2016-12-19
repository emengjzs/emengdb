/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util;

/**
 * Created by emengjzs on 2016/10/15.
 */
public class ArrayUtils {

    public static String toString(byte[] a, final int start, final int len) {
        if (a == null)
            return "null";
        int iMax = len - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = start; ; i++) {
            b.append(a[i]);
            if (i == iMax + start)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
