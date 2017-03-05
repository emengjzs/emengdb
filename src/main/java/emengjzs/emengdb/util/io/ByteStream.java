/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */
package emengjzs.emengdb.util.io;

import emengjzs.emengdb.util.byt.Slice;
import emengjzs.emengdb.util.byt.SliceByteStreamHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class ByteStream {

    public static SliceByteStreamHandler newSliceWriteHandler(OutputStream outputStream) {
        return (bytes, offset, length) -> {
            try {
                outputStream.write(bytes, offset, length);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        };
    }

    public static void write(Slice slice, OutputStream out) {
        slice.serialize(newSliceWriteHandler(out));
    }

    public static void write(Slice slice, ByteBuffer bf) {
        slice.serialize(bf::put);
    }

}
