/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.api;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import emengjzs.emengdb.util.byt.Slice;

import java.util.function.Function;

/**
 * Created by emengjzs on 2016/12/22.
 */

/**
 * use converter directly, it may be not efficient.
 */
public class PrimitiveEmengAdapter implements EmengDB {

    private EmengDB db;

    public PrimitiveEmengAdapter(EmengDB db) {
        this.db = db;
    }


    public Slice get(String key) {
        return db.get(Slice.from(key));
    }


    public Slice get(int key) {
        return db.get(Slice.from(Ints.toByteArray(key)));
    }

    public Slice get(long key) {
        return db.get(Slice.from(Longs.toByteArray(key)));
    }

    public String getString(String key) {
        return db.get(Slice.from(key)).toString();
    }


    public String getString(int key) {
        return get(key, intKey ->Slice.from(Ints.toByteArray(intKey)), Slice::toString);
    }

    public <S, T> T get(S key, Function<S, ? extends Slice> keyConvert, Function<Slice, T> targetConvert) {
        return keyConvert
                .andThen(sliceKey -> db.get(sliceKey))
                .andThen(targetConvert)
                .apply(key);
    }


    public void put(String key, String value) {
        db.put(Slice.from(key), Slice.from(value));
    }


    public void del(String key) {
        db.del(Slice.from(key));
    }

    @Override
    public Slice get(Slice key) {
        return db.get(key);
    }

    @Override
    public void put(Slice key, Slice value) {
        db.put(key, value);
    }

    @Override
    public void del(Slice key) {
        db.del(key);
    }
}
