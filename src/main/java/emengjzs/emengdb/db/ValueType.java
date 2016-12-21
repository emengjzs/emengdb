/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/30.
 */
public enum ValueType {

    DELETE(0x00),
    VALUE(0x01),
    UNKNOWN(0x02);

    final byte v;

    ValueType(int v) {
        this.v = (byte) v;
    }

    static ValueType of(int i) {
        return i > ValueType.values().length ? UNKNOWN : ValueType.values()[i];
    }


    byte toByte() {
        return v;
    }

    boolean equals(byte v) {
        return this.v == v;
    }
}
