/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.util.Base64;

/**
 * Network utilities.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Network {

    /**
     * The network timeout, in milliseconds.
     */
    public static final int NETWORK_TIMEOUT = 15000;

    /**
     * HTTP status code OK.
     */
    public static final int HTTP_STATUS_OK = 200;

    /**
     * The redirection status code {@code 302}.
     */
    public static final int HTTP_STATUS_FOUND = 302;

    /**
     * Max redirection allowed.
     */
    public static final int MAX_REDIRECTION_ALLOWED = 9;

    /**
     * Header field "Location".
     */
    public static final String HEADER_LOCATION = "Location";

    /**
     * The date format of header fields.
     */
    public static final String HEADER_DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss z";

    /**
     * Header field "Expires".
     */
    public static final String HEADER_EXPIRES = "Expires";

    private static final String PROPERTY_SYS_HTTP_PROXY_HOST = "http.proxyHost";
    private static final String PROPERTY_SYS_HTTP_PROXY_PORT = "http.proxyPort";

    private static final String PROPERTY_SYS_HTTPS_PROXY_HOST = "https.proxyHost";
    private static final String PROPERTY_SYS_HTTPS_PROXY_PORT = "https.proxyPort";

    /**
     * Opens new connection to {@code url} with default settings.
     * 
     * @param url
     *            the URL.
     * @return the connection. Or {@code null} if an error occurred.
     */
    public static HttpURLConnection openHttpConnection(String url) {
        if (Texts.isEmpty(url))
            return null;

        if (Preferences.getInstance().isUsingProxy()) {
            /*
             * HTTP
             */
            System.setProperty(PROPERTY_SYS_HTTP_PROXY_HOST, Preferences
                    .getInstance().getProxyHost());
            System.setProperty(PROPERTY_SYS_HTTP_PROXY_PORT,
                    Integer.toString(Preferences.getInstance().getProxyPort()));

            /*
             * HTTPS
             */
            System.setProperty(PROPERTY_SYS_HTTPS_PROXY_HOST, Preferences
                    .getInstance().getProxyHost());
            System.setProperty(PROPERTY_SYS_HTTPS_PROXY_PORT,
                    Integer.toString(Preferences.getInstance().getProxyPort()));
        } else {
            for (String s : new String[] { PROPERTY_SYS_HTTP_PROXY_HOST,
                    PROPERTY_SYS_HTTP_PROXY_PORT,
                    PROPERTY_SYS_HTTPS_PROXY_HOST,
                    PROPERTY_SYS_HTTPS_PROXY_PORT })
                System.clearProperty(s);
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();

            if (Preferences.getInstance().isUsingProxy()
                    && !Texts.isEmpty(Preferences.getInstance()
                            .getProxyUsername())) {
                char[] password = Preferences.getInstance().getProxyPassword();
                String proxyAuthorization = Base64
                        .encodeToString(
                                (Preferences.getInstance().getProxyUsername()
                                        + ":" + (password != null ? new String(
                                        password) : "")).getBytes(Texts.UTF8),
                                Base64.DEFAULT);
                // https://en.wikipedia.org/wiki/List_of_HTTP_header_fields
                conn.setRequestProperty("Proxy-Authorization", "Basic "
                        + proxyAuthorization);
            }

            conn.setConnectTimeout(NETWORK_TIMEOUT);
            conn.setReadTimeout(NETWORK_TIMEOUT);
            return conn;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            /*
             * Perhaps there is no available Internet connections.
             */
            e.printStackTrace();
            return null;
        }
    }// openHttpConnection()
}
