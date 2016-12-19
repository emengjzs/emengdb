/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util;

/**
 * Created by emengjzs on 2016/8/28.
 */
public class Validate {

    public static void isTrue(boolean v) {
        if (!v) {
            throw new IllegalArgumentException("Not True");
        }
    }
    public static void isTrue(boolean v, String s) {
        if (!v) {
            throw new IllegalArgumentException(s);
        }
    }
}
