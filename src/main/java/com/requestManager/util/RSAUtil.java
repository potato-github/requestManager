package com.requestManager.util;

import com.alibaba.fastjson.JSONObject;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {
    public static final String SI_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCSLAh1Vk/IX+sw0ATuRb2C5mzRiO0VsF8XrbGq/O2hQVhE0CUUAMGMSVtKVZqlnjolHVNUD5jAz0wkIa0wQ1Mp81OYISlYxt4ZmuAPYyn+2WbsfcGRlGYaXaO+3UPY3a1lQPEm3EW6YAdgWM9aQzPXLId3Taq449/z6kt1yfeKlmP/i3KW+b9PTJJc7x7KyamJi67B4d3R00Ozc9vfT96vtyFVI9nBZV/tW4SqiENdk4cBlNpLb70lu1dk2y1FYglIfn0tVQ75SK3nIgLRLkyYMdSSeW0Hq/iHOPEVu+uvdsbXfTDSpzLW/mQwgjWVMe7axoGpZImqV95D75H/5ZzZAgMBAAECggEACYw0QjvQyskPIvlXdRvbkNcNwRMFT28div2mtwE94VRUGGyTG28gNVJeUujsCMjgyedUN8+r/d4cflivyBEBt+IDshJqcbjNrZT68S5cQh//s89UCzcLA0peBXvM8HesWYqIJe5p5I5ctPugEaiFlJa9Q3rwfZKu4G8LnkZCs8vdLpK2zSjmjTFdv6pTJUDGh4+Sj5zprgZn5XmiVo3MxpyqNM48g+U2qLbHYN5mYuMqNG3GG5ZqGqk1sBbzv6RvqMU2XFp3j6euItSj2Zi99jA+LGDzyRb8w0Bin6T4Uicfm7mfQyuebAqMjor4o862+ZcA4f+16Dr2BzzwCbxszQKBgQDL0VV5dQfVblVBIWUq0FKN54HHEaegTOrvvwTE1O0eFTgc3ka9OvjPg2wHf/A1FlUdewKtSrhtay8u7R61LZR7NBa+30G+FcaD3lE/7FwWsDdpzPNVwZ4a0mM9vnVtyL2jOnExT4DYZzvIODFqfbqMzSgN6Yur8gEELSrs5JtqRQKBgQC3mHipG/uHd87hGc5J8KHo4qJk9ris5728/YHVlx94O5AllO7S3sx0T5iewG19sDWSJBejWHH1d5RnqUJkSuxSArYbWpl1VMKZWzjOmdpilTbPnxQB85XC5oluByYzUfAJiJxJnsF9mxqWhb9AtOubgVptpj78EsxwMk8tFlu7hQKBgQCr01Styj3KbvOsrsytJWcTfsJn4rhgabf+WhlZHvCv1c+ydqlISnkL6I2gXPlSp/slmViOY1nL1Pq4IusqbryoiSdLzseLyqvK7BevPkH8Vp7AsSnLeCkkD43etbwly4AkJdbbbV0AEzfQ+Z6fWlN09Ff6MpOvZ5v3u2RexfNHyQKBgCMKOp0r9gmY2CcmIbUC9CPafVv/qqd1Y0UarRjyytLLq2tAzYP5edQJDPvfhgYjB1ZpBQnr63QJ6o0ORNmXYxsT4WW/GOLH1owmWOU1f/SovdlKZysoLkEQdtJFEwlgbgujwxBZ+FvcqmpoBGR5Iy5+tZ+Pxx2dCN+w+mOEjOVxAoGBAK41MnGyH8IQ0xw0wbsk5WQ7ij4zr7Zre8yMjeGYLzT4qCsm7n/JJ31t8d0S1YwM4nmXcwQYYd9lFzvoRFsniPkFNbQRh7S5NY4PltUP7G+uKSyzV9vYeIPOe4zEPxy/oHjIRDouXbILIaqUItqOoVAoUYXx3DvxSMSih3/nqbUy";
    public static final String USER_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkiwIdVZPyF/rMNAE7kW9guZs0YjtFbBfF62xqvztoUFYRNAlFADBjElbSlWapZ46JR1TVA+YwM9MJCGtMENTKfNTmCEpWMbeGZrgD2Mp/tlm7H3BkZRmGl2jvt1D2N2tZUDxJtxFumAHYFjPWkMz1yyHd02quOPf8+pLdcn3ipZj/4tylvm/T0ySXO8eysmpiYuuweHd0dNDs3Pb30/er7chVSPZwWVf7VuEqohDXZOHAZTaS2+9JbtXZNstRWIJSH59LVUO+Uit5yIC0S5MmDHUknltB6v4hzjxFbvrr3bG130w0qcy1v5kMII1lTHu2saBqWSJqlfeQ++R/+Wc2QIDAQAB";

    //生成秘钥对
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        return keyPair;
    }

    //获取公钥(Base64编码)
    public static String getPublicKey(KeyPair keyPair){
        PublicKey publicKey = keyPair.getPublic();
        byte[] bytes = publicKey.getEncoded();
        return byte2Base64(bytes);
    }

    //获取私钥(Base64编码)
    public static String getPrivateKey(KeyPair keyPair){
        PrivateKey privateKey = keyPair.getPrivate();
        byte[] bytes = privateKey.getEncoded();
        return byte2Base64(bytes);
    }

    //将Base64编码后的公钥转换成PublicKey对象
    public static PublicKey string2PublicKey(String pubStr) throws Exception{
        byte[] keyBytes = base642Byte(pubStr);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
    }

    //将Base64编码后的私钥转换成PrivateKey对象
    public static PrivateKey string2PrivateKey(String priStr) throws Exception{
        byte[] keyBytes = base642Byte(priStr);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }

    //公钥加密
    public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws Exception{
        int maxLength = 245;
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = content.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        for(int i = 0; inputLen - offSet > 0; offSet = i * maxLength) {
            byte[] cache;
            if(inputLen - offSet > maxLength) {
                cache = cipher.doFinal(content, offSet, maxLength);
            } else {
                cache = cipher.doFinal(content, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }

    //私钥解密
    public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws Exception{
        int maxLength = 256;
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        int inputLen = content.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        for(int i = 0; inputLen - offSet > 0; offSet = i * maxLength) {
            byte[] cache;
            if(inputLen - offSet > maxLength) {
                cache = cipher.doFinal(content, offSet, maxLength);
            } else {
                cache = cipher.doFinal(content, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            ++i;
        }
        byte[] bytes = out.toByteArray();
        out.close();
        return bytes;
    }

    //字节数组转Base64编码
    public static String byte2Base64(byte[] bytes){
        return Base64.getEncoder().encodeToString(bytes);
    }

    //Base64编码转字节数组
    public static byte[] base642Byte(String base64Key) throws IOException {
        return Base64.getDecoder().decode(base64Key);
    }

    public static String decodeData(String data){
        byte[] privateDecrypt = null;
        try {
            byte[] base642Byte = RSAUtil.base642Byte(data);
            PrivateKey privateKey = RSAUtil.string2PrivateKey(SI_KEY);
            privateDecrypt = RSAUtil.privateDecrypt(base642Byte, privateKey);
            return new String(privateDecrypt);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String encodeData(String data){
        JSONObject jsonObject = new JSONObject();
        PublicKey publicKey = null;
        try {
            publicKey = RSAUtil.string2PublicKey(USER_KEY);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //用公钥加密
        byte[] publicEncrypt = new byte[0];
        try {
            publicEncrypt = RSAUtil.publicEncrypt(data.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        //加密后的内容Base64编码
        String resultData = RSAUtil.byte2Base64(publicEncrypt);
        return resultData;
    }
}
