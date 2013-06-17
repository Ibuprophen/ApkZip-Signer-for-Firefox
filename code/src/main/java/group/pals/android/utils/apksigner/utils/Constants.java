/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */
package group.pals.android.utils.apksigner.utils;

/**
 * The constants.
 *
 * @author Hai Bison
 */
public class Constants {

    /**
     * Regex to filter APK files.
     */
    public static final String REGEX_APK_FILES = "(?si).+\\.apk";
    /**
     * Description of APK files.
     */
    public static final String DESC_APK_FILES = "APK Files (*.apk)";
    /**
     * Regex to filter keystore files.
     */
    public static final String REGEX_KEYSTORE_FILES = "(?si).+\\.keystore";
    /**
     * Description of keystore files.
     */
    public static final String DESC_KEYSTORE_FILES = "Keystore Files (*.keystore)";
    /**
     * File extension of APK files.
     */
    public static final String FILE_EXT_APK = ".apk";
    /**
     * File extension of keystore files.
     */
    public static final String FILE_EXT_KEYSTORE = ".keystore";
}
