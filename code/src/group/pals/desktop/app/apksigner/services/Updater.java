/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.services;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.utils.Files;
import group.pals.desktop.app.apksigner.utils.Hasher;
import group.pals.desktop.app.apksigner.utils.L;
import group.pals.desktop.app.apksigner.utils.Network;
import group.pals.desktop.app.apksigner.utils.SpeedTracker;
import group.pals.desktop.app.apksigner.utils.Sys;
import group.pals.desktop.app.apksigner.utils.Texts;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Application updater service.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Updater extends BaseThread {

    private static final String CLASSNAME = Updater.class.getName();

    /**
     * The URLs pointing to `update.properties` file. We should try them all (in
     * case one of them doesn't exist, or for some reason we can't reach it).
     */
    public static final String[] URLS_UPDATE_PROPERTIES = {
            "https://bitbucket.org/haibisonapps/apk-signer/downloads/update.properties",
            "https://sites.google.com/site/haibisonapps/apps/apk-signer/update.properties" };

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
     * The download filename.
     */
    public static final String KEY_DOWNLOAD_FILENAME = "download_filename";

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
    public static final int MAX_UPDATE_FILESIZE = Sys.DEBUG ? Integer.MAX_VALUE
            : 9 * 1024 * 1024;

    /**
     * There is a local update file available.
     */
    public static final int MSG_LOCAL_UPDATE_AVAILABLE = 1;

    /**
     * The update filesize exceeds limit.
     */
    public static final int MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT = 2;

    /**
     * The update progress (percentage done...)
     */
    public static final int MSG_UPDATE_PROGRESS = 3;

    /**
     * The update cancelled.
     */
    public static final int MSG_UPDATE_CANCELLED = 4;

    /**
     * The update finished.
     */
    public static final int MSG_UPDATE_FINISHED = 5;

    /**
     * Used for debugging...
     */
    private static final String DEBUG_UPDATE_LINK_EXECUTABLE = "http://dlc.sun.com.edgesuite.net/virtualbox/4.2.14/VirtualBox-4.2.14-86644-Linux_amd64.run";

    /**
     * Creates new instance.
     */
    public Updater() {
        super();
        setName(Messages.getString(R.string.updater_service));
    }// Updater()

    @Override
    public void run() {
        try {
            L.i("%s >> starting", Updater.class.getSimpleName());

            /*
             * DOWNLOAD UPDATE.PROPERTIES AND PARSE INFO TO MEMORY
             */
            final Properties updateProperties = downloadUpdateProperties();
            if (updateProperties == null || isInterrupted())
                return;

            L.i("\tCurrent version: %,d (%s) -- Update version: %s",
                    Sys.APP_VERSION_CODE, Sys.APP_VERSION_NAME,
                    updateProperties.getProperty(KEY_APP_VERSION_CODE));

            try {
                if (Sys.APP_VERSION_CODE >= Integer.parseInt(updateProperties
                        .getProperty(KEY_APP_VERSION_CODE)) && !Sys.DEBUG)
                    return;
            } catch (Throwable t) {
                /*
                 * Can be number format exception or NPE...
                 */
                return;
            }

            L.d("\t>> %s", updateProperties);

            /*
             * CHECK TO SEE IF THE UPDATE FILE HAS BEEN DOWNLOADED BEFORE
             */
            if (isInterrupted() || checklocalUpdateFile(updateProperties))
                return;

            /*
             * DOWNLOAD THE UPDATE FILE
             */
            downloadUpdateFile(updateProperties);
        } catch (Exception e) {
            L.e("%s >> %s", Updater.class.getSimpleName(), e);
        } finally {
            L.i("%s >> finishing", Updater.class.getSimpleName());
            sendNotification(MSG_DONE);
        }
    }// run()

    /**
     * Follows the redirection ({@ink Network#HTTP_STATUS_FOUND}) within
     * {@link Network#MAX_REDIRECTION_ALLOWED}.
     * 
     * @param url
     *            the original URL.
     * @return the last <i>established</i>-connection, maybe {@code null} if
     *         could not connect to. You must always re-check the response code
     *         before doing further actions.
     */
    private HttpURLConnection followRedirection(String url) {
        L.i("%s >> followRedirection() >> %s", Updater.class.getSimpleName(),
                url);

        HttpURLConnection conn = Network.openHttpConnection(url);
        if (conn == null)
            return null;

        int redirectCount = 0;
        try {
            conn.connect();
            while (conn.getResponseCode() == Network.HTTP_STATUS_FOUND
                    && redirectCount++ < Network.MAX_REDIRECTION_ALLOWED) {
                final InputStream inputStream = conn.getInputStream();

                /*
                 * Expiration.
                 */
                String field = conn.getHeaderField(Network.HEADER_EXPIRES);
                try {
                    if (!Texts.isEmpty(field)
                            && Calendar.getInstance().after(
                                    new SimpleDateFormat(
                                            Network.HEADER_DATE_FORMAT)
                                            .parse(field))) {
                        L.i("\t%,d is expired (%s)", conn.getResponseCode(),
                                field);
                        inputStream.close();
                        return null;
                    }
                } catch (ParseException e) {
                    /*
                     * Ignore it.
                     */
                    L.e("\tcan't parse '%s', ignoring it...", field);
                }

                /*
                 * Location.
                 */
                field = conn.getHeaderField(Network.HEADER_LOCATION);
                if (Texts.isEmpty(field)) {
                    L.i("\t%,d sends to null", conn.getResponseCode());
                    inputStream.close();
                    return null;
                }

                /*
                 * Close current connection and open the redirected URI.
                 */
                inputStream.close();
                if ((conn = Network.openHttpConnection(field)) == null)
                    return null;

                L.i("\t>> %s", field);
                conn.connect();
            }// while
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return conn;
    }// followRedirection()

    /**
     * Downloads the `update.properties` file from server.
     * 
     * @return the {@link Properties} object containing update information. Or
     *         {@code null} if an error occurred.
     */
    private Properties downloadUpdateProperties() {
        L.i("%s >> downloadUpdateProperties()", Updater.class.getSimpleName());

        for (String url : URLS_UPDATE_PROPERTIES) {
            HttpURLConnection conn = followRedirection(url);
            if (conn == null)
                return null;

            try {
                final InputStream inputStream = new BufferedInputStream(
                        conn.getInputStream(), Files.FILE_BUFFER);
                try {
                    if (conn.getResponseCode() != Network.HTTP_STATUS_OK)
                        continue;
                    if (conn.getContentLength() > MAX_UPDATE_PROPERTIES_FILESIZE)
                        continue;

                    /*
                     * We can load directly from the `InputStream` over the
                     * network, since the size is small.
                     */
                    Properties result = new Properties();
                    result.load(inputStream);
                    return result;
                } finally {
                    inputStream.close();
                }
            } catch (IOException e) {
                /*
                 * Ignore it.
                 */
                e.printStackTrace();

                /*
                 * Maybe the current URL doesn't exist. Try the next one.
                 */
                continue;
            } catch (NullPointerException e) {
                /*
                 * Ignore it.
                 */
                continue;
            }
        }// for URL

        return null;
    }// downloadUpdateProperties()

    /**
     * Checks to see if there is update file which has been downloaded before.
     * 
     * @param updateProperties
     *            the update information.
     * @return {@code true} or {@code false}.
     */
    private boolean checklocalUpdateFile(Properties updateProperties) {
        L.i("%s >> checklocalUpdateFile()", Updater.class.getSimpleName());

        File file = new File(Sys.getAppDir().getAbsolutePath()
                + File.separator
                + Files.fixFilename(updateProperties
                        .getProperty(KEY_DOWNLOAD_FILENAME)));
        if (file.isFile()) {
            /*
             * Check SHA-1.
             */
            try {
                MessageDigest md = MessageDigest.getInstance(Hasher.SHA1);

                final byte[] buf = new byte[Files.FILE_BUFFER];
                int read;
                final InputStream inputStream = new BufferedInputStream(
                        new FileInputStream(file), Files.FILE_BUFFER);
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
                                String.format("%0" + (md.digest().length * 2)
                                        + "x", bi));
                if (result)
                    sendNotification(
                            MSG_LOCAL_UPDATE_AVAILABLE,
                            Messages.getString(R.string.msg_local_update_available),
                            Messages.getString(
                                    R.string.pmsg_local_update_available, file
                                            .getAbsolutePath(),
                                    updateProperties
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
        L.i("%s >> downloadUpdateFile()", Updater.class.getSimpleName());

        HttpURLConnection conn = followRedirection(Sys.DEBUG ? DEBUG_UPDATE_LINK_EXECUTABLE
                : updateProperties.getProperty(KEY_DOWNLOAD_URI));
        if (conn == null)
            return;

        try {
            final InputStream inputStream = conn.getInputStream();
            try {
                if (conn.getResponseCode() != Network.HTTP_STATUS_OK)
                    return;

                final int contentLength = conn.getContentLength();
                if (contentLength == 0)
                    return;
                if (contentLength > 0 && contentLength > MAX_UPDATE_FILESIZE) {
                    sendNotification(
                            MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                            Messages.getString(R.string.msg_cancelled_update),
                            Messages.getString(
                                    R.string.pmsg_update_filesize_exceeds_limit,
                                    Texts.sizeToStr(contentLength),
                                    Texts.sizeToStr(MAX_UPDATE_FILESIZE)));
                    return;
                }

                /*
                 * PARSE FILENAME FROM SERVER
                 */

                String fileName = null;
                final String contentDisposition = conn
                        .getHeaderField("Content-Disposition");
                if (Texts.isEmpty(contentDisposition)
                        || !contentDisposition.matches("(?si).*?attachment.+")) {
                    fileName = Files.fixFilename(updateProperties
                            .getProperty(KEY_DOWNLOAD_FILENAME));
                } else {
                    Matcher m = Pattern.compile("(?si)filename=\"?.+?\"?$")
                            .matcher(contentDisposition);
                    if (m.find())
                        fileName = Files.fixFilename(m.group()
                                .replaceFirst("(?si)^filename=\"?", "")
                                .replaceFirst("\"$", ""));
                }
                if (Texts.isEmpty(fileName))
                    return;

                File targetFile = new File(Sys.getAppDir().getAbsolutePath()
                        + File.separator + fileName);

                L.d("%s >> %s", CLASSNAME, targetFile.getAbsolutePath());

                if (contentLength > 0
                        && targetFile.getParentFile() != null
                        && targetFile.getParentFile().getFreeSpace() <= contentLength * 1.5) {
                    sendNotification(MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                            Messages.getString(R.string.msg_cancelled_update),
                            Messages.getString(
                                    R.string.pmsg_available_space_is_low, Texts
                                            .sizeToStr(targetFile
                                                    .getParentFile()
                                                    .getFreeSpace())));
                    return;
                }

                /*
                 * START DOWNLOADING
                 */

                final OutputStream outputStream = new BufferedOutputStream(
                        new FileOutputStream(targetFile), Files.FILE_BUFFER);
                final long[] totalRead = { 0 };
                final SpeedTracker speedTracker = new SpeedTracker();
                final Timer timer = new Timer();
                timer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        if (contentLength > 0) {
                            sendNotification(
                                    MSG_UPDATE_PROGRESS,
                                    Messages.getString(
                                            R.string.pmsg_updating_with_percentage,
                                            Texts.percentToStr(totalRead[0]
                                                    * 100f / contentLength),
                                            Texts.sizeToStr(totalRead[0]),
                                            Texts.sizeToStr(speedTracker
                                                    .calcInstantaneousSpeed())));
                        }// contentLength > 0
                        else {
                            sendNotification(MSG_UPDATE_PROGRESS, Messages
                                    .getString(R.string.pmsg_updating, Texts
                                            .sizeToStr(totalRead[0]), Texts
                                            .sizeToStr(speedTracker
                                                    .calcInstantaneousSpeed())));
                        }// //contentLength == 0
                    }// run()
                }, 999, 999);
                try {
                    final MessageDigest md = MessageDigest
                            .getInstance(Hasher.SHA1);
                    byte[] buf = new byte[Files.FILE_BUFFER];
                    int read;
                    long tick = System.nanoTime();
                    while ((read = inputStream.read(buf)) > 0) {
                        if (isInterrupted()) {
                            outputStream.close();
                            targetFile.delete();
                            return;
                        }

                        outputStream.write(buf, 0, read);
                        totalRead[0] += read;

                        md.update(buf, 0, read);

                        tick = System.nanoTime() - tick;
                        speedTracker.add(tick > 0 ? totalRead[0] / (tick / 1e6)
                                : totalRead[0] / 1e6);

                        long freeSpace = 0;
                        if (targetFile.getParentFile() != null) {
                            freeSpace = targetFile.getParentFile()
                                    .getFreeSpace();
                            if (freeSpace < totalRead[0] * 1.5) {
                                sendNotification(
                                        MSG_UPDATE_FILESIZE_EXCEEDS_LIMIT,
                                        Messages.getString(R.string.msg_cancelled_update),
                                        Messages.getString(
                                                R.string.pmsg_available_space_is_low,
                                                Texts.sizeToStr(freeSpace)));
                                outputStream.close();
                                targetFile.delete();
                                return;
                            }
                        }

                        tick = System.nanoTime();
                    }// while

                    outputStream.close();
                    timer.cancel();

                    /*
                     * CHECK SHA-1
                     */
                    BigInteger bi = new BigInteger(1, md.digest());
                    if (updateProperties.getProperty(KEY_SHA1)
                            .equalsIgnoreCase(
                                    String.format("%0"
                                            + (md.digest().length * 2) + "x",
                                            bi))) {
                        sendNotification(
                                MSG_UPDATE_FINISHED,
                                Messages.getString(R.string.msg_update_finished),
                                Messages.getString(
                                        R.string.pmsg_update_finished,
                                        targetFile.getAbsolutePath(),
                                        updateProperties
                                                .getProperty(KEY_APP_VERSION_NAME)));
                    } else {
                        targetFile.delete();
                        sendNotification(
                                MSG_UPDATE_CANCELLED,
                                Messages.getString(R.string.msg_update_cancelled),
                                Messages.getString(R.string.msg_update_cancelled_because_wrong_checksum));
                    }
                } catch (NoSuchAlgorithmException e) {
                    /*
                     * Never catch this.
                     */
                } finally {
                    outputStream.close();
                    timer.cancel();
                }
            } finally {
                if (inputStream != null)
                    inputStream.close();
            }
        } catch (Throwable t) {
            /*
             * Ignore it.
             */
            t.printStackTrace();
        }
    }// downloadUpdateFile()
}
