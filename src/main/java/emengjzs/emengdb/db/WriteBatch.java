package emengjzs.emengdb.db;

import emengjzs.emengdb.util.byt.Slice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2017 emengjzs. All rights reserved.
 * Use of this source code is governed by MIT license that can be
 * found in the LICENSE file.
 */
public class WriteBatch {
    List<Map.Entry<byte[], byte[]>> contents;
    int size = 0;
    long seq;



    WriteBatch() {
        contents = new ArrayList<>();
        size = 0;
    }


    public void add(WriteBatch writeBatch) {
        contents.addAll(writeBatch.contents);
        size += contents
            .stream()
            .map(kv -> kv.getValue().length + kv.getKey().length)
            .reduce(0, (a, b) -> a + b);
    }

    public void delete(Slice key) {

    }

    public void add(Slice key, Slice value) {


    }


    public int getDataSize() {
        return size;
    }

    public int getDataCount() {
        return contents.size();
    }

    Iterator<Map.Entry<byte[], byte[]>> iterator() {
        return contents.iterator();
    }

}
