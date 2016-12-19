/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Created by emengjzs on 2016/10/15.
 */
public class BufferedWritableFile extends OutputStreamWrapWritableFile<BufferedOutputStream> {

    protected WritableFile writableFile;

    public BufferedWritableFile(WritableFile writableFile) {
        super(new BufferedOutputStream(writableFile));
        this.writableFile = writableFile;
    }

    @Override
    public void sync() throws IOException {
        writableFile.sync();
    }

}
