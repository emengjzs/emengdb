/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by emengjzs on 2017/1/1.
 */
public class AtomicIntegerThresholdMarker extends ThresholdMarker {

    private AtomicInteger integer;

    public AtomicIntegerThresholdMarker(int threshold) {
        super(threshold);
        integer = new AtomicInteger(0);
    }

    @Override
    public void reset() {
        integer.set(0);
    }

    @Override
    public int get() {
        return integer.get();
    }

    @Override
    public void increase(int size) {
        integer.addAndGet(size);
    }

    @Override
    public int increaseAndGet(int size) {
        return integer.addAndGet(size);
    }
}
