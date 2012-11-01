/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.android.utils.apksigner.utils;

import group.pals.android.utils.apksigner.panels.ui.JEditorPopupMenu;
import java.awt.Color;
import java.awt.Container;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Hai Bison
 */
public class UI {

    public static final Color mSelectedFileColor = Color.yellow;

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

    public static void setEditorPopupMenu(Container c, JEditorPopupMenu epm) {
        for (int i = 0; i < c.getComponentCount(); i++) {
            if (c.getComponent(i) instanceof JTextComponent) {
                ((JTextComponent) c.getComponent(i)).setComponentPopupMenu(epm);
            } else if (c.getComponent(i) instanceof Container) {
                setEditorPopupMenu((Container) c.getComponent(i), epm);
            }
        }
    }//setEditorPopupMenu
}
