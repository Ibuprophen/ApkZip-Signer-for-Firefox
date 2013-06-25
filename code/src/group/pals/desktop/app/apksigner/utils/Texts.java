/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

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
     * "SHA-1"
     */
    public static final String SHA1 = "SHA-1";

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
        if (percent == 0) {
            return "0%";
        } else if (percent < 100) {
            return String.format("%02.02f%%", percent);
        } else {
            return "100%";
        }
    }// percentToStr()
}
