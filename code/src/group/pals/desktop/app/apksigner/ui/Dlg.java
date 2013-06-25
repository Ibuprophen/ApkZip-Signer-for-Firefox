/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.ui;

import group.pals.desktop.app.apksigner.i18n.Messages;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Utilities for dialog boxes.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class Dlg {

    /**
     * Shows an error message.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showErrMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg,
                title == null ? Messages.getString("error") : title,
                JOptionPane.ERROR_MESSAGE);
    }// showErrMsg()

    /**
     * Shows an exception message.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param e
     *            the exception.
     */
    public static void showException(Component comp, String title, Exception e) {
        String msg = String.format(Messages.getString("pmsg_exception"), e
                .getClass().getName(), e.getMessage());
        showErrMsg(comp, title, msg);
    }// showException()

    /**
     * Shows an information message.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showInfoMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg,
                (title == null ? Messages.getString("information") : title),
                JOptionPane.INFORMATION_MESSAGE);
    }// showInfoMsg()

    /**
     * Shows a huge information message. The dialog size will be hardcoded with
     * {@code width} and {@code height}.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsg(Component comp, String title,
            String msg, int width, int height) {
        JTextArea textArea = new JTextArea(msg);
        textArea.setEditable(false);
        textArea.setMargin(new Insets(9, 9, 9, 9));
        textArea.setComponentPopupMenu(new JEditorPopupMenu());

        JScrollPane scrollPane = new JScrollPane(textArea);
        if (width > 0 && height > 0) {
            Dimension size = new Dimension(width, height);
            scrollPane.setMaximumSize(size);
            scrollPane.setMinimumSize(size);
            scrollPane.setPreferredSize(size);
        }

        showInfoMsg(comp, title, scrollPane);
    }// showHugeInfoMsg()

    /**
     * Shows a warning message.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title.
     * @param msg
     *            the message.
     */
    public static void showWarningMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg,
                title == null ? Messages.getString("warning") : title,
                JOptionPane.WARNING_MESSAGE);
    }// showWarningMsg()

    /**
     * Shows a yes-no confirmation dialog.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title.
     * @param msg
     *            the message.
     * @param defaultButton
     *            the default suggested button. {@code 0} for "Yes", {@code 1}
     *            for "No". Other than that, {@link IndexOutOfBoundsException}
     *            will be thrown :-)
     * @return {@code true} if the user chose "yes", otherwise {@code false}.
     */
    public static boolean confirmYesNo(Component comp, String title,
            Object msg, int defaultButton) {
        Object[] options = { Messages.getString("yes"),
                Messages.getString("no") };
        int opt = JOptionPane.showOptionDialog(comp, msg,
                title == null ? Messages.getString("confirmation") : title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                options, options[defaultButton]);
        return opt == 0;
    }// confirmYesNo()
}
