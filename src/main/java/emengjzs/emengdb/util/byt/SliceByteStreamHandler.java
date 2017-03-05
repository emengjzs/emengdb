/*
 * Copyright (c) 2017. emengjzs. All rights reserved.
 */
package emengjzs.emengdb.util.byt;

@FunctionalInterface
public interface SliceByteStreamHandler {
    // each time give you some bytes to handle, only limit to [start, start + length]
    void handle(byte[] bytes, int start, int length);
}
