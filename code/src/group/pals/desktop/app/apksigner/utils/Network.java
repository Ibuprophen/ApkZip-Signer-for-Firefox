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
