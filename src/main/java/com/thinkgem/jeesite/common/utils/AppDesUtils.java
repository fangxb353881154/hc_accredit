package com.thinkgem.jeesite.common.utils;

import com.thinkgem.jeesite.common.config.Global;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @author Fangxb
 * @version 2016-10-03 19:47
 */
public class AppDesUtils {
    // 指定DES加密解密所用的密钥
    private static Key key;
    private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };

    public AppDesUtils() {
        setKey(Global.getConfig("des.key"));
    }

    public AppDesUtils(String keyStr) {
        setKey(keyStr);
    }
    public static void setKey(String keyStr) {
        try {
            /*DESedeKeySpec  spec = new DESedeKeySpec(keyStr.getBytes());
            SecretKeyFactory objKeyFactory = SecretKeyFactory.getInstance("DES");
            key = objKeyFactory.generateSecret(spec);*/
            DESKeySpec objDesKeySpec = new DESKeySpec(keyStr.getBytes("UTF-8"));
            SecretKeyFactory objKeyFactory = SecretKeyFactory.getInstance("DES");
            key = objKeyFactory.generateSecret(objDesKeySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 对字符串进行DES加密，返回BASE64编码的加密字符串
    public final String encryptString(String str) {
        byte[] bytes = str.getBytes();
        try {
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
            byte[] encryptStrBytes = cipher.doFinal(bytes);
            return Base64.encode(encryptStrBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 对BASE64编码的加密字符串进行解密，返回解密后的字符串
    public final String decryptString(String str) {
        try {
            byte[] bytes = Base64.decode(str);
            IvParameterSpec zeroIv = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
            bytes = cipher.doFinal(bytes);
            return new String(bytes);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static void main(String[] args) {
        String s = "7";
        AppDesUtils app = new AppDesUtils();
        System.out.println("==========加密结果：" + app.encryptString(s));
        System.out.println("==========解密结果：" + app.decryptString(app.encryptString(s)));
    }
}
