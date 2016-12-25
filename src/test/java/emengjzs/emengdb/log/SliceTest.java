/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import emengjzs.emengdb.db.StringSlice;
import emengjzs.emengdb.test.core.MyTest;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * Created by emengjzs on 2016/12/23.
 */
public class SliceTest extends MyTest {

    @Test
    public void StringSliceByteTest() throws UnsupportedEncodingException {
        String str = new String(new byte[]
                { 0x65, 0x2F, 0x63, 0x01, 0x4E, 0x0D,
                  0x65, 0x2F, 0x63, 0x01}, Charset.forName("UTF8"));
        StringSlice slice = new StringSlice(str);
        Charset utf8 = Charset.forName("utf8");
        byte[] bytes = ("支持不支持").getBytes("UnicodeBigUnmarked");
        StringSlice slice2 = new StringSlice("支持不支持");

        log.debug(toHexString(bytes));
        Assert.that(slice2.get(0)).isEqualTo(bytes[0]);
        Assert.that(slice2.get(1)).isEqualTo(bytes[1]);
        Assert.that(slice2.get(3)).isEqualTo(bytes[3]);
        Assert.that(slice2.get(4)).isEqualTo(bytes[4]);
    }

}
