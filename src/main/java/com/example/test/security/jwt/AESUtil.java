package com.example.test.security.jwt;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {

    private static String algorithm = "AES/CBC/PKCS5Padding";

    public static String encrypt(String input, IvParameterSpec ivSperc, SecretKey secretKeySpec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSperc);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String cipherText, IvParameterSpec ivSperc, SecretKey secretKeySpec)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSperc);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);

    }

    public static SecretKey getKeySecretKey(char[] secretKey, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(secretKey, salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    public static IvParameterSpec getIv(String iv) {
        return new IvParameterSpec(iv.getBytes());
    }

    // public static SecretKeySpec getPrivateKey(String privateKey) throws NoSuchAlgorithmException {
    //     SecretKey key = AESUtil.generateKeyEere(128);
    //     String s = key.toString();
    //     return new SecretKeySpec(Base64.getDecoder().decode(privateKey), "AES");
    // }

    // public static SecretKeySpec getPrivateKey(Key privateKey) throws NoSuchAlgorithmException {
    //     SecretKey key = AESUtil.generateKeyEere(128);
    //     String s = key.toString();
    //     return new SecretKeySpec(privateKey.getEncoded(), "AES");
    // }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    // public static SecretKey generateKeyEere(int n) throws NoSuchAlgorithmException {
    //     KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    //     keyGenerator.init(n);
    //     SecretKey key = keyGenerator.generateKey();
    //     return key;
    // }

}