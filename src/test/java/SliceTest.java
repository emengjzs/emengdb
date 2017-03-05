/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

import emengjzs.emengdb.util.byt.Slice;
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
        Slice s = Slice.from("dddd");
        Assertions.assertThat(s.toString()).isEqualTo("dddd");
        log.debug(s.toByteString());
        log.debug(s.toString());

        s = Slice.from("好大");
        Assertions.assertThat(s.toString()).isEqualTo("好大");
        log.debug(s.toByteString());
        log.debug(s.toString());
        Assertions.assertThat(Slice.from(new byte[]{(byte) 0xFF}).compareTo(Slice.from(new byte[] {(byte) 0x00})))
                .isGreaterThan(0);
    }



    @Test
    public void testByteBufferPutLong() {
        ByteBuffer bf = ByteBuffer.allocate(8);
        bf.putLong(Long.MAX_VALUE);
        Assertions.assertThat(bf.position()).isEqualTo(8);
        log.debug(Slice.from(bf.array()).toByteString());
    }




}
