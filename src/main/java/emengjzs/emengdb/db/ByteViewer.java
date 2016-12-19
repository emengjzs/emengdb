/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/8/31.
 */
public class ByteViewer {

    private byte[] bytes;
    final int offset;
    final int length;


    public ByteViewer(byte[] bytes, int offset, int length) {
        this.bytes = bytes;
        this.offset = offset;
        this.length = length;
    }

}
