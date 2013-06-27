/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.i18n;

import java.beans.Beans;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
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

    /**
     * Map of resources IDs to their name.
     */
    private static final Map<Integer, String> MAP_IDS = new HashMap<Integer, String>();

    /**
     * Gets a string by its resource ID.
     * 
     * @param resId
     *            the resource ID.
     * @return the string, or {@code null} if not found.
     */
    public static String getString(int resId) {
        if (MAP_IDS.containsKey(resId))
            return getString(MAP_IDS.get(resId));

        Field[] fields = R.string.class.getFields();
        for (Field f : fields) {
            if (Modifier.isStatic(f.getModifiers())
                    && f.getType().isAssignableFrom(Integer.TYPE)) {
                try {
                    if (f.getInt(null) == resId) {
                        MAP_IDS.put(resId, f.getName());
                        return getString(f.getName());
                    }
                } catch (IllegalArgumentException e) {
                    /*
                     * Never catch this, we checked the object type before.
                     */
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    /*
                     * Never catch this, since we asked for all *public* fields.
                     */
                    e.printStackTrace();
                }
            }
        }

        return null;
    }// getString()

    /**
     * Gets a formattable string and format it with {@code args}.
     * 
     * @param resId
     *            the resource ID.
     * @param args
     *            the format arguments.
     * @return the formatted string.
     * @throws NullPointerException
     *             if the resource is not found.
     */
    public static String getString(int resId, Object... args) {
        return String.format(getString(resId), args);
    }// getString()
}
