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
import java.util.regex.Matcher;

/**
 * Helper class to sign APK files.
 *
 * @author Hai Bison
 */
public class ApkSigner {

    /**
     * Used to append to newly signed APK's file name.
     */
    private static final String SIGNED = "SIGNED";

    /**
     * Signs an APK file.
     *
     * @param jdkPath the path to JDK, can be {@code null} on Unix system.
     * @param apkFile the APK file.
     * @param keyFile the keystore file.
     * @param storepass the keystore's password.
     * @param alias the keystore alias.
     * @param keypass the keystore's alias password.
     * @return
     * @throws IOException if any occurred.
     * @throws InterruptedException if any occurred.
     */
    public static String sign(File jdkPath, File apkFile, File keyFile, char[] storepass, String alias,
            char[] keypass) throws IOException, InterruptedException {

        /*
         * JDK for Linux does not need to specify full path
         */
        String jarsigner = jdkPath != null && jdkPath.isDirectory() ? jdkPath.getAbsolutePath() + "/jarsigner.exe" : "jarsigner";

        /*
         * jarsigner -keystore KEY_FILE -sigalg MD5withRSA -digestalg SHA1
         * -storepass STORE_PASS -keypass KEY_PASS APK_FILE ALIAS_NAME
         */
        ProcessBuilder pb = new ProcessBuilder(new String[]{jarsigner,
                    "-keystore", keyFile.getAbsolutePath(),
                    "-sigalg", "MD5withRSA",
                    "-digestalg", "SHA1",
                    "-storepass", new String(storepass),
                    "-keypass", new String(keypass),
                    apkFile.getAbsolutePath(), alias});
        Process p = pb.start();

        StringBuilder console = new StringBuilder();
        InputStream stream = p.getInputStream();
        try {
            int read;
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

        /*
         * Renames newly signed file...
         */

        if (console.toString().trim().isEmpty()) {
            final String oldApkName = apkFile.getName();
            String newApkName;
            if (oldApkName.matches("(?si).*?unsigned.+")) {
                newApkName = oldApkName.replaceFirst("(?si)unsigned", Matcher.quoteReplacement(SIGNED));
            } else if (oldApkName.matches("(?si).+\\.apk$")) {
                newApkName = oldApkName.replaceFirst("(?si)\\.apk$", Matcher.quoteReplacement(String.format("_%s.apk", SIGNED)));
            } else {
                newApkName = String.format("%s_%s.apk", oldApkName, SIGNED);
            }

            if (apkFile.renameTo(new File(apkFile.getParent() + File.separator + newApkName))) {
                return null;
            }
            return String.format("Can't rename source APK file to \"%s\"!\n\n", newApkName)
                    + "The result of signing process was "
                    + "(if empty, it means everything is OK):\n\n" + console.toString().trim();
        }// results from console is empty
        else {
            return console.toString().trim();
        }// results from console is NOT empty
    }//sign()
}
