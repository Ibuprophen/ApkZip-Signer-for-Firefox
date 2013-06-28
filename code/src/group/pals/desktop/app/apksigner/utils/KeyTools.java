/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Utilities for keystore files.
 * 
 * @author Hai Bison
 */
public class KeyTools {

    /**
     * Default keystore type.
     */
    public static final String DEFAULT_KEYSTORE_TYPE = "JKS";

    /**
     * Lists entries in a keystore file.
     * 
     * @param jdkPath
     *            the JDK path, can be {@code null} on Unix system.
     * @param keyFile
     *            the keystore file.
     * @param storepass
     *            the keystore password.
     * @return the information, never be {@code null}.
     */
    public static CharSequence listEntries(File jdkPath, File keyFile,
            char[] storepass) {
        /*
         * JDK for Linux does not need to specify full path.
         */
        String keytool = jdkPath != null && jdkPath.isDirectory() ? jdkPath
                .getAbsolutePath() + "/keytool.exe" : "keytool";

        final StringBuilder console = new StringBuilder();

        /*
         * keytool -list -v -keystore aaa.keystore -storepass XXX
         */
        ProcessBuilder pb = new ProcessBuilder(new String[] { keytool, "-list",
                "-v", "-keystore", keyFile.getAbsolutePath(), "-storepass",
                new String(storepass) });
        try {
            Process p = pb.start();

            InputStream stream = p.getInputStream();
            try {
                int read = 0;
                byte[] buf = new byte[1024 * 99];
                while ((read = stream.read(buf)) > 0) {
                    console.append(new String(buf, 0, read));
                }
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }

            /*
             * TODO: parse output for errors, warnings...
             */

            p.waitFor();
        } catch (Throwable t) {
            console.append("*** ERROR ***\n\n").append(t);
        }

        return console;
    }// listEntries()

    /**
     * Gets all alias names from {@code keyFile}.
     * 
     * @param keyFile
     *            the keyfile.
     * @param storepass
     *            the password.
     * @return list of alias names, can be empty.
     */
    public static List<String> getAliases(File keyFile, char[] storepass) {
        final List<String> result = new ArrayList<String>();

        try {
            InputStream inputStream = new FileInputStream(keyFile);
            try {
                KeyStore ks = KeyStore.getInstance(DEFAULT_KEYSTORE_TYPE);
                ks.load(inputStream, storepass);

                Enumeration<String> aliases = ks.aliases();
                while (aliases.hasMoreElements())
                    result.add(aliases.nextElement());
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            /*
             * Ignore it.
             */
        }

        return result;
    }// getAliases()
}
