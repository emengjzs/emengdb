/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

/**
 * Created by emengjzs on 2016/9/3.
 */
public interface LogErrorListener {


    void onReadInterrupt(long size, Exception e);
}
