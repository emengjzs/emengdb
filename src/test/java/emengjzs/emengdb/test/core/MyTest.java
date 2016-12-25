/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.test.core;

import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by emengjzs on 2016/12/23.
 */
public abstract class MyTest {


    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected static final Assert Assert = new Assert();



    protected String toHexString(byte[] array) {

        return Bytes.asList(array).stream()
                .map(b -> Integer.toHexString(b & 0x00FF) )
                .reduce("[", (str, hexByteStr )-> str + ", " + hexByteStr)
                + "]";

    }

}
