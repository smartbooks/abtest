package com.github.smartbooks.abtest.core.algorithm;

import com.github.smartbooks.abtest.core.ExperimentAlgorithm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 默认分流算法
 */
public class DefaultHashAlgorithm extends ExperimentAlgorithm {

    private static final Logger logger = LogManager.getLogger(DefaultHashAlgorithm.class);

    private static MessageDigest md5;

    static {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.error(e);
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }
        return new String(resultCharArray);
    }

    private static long splitBucket(MessageDigest md5, long val, String shuffle) {
        String key = String.valueOf(val) + ((shuffle == null) ? "" : shuffle);
        byte[] ret = md5.digest(key.getBytes());
        String s = byteArrayToHex(ret);
        long hash = Long.parseUnsignedLong(s.substring(s.length() - 16, s.length() - 1), 16);
        if (hash < 0) {
            hash = hash * (-1);
        }
        return hash;
    }

    @Override
    public long allocation(long id, long bucketSize, String factor) {
        return splitBucket(md5, id, factor) % bucketSize;
    }
}
