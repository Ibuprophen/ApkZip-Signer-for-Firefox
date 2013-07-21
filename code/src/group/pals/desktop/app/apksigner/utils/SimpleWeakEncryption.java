/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

/**
 * The <b>simple-and-weak</b> encryption utilities.
 * 
 * @author Hai Bison.
 * 
 */
public class SimpleWeakEncryption {

    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
    private static final String SECRET_KEY_FACTORY_ALGORITHM = "PBKDF2WithHmacSHA1";
    private static final String SECRET_KEY_SPEC_ALGORITHM = "AES";

    /**
     * Only {@code 128} bits. If this is {@code 256}, some JVMs must need extra
     * tools to be installed. Poor Java :|
     */
    private static final int KEY_LEN = 128;
    private static final int ITERATION_COUNT = (int) Math.pow(2, 16);
    private static final String SEPARATOR = "\t";

    public static final String UTF8 = "UTF-8";

    /**
     * Encrypts {@code data} by {@code key}.
     * 
     * @param password
     *            the secret key.
     * @param data
     *            the data.
     * @return the encrypted data.
     */
    public static String encrypt(final char[] password, final String data) {
        byte[] bytes = null;
        try {
            bytes = data.getBytes(UTF8);
        } catch (UnsupportedEncodingException e) {
            /*
             * Never catch this.
             */
        }

        Cipher cipher = null;

        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, genKey(password));
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            bytes = cipher.doFinal(bytes);
            return String.format("%s%s%s",
                    Base64.encodeToString(cipher.getIV(), Base64.DEFAULT),
                    SEPARATOR, Base64.encodeToString(bytes, Base64.DEFAULT));
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }// encrypt()

    /**
     * Decrypts an encrypted string ({@code data}) by {@code key}.
     * 
     * @param password
     *            the password.
     * @param data
     *            the data.
     * @return the decrypted string.
     */
    public static String decrypt(final char[] password, final String data) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(TRANSFORMATION);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final int iSeparator = data.indexOf(SEPARATOR);

        try {
            cipher.init(
                    Cipher.DECRYPT_MODE,
                    genKey(password),
                    new IvParameterSpec(Base64.decode(
                            data.substring(0, iSeparator), Base64.DEFAULT)));
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            return new String(cipher.doFinal(Base64.decode(
                    data.substring(iSeparator + 1), Base64.DEFAULT)), UTF8);
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            /*
             * Never catch this.
             */
        }

        return null;
    }// decrypt()

    /**
     * Generates secret key.
     * 
     * @param password
     *            the password.
     * @return the secret key.
     */
    private static Key genKey(char[] password) {
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory
                    .getInstance(SECRET_KEY_FACTORY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        KeySpec spec = null;
        try {
            spec = new PBEKeySpec(password,
                    new String(password).getBytes(UTF8), ITERATION_COUNT,
                    KEY_LEN);
        } catch (UnsupportedEncodingException e) {
            /*
             * Never catch this.
             */
        }

        SecretKey tmp = null;
        try {
            tmp = factory.generateSecret(spec);
        } catch (InvalidKeySpecException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return new SecretKeySpec(tmp.getEncoded(), SECRET_KEY_SPEC_ALGORITHM);
    }// genKey()
}
