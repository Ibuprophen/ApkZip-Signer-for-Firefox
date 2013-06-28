/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hashing tool.
 * 
 * @author Hai Bison
 * @since v1.6.7 beta
 */
public class Hasher {

    /**
     * Calculates hash string of {@code data}.
     * 
     * @param algorithm
     *            the algorithm.
     * @param data
     *            the input data.
     * @param formatAsFingerprint
     *            {@code true} to format the result as a digital fingerprint.
     * @return the hash string, or an empty string if {@code algorithm} is not
     *         supported.
     */
    public static CharSequence calcHash(String algorithm, byte[] data,
            boolean formatAsFingerprint) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);

            BigInteger bi = new BigInteger(1, md.digest());
            final StringBuilder result = new StringBuilder(String.format("%0"
                    + (md.digest().length * 2) + "x", bi));
            if (formatAsFingerprint) {
                final int count = result.length() / 2 - 1;
                for (int i = 1; i <= count; i++)
                    result.insert(3 * i - 1, ':');
            }

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }// calcHash()
}
