package com.hill.devlibs.encryption;

import com.hill.devlibs.util.StringUtil;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.Nullable;

/**
 * Created by Hill on 2019/7/31
 */
public class MD5 {
//    private static final String[] hexDigest = new String[]{"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
//    public static String MD5Encode(String origin) {
//        return MD5Encode(origin,"UTF-8");
//    }
//
//    public static String MD5Encode(String origin, String encodingType) {
//        byte[] md5Bytes = toMd5(origin, encodingType);
//        return byteArrayToHexString(md5Bytes);
//    }
//
//    public static String byteArrayToHexString(byte[] b) {
//        StringBuffer resultSb = new StringBuffer();
//        for (int i = 0; i < b.length; i++) {
//            resultSb.append(byteToHexString(b[i]));
//        }
//        return resultSb.toString();
//    }
//
//    public synchronized static final byte[] toMd5(String data, String encodingType) {
//        MessageDigest digest = null;
//        if (digest == null) {
//            try {
//                digest = MessageDigest.getInstance("MD5");
//            } catch (NoSuchAlgorithmException nsae) {
//                //System.err.println("Failed to load the MD5 MessageDigest. ");
//                nsae.printStackTrace();
//            }
//        }
//        if (StringUtil.isEmptyString(data)) {
//            return null;
//        }
//        try {
//            //--最重要的是这句,需要加上编码类型
//            digest.update(data.getBytes(encodingType));
//        } catch (UnsupportedEncodingException e) {
//            digest.update(data.getBytes());
//        }
//        return digest.digest();
//    }
//
//    private static String byteToHexString(byte b) {
//        int n = b;
//        if (n < 0) n = 256 + n;
//        int d1 = n / 16;
//        int d2 = n % 16;
//        return hexDigest[d1] + hexDigest[d2];
//    }

    public  static String encode(String data){
        return  encode(data,"UTF-8");
    }

    public static String encode(String data, String encodingType) {
        try {
            byte[] buffer=data.getBytes(encodingType);
            return encode(buffer);
        }catch (Exception e){

        }
        return "";
    }
    public static String encode(byte[] buffer) {

        try {
            byte[] result = digest(buffer);
            StringBuffer buf = new StringBuffer(result.length * 2);

            for (int i = 0; i < result.length; i++) {
                int intVal = result[i] & 0xff;
                if (intVal < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }

            return buf.toString();
        } catch (Exception e) {
        }
        return "";
    }
    public static byte[] digest(String data){
        try {
            return digest(data.getBytes("ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public static byte[] digest(byte[] buffer){
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(buffer);
            return md5.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
