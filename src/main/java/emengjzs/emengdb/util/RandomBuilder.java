/*
 * Copyright (c) 2016. emengjzs. All rights reserved.
 */

package emengjzs.emengdb.util;

import java.util.Random;

/**
 * Created by emengjzs on 2016/10/15.
 */
public class RandomBuilder {

    private static final String charSet = "abcdefghijklmnopqrstuvwxyz0123456789";
    private Random random = new Random();


    public int getRandomInt(int bound) {
        return random.nextInt(bound);
    }


    public String getRandomString(int size) {
        char ch[]  = new char[size];
        for (int i = 0; i < size; i ++) {
            ch[i] = charSet.charAt(random.nextInt(charSet.length()));
        }
        return new String(ch);
    }

    public byte[] getRandomBytes(int size) {
        byte ch[]  = new byte[size];
        for (int i = 0; i < size; i ++) {
            ch[i] = (byte) random.nextInt(0xFF + 1);
        }
        return ch;
    }

}
