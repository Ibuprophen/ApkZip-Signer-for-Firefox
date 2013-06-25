/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.ui.JEditorPopupMenu;

import java.awt.Color;
import java.awt.Container;

import javax.swing.text.JTextComponent;

/**
 * UI utilities.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class UI {

    /**
     * The colour of selected file.
     */
    public static final Color COLOUR_SELECTED_FILE = Color.yellow;

    /**
     * The colour of waiting command.
     */
    public static final Color COLOUR_WAITING_CMD = Color.cyan;

    /**
     * Moves the {@code window} to center of the screen, also resizes it by
     * ratio 16:9 :-) (in a multiplication with {@code luckyNo}).
     * 
     * @param window
     *            the window.
     * @param luckyNo
     *            your lucky number :-)
     */
    public static void setWindowCenterScreen(java.awt.Window window, int luckyNo) {
        java.awt.Dimension dim;

        if (luckyNo > 0) {
            dim = new java.awt.Dimension(luckyNo * 16, luckyNo * 9);
            window.setMinimumSize(dim);
            window.setSize(dim);
        }

        dim = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((dim.width - window.getWidth()) / 2,
                (dim.height - window.getHeight()) / 2);
    }// setWindowCenterScreen()

    /**
     * Sets a {@link JEditorPopupMenu} to all sub {@link JTextComponent}'s of
     * {@code container}.
     * 
     * @param container
     *            the container.
     * @param epm
     *            the editor popup menu.
     */
    public static void setEditorPopupMenu(Container container,
            JEditorPopupMenu epm) {
        for (int i = 0; i < container.getComponentCount(); i++) {
            if (container.getComponent(i) instanceof JTextComponent) {
                ((JTextComponent) container.getComponent(i))
                        .setComponentPopupMenu(epm);
            } else if (container.getComponent(i) instanceof Container) {
                setEditorPopupMenu((Container) container.getComponent(i), epm);
            }
        }
    }// setEditorPopupMenu()
}
