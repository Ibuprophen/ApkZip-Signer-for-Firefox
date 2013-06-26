/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.i18n;

import java.beans.Beans;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Manager for i18n strings.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Messages {

    // //////////////////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    // //////////////////////////////////////////////////////////////////////////
    private Messages() {
        // do not instantiate
    }// Message()

    // //////////////////////////////////////////////////////////////////////////
    //
    // Bundle access
    //
    // //////////////////////////////////////////////////////////////////////////
    private static final String BUNDLE_NAME = "group.pals.desktop.app.apksigner.i18n.messages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = loadBundle();

    private static ResourceBundle loadBundle() {
        return ResourceBundle.getBundle(BUNDLE_NAME);
    }// loadBundle()

    // //////////////////////////////////////////////////////////////////////////
    //
    // Strings access
    //
    // //////////////////////////////////////////////////////////////////////////
    public static String getString(String key) {
        try {
            ResourceBundle bundle = Beans.isDesignTime() ? loadBundle()
                    : RESOURCE_BUNDLE;
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            return "!" + key + "!";
        }
    }// getString()

    /**
     * Gets a formattable string and format it with {@code args}.
     * 
     * @param key
     *            the key.
     * @param args
     *            the format arguments.
     * @return the formatted string.
     */
    public static String getString(String key, Object... args) {
        return String.format(getString(key), args);
    }// getString()
}
