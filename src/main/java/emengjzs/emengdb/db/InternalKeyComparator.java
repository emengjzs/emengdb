/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.Comparator;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class InternalKeyComparator implements Comparator<InternalKey> {

    private Comparator<Slice> userComparator;

    InternalKeyComparator(Comparator<Slice> userComparator) {
        this.userComparator = userComparator;
    }

    InternalKeyComparator() {
        this.userComparator = Slice::compareTo;
    }

    @Override
    /**
     * user_key + -> seq - -> flag -
     */
    public int compare(InternalKey internalKey1, InternalKey internalKey2) {
        int res =  userComparator.compare(internalKey1.getUserKey(), internalKey2.getUserKey());
        if (res == 0) {
            return Long.compareUnsigned(
                    internalKey2.getSeqFlag(),
                    internalKey1.getSeqFlag());
        }
        return res;
    }


    public Comparator<Slice> getUserComparator() {
        return this.userComparator;
    }
    /*
    public int compare(InternalKey o1, InternalKey o2) {
        return 0;
    }
    */
}
