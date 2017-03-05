/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import emengjzs.emengdb.util.byt.Slice;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class MemTableGetResult {

    public static final int NOT_FOUND = 0;
    public static final int SUCCESS   = 1;
    public static final int DELETED   = 2;

    Slice value;
    int status;

    MemTableGetResult() {
        status = NOT_FOUND;
        this.value = null;
    }


    MemTableGetResult(int status) {
        this.status = status;
        this.value = null;
    }

    MemTableGetResult(Slice value, int status) {
        this.value = Slice.from(value.toBytes());
        this.status = status;
    }

    boolean isSuccess() {
        return status == SUCCESS;
    }
}


