/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

/**
 * The assets.
 * 
 * @author Hai Bison
 * 
 */
public class Assets {

    private static Font mDefaultFont, mDefaultMonoFont;
    private static Image mIconLogo, mIconSplash;

    /**
     * Gets font from resource.
     * 
     * @param resName
     *            the resource name to the font.
     * @return the font, or {@code null} if any error occurred.
     */
    public static Font getFont(String resName) {
        try {
            InputStream inputStream = Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(resName);
            try {
                return Font.createFont(Font.TRUETYPE_FONT, inputStream);
            } finally {
                if (inputStream != null)
                    inputStream.close();
            }
        } catch (FontFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }// getFont()

    /**
     * Gets default font.
     * 
     * @return the default font.
     */
    public static Font getDefaultFont() {
        if (mDefaultFont == null)
            mDefaultFont = getFont("assets/fonts/Roboto-Regular.ttf")
                    .deriveFont(12f);
        return mDefaultFont;
    }// getDefaultFont()

    /**
     * Gets default mono font.
     * 
     * @return the default mono font.
     */
    public static Font getDefaultMonoFont() {
        if (mDefaultMonoFont == null)
            mDefaultMonoFont = getFont("assets/fonts/SourceCodePro-Regular.ttf")
                    .deriveFont(12.5f);
        return mDefaultMonoFont;
    }// getDefaultMonoFont()

    /**
     * Gets the icon logo.
     * 
     * @return the icon logo.
     */
    public static Image getIconLogo() {
        if (mIconLogo == null)
            mIconLogo = Toolkit.getDefaultToolkit().getImage(
                    SplashDialog.class
                            .getResource("/assets/images/logo_256.png"));
        return mIconLogo;
    }// getIconLogo()

    /**
     * Gets the icon splash.
     * 
     * @return the icon splash.
     */
    public static Image getIconSplash() {
        if (mIconSplash == null)
            mIconSplash = Toolkit.getDefaultToolkit().getImage(
                    SplashDialog.class
                            .getResource("/assets/images/logo_399x144.png"));
        return mIconSplash;
    }// getIconSplash()

    /**
     * Get pattern HTML about.
     * 
     * @return the pattern HTML about.
     */
    public static CharSequence getPhtmlAbout() {
        final StringBuilder result = new StringBuilder();
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("assets/phtml_about");
        try {
            final byte[] buf = new byte[1024 * 32];
            int read = 0;
            try {
                while ((read = inputStream.read(buf)) > 0)
                    result.append(new String(buf, 0, read));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }// getPhtmlAbout()
}
