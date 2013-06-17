/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */
package group.pals.android.utils.apksigner.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Helper class to generate keystore file.
 *
 * @author Hai Bison
 */
public class KeyGen {

    /**
     * Generates new keystore file.
     *
     * @param jdkPath the JDK path, can be {@code null} on Unix system.
     * @param target the target file which will be generated to.
     * @param storepass the keystore's password.
     * @param alias the keystore's alias name.
     * @param keypass the keystore's alias password.
     * @param aliasYears the validity, in years.
     * @param coName the company name.
     * @param ouName the organization unit name.
     * @param oName the organization name.
     * @param city the city name.
     * @param state the state name.
     * @param country the country ISO code.
     * @throws IOException if any occurred.
     * @throws InterruptedException if any occurred.
     */
    public static void genKey(File jdkPath, File target, char[] storepass, String alias,
            char[] keypass, int aliasYears, String coName, String ouName,
            String oName, String city, String state, String country)
            throws IOException, InterruptedException {
        target.delete();

        /*
         * keytool -genkey -sigalg MD5withRSA -digestalg SHA1 -alias ALIAS_NAME
         * -keypass KEY_PASS -validity YEARS -keystore TARGET_FILE
         * -storepass STORE_PASS -genkeypair -dname
         * "CN=Mark Jones, OU=JavaSoft, O=Sun, L=city, S=state C=US"
         */

        String[] values = {coName, ouName, oName, city, state, country};
        String[] keys = {"CN", "OU", "O", "L", "S", "C"};
        String dname = "";
        for (int i = 0; i < values.length; i++) {
            if (!values[i].isEmpty()) {
                dname += String.format("%s=%s ", keys[i], values[i]);
            }
        }
        dname = dname.trim();

        /*
         * JDK for Linux does not need to specify full path
         */
        String keytool = jdkPath != null && jdkPath.isDirectory() ? jdkPath.getAbsolutePath() + "/keytool.exe" : "keytool";

        ProcessBuilder pb = new ProcessBuilder(new String[]{
                    keytool,
                    "-genkey",
                    "-keyalg", "RSA",
                    "-alias",
                    alias,
                    "-keypass",
                    new String(keypass),
                    "-validity",
                    Integer.toString(aliasYears),
                    "-keystore",
                    target.getAbsolutePath(),
                    "-storepass",
                    new String(storepass),
                    "-genkeypair",
                    "-dname",
                    String.format("%s", dname)});
        Process p = pb.start();

        StringBuffer sb = new StringBuffer();
        InputStream stream = p.getInputStream();
        try {
            int read;
            byte[] buf = new byte[1024 * 99];
            while ((read = stream.read(buf)) > 0) {
                sb.append(new String(buf, 0, read));
            }
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        /*
         * TODO: get output of keytool to parse for errors, warnings...
         */

        p.waitFor();

        if (!target.isFile()) {
            throw new IOException("Error: " + sb);
        }
    }//genKey()
}
