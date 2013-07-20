/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.i18n.Messages;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;
import java.util.UUID;

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
            Reader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mPropertiesFile), Texts.UTF8));
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
            Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(mPropertiesFile), Texts.UTF8));
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
     *            the value of the key. If {@code null}, key {@code k} will be
     *            removed.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences set(String k, String v) {
        Properties p = mTransaction != null ? mTransaction : mProperties;

        if (v != null)
            p.setProperty(k, v.trim());
        else
            p.remove(k);

        return this;
    }// set()

    /**
     * Encrypts and sets a preference.
     * 
     * @param k
     *            the key name.
     * @param v
     *            the value of the key. If {@code null}, key {@code k} will be
     *            removed.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences xSet(String k, String v) {
        return set(
                k,
                v != null ? SimpleWeakEncryption.encrypt(
                        getUid().toCharArray(), v) : null);
    }// xSet()

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

    /**
     * Gets and decrypts value of a key.
     * 
     * @param k
     *            the key name.
     * @return the value of the given key.
     */
    public String xGet(String k) {
        return xGet(k, null);
    }// xGet()

    /**
     * Gets and decrypts value of a key.
     * 
     * @param k
     *            the key name.
     * @param def
     *            the default value if the given key does not exist.
     * @return the value of the given key, or {@code def} if the given key does
     *         not exist.
     */
    public String xGet(String k, String def) {
        String v = get(k);
        if (v == null)
            return def;
        return SimpleWeakEncryption.decrypt(getUid().toCharArray(), v);
    }// xGet()

    /*
     * PREFERENCES
     */

    public static final String KEY_JDK_PATH = "JDK_PATH";
    public static final String KEY_LOCALE_TAG = "locale_tag";
    public static final String KEY_UID = "uid";
    public static final String KEY_NETWORK_USE_PROXY = "network.use_proxy";
    public static final String KEY_NETWORK_PROXY_HOST = "network.proxy.host";
    public static final String KEY_NETWORK_PROXY_PORT = "network.proxy.port";
    public static final String KEY_NETWORK_PROXY_USERNAME = "network.proxy.username";
    public static final String KEY_NETWORK_PROXY_PASSWORD = "network.proxy.password";

    /**
     * Gets global unique ID.
     * 
     * @return the global unique ID.
     */
    protected String getUid() {
        String res = null;

        if (mProperties.containsKey(KEY_UID))
            res = mProperties.getProperty(KEY_UID, null);

        if (Texts.isEmpty(res)) {
            res = UUID.randomUUID().toString();
            set(KEY_UID, res);
        }

        return res;
    }// getUid()

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
        return set(KEY_JDK_PATH, path != null ? path.getAbsolutePath() : null);
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
        return set(KEY_LOCALE_TAG, tag);
    }// setLocaleTag()

    /**
     * Checks if we're using a proxy.
     * 
     * @return {@code true} or {@code false}.
     */
    public boolean isUsingProxy() {
        return Boolean.toString(true).equals(
                get(KEY_NETWORK_USE_PROXY, Boolean.toString(false)));
    }// isUsingProxy()

    /**
     * Sets using proxy.
     * 
     * @param v
     *            {@code true} or {@code false}.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setUsingProxy(boolean v) {
        return set(KEY_NETWORK_USE_PROXY, Boolean.toString(v));
    }// setUsingProxy()

    /**
     * Gets the proxy host address.
     * 
     * @return the proxy host address.
     */
    public String getProxyHost() {
        return get(KEY_NETWORK_PROXY_HOST);
    }// getProxyHost()

    /**
     * Sets the proxy host address.
     * 
     * @param v
     *            the proxy host address.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setProxyHost(String v) {
        return set(KEY_NETWORK_PROXY_HOST, v);
    }// setProxyHost()

    /**
     * Gets the proxy port.
     * 
     * @return the proxy port, or {@code -1} if not set.
     */
    public int getProxyPort() {
        int res;
        try {
            res = Integer.parseInt(get(KEY_NETWORK_PROXY_PORT,
                    Integer.toString(-1)));
        } catch (Exception e) {
            res = -1;
        }

        return res;
    }// getProxyPort()

    /**
     * Sets the proxy port.
     * 
     * @param v
     *            the proxy port.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setProxyPort(int v) {
        return set(KEY_NETWORK_PROXY_PORT, Integer.toString(v));
    }// setProxyPort()

    /**
     * Gets the proxy username.
     * 
     * @return the proxy username.
     */
    public String getProxyUsername() {
        return xGet(KEY_NETWORK_PROXY_USERNAME);
    }// getProxyUsername()

    /**
     * Sets the proxy username.
     * 
     * @param v
     *            the proxy username.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setProxyUsername(String v) {
        return xSet(KEY_NETWORK_PROXY_USERNAME, v);
    }// setProxyUsername()

    /**
     * Gets the proxy password.
     * 
     * @return the proxy password.
     */
    public char[] getProxyPassword() {
        String v = xGet(KEY_NETWORK_PROXY_PASSWORD);
        return v != null ? v.toCharArray() : null;
    }// getProxyPassword()

    /**
     * Sets the proxy password.
     * 
     * @param v
     *            the proxy password.
     * @return the instance of this object, to allow chaining multiple calls
     *         into a single statement.
     */
    public Preferences setProxyPassword(char[] v) {
        return xSet(KEY_NETWORK_PROXY_PASSWORD, v != null ? new String(v)
                : null);
    }// setProxyPassword()
}
