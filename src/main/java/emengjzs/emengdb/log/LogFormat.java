/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.log;

/**
 * Created by emengjzs on 2016/9/2.
 */


/**
 * - log file contains sequences of blocks.
 * - each may contains sequences of records, and their sizes may
 * be not the same.
 * - the record must stay only in a block
 * - data is divided into a few records.
 * +----------------------------------------+-----+---------+  \
 * |   record   |            record         | ... |  record |   |---> Block 32 KB
 * |--------------------------------------------------------+  /
 * |              record            |    record   |  record |
 * +--------------------------------------------------------+
 * |                        ...                             |
 * +--------------------------------------------------------+
 *
 * record:
 * +-------------------+--------------+--------+--------------------+
 * |        CRC        |    length    |  type  |        data        |
 * +-------------------+--------------+--------+--------------------+
 * |          4                2           1             length     |
 *
 */
public class LogFormat {

    static RecordType MAX_RECORD_SIZE = RecordType.LAST_TYPE;

    static int K_BLOCK_SIZE = 32768;

    static int K_HEADER_SIZE = 4 + 1 + 2;
}


