/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

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

    public static final String PREFS_FILENAME = "apk-signer.preferences";

    private final File mPropertiesFile = new File(Sys.getAppDir()
            .getAbsolutePath() + File.separator + PREFS_FILENAME);
    private final Properties mProperties = new Properties();

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
            System.err.printf("[%s] Error loading preferences: %s\n",
                    CLASSNAME, e);
        }
    }// Preferences()

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
            System.err.printf("[%s] Error storing preferences: %s\n",
                    CLASSNAME, e);
        }
    }// store()

    /**
     * Sets a preference.
     * 
     * @param k
     *            the key name.
     * @param v
     *            the value of the key.
     */
    public void set(String k, String v) {
        mProperties.setProperty(k, v != null ? v.trim() : "");
        store();
    }// set()

    /**
     * Gets value of a key.
     * 
     * @param k
     *            the key name.
     * @return the value of the given key.
     */
    public String get(String k) {
        return mProperties.getProperty(k);
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
        return mProperties.getProperty(k, def);
    }// get()

    /*
     * PREFERENCES
     */

    public static final String KEY_JDK_PATH = "JDK_PATH";

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
     */
    public void setJdkPath(File path) {
        if (path != null)
            set(KEY_JDK_PATH, path.getAbsolutePath());
        else
            mProperties.remove(KEY_JDK_PATH);
    }// setJdkPath()
}
