/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by emengjzs on 2016/10/22.
 */
public class FieldAccessTest {


        @Test
        public  void doit() {
            DemoData data = new DemoData();
            System.out.println("1 ==> " + DemoData.update1.getAndSet(data, 10));
            System.out.println("2 ==> " + DemoData.update2.incrementAndGet(data));
            System.out.println("3 ==> +"+ DemoData.update3.decrementAndGet(data));
            System.out.println("4 ==> "+ DemoData.update4.compareAndSet(data, 4, 5));
            AtomicIntegerFieldUpdater<DemoData> value3 = AtomicIntegerFieldUpdater.newUpdater(DemoData.class, "value3");
            System.out.println("3 ==> +"+ value3.decrementAndGet(data));

        }

}
