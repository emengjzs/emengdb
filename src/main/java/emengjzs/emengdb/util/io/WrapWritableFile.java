/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import java.io.IOException;

/**
 * Created by emengjzs on 2016/10/14.
 */
public class WrapWritableFile extends WritableFile  {
    protected WritableFile out;

    public WrapWritableFile(WritableFile out) {
        this.out = out;
    }

    @Override
    public void sync() throws IOException {
        out.sync();
    }

    @Override
    public void write(byte val) throws IOException {
        out.write(val);
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }

    @Override
    public void close() throws IOException {
        out.close();
    }
}
