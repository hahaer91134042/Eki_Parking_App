package com.hill.devlibs.encryption;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Hill on 2019/7/31
 */
public class SHA1 {
    public static String encodeToBase64(byte[] buffer) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-1");
            return Base64.encodeToString(md.digest(buffer),Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String encodeToBase64(String convertme) {

        try {
            return encodeToBase64(convertme.getBytes("ISO-8859-1"));
        }  catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
    public static String encodeToString(byte[] buffer) {

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            //byte[] textBytes = text.getBytes("iso-8859-1");
            //md.update(buffer, 0, buffer.length);
            return convertToHex(md.digest(buffer)).toUpperCase();
//            md = MessageDigest.getInstance("SHA-1");
//            return new String(md.digest(buffer),"ISO-8859-1").replace("-","");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String encodeToString(String convertme) {
        try {
            return encodeToString(convertme.getBytes("ISO-8859-1"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
