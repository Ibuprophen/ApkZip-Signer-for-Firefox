/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.ui.FileDrop;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

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
     * Default colour for border of {@link FileDrop}.
     */
    public static final Color COLOUR_BORDER_FILE_DROP = Color.yellow;

    /**
     * Default border for {@link FileDrop}.
     */
    public static final Border BORDER_FILE_DROP = BorderFactory
            .createMatteBorder(1, 2, 1, 2, COLOUR_BORDER_FILE_DROP);

    /**
     * Default tab size for text component.
     */
    public static final int TEXT_COMPONENT_TAB_SIZE = 4;

    /**
     * Delay time between updates of UI, in milliseconds.
     */
    public static final int DELAY_TIME_UPDATING_UI = 499;

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

}
