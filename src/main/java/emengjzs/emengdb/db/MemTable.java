/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * Created by emengjzs on 2016/8/30.
 */
public class MemTable {
    public final static long MAX_SEQ = (0x01L << 56) - 1;

    public static final Logger LOG = LoggerFactory.getLogger(MemTable.class);

    // Use Skip List
    private ConcurrentNavigableMap<byte[], byte[]> table;

    private Comparator<Slice> userKeyComparator;

    private InternalKeyCoder internalKeyCoder;

    /*
     *  a general key comparator where the key to be compared
     *  is the actual key inserted in skipList for implementation,
     *  as the computed key contains the key defined by user, we need
     *  the user-defined comparator to decide the position when putting a
     *  entry
     *
     *  keyCmp;Key> -> interCmp\<internalKey> -> userCmp<?>
     */
    public MemTable(InternalKeyComparator cmp) {

        internalKeyCoder = new InternalKeyCoder();
        cmp.setInternalKeyCoder(internalKeyCoder);

        userKeyComparator = cmp.getUserComparator();

        table = new ConcurrentSkipListMap<>(cmp);

    }


    public void add(long seq, ValueType type, byte[] key, byte[] value) {
        table.put(internalKeyCoder.encode(seq, type, key), value);
    }


    public MemTableGetResult get(LookupKey lookupKey) {
        byte[] encodeLookupKey = internalKeyCoder.encode(lookupKey.getSeq(), lookupKey.getValueType(), lookupKey.key);
        Entry<byte[], byte[]> floorEntry = table.floorEntry(encodeLookupKey);

        MemTableGetResult memTableGetResult = new MemTableGetResult();

        // found the key
        if (floorEntry != null) {

            byte[] floorKey = floorEntry.getKey();
            if (userKeyComparator.compare(
                    internalKeyCoder.getUserKeySlice(floorKey),
                    lookupKey.getUserKey()) == 0) {
                byte valueType = internalKeyCoder.decodeTypeByte(floorKey, floorKey.length);
                if (valueType == ValueType.VALUE.toByte()) {
                    memTableGetResult.value = new Slice(floorEntry.getValue());
                    memTableGetResult.status = MemTableGetResult.SUCCESS;
                }
                else if (valueType == ValueType.DELETE.toByte()){
                    memTableGetResult.status = MemTableGetResult.DELETED;
                }
                else {
                    LOG.warn("Unkonon ValueType: " + valueType + "key=[{}] value=[{}]",
                            floorKey, floorEntry.getValue());
                    memTableGetResult.status = MemTableGetResult.NOT_FOUND;
                }
            }

        }
        else {
            memTableGetResult.status = MemTableGetResult.NOT_FOUND;
        }
        return memTableGetResult;
    }


    public ListIterator<Entry<InternalKey, Slice>> getIterator() {
        return new MemTableIterator();
    }


    private class MemTableIterator implements ListIterator<Entry<InternalKey, Slice>> {

        private Iterator<Entry<byte[], byte[]>> itr;

        Entry<byte[], byte[]> next;

        MemTableIterator() {
            itr = table.entrySet().iterator();
        }

        @Override
        public boolean hasNext() {
            return itr.hasNext();
        }


        @Override
        public Entry<InternalKey, Slice> next() {
            return new IteratorEntry((next = itr.next()));
        }

        @Override
        public boolean hasPrevious() {
            return next != null && table.lowerKey(next.getKey()) != null;
        }

        @Override
        public Entry<InternalKey, Slice> previous() {
            return next == null ? null : new IteratorEntry(table.lowerEntry(next.getKey()));
        }


        @Override
        public int nextIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int previousIndex() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Entry<InternalKey, Slice> internalKeySliceEntry) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Entry<InternalKey, Slice> internalKeySliceEntry) {
            throw new UnsupportedOperationException();
        }

        private class IteratorEntry extends AbstractMap.SimpleImmutableEntry<InternalKey, Slice> {

            private final byte[] rawValue ;

            IteratorEntry(Entry<byte[], byte[]> entry) {
                this(new DecodeInternalKey(entry.getKey(), internalKeyCoder), entry.getValue());
            }

            IteratorEntry(InternalKey key, byte[] value) {
                super(key, null);
                rawValue = value;
            }

            @Override
            public Slice getValue() {
                return new Slice(rawValue);
            }

        }
    }


}
