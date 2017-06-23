package com.kyle.mycar.MyUtils;

import android.text.TextUtils;

import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by Zhang on 2017/6/22.
 */

public class SHA {
    private static final char[] HEXES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
            'f'};
    private static final int SALT_BYTE_SIZE = 24;

    /**
     * @param psw
     * @param salt
     * @return 两个容量的数组，0位置psw，1位置salt
     * @throws Exception
     */
    public static String[] encrypt(String psw, String salt) throws Exception {
        String[] strings = new String[2];
        byte[] bytes = null;
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        if (salt == null) {
            SecureRandom random = new SecureRandom();
            bytes = new byte[SALT_BYTE_SIZE];
            random.nextBytes(bytes);
            salt=bytes2Hex(bytes);
            strings[1] = salt;
        }
        psw = salt + psw;
        bytes = psw.getBytes("UTF-8");
        bytes = digest.digest(bytes);
        psw = bytes2Hex(bytes);
        strings[0] = psw;
        return strings;
    }

    /**
     * @param psw
     * @return 两个容量的数组，0位置psw，1位置salt
     * @throws Exception
     */
    public static String[] encrypt(String psw) throws Exception {
        return encrypt(psw, null);
    }


    public static boolean equal(String psw, String salt, String encryptedPsw) throws Exception {
        String[] strings = encrypt(psw, salt);
        return TextUtils.equals(strings[0], encryptedPsw);
    }

    private static String bytes2Hex(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(HEXES[(b >> 4) & 0x0F]);
            sb.append(HEXES[b & 0x0F]);
        }
        return sb.toString();
    }

    // 从十六进制字符串到字节数组转换
    public static byte[] hex2Bytes(String hexstr) {
        byte[] b = new byte[hexstr.length() / 2];
        int length = b.length;
        int j = 0;
        char c0, c1;
        for (int i = 0; i < length; i++) {
            c0 = hexstr.charAt(j++);
            c1 = hexstr.charAt(j++);
            b[i] = (byte) ((parse(c0) << 4) | parse(c1));
        }
        return b;
    }

    private static int parse(char c) {
        if (c >= 'a') return (c - 'a' + 10) & 0x0f;
        if (c >= 'A') return (c - 'A' + 10) & 0x0f;
        return (c - '0') & 0x0f;
    }


}
