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

/**
 * The assets.
 * 
 * @author Hai Bison
 * 
 */
public class Assets {

    private static Font mDefaultFont, mDefaultMonoFont;
    private static Image mIconLogo;

    /**
     * Gets font from resource.
     * 
     * @param resName
     *            the resource name to the font.
     * @return the font, or {@code null} if any error occurred.
     */
    public static Font getFont(String resName) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, Thread.currentThread()
                    .getContextClassLoader().getResourceAsStream(resName));
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
            mDefaultFont = getFont("fonts/Ubuntu-Regular.ttf").deriveFont(12f);
        return mDefaultFont;
    }// getDefaultFont()

    /**
     * Gets default mono font.
     * 
     * @return the default mono font.
     */
    public static Font getDefaultMonoFont() {
        if (mDefaultMonoFont == null)
            mDefaultMonoFont = getFont("fonts/UbuntuMono-Regular.ttf")
                    .deriveFont(13f);
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
                    SplashDialog.class.getResource("/images/logo_256.png"));
        return mIconLogo;
    }// getIconLogo()
}
