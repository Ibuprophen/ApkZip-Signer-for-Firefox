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
 * @author Hai Bison
 */
public class Prefs {

    private static Prefs instance;
    
    public static Prefs getInstance() {
        if (instance == null)
            instance = new Prefs();
        return instance;
    }//getInstance
    
    public static String PrefsFilename = ".prefs";
    private final Properties P;
    
    private Prefs() {
        P = new Properties();
        
        try {
            FileReader r = new FileReader(
                    Sys.getStartupDir().getAbsolutePath() + "/" + PrefsFilename);
            try {
                P.load(r);
            } finally {
                r.close();
            }
        } catch (Exception e) {
            System.err.printf("[%s] Error loading preferences: %s\n", Prefs.class.getName(), e);
        }
    }//Prefs

    public void store() {
        try {
            FileWriter w = new FileWriter(
                    Sys.getStartupDir().getAbsolutePath() + "/" + PrefsFilename);
            try {
                P.store(w, null);
            } finally {
                w.close();
            }
        } catch (Exception e) {
            System.err.printf("[%s] Error storing preferences: %s\n", Prefs.class.getName(), e);
        }
    }
    
    public void set(String k, String v) {
        P.setProperty(k, v != null ? v.trim() : "");
        store();
    }
    
    public String get(String k) {
        return P.getProperty(k);
    }

    public String get(String k, String def) {
        return P.getProperty(k, def);
    }
}
