/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb;

import emengjzs.emengdb.api.EmengDB;
import emengjzs.emengdb.api.PrimitiveEmengAdapter;
import emengjzs.emengdb.db.EmengdbImpt;
import emengjzs.emengdb.db.Slice;
import emengjzs.emengdb.test.core.MyTest;
import org.junit.Test;

/**
 * Created by emengjzs on 2016/12/25.
 */
public class MemtableTest extends MyTest {

    EmengDB db = new EmengdbImpt();
    PrimitiveEmengAdapter adapter = new PrimitiveEmengAdapter(db);

    @Test
    public void testStringMemtable() {



        String key = "董先生支不支持";
        String value = "无可奉告";

        Assert.that((Object) adapter.get(key)).isNull();

        adapter.put(key, value);
        Assert.that(adapter.getString(key)).isEqualTo(value);

        adapter.del(key);
        Assert.that((Object) adapter.get(key)).isNull();

    }


    @Test
    public void testRepeatBytesMemtable() {



        Slice keySlice = new Slice(new byte[] { 0x01, 0x02});
        Slice value = new Slice(new byte[] { 0x03, 0x04});


        for (int i = 0; i < 10000; i ++ ) {
            Assert.that((Object) adapter.get(keySlice)).isNull();

            adapter.put(keySlice, value);
            Assert.that((Object) adapter.get(keySlice)).isEqualTo(value);

            adapter.del(keySlice);
            Assert.that((Object) adapter.get(keySlice)).isNull();
        }

    }


}
