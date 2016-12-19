/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

import java.util.Arrays;
import java.util.Comparator;
import java.util.ListIterator;

/**
 * Created by emengjzs on 2016/8/30.
 */
public class MemTable {
    public final static long MAX_SEQ = (0x01L << 56) - 1;

    private SkipList<Key> table;


    /**
     * keyCmp;Key> -> interCmp\<internalKey> -> userCmp<?>
     */
    private KeyComparator keyComparator;

    public MemTable(InternalKeyComparator cmp) {
        keyComparator = new KeyComparator(cmp);
        table = new SkipList<>(keyComparator);
    }


    public void add(long seq, ValueType type, byte[] key, byte[] value) {
        table.insert((new Key(seq, type, key, value)));
    }


    public MemTableGetResult get(LookupKey lookupKey) {
        ListIterator<Key> keyListIterator = table.listIterator(new Key(lookupKey));
        // found the key
        if (keyListIterator.hasNext()) {
            Key next = keyListIterator.next();
            if (keyComparator.interCmp.getUserComparator()
                    .compare(next.getUserKey(), lookupKey.getUserKey()) == 0) {
                if (next.getValueType() == ValueType.VALUE) {
                    return new MemTableGetResult(next.getValue(), Status.SUCCESS);
                }
                else {
                    return new MemTableGetResult(Status.DELETED);
                }
            }
        }
        return new MemTableGetResult(Status.NOT_FOUND);
    }


    /**
     * a general key comparator where the key to be compared
     * is the actual key inserted in skipList for implementation,
     * as the computed key contains the key defined by user, we need
     * the user-defined comparator to decide the position when putting a
     * entry
     */
    private class KeyComparator implements Comparator<Key> {

        InternalKeyComparator interCmp;

        KeyComparator(InternalKeyComparator interCmp) {
            this.interCmp = interCmp;
        }


        /**
         * we need to extract the internal key
         * @param o1
         * @param o2
         * @return
         */
        @Override
        public int compare(Key o1, Key o2) {
            return interCmp.compare(
                    o1.getKey(),
                    o2.getKey());
        }
    }

    interface Entry<K,V> {
        /**
         * here is internalKey
         * @return
         */
        K getKey();
        V getValue();
    }


    class Key implements InternalKey, Entry<InternalKey, Slice> {
        byte[] key;
        long seqAndFlag;
        byte[] value;


        Key(LookupKey lookupKey) {
            this.key = lookupKey.key;
            this.seqAndFlag = lookupKey.seqAndFlag;
            value = new byte[0];
        }


        Key(long seq, ValueType type, byte[] key) {
            this(seq, type, key, new byte[0]);
        }

        /* byte[] bytes; */

        Key(long seq, ValueType type, byte[] key, byte[] value) {
            // bytes = new byte[4 + key.length + 8 + 4 + value.length];
            // writeUTF8(seq, type, key, value);
            this.key = Arrays.copyOf(key, key.length);
            this.seqAndFlag = seq << 8 | type.toByte();
            this.value = Arrays.copyOf(value, value.length);
        }



        /**
         * encode the key, the format shows as below
         * +-----------------+--------+------+-------+---------------+--------+
         * |  key.length + 8 |   key  |  seq |  type |  value.length |  value |
         * +-----------------+--------+------+-------+---------------+--------+
         *          4          key.len    7       1           4        val.len
         *
         * @param seq
         * @param type
         * @param key
         * @param value
         */
        private void write(long seq, ValueType type, byte[] key, byte[] value) {
            // use byteBuffer to simply operation
            /*
            ByteBuffer bf = ByteBuffer.allocate(4 + key.length + 8 + 4 + value.length);
            bf.putInt(key.length + 8)
                    .put(key)
                    .putLong(seq << 8 | type.toByte())
                    .putInt(value.length)
                    .put(value);
            // copy vs no-copy ?
            this.bytes = bf.array();
            */
        }




        public Slice getUserKey() {
            return new Slice(key);
        }

        public long getSeqFlag() {
            return seqAndFlag;
        }

        public int getKeyLength() {
            return key.length;
        }

        public long getSeq() {
            return seqAndFlag >>> 8;
        }

        public ValueType getValueType() {
            return ValueType.values()[(int) (seqAndFlag & 0xFF)];
        }


        /**
         * internal key
         */
        @Override
        public InternalKey getKey() {
            return this;
        }

        public Slice getValue() {
            return new Slice(value);
        }


    }

    public ListIterator<Entry<InternalKey, Slice>> getIterator() {
        return new MemTableIterator();
    }

    /*
    public ListIterator<Key> getIterator(Slice userKey) {
        return new MemTableIterator(new Key());
    }
       */

    private class MemTableIterator implements ListIterator<Entry<InternalKey, Slice>> {

        ListIterator<Key> itr;

        MemTableIterator() {
            itr = table.listIterator();
        }

        @Override
        public boolean hasNext() {
            return itr.hasNext();
        }

        @Override
        public Entry<InternalKey, Slice> next() {
            return itr.next();
        }

        @Override
        public boolean hasPrevious() {
            return itr.hasPrevious();
        }

        @Override
        public Entry<InternalKey, Slice> previous() {
            return itr.previous();
        }

        @Override
        public int nextIndex() {
            return itr.nextIndex();
        }

        @Override
        public int previousIndex() {
            return itr.previousIndex();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void set(Entry<InternalKey, Slice> slice) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void add(Entry<InternalKey, Slice> slice) {
            throw new UnsupportedOperationException();
        }
    }

}
