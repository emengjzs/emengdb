/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by emengjzs on 2016/10/14.
 */
public class OutputStreamWrapWritableFile<Output extends OutputStream> extends WritableFile {

    protected Output out;

    public OutputStreamWrapWritableFile(Output out) {
        this.out = out;
    }

    @Override
    public void sync() throws IOException {

    }

    @Override
    public void write(byte val) throws IOException {
        out.write(val);
    }

    @Override
    public void write(byte b[], int offset, int len) throws IOException {
        out.write(b, offset, len);
    }

    @Override
    public void write(byte b[]) throws IOException {
        out.write(b, 0, b.length);
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
