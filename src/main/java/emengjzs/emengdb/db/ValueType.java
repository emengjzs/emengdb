/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/30.
 */
public enum ValueType {
    DELETE,
    VALUE;

    byte toByte() {
        return (byte) this.ordinal();
    }

}
