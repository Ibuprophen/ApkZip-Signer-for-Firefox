/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.services;

/**
 * Application updater service.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Updater extends Thread {

    /**
     * The URL pointing to `update.properties` file.
     */
    public static final String URL_UPDATE_PROPERTIES = "https://apk-signer.googlecode.com/hg/bin/update.properties";
}
