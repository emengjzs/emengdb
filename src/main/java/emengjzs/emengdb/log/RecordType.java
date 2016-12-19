/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

/**
 * Created by emengjzs on 2016/9/2.
 */
public enum RecordType {



    ZERO_TYPE(0),
    FULL_TYPE(1),
    FIRST_TYPE(2),
    MIDDLE_TYPE(3),
    LAST_TYPE(4),
    EOF(5);

    int id;
    final static int NUMS = RecordType.values().length;

    static RecordType of(int id) throws LogFileException {
        if (id < 0 || id > NUMS) {
            throw new LogFileException(LogFileException.Type.UNKNOWN_RECORD_TYPE_ERROR);
        }
        return RecordType.values()[id];
    }


    RecordType(int id) {
        this.id = id;
    }

    boolean equals(int i) {
        return this.id == i;
    }
};

