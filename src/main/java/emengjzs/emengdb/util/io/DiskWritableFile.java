/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by emengjzs on 2016/10/7.
 */
public class DiskWritableFile extends OutputStreamWrapWritableFile<FileOutputStream> {

    public DiskWritableFile(String path) throws FileNotFoundException {
        super(new FileOutputStream(path));
    }

    @Override
    public void sync() throws IOException {
        out.flush();
        out.getFD().sync();
    }

}
