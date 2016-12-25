/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.api;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import emengjzs.emengdb.db.Slice;

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
        return db.get(new Slice(key));
    }


    public Slice get(int key) {
        return db.get(new Slice(Ints.toByteArray(key)));
    }

    public Slice get(long key) {
        return db.get(new Slice(Longs.toByteArray(key)));
    }

    public String getString(String key) {
        return db.get(new Slice(key)).toString();
    }


    public String getString(int key) {
        return get(key, intKey -> new Slice(Ints.toByteArray(intKey)), Slice::toString);
    }

    public <S, T> T get(S key, Function<S, ? extends Slice> keyConvert, Function<Slice, T> targetConvert) {
        return keyConvert
                .andThen(sliceKey -> db.get(sliceKey))
                .andThen(targetConvert)
                .apply(key);
    }


    public void put(String key, String value) {
        db.put(new Slice(key), new Slice(value));
    }


    public void del(String key) {
        db.del(new Slice(key));
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
