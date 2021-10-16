package com.app.middleware.utility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESInteroperable {

    /**
     * Encrypt using AES 128-bit encryption with CBC mode
     *
     * @param plainText (byte[]) The plain text
     * @param key (byte[]) The secret key
     * @param iv (byte) the initialization vector
     *
     * @return (String) Encrypted text
     */
    public static String encrypt(byte[] plainText, byte[] key, byte[] iv) {
        try {
            SecretKeySpec secretKeySpec;
            secretKeySpec = new SecretKeySpec(key, "AES");

            // PKCS#5 Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameters algorithmParams = AlgorithmParameters.getInstance("AES");
            algorithmParams.init(new IvParameterSpec(iv));

            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, algorithmParams);
            byte[] encryptedBytes = cipher.doFinal(plainText);
            return DatatypeConverter.printBase64Binary(encryptedBytes);
        } catch (NoSuchPaddingException | BadPaddingException e) {
            System.out.println("Padding exception in encrypt(): " + e);
        } catch ( NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException e ) {
            System.out.println("Encryption exception in encrypt(): " + e);
        } catch (Exception e) {
            System.out.println("Exception in encrypt(): " + e);
        }
        return null;
    }

    /**
     * Decrypt using AES 128-bit encryption with CBC mode
     *
     * @param cipherText (byte[]) The cipher text
     * @param key (byte[]) The secret key
     * @param iv (byte) the initializatoin vector
     *
     * @return (String) Plain text
     */
    public static String decrypt(String cipherText, byte[] key, byte[] iv ) {
        try {
            SecretKeySpec secretKeySpec;
            secretKeySpec = new SecretKeySpec(key, "AES");

            // PKCS#5 Padding
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            AlgorithmParameters algorithmParams = AlgorithmParameters.getInstance("AES");
            algorithmParams.init(new IvParameterSpec(iv));

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, algorithmParams);
            return new String(cipher.doFinal(DatatypeConverter.parseBase64Binary(cipherText)), "UTF-8");
        } catch (NoSuchPaddingException | BadPaddingException e) {
            System.out.println("Padding exception in decrypt(): " + e);
        } catch ( NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException e ) {
            System.out.println("Decryption exception in decrypt(): " + e);
        } catch (Exception e) {
            System.out.println("Exception in decrypt(): " + e);
        }
        return null;
    }
}
