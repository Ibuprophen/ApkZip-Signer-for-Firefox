/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import java.io.File;
import java.io.InputStream;

/**
 * Utilities for keystore files.
 * 
 * @author Hai Bison
 */
public class KeyTools {

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
}
