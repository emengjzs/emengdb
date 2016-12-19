/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

import emengjzs.emengdb.db.Slice;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;

/**
 * Created by emengjzs on 2016/9/5.
 */
public class SliceTest {

    Logger log = LoggerFactory.getLogger(SliceTest.class);

    @Test
    public void testSlice1() {
        Slice s = new Slice("dddd");
        Assertions.assertThat(s.toString()).isEqualTo("dddd");
        log.debug(s.toByteString());
        log.debug(s.toString());

        s = new Slice("好大");
        Assertions.assertThat(s.toString()).isEqualTo("好大");
        log.debug(s.toByteString());
        log.debug(s.toString());
        Assertions.assertThat(new Slice(new byte[]{(byte) 0xFF}).compareTo(new Slice(new byte[] {(byte) 0x00})))
                .isGreaterThan(0);
    }



    @Test
    public void testByteBufferPutLong() {
        ByteBuffer bf = ByteBuffer.allocate(8);
        bf.putLong(Long.MAX_VALUE);
        Assertions.assertThat(bf.position()).isEqualTo(8);
        log.debug(new Slice(bf.array()).toByteString());
    }




}
