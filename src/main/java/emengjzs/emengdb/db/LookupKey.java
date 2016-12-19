/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.Arrays;

/**
 * Created by emengjzs on 2016/9/2.
 */
public class LookupKey implements InternalKey {


    byte[] key;
    long seqAndFlag;


    LookupKey(byte[] key, long seq) {
        this.key = Arrays.copyOf(key, key.length);
        this.seqAndFlag = seq << 8 | ValueType.VALUE.toByte();
    }

    @Override
    public Slice getUserKey() {
        return new Slice(key);
    }

    @Override
    public int getKeyLength() {
        return key.length;
    }

    @Override
    public long getSeq() {
        return seqAndFlag >>> 8;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.values()[(int) (seqAndFlag & 0xFF)];
    }

    @Override
    public long getSeqFlag() {
        return seqAndFlag;
    }
}
