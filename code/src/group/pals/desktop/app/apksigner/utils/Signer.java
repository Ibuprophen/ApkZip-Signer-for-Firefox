/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

/**
 * Helper class to sign APK files.
 * 
 * @author Hai Bison
 */
public class Signer {

    /**
     * Used to append to newly signed target's file name.
     */
    private static final String SIGNED = "SIGNED";

    /**
     * Used to append to newly signed target's file name.
     */
    private static final String SIGNED_UNALIGNED = "SIGNED_UNALIGNED";

    /**
     * Signs an APK file.
     * 
     * @param jdkPath
     *            the path to JDK, can be {@code null} on Unix system.
     * @param targetFile
     *            the target file, can be an APK, JAR or ZIP.
     * @param keyFile
     *            the keystore file.
     * @param storepass
     *            the keystore's password.
     * @param alias
     *            the keystore alias.
     * @param keypass
     *            the keystore's alias password.
     * @return
     * @throws IOException
     *             if any occurred.
     * @throws InterruptedException
     *             if any occurred.
     */
    public static String sign(File jdkPath, File targetFile, File keyFile,
            char[] storepass, String alias, char[] keypass) throws IOException,
            InterruptedException {

        /*
         * JDK for Linux does not need to specify full path
         */
        String jarsigner = jdkPath != null && jdkPath.isDirectory() ? jdkPath
                .getAbsolutePath() + "/jarsigner.exe" : "jarsigner";

        /*
         * jarsigner -keystore KEY_FILE -sigalg MD5withRSA -digestalg MD5
         * -storepass STORE_PASS -keypass KEY_PASS APK_FILE ALIAS_NAME
         */
        ProcessBuilder pb = new ProcessBuilder(new String[] { jarsigner,
                "-keystore", keyFile.getAbsolutePath(), "-sigalg",
                "MD5withRSA", "-digestalg", "MD5", "-storepass",
                new String(storepass), "-keypass", new String(keypass),
                targetFile.getAbsolutePath(), alias });
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

        final String result = console.toString().trim();
        if (result.isEmpty()) {
            final String oldName = targetFile.getName();
            String newName;
            if (oldName.matches("(?si).*?unsigned.+")) {
                if (oldName.matches(Texts.REGEX_APK_FILES))
                    newName = oldName.replaceFirst("(?si)unsigned",
                            Matcher.quoteReplacement(SIGNED_UNALIGNED));
                else
                    newName = oldName.replaceFirst("(?si)unsigned",
                            Matcher.quoteReplacement(SIGNED));
            } else if (oldName.matches(Texts.REGEX_APK_FILES))
                newName = Files.appendFilename(oldName, '_' + SIGNED_UNALIGNED);
            else if (oldName.matches(Texts.REGEX_JAR_FILES)
                    || oldName.matches(Texts.REGEX_ZIP_FILES))
                newName = Files.appendFilename(oldName, '_' + SIGNED);
            else
                newName = String.format("%s_%s", oldName, SIGNED);

            if (targetFile.renameTo(new File(targetFile.getParent()
                    + File.separator + newName)))
                return null;

            return Messages
                    .getString(
                            R.string.pmsg_file_is_signed_but_cannot_be_renamed_to_new_one,
                            newName);
        }// results from console is empty
        else {
            return result;
        }// results from console is NOT empty
    }// sign()
}
