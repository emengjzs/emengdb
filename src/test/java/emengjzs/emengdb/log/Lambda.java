/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by emengjzs on 2016/10/15.
 */
public class Lambda {


    static void forRange(int i, Consumer<Integer> action) {
        for (int j = 0; j < i; j ++)
            action.accept(j);
    }

    static void forRange(int i, Runnable action) {
        for (int j = 0; j < i; j ++)
            action.run();
    }

    static <From, To> List<To> flatMap(Function<From, List<To>> mapf, List<From> fromList) {
        return
        fromList.stream()
                .map(mapf)
                .reduce(new ArrayList<>(fromList.size()), (r, toList) -> {r.addAll(toList); return r;});
    }



}
