/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.Comparator;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class InternalKeyComparator implements Comparator<byte[]> {
    private InternalKeyCoder internalKeyCoder;
    private Comparator<Slice> userComparator;

    public InternalKeyComparator(Comparator<Slice> userComparator) {
        this.userComparator = userComparator;
    }

    public InternalKeyComparator() {
        this(Slice::compareTo);
    }

    public void setInternalKeyCoder(InternalKeyCoder internalKeyCoder) {
        this.internalKeyCoder = internalKeyCoder;
    }

    /**
     * user_key + -> seq - -> flag -
     */
    @Override
    public int compare(byte[] internalKey1, byte[] internalKey2) {
        Slice userKeySlice1 = internalKeyCoder.getUserKeySlice(internalKey1);
        Slice userKeySlice2 = internalKeyCoder.getUserKeySlice(internalKey2);

        /* user_key cmp(+) */
        int res = userComparator.compare(userKeySlice1, userKeySlice2);

        if (res == 0) {

            /* seq cmp(-) ,  flag cmp(-)*/
            return Long.compareUnsigned(
                    internalKeyCoder.decodeSeqAndType(internalKey2, userKeySlice1.getLength()),
                    internalKeyCoder.decodeSeqAndType(internalKey1, userKeySlice1.getLength()));
        }
        return res;
    }


    public Comparator<Slice> getUserComparator() {
        return this.userComparator;
    }

}

