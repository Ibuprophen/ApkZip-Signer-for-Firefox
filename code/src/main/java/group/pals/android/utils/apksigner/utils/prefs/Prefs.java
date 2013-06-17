/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */
package group.pals.android.utils.apksigner.utils.prefs;

import group.pals.android.utils.apksigner.utils.Sys;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

/**
 * Convenient class for storing/ loading preferences.
 *
 * @author Hai Bison
 */
public class Prefs {

    private static Prefs mInstance;

    /**
     * Gets the global instance of this class.
     *
     * @return the global instance of this class.
     */
    public static Prefs getInstance() {
        if (mInstance == null) {
            mInstance = new Prefs();
        }

        return mInstance;
    }//getInstance()
    public static final String PREFS_FILENAME = ".prefs";
    private final Properties mProperties;

    /**
     * Creates new instance.
     */
    private Prefs() {
        mProperties = new Properties();

        try {
            FileReader r = new FileReader(
                    Sys.getStartupDir().getAbsolutePath() + "/" + PREFS_FILENAME);
            try {
                mProperties.load(r);
            } finally {
                r.close();
            }
        } catch (Exception e) {
            System.err.printf("[%s] Error loading preferences: %s\n", Prefs.class.getName(), e);
        }
    }//Prefs()

    /**
     * Stores all preferences to file.
     */
    public void store() {
        try {
            FileWriter w = new FileWriter(
                    Sys.getStartupDir().getAbsolutePath() + "/" + PREFS_FILENAME);
            try {
                mProperties.store(w, null);
            } finally {
                w.close();
            }
        } catch (Exception e) {
            System.err.printf("[%s] Error storing preferences: %s\n", Prefs.class.getName(), e);
        }
    }//store()

    /**
     * Sets a preference.
     *
     * @param k the key name.
     * @param v the value of the key.
     */
    public void set(String k, String v) {
        mProperties.setProperty(k, v != null ? v.trim() : "");
        store();
    }//set()

    /**
     * Gets value of a key.
     *
     * @param k the key name.
     * @return the value of the given key.
     */
    public String get(String k) {
        return mProperties.getProperty(k);
    }//get()

    /**
     * Gets value of a key.
     *
     * @param k the key name.
     * @param def the default value if the given key does not exist.
     * @return the value of the given key, or {@code def} if the given key does
     * not exist.
     */
    public String get(String k, String def) {
        return mProperties.getProperty(k, def);
    }//get()
}
