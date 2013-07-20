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

    private static final String PROPERTY_SYS_PROXY_HOST = "http.proxyHost";
    private static final String PROPERTY_SYS_PROXY_PORT = "http.proxyPort";

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
            System.setProperty(PROPERTY_SYS_PROXY_HOST, Preferences
                    .getInstance().getProxyHost());
            System.setProperty(PROPERTY_SYS_PROXY_PORT,
                    Integer.toString(Preferences.getInstance().getProxyPort()));
        } else {
            System.clearProperty(PROPERTY_SYS_PROXY_HOST);
            System.clearProperty(PROPERTY_SYS_PROXY_PORT);
        }

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url)
                    .openConnection();
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
