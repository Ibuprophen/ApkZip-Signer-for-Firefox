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
