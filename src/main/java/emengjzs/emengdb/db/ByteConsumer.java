/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * Created by emengjzs on 2016/9/3.
 */
public interface ByteConsumer extends Consumer<Byte> {

    void accept(byte t);

    default void accept(Byte t) {
        accept(t.byteValue());
    }

    default ByteConsumer andThen(ByteConsumer after) {
        Objects.requireNonNull(after);
        return (byte t) -> { accept(t); after.accept(t); };
    }
}
