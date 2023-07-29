package com.requestManager.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AesCBC {
    private static String sKey = "1234567890123456";
    private static String ivParameter = "1234567890123456";
    private static AesCBC instance = null;

    private AesCBC() {

    }


    public static AesCBC getInstance() {
        if (instance == null)
            instance = new AesCBC();
        return instance;

    }

    // 加密
    public static String encrypt(String sSrc) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] raw = sKey.getBytes();
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());//使用CBC模式，需要一个向量iv，可增加加密算法的强度
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            byte[] encrypted = cipher.doFinal(sSrc.getBytes("utf-8"));
            return Base64.getEncoder().encodeToString(encrypted);//此处使用BASE64做转码。
        } catch (Exception e) {
            return "";
        }
    }


    // 解密
    public static String decrypt(String sSrc) throws Exception {
        try {
            byte[] raw = sKey.getBytes("ASCII");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            IvParameterSpec iv = new IvParameterSpec(ivParameter.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] encrypted1 = Base64.getDecoder().decode(sSrc);//先用base64解密
            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original, "utf-8");
            return originalString;
        } catch (Exception ex) {
            return null;
        }
    }


    public static void main(String[] args) throws Exception {
        // 需要加密的字串
//        String cSrc = "{\"code\":200,\"data\":\"test\",\"msg\":\"\"}";
        String cSrc = "msg";
        // 加密
        String enString = AesCBC.getInstance().encrypt(cSrc);
        System.out.println("加密后的字串是：" + enString);

        // 解密
        String DeString = AesCBC.getInstance().decrypt(enString);
        System.out.println("解密后的字串是：" + DeString);
    }
}
