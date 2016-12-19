/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import emengjzs.emengdb.util.RandomBuilder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.CountDownLatch;

/**
 * Created by emengjzs on 2016/10/15.
 */

/**
 * Conclusion about multiple-thread reading in a file:
 * 1.  read(ByteBuffer dst, long position) use pread() in Linux so it is thread-safe.
 * 2.  In windows, it is still thread-safe and it is fast enough.
 * 3.  We can also use mmap to improve performance for multiple-thread reading.
 */
public class MutithreadReadTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    int byteLength = 1000;
    String fileName = "pread.test";
    byte[] bs;
    RandomBuilder randomBuilder = new RandomBuilder();
    @Before
    public void createFile() throws FileNotFoundException {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try(OutputStream out = new BufferedOutputStream(new FileOutputStream(fileName));
        ){
            byte[] randomBytes = randomBuilder.getRandomBytes(byteLength);
            out.write(randomBytes);
            byteArrayOutputStream.write(randomBytes);

        } catch (IOException e) {
            e.printStackTrace();
        }
        bs = byteArrayOutputStream.toByteArray();
    }


    @Test
    public void singleThreadRead() throws IOException {
        int threadCount = 1000;


        RandomAccessFile file = new RandomAccessFile(fileName, "r");
        FileChannel channel = file.getChannel();
        CountDownLatch count = new CountDownLatch(threadCount);

        Lambda.forRange(threadCount, ()->{
            new Thread( () -> {
                ByteBuffer byteBuffer = ByteBuffer.wrap(new byte[byteLength]);
                Lambda.forRange(20, ()->{
                    int start = randomBuilder.getRandomInt(byteLength >>> 1);
                    int len = randomBuilder.getRandomInt(byteLength - start);
                    byteBuffer.limit(len);
                    try {
                        //log.debug("Read Begin!");
                        channel.read(byteBuffer, start);
                        //log.debug("Read End!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    byteBuffer.flip();

                    // log.debug("Read {}", ArrayUtils.toString(bs, start, len));
                    for(int j = start; j < start + len; j ++) {
                        Assertions.assertThat(bs[j]).isEqualTo(byteBuffer.get());
                    }
                    Assertions.assertThat(byteBuffer.remaining()).isEqualTo(0);
                    byteBuffer.clear();
                });
                count.countDown();
            }).start();

        });
        try {
            count.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            channel.close();
            file.close();
        }

    }

}
