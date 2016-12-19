/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class QueryKey {

    private long seq;
    private ValueType type;
    private Slice userKey;


    public QueryKey(long seq, Slice userKey) {
        this.seq = seq;
        type = ValueType.VALUE;
        this.userKey = new Slice(userKey, true);
    }

    public Slice getUserKey() {
        return userKey;
    }

    public int getKeySize() {
        return userKey.getLength();
    }

    public long getSeq() {
        return seq;
    }

    public long getSeqType() {
        return (seq << 8) | type.toByte();
    }

}
