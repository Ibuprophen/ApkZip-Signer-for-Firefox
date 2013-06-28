/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.io.File;

/**
 * System utilities.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Sys {

    /**
     * Debug flag.
     */
    public static final boolean DEBUG = false;

    /**
     * The app name.
     */
    public static final String APP_NAME = "apk-signer";

    /**
     * The app version code.
     */
    public static final int APP_VERSION_CODE = 17;

    /**
     * The app version name.
     */
    public static final String APP_VERSION_NAME = "1.6.8";

    /**
     * Gets the app directory.
     * 
     * @return the app directory.
     */
    public static File getAppDir() {
        L.i("getAppDir() >> %s", ClassLoader.getSystemResource(Texts.EMPTY));

        try {
            return new File(ClassLoader.getSystemResource(Texts.EMPTY)
                    .getPath());
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }// getAppDir()
}
