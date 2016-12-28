/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by emengjzs on 2016/12/26.
 */
public class UmmapWorker implements Closeable {

    private ExecutorService service = Executors.newSingleThreadExecutor();

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    void unMapAsync(ByteBuffer buffer) {
        service.submit(() -> {
            unmapMmaped0(buffer);
        });
    }

    void unMapSync(ByteBuffer buffer) {

        unmapMmaped0(buffer);

    }


    public void waitForFinish() throws InterruptedException {
        service.awaitTermination(60, TimeUnit.SECONDS);
    }

    private void unmapMmaped0(ByteBuffer buffer) {

        if (buffer instanceof sun.nio.ch.DirectBuffer) {
            //log.debug("Clean up ! {}",  buffer.capacity());
            sun.misc.Cleaner cleaner = ((sun.nio.ch.DirectBuffer) buffer).cleaner();
            if (cleaner != null) cleaner.clean();
            //log.debug("Clean up down !");
        }

    }


    private void exceptionCatch(Throwable e) {
        e.printStackTrace();
        log.error(e.getMessage());
        service.shutdown();
    }

    @Override
    public void close() throws IOException {
        service.shutdown();
    }
}

