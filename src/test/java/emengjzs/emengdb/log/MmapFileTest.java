/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by emengjzs on 2016/10/11.
 */
public class MmapFileTest {

    int aNum = 1 << 16;


    @Before
    public void initFile() {
        byte b[] = new byte[] { 0x31 };
        try (
        OutputStream a = (new FileOutputStream("a.txt"))) {
            for (int i = 0; i < aNum; i ++)
            a.write(b);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test() {
        try {
            RandomAccessFile fileInputStream = new RandomAccessFile("a.txt", "rw");
            FileChannel channel = fileInputStream.getChannel();
            MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, aNum);
            map.put((byte) 0x32);
            map = null;
            fileInputStream.getFD().sync();
        }catch (Exception e) {
            e.printStackTrace();
            Assertions.assertThat(false).isTrue();
        }

        try {
        FileInputStream fileInputStream = new FileInputStream(new File("a.txt"));
            byte b[] = new byte[1];
            fileInputStream.read(b);
            Assertions.assertThat(b).containsExactly((byte)0x32);
        }catch (Exception e) {
            e.printStackTrace();
            Assertions.assertThat(false).isTrue();
        }
    }
    @After
    public void clearFile() {
        (new File("a.txt")).delete();
    }
}
