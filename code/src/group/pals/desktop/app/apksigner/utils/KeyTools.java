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
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
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
     * "JKS"
     */
    public static final String KEYSTORE_TYPE_JKS = "JKS";

    /**
     * "JCEKS"
     */
    public static final String KEYSTORE_TYPE_JCEKS = "JCEKS";

    /**
     * "PKCS12"
     */
    public static final String KEYSTORE_TYPE_PKCS12 = "PKCS12";

    /**
     * Default keystore type.
     */
    public static final String DEFAULT_KEYSTORE_TYPE = KEYSTORE_TYPE_JKS;

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
     * @deprecated Use {@link #listEntries(File, String, char[])} instead.
     */
    @Deprecated
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
     * Lists entries in a keystore file.
     * 
     * @param keyFile
     *            the keystore file.
     * @param keystoreType
     *            the keystore type.
     * @param storepass
     *            the keystore password.
     * @return the information, never be {@code null}.
     */
    public static CharSequence listEntries(File keyFile, String keystoreType,
            char[] storepass) {
        final StringBuilder result = new StringBuilder();

        try {
            InputStream inputStream = new FileInputStream(keyFile);
            try {
                KeyStore keyStore = KeyStore.getInstance(keystoreType);
                keyStore.load(inputStream, storepass);

                /*
                 * HEADER
                 */

                result.append(String.format("%s: %s\n",
                        Messages.getString(R.string.keystore_type),
                        keyStore.getType()));
                result.append(String.format("%s: %s\n",
                        Messages.getString(R.string.keystore_provider),
                        keyStore.getProvider()));
                result.append("\n");

                final int entryCount = keyStore.size();
                if (entryCount <= 1)
                    result.append(Messages.getString(
                            R.string.pmsg_your_keystore_contains_x_entry,
                            entryCount));
                else
                    result.append(Messages.getString(
                            R.string.pmsg_your_keystore_contains_x_entries,
                            entryCount));
                result.append("\n\n");

                /*
                 * ENTRIES
                 */

                Enumeration<String> aliases = keyStore.aliases();
                while (aliases.hasMoreElements()) {
                    final String alias = aliases.nextElement();
                    final Certificate cert = keyStore.getCertificate(alias);

                    result.append(String.format("%s: %s\n",
                            Messages.getString(R.string.alias_name), alias));
                    result.append(String.format("%s: %s\n",
                            Messages.getString(R.string.creation_date),
                            Texts.formatDate(keyStore.getCreationDate(alias))));
                    result.append(String.format("%s: %s\n",
                            Messages.getString(R.string.entry_type),
                            cert.getType()));

                    final Certificate[] certChain = keyStore
                            .getCertificateChain(alias);
                    if (certChain != null) {
                        result.append(String.format("%s: %,d\n", Messages
                                .getString(R.string.certificate_chain_length),
                                certChain.length));
                        for (int i = 0; i < certChain.length; i++) {
                            result.append(String.format("\t%s[%,d]:\n",
                                    Messages.getString(R.string.certificate),
                                    i + 1));

                            if (certChain[i] instanceof X509Certificate) {
                                X509Certificate x509Cert = (X509Certificate) certChain[i];

                                result.append(String.format("\t\t%s: %s\n",
                                        Messages.getString(R.string.owner),
                                        x509Cert.getIssuerX500Principal()
                                                .getName()));
                                result.append(String.format("\t\t%s: %s\n",
                                        Messages.getString(R.string.issuer),
                                        x509Cert.getIssuerX500Principal()
                                                .getName()));
                                result.append(String.format(
                                        "\t\t%s: %x\n",
                                        Messages.getString(R.string.serial_number),
                                        x509Cert.getSerialNumber()));
                                result.append("\t\t")
                                        .append(Messages.getString(
                                                R.string.pmsg_valid_from_until,
                                                Texts.formatDate(x509Cert
                                                        .getNotBefore()), Texts
                                                        .formatDate(x509Cert
                                                                .getNotAfter())))
                                        .append('\n');
                            }

                            result.append(String.format(
                                    "\t\t%s:\n",
                                    Messages.getString(R.string.certificate_fingerprints)));
                            for (String algorithm : new String[] { Hasher.MD5,
                                    Hasher.SHA1, Hasher.SHA256 }) {
                                String hash = Hasher
                                        .calcHash(algorithm,
                                                certChain[i].getEncoded(), true)
                                        .toString().toUpperCase();
                                result.append(String.format("\t\t\t%s: %s\n",
                                        algorithm, hash));
                            }
                        }
                    }
                }// while
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            result.append(L.printStackTrace(e));
        }

        return result;
    }// listEntries()

    /**
     * Gets all alias names from {@code keyFile}.
     * 
     * @param keyFile
     *            the keyfile.
     * @param keystoreType
     *            the keystore type.
     * @param storepass
     *            the password.
     * @return list of alias names, can be empty.
     */
    public static List<String> getAliases(File keyFile, String keystoreType,
            char[] storepass) {
        final List<String> result = new ArrayList<String>();

        try {
            InputStream inputStream = new FileInputStream(keyFile);
            try {
                KeyStore keyStore = KeyStore.getInstance(keystoreType);
                keyStore.load(inputStream, storepass);

                Enumeration<String> aliases = keyStore.aliases();
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
