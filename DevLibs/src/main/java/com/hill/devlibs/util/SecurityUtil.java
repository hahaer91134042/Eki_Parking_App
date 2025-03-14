package com.hill.devlibs.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hill on 2019/6/25
 */
public class SecurityUtil {

    /**
     * SHA加密
     *
     * @param strSrc
     *            明文
     * @return 加密之後的密文
     */
    public static String sha256Encrypt(String strSrc) {
        MessageDigest md = null;
        String strDes = null;
        byte[] bt = strSrc.getBytes();
        try {
            md = MessageDigest.getInstance("SHA-256");// 將此換成SHA-1、SHA-512、SHA-384等參數
            md.update(bt);
            strDes = bytes2Hex(md.digest()); // to HexString
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        return strDes;
    }

    /**
     * byte數組轉換為16進制字符串
     *
     * @param bts
     *            數據源
     * @return 16進制字符串
     */
    public static String bytes2Hex(byte[] bts) {
        String des = "";
        String tmp ;
        for (int i = 0; i < bts.length; i++) {
            tmp = (Integer.toHexString(bts[i] & 0xFF));
            if (tmp.length() == 1) {
                des += "0";
            }
            des += tmp;
        }
        return des;
    }

}
