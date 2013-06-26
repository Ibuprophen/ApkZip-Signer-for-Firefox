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
        try {
            return new File(System.getProperty("java.class.path"))
                    .getParentFile();
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }// getAppDir()
}
