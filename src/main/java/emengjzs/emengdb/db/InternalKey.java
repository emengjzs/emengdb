/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import emengjzs.emengdb.util.byt.Slice;

/**
 * Created by emengjzs on 2016/8/31.
 */
interface InternalKey {
    Slice getUserKey();
    int getKeyLength();
    long getSeq();
    ValueType getValueType();
    long getSeqFlag();
}