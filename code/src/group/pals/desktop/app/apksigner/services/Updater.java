/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.services;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.services.INotification.Message;
import group.pals.desktop.app.apksigner.utils.Network;
import group.pals.desktop.app.apksigner.utils.Texts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * Application updater service.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Updater extends Thread {

    /**
     * The URL pointing to `update.properties` file.
     */
    public static final String URL_UPDATE_PROPERTIES = "https://apk-signer.googlecode.com/hg/bin/update.properties";

    /**
     * The app version code.
     */
    public static final String KEY_APP_VERSION_CODE = "app_version_code";

    /**
     * The app version name.
     */
    public static final String KEY_APP_VERSION_NAME = "app_version_name";

    /**
     * The download URI.
     */
    public static final String KEY_DOWNLOAD_URI = "download_uri";

    /**
     * The SHA-1 of new file.
     */
    public static final String KEY_SHA1 = "SHA-1";

    /**
     * Maximum filesize allowed for `update.properties`.
     */
    public static final int MAX_UPDATE_PROPERTIES_FILESIZE = 9 * 1024;

    /**
     * Maximum filesize allowed for the new version.
     */
    public static final int MAX_UPDATE_FILESIZE = 9 * 1024 * 1024;

    /**
     * File handling buffer (reading, writing...).
     */
    private static final int FILE_BUFFER = 99 * 1024;

    /**
     * There is a local update file available.
     */
    public static final int MSG_LOCAL_UPDATE_AVAILABLE = 0;

    /**
     * The update filesize exceeds limit.
     */
    public static final int MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT = 1;

    private INotification mNotification;

    /**
     * Sets the notification.
     * 
     * @param notification
     *            the notification to set.
     * @return the instance of this service, to allow chaining multiple calls
     *         into a single statement.
     */
    public Updater setNotification(INotification notification) {
        mNotification = notification;
        return this;
    }// setNotification()

    /**
     * Gets the notification.
     * 
     * @return the notification.
     */
    public INotification getNotification() {
        return mNotification;
    }// getNotification()

    /**
     * Sends notification to listener.
     * 
     * @param msgId
     *            the message ID.
     * @param shortMsg
     *            the short message.
     * @param detailedMsg
     *            the detailed message.
     * @return the result of {@link INotification#onMessage(Message)}, or
     *         {@code false} if there is no listener.
     */
    private boolean sendNotification(int msgId, String shortMsg,
            String detailedMsg) {
        if (getNotification() == null)
            return false;

        Message msg = new Message();
        msg.id = msgId;
        msg.shortMessage = shortMsg;
        msg.detailedMessage = detailedMsg;

        return getNotification().onMessage(msg);
    }// sendNotification()

    @Override
    public void run() {
        /*
         * DOWNLOAD UPDATE.PROPERTIES AND PARSE INFO TO MEMORY
         */
        final Properties updateProperties = downloadUpdateProperties();
        if (updateProperties == null || isInterrupted())
            return;

        try {
            if (Integer.parseInt(Messages.getString(KEY_APP_VERSION_CODE)) >= Integer
                    .parseInt(updateProperties
                            .getProperty(KEY_APP_VERSION_CODE)))
                return;
        } catch (Throwable t) {
            /*
             * Can be number format exception or NPE...
             */
            return;
        }

        /*
         * CHECK TO SEE IF THE UPDATE FILE HAS BEEN DOWNLOADED BEFORE
         */
        if (isInterrupted() || checklocalUpdateFile(updateProperties))
            return;

        /*
         * DOWNLOAD THE UPDATE FILE
         */
    }// run()

    /**
     * Downloads the `update.properties` file from server.
     * 
     * @return the {@link Properties} object containing update information. Or
     *         {@code null} if an error occurred.
     */
    private Properties downloadUpdateProperties() {
        HttpURLConnection conn = Network
                .openHttpConnection(URL_UPDATE_PROPERTIES);
        if (conn == null)
            return null;

        try {
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            if (conn.getContentLength() > MAX_UPDATE_PROPERTIES_FILESIZE) {
                inputStream.close();
                return null;
            }

            Properties result = new Properties();
            result.load(inputStream);
            inputStream.close();

            return result;
        } catch (IOException e) {
            /*
             * Ignore it.
             */
            e.printStackTrace();
            return null;
        } catch (NullPointerException e) {
            /*
             * Ignore it.
             */
            return null;
        }
    }// downloadUpdateProperties()

    /**
     * Checks to see if there is update file which has been downloaded before.
     * 
     * @param updateProperties
     *            the update information.
     * @return {@code true} or {@code false}.
     */
    private boolean checklocalUpdateFile(Properties updateProperties) {
        URL downloadUri;
        try {
            downloadUri = new URL(updateProperties.getProperty(
                    URL_UPDATE_PROPERTIES, Texts.EMPTY));
        } catch (MalformedURLException e) {
            /*
             * Ignore it.
             */
            e.printStackTrace();
            return false;
        }

        File file = new File(downloadUri.getFile());
        if (file.isFile()) {
            /*
             * Check SHA-1.
             */
            try {
                MessageDigest md = MessageDigest.getInstance(Texts.SHA1);

                final byte[] buf = new byte[FILE_BUFFER];
                int read;
                FileInputStream inputStream = new FileInputStream(file);
                try {
                    while ((read = inputStream.read(buf)) > 0) {
                        if (isInterrupted())
                            return false;
                        md.update(buf, 0, read);
                    }
                } finally {
                    inputStream.close();
                }

                BigInteger bi = new BigInteger(1, md.digest());
                final boolean result = updateProperties.getProperty(KEY_SHA1)
                        .equalsIgnoreCase(
                                String.format("%0" + (md.getDigestLength() * 2)
                                        + "x", bi));
                if (result)
                    sendNotification(MSG_LOCAL_UPDATE_AVAILABLE,
                            Messages.getString("msg_local_update_available"),
                            String.format(Messages
                                    .getString("pmsg_local_update_available"),
                                    file.getAbsolutePath(), updateProperties
                                            .getProperty(KEY_APP_VERSION_NAME)));
                return result;
            } catch (NoSuchAlgorithmException e) {
                /*
                 * Never catch this.
                 */
                e.printStackTrace();
                return false;
            } catch (FileNotFoundException e) {
                /*
                 * Never catch this.
                 */
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                /*
                 * Ignore it.
                 */
                return false;
            } catch (NullPointerException e) {
                return false;
            }
        }// file.isFile()
        else
            return false;
    }// checklocalUpdateFile()

    /**
     * Downloads the update file.
     * 
     * @param updateProperties
     *            the update information.
     */
    private void downloadUpdateFile(Properties updateProperties) {
        HttpURLConnection conn = Network.openHttpConnection(updateProperties
                .getProperty(KEY_DOWNLOAD_URI));
        if (conn == null)
            return;

        try {
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            try {
                final int length = conn.getContentLength();
                if (length == 0)
                    return;
                if (length > 0 && length > MAX_UPDATE_FILESIZE) {
                    sendNotification(
                            MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                            Messages.getString("msg_cancelled_update"),
                            String.format(
                                    Messages.getString("pmsg_update_filesize_exceeds_limit"),
                                    Texts.sizeToStr(length), Texts
                                            .sizeToStr(MAX_UPDATE_FILESIZE)));
                    return;
                }

                File targetFile = new File(conn.getURL().getFile());

                if (length > 0 && targetFile.getParentFile() != null
                        && targetFile.getFreeSpace() <= length * 1.5) {
                    sendNotification(MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                            Messages.getString("msg_cancelled_update"),
                            String.format(Messages
                                    .getString("pmsg_available_space_is_low"),
                                    Texts.sizeToStr(targetFile.getParentFile()
                                            .getFreeSpace())));
                    return;
                }

                OutputStream outputStream = new FileOutputStream(targetFile);
                try {
                    final MessageDigest md = MessageDigest
                            .getInstance(Texts.SHA1);
                    byte[] buf = new byte[FILE_BUFFER];
                    int read;
                    long totalRead = 0;
                    while ((read = inputStream.read(buf)) > 0) {
                        if (isInterrupted()) {
                            outputStream.close();
                            targetFile.delete();
                            return;
                        }

                        outputStream.write(buf, 0, read);
                        totalRead += read;

                        md.update(buf, 0, read);

                        long freeSpace = 0;
                        if (targetFile.getParentFile() != null)
                            freeSpace = targetFile.getParentFile()
                                    .getFreeSpace();

                        if (freeSpace < totalRead * 1.5) {
                            sendNotification(
                                    MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                                    Messages.getString("msg_cancelled_update"),
                                    String.format(
                                            Messages.getString("pmsg_available_space_is_low"),
                                            Texts.sizeToStr(freeSpace)));
                            return;
                        }
                    }// while

                    /*
                     * CHECK SHA-1
                     */
                    BigInteger bi = new BigInteger(1, md.digest());
                    if (updateProperties.getProperty(KEY_SHA1)
                            .equalsIgnoreCase(
                                    String.format("%0"
                                            + (md.getDigestLength() * 2) + "x",
                                            bi))) {
                    }
                } catch (NoSuchAlgorithmException e) {
                    /*
                     * Never catch this.
                     */
                } finally {
                    outputStream.close();
                }
            } finally {
                if (inputStream != null)
                    inputStream.close();
            }
        } catch (IOException e) {
            /*
             * Ignore it.
             */
            e.printStackTrace();
        }
    }// downloadUpdateFile()
}
