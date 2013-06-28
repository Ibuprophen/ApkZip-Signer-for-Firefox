/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.text.DateFormat;
import java.util.Date;

/**
 * Text utilities.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Texts {

    /**
     * "UTF-8"
     */
    public static final String UTF8 = "UTF-8";

    /**
     * "MD5"
     */
    public static final String MD5 = "MD5";

    /**
     * "SHA-1"
     */
    public static final String SHA1 = "SHA-1";

    /**
     * "SHA-256"
     */
    public static final String SHA256 = "SHA-256";

    /**
     * An empty string.
     */
    public static final String EMPTY = "";

    /**
     * Regex to filter APK files.
     */
    public static final String REGEX_APK_FILES = "(?si).+\\.apk";

    /**
     * Regex to filter keystore files.
     */
    public static final String REGEX_KEYSTORE_FILES = "(?si).+\\.keystore";

    /**
     * Regex to filter JAR files.
     */
    public static final String REGEX_JAR_FILES = "(?si).+\\.jar";

    /**
     * Regex to filter ZIP files.
     */
    public static final String REGEX_ZIP_FILES = "(?si).+\\.zip";

    /**
     * File extension of APK files.
     */
    public static final String FILE_EXT_APK = ".apk";

    /**
     * File extension of keystore files.
     */
    public static final String FILE_EXT_KEYSTORE = ".keystore";

    /**
     * Converts {@code size} (in bytes) to string. This tip is from:
     * http://stackoverflow.com/a/5599842/942821
     * 
     * @param size
     *            the size in bytes.
     * @return e.g.:<br>
     *         - 128 B<br>
     *         - 1.5 KB<br>
     *         - 10 MB<br>
     *         - ...
     */
    public static String sizeToStr(double size) {
        if (size <= 0)
            return "0 B";
        final String[] units = { "", "Ki", "Mi", "Gi", "Ti", "Pi", "Ei", "Zi",
                "Yi" };
        final Short blockSize = 1024;

        int digitGroups = (int) (Math.log10(size) / Math.log10(blockSize));
        if (digitGroups >= units.length)
            digitGroups = units.length - 1;
        size = size / Math.pow(blockSize, digitGroups);

        return String.format(
                String.format("%s %%sB", digitGroups == 0 ? "%,.0f" : "%,.2f"),
                size, units[digitGroups]);
    }// sizeToStr()

    /**
     * Converts a percentage to string.
     * 
     * @param percent
     * @return
     */
    public static String percentToStr(float percent) {
        return percentToStr((double) percent);
    }// percentToStr()

    /**
     * Converts a percentage to string.
     * 
     * @param percent
     * @return
     */
    public static String percentToStr(double percent) {
        if (percent == 0)
            return "0%";
        else if (percent < 100)
            return String.format("%02.02f%%", percent);
        else
            return "100%";
    }// percentToStr()

    /**
     * Checks whether {@code s} is empty or {@code null}.
     * 
     * @param s
     *            the string to check.
     * @return {@code true} or {@code false}.
     */
    public static boolean isEmpty(CharSequence s) {
        return s == null || s.length() == 0;
    }// isEmpty()

    /**
     * Formats {@code date} based on current locale.
     * 
     * @param date
     *            the date.
     * @return the formatted string of {@code date}.
     */
    public static String formatDate(Date date) {
        return DateFormat.getInstance().format(date);
    }// formatDate()
}
