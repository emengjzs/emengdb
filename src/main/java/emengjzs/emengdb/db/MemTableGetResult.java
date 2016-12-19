/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class MemTableGetResult {

    Slice value;
    Status status;



    MemTableGetResult(Status status) {
        this.status = status;
        this.value = null;
    }

    MemTableGetResult(Slice value, Status status) {
        this.value = new Slice(value.toBytes());
        this.status = status;

    }

}

enum Status {
    SUCCESS,
    DELETED,
    NOT_FOUND;
    boolean isSuccess() {
        return this == SUCCESS;
    }
}
