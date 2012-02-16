/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package group.pals.android.utils.apksigner.utils;

/**
 *
 * @author Haiti Meid
 */
public class UI {

    /**
     * Set <code>window</code> center screen, also resizes it by ratio 16:9  :-)
     * (in a multiplication with {@code luckyNo}
     * @param window
     * @param luckyNo 
     */
    public static void setWindowCenterScreen(java.awt.Window window, int luckyNo) {
        java.awt.Dimension dim;

        if (luckyNo > 0) {
            dim = new java.awt.Dimension(luckyNo * 16, luckyNo * 9);
            window.setMinimumSize(dim);
            window.setSize(dim);
        }

        dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((dim.width - window.getWidth()) / 2, (dim.height - window.getHeight()) / 2);
    }//setWindowCenterScreen
}
