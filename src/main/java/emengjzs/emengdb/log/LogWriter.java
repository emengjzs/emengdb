/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

import emengjzs.emengdb.db.Slice;
import emengjzs.emengdb.util.Validate;
import emengjzs.emengdb.util.io.PrimitiveWritable;
import emengjzs.emengdb.util.io.WritableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by emengjzs on 2016/9/2.
 */
public class LogWriter extends LogFormat {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private PrimitiveWritable writableFile;

    private int blockOffset = 0;

    LogWriter(WritableFile writableFile) {
        this.writableFile = new PrimitiveWritable(writableFile);
    }

    // for zero-filled
    private final byte[] dummyBytes = new byte[]{0, 0, 0, 0, 0, 0};

    // TODO: consider the zero-length empty data
    public void addData(Slice data) throws IOException {
        int leftSize = data.getLength();
        int blockLeftSize = 0;

        // the start index of the data to be read.
        int start = 0;

        RecordType type = RecordType.FIRST_TYPE;
        while (leftSize > 0) {
            blockLeftSize = K_BLOCK_SIZE - blockOffset;
            if (blockLeftSize < K_HEADER_SIZE) {

                if (blockLeftSize > 0) {
                    // fill zero
                    writableFile.write(dummyBytes, 0, blockLeftSize);
                }
                // next new bolck

                /*
                if (log.isDebugEnabled()) {
                    log.debug("[LOG FILE] New Block : -{}", blockLeftSize);
                }
*/
                blockOffset = 0;
                blockLeftSize = K_BLOCK_SIZE;
            }
            Validate.isTrue(K_BLOCK_SIZE - blockOffset >= K_HEADER_SIZE, "The block did not" +
                    "fill zero in the tail.");

            // last part of data, may be the whole data
            int blockAvailSize = blockLeftSize - K_HEADER_SIZE;
            if (blockAvailSize >= leftSize ) {
                if (type == RecordType.FIRST_TYPE)
                    type = RecordType.FULL_TYPE;
                else
                    type = RecordType.LAST_TYPE;
                blockOffset += addRecord(type, data, start, leftSize);
                start += leftSize;
                leftSize = 0;
            }
            else {

                blockOffset += addRecord(type, data, start, blockAvailSize);
                if (type == RecordType.FIRST_TYPE) {
                    type = RecordType.MIDDLE_TYPE;
                }
                start += (blockAvailSize);
                leftSize -= (blockAvailSize);
            }

        }
    }

    private int addRecord(RecordType type, Slice data, int start, int length) throws IOException {

//        byte header[] = new byte[7];
//
//        Bits.putInt(header, 0, 0x12345678);
//        Bits.putShort(header, 4, (short) (length & 0xFFFF));
//        header[6] = (byte) type.id;
//        writableFile.write(header);

        // here write header separately is better than write from
        // an array because it cost time to apply for a new array

        // omit the part of CRC
        writableFile.writeInt(0x12345678);
        writableFile.writeShort(length & 0xFFFF);
        writableFile.writeByte(type.id);

        writableFile.write(data.array(), data.getStart() + start, length);
        writableFile.flush();
        /*
        if (log.isDebugEnabled()) {
            log.debug("[LOG FILE] Write: {}, {} - [{}]",
                    type.toString(),
                    length,
                    data.getSubSlice(start, length).toByteString());
        }
        */
        return K_HEADER_SIZE + length;
    }
}
