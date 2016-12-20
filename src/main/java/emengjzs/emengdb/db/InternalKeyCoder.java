/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.db;

/**
 * Created by emengjzs on 2016/12/20.
 */

import emengjzs.emengdb.util.Bits;

/**
 * InternalKey is used for the putting low-level key in skip list,
 * which contains of original user key and other meta data such as
 * the length of the key, seq number, etc.
 *
 * Here defines the encode and decode of the InternalKey.
 *
 */
public class InternalKeyCoder {

    /* if need to compress the metadata of key and value, e.g. key.length*/
    private boolean useZipMetaData = true;

    /* if need to add crc code of data */
    private boolean useCRCCheckData = true;


    public byte[] construct(long seq, ValueType type, byte[] key) {
        int byteLength = Integer.BYTES + key.length + Long.BYTES;
        byte[]  metaDataWithKeyBytes = new byte[byteLength];

        Bits.putInt(metaDataWithKeyBytes, 0, key.length);
        Bits.putBytes(metaDataWithKeyBytes, 0 + Long.BYTES, key);
        Bits.putLong(metaDataWithKeyBytes, 0 + key.length + Long.BYTES, seq << 8 | type.toByte());

        return metaDataWithKeyBytes;
    }


    public byte[] extractUserKey(byte[] internalKey) {
        return null;
    }

    long extractSeqAndType() {
        return 0;
    }

    long extractSeq() {
        return 0;
    }

}
