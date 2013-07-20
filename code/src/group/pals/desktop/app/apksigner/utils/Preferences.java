/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.i18n.Messages;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 * Convenient class for storing/ loading preferences.
 * 
 * @author Hai Bison
 */
public class Preferences {

    /**
     * Used for debugging...
     */
    private static final String CLASSNAME = Preferences.class.getName();

    private static Preferences mInstance;

    /**
     * Gets the global instance of this class.
     * 
     * @return the global instance of this class.
     */
    public static Preferences getInstance() {
        if (mInstance == null)
            mInstance = new Preferences();
        return mInstance;
    }// getInstance()

    public static final String PREFS_FILENAME = Sys.APP_NAME + ".preferences";

    private final File mPropertiesFile = new File(Sys.getAppDir()
            .getAbsolutePath() + File.separator + PREFS_FILENAME);
    private final Properties mProperties = new Properties();
    private Properties mTransaction;

    /**
     * Creates new instance.
     */
    private Preferences() {
        L.d("Preferences() >> file = %s", mPropertiesFile);
        try {
            FileReader reader = new FileReader(mPropertiesFile);
            try {
                mProperties.load(reader);
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            L.e("[%s] Error loading preferences: %s", CLASSNAME, e);
        }
    }// Preferences()

    /**
     * Begins a transaction. Currently this method supports only one instance of
     * a transaction. This mean calling this method multiple times has only one
     * affect.
     * 
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     * @see #endTransaction()
     */
    public synchronized Preferences beginTransaction() {
        if (mTransaction == null)
            mTransaction = new Properties();
        return this;
    }// beginTransaction()

    /**
     * Ends a transaction.
     * 
     * @see #beginTransaction()
     */
    public synchronized void endTransaction() {
        if (mTransaction == null)
            return;

        mProperties.putAll(mTransaction);

        destroyTransaction();
    }// endTransaction()

    /**
     * Cancels a transaction.
     * 
     * @see #beginTransaction()
     */
    public synchronized void cancelTransaction() {
        destroyTransaction();
    }// cancelTransaction()

    /**
     * Destroys the transaction.
     */
    private synchronized void destroyTransaction() {
        if (mTransaction == null)
            return;

        mTransaction.clear();
        mTransaction = null;
    }// destroyTransaction()

    /**
     * Stores all preferences to file.
     */
    public void store() {
        try {
            FileWriter writer = new FileWriter(mPropertiesFile);
            try {
                mProperties.store(writer, null);
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            L.e("[%s] Error storing preferences: %s", CLASSNAME, e);
        }
    }// store()

    /**
     * Sets a preference.
     * 
     * @param k
     *            the key name.
     * @param v
     *            the value of the key.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences set(String k, String v) {
        Properties p = mTransaction != null ? mTransaction : mProperties;
        p.setProperty(k, v != null ? v.trim() : "");
        return this;
    }// set()

    /**
     * Gets value of a key.
     * 
     * @param k
     *            the key name.
     * @return the value of the given key.
     */
    public String get(String k) {
        return get(k, null);
    }// get()

    /**
     * Gets value of a key.
     * 
     * @param k
     *            the key name.
     * @param def
     *            the default value if the given key does not exist.
     * @return the value of the given key, or {@code def} if the given key does
     *         not exist.
     */
    public String get(String k, String def) {
        String v;

        if (mTransaction != null && mTransaction.containsKey(k))
            v = mTransaction.getProperty(k, def);
        else
            v = mProperties.getProperty(k, def);

        return v;
    }// get()

    /*
     * PREFERENCES
     */

    public static final String KEY_JDK_PATH = "JDK_PATH";
    public static final String KEY_LOCALE_TAG = "locale_tag";

    /**
     * Gets JDK path.
     * 
     * @return the JDK path, or {@code null} if not available.
     */
    public File getJdkPath() {
        String path = get(KEY_JDK_PATH);
        return path == null ? null : new File(path);
    }// getJdkPath()

    /**
     * Sets the JDK path.
     * 
     * @param path
     *            the JDK path.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setJdkPath(File path) {
        if (path != null)
            set(KEY_JDK_PATH, path.getAbsolutePath());
        else
            mProperties.remove(KEY_JDK_PATH);
        return this;
    }// setJdkPath()

    /**
     * Gets the locale.
     * 
     * @return the locale tag, default is {@link Messages#DEFAULT_LOCALE}.
     */
    public String getLocaleTag() {
        return get(KEY_LOCALE_TAG, Messages.DEFAULT_LOCALE);
    }// getLocaleTag()

    /**
     * Sets the locale tag.
     * 
     * @param tag
     *            the locale tag.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setLocaleTag(String tag) {
        if (Texts.isEmpty(tag))
            mProperties.remove(KEY_LOCALE_TAG);
        else
            set(KEY_LOCALE_TAG, tag);
        return this;
    }// setLocaleTag()
}
