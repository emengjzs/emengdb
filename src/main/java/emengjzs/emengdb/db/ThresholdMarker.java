/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2017/1/1.
 */
public abstract class ThresholdMarker {

    protected final int threshold;

    public ThresholdMarker(int threshold) {
        this.threshold = threshold;
    }

    public abstract void reset();

    public boolean isReach() {
        return get() >= threshold || get() < 0; // over-value
    }

    public abstract int get();

    public void increase() {
        increase(1);
    }

    public abstract void increase(int size);


    public boolean increaseAndCheckIsReach(int size) {
        int s = increaseAndGet(size);
        return  s >= threshold || s < 0;
    }

    public abstract int increaseAndGet(int size);


}
