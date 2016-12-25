/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import emengjzs.emengdb.api.EmengDB;

/**
 * Created by emengjzs on 2016/12/25.
 */
public class EmengdbImpt implements EmengDB {

    private MemTable table;


    public EmengdbImpt() {
        table = new MemTable(new InternalKeyComparator());
    }

    @Override
    public Slice get(Slice key) {
        return table.get(new LookupKey(key, table.getSeqNum())).value;
    }

    @Override
    public void put(Slice key, Slice value) {
        put(key, ValueType.VALUE, value.toBytes());
    }

    @Override
    public void del(Slice key) {
        put(key, ValueType.DELETE, new byte[0]);
    }


    private void put(Slice key, ValueType type, byte[] value) {
        long seqBase = table.getSeqNum(), seqNum = seqBase;

        table.add(seqNum ++, type, key.toBytes(), value);

        table.addSeqNum(seqNum - seqBase);
    }

}
