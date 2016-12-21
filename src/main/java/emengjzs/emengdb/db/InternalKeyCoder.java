/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/12/20.
 */

import emengjzs.emengdb.util.Bits;

import java.util.Arrays;

/**
 * InternalKey is used for the putting low-level key in skip list,
 * which contains of original user key and other meta data such as
 * the length of the key, seq number, etc.
 *
 * Here defines the encode and decode of the InternalKey.
 *
 */
public class InternalKeyCoder {

    /* if need to compress the metadata of key and value, e.g. key.length */
    private boolean useZipMetaData = true;

    /* if need to add crc code of data */
    private boolean useCRCCheckData = true;

    private static final int NO_ZIP_USER_KEY_OFFSET = 0 + Integer.BYTES;

    public InternalKeyCoder() {

    }

    /**
     * encode the key, the format shows as below
     * +-----------------+--------+------+-------+
     * |  key.length + 8 |   key  |  seq |  type |
     * +-----------------+--------+------+-------+
     *          4          key.len    7       1
     **/
    public byte[] encode(long seq, ValueType type, byte[] key) {
        int byteLength = Integer.BYTES + key.length + Long.BYTES;
        byte[]  metaDataWithKeyBytes = new byte[byteLength];

        Bits.putInt(metaDataWithKeyBytes, 0, key.length);
        Bits.putBytes(metaDataWithKeyBytes, NO_ZIP_USER_KEY_OFFSET, key);
        Bits.putLong(metaDataWithKeyBytes, NO_ZIP_USER_KEY_OFFSET + key.length, seq << 8 | type.toByte());

        return metaDataWithKeyBytes;
    }


    public byte[] decodeUserKey(byte[] internalKey) {
        return Arrays.copyOfRange(
                internalKey,
                NO_ZIP_USER_KEY_OFFSET,
                NO_ZIP_USER_KEY_OFFSET + Bits.getInt(internalKey, 0)
                );
    }

    public Slice getUserKeySlice(byte[] internalKey) {
        return new Slice(internalKey, NO_ZIP_USER_KEY_OFFSET, Bits.getInt(internalKey, 0));
    }

    public long decodeSeqAndType(byte[] internalKey) {
        return decodeSeqAndType(internalKey, Bits.getInt(internalKey, 0));
    }

    long decodeSeqAndType(byte[] internalKey, int keyLength) {
        return Bits.getLong(internalKey, NO_ZIP_USER_KEY_OFFSET + keyLength);
    }

    byte decodeTypeByte(byte[] internalKey, int keyLength) {
        return internalKey[NO_ZIP_USER_KEY_OFFSET + keyLength + Long.BYTES - 1];
    }


}
