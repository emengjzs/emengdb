/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by emengjzs on 2016/10/22.
 */

public class DemoData{
    public    volatile int value1 = 1;
               volatile int value2 = 2;
    protected volatile int value3 = 3;
    private   volatile int value4 = 4;

    public static AtomicIntegerFieldUpdater<DemoData> update1;
    public static AtomicIntegerFieldUpdater<DemoData> update2;
    public static AtomicIntegerFieldUpdater<DemoData> update3;
    public static AtomicIntegerFieldUpdater<DemoData> update4;

    static {

        update1 = AtomicIntegerFieldUpdater.newUpdater(DemoData.class, "value1");
        update2 = AtomicIntegerFieldUpdater.newUpdater(DemoData.class, "value2");
        update3 = AtomicIntegerFieldUpdater.newUpdater(DemoData.class, "value3");
        update4 = AtomicIntegerFieldUpdater.newUpdater(DemoData.class, "value4");

    }



}