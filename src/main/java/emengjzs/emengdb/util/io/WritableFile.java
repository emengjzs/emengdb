/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import emengjzs.emengdb.db.Slice;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by emengjzs on 2016/9/3.
 */
public abstract class WritableFile extends OutputStream {

    abstract void sync() throws IOException;

    abstract void write(byte val) throws IOException;

    /**
     * use  write(byte val) instead
     * @param val
     * @throws IOException
     */
    @Deprecated
    public void write(int val) throws IOException {
        write((byte) val);
    }


    void write(Slice data) throws IOException {
        write(data.array(), data.getStart(), data.getLength());
    }


}
