/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.i18n;

import group.pals.desktop.app.apksigner.utils.Preferences;

import java.beans.Beans;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
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

    /**
     * Default locale.
     */
    public static final String DEFAULT_LOCALE = "en";

    /**
     * Map of available locale tags to their human readable names.
     */
    public static final Map<String, String> AVAILABLE_LOCALES = new LinkedHashMap<String, String>();

    static {
        AVAILABLE_LOCALES.put(DEFAULT_LOCALE, "English (Default)");
        AVAILABLE_LOCALES.put("vi", "Vietnamese (Tiếng Việt)");
    }// static

    private Messages() {
        // do not instantiate
    }// Message()

    private static final String BUNDLE_NAME = "group.pals.desktop.app.apksigner.i18n.messages"; //$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = loadBundle();
    private static ResourceBundle mDefaultResourceBundle;

    /**
     * Loads message resource.
     * 
     * @return the resource bundle which contains message resource.
     */
    private static ResourceBundle loadBundle() {
        String localeTag = Preferences.getInstance().getLocaleTag();
        if (!AVAILABLE_LOCALES.containsKey(localeTag)) {
            Preferences.getInstance().setLocaleTag(DEFAULT_LOCALE);
            localeTag = DEFAULT_LOCALE;
        }
        return ResourceBundle.getBundle(BUNDLE_NAME,
                Locale.forLanguageTag(localeTag));
    }// loadBundle()

    /**
     * Gets a string for current locale by its key. If it's not available for
     * current locale, the default string in built-in locale (English) will
     * return.
     * 
     * @param key
     *            the string's key.
     * @return the string, or {@code null} if not found.
     */
    private static String getString(String key) {
        try {
            ResourceBundle bundle = Beans.isDesignTime() ? loadBundle()
                    : RESOURCE_BUNDLE;
            if (bundle.containsKey(key))
                return bundle.getString(key);

            /*
             * Try to find the `key` in default locale.
             */
            if (!new Locale(DEFAULT_LOCALE).getLanguage().equals(
                    bundle.getLocale().getLanguage())) {
                if (mDefaultResourceBundle == null)
                    mDefaultResourceBundle = ResourceBundle.getBundle(
                            BUNDLE_NAME, Locale.forLanguageTag(DEFAULT_LOCALE));
                return mDefaultResourceBundle.getString(key);
            }

            return null;
        } catch (MissingResourceException e) {
            return null;
        }
    }// getString()

    /**
     * Map of resources IDs to their name.
     */
    private static final Map<Integer, String> MAP_IDS = new HashMap<Integer, String>();

    /**
     * Gets a string by its resource ID. If it's not available for current
     * locale, the default string in built-in locale (English) will return.
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
