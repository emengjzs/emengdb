/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.table;

import emengjzs.emengdb.db.Slice;

import java.nio.ByteBuffer;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Created by emengjzs on 2016/10/28.
 */
public class TableBlock {

    // the whole data except the numOfRestarts;
    private byte[] data;

    private int numOfRestarts;

    private int restartsOffset;





    public int getNumOfRestarts() {
        return numOfRestarts;
    }


    public boolean getRecord(ByteBuffer byteBuffer, RecordInfo recordInfo) {
        if (byteBuffer.remaining() < 3 * Integer.BYTES) {
            return false;
        }
        recordInfo.sharedKeyLength = byteBuffer.getInt();
        recordInfo.unsharedKeyLength = byteBuffer.getInt();
        recordInfo.valueLength = byteBuffer.getInt();
        recordInfo.unsharedKeyOffset = byteBuffer.position();
        if (byteBuffer.remaining() < recordInfo.unsharedKeyLength + recordInfo.valueLength) {
            return false;
        }
        return true;
    }


    static class RecordInfo {
        int sharedKeyLength;
        int unsharedKeyLength;
        int valueLength;
        int unsharedKeyOffset;
    }


    class TableIterator implements ListIterator<Slice> {

        int currentPosition;
        Comparator<Slice> cmp;


        @Override
        public boolean hasNext() {
            return currentPosition <= data.length;
        }

        @Override
        public Slice next() {
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Slice previous() {
            return null;
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return 0;
        }

        @Override
        public void remove() {

        }

        @Override
        public void set(Slice slice) {

        }

        @Override
        public void add(Slice slice) {

        }
    }

}
