/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import emengjzs.emengdb.util.byt.Slice;

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


    public void write(Slice data) throws IOException {
        data.serialize((bytes, offset, length) -> {
            try {
                write(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        });
    }


}
