/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/12/21.
 */
public class DecodeInternalKey implements InternalKey {

    private Slice userKeySlice;
    private long seqAndType;


    public DecodeInternalKey(byte[] encodeInternalKey, InternalKeyCoder internalKeyCoder) {
        userKeySlice = internalKeyCoder.getUserKeySlice(encodeInternalKey);
        seqAndType = internalKeyCoder.decodeSeqAndType(encodeInternalKey, userKeySlice.length());
    }

    @Override
    public Slice getUserKey() {
        return userKeySlice;
    }


    @Override
    public int getKeyLength() {
        return userKeySlice.length();
    }


    @Override
    public long getSeq() {
        return seqAndType >>> 8;
    }

    @Override
    public ValueType getValueType() {
        return ValueType.of( (int) (seqAndType | 0x000F));
    }

    @Override
    public long getSeqFlag() {
        return seqAndType;
    }
}
