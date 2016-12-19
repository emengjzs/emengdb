/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import emengjzs.emengdb.db.Slice;
import emengjzs.emengdb.util.io.MmapWriterableFile;
import emengjzs.emengdb.util.io.WritableFile;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by emengjzs on 2016/10/7.
 */
public class LogWriteTest {

    LogReader reader;
    LogWriter writer;
    WritableFile diskWritableFile;

    final String fileName = "testLog.log";
    final int turns = 10000;
    final int lengthRange = 10000;

    final String charSet = "abcdefghijklmnopqrstuvwxyz0123456789";
    Random random = new Random();

    List<String> DataSet;

    @Before
    public void init() throws IOException {
        LogFormat.K_BLOCK_SIZE = 1024;
        DataSet = new ArrayList(turns + 2);
        diskWritableFile = new MmapWriterableFile(fileName, 0);
        writer = new LogWriter(diskWritableFile);
    }

    String getRandomString(int size) {
        char ch[]  = new char[size];
        for (int i = 0; i < size; i ++) {
            ch[i] = charSet.charAt(random.nextInt(charSet.length()));
        }
        return new String(ch);
    }

    @Test

    public void write() throws IOException, LogFileException {
        for (int i = 0; i < turns; i ++) {
            String str = getRandomString(random.nextInt(lengthRange) + 1);

            DataSet.add(str);
        }
        System.out.print("data !");
        for(String str : DataSet) {
            writer.addData(new Slice(str));
        }

        System.out.print("writeUTF8 !");
        diskWritableFile.flush();
        // diskWritableFile.sync();
        diskWritableFile.close();
        System.out.print("writeUTF8 !");
        RandomAccessFile r = new RandomAccessFile(fileName, "r");
        reader = new LogReader(r, 0);
        for (String str : DataSet) {
            Assertions.assertThat(str)
                    .isEqualTo(reader.readNextData().toString());
        }

        r.close();
    }

    @After
    public void clearFile() {
        (new File(fileName)).delete();
        Assertions.assertThat((new File(fileName)).exists()).isFalse();
    }

}
