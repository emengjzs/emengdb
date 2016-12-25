/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.api;

import emengjzs.emengdb.db.Slice;

/**
 * Created by emengjzs on 2016/12/22.
 */
public interface EmengDB {


    Slice get(Slice key);

    void put(Slice key, Slice value);

    void del(Slice key);

}
