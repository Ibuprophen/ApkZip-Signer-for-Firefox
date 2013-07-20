/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils.ui;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.utils.Texts;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

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
                Texts.isEmpty(title) ? Messages.getString(R.string.error)
                        : title, JOptionPane.ERROR_MESSAGE);
    }// showErrMsg()

    /**
     * Shows an error message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showErrMsgAsync(final Component comp,
            final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showErrMsg(comp, title, msg);
            }// run()
        });
    }// showErrMsgAsync()

    /**
     * Shows an error message.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showErrMsg(Component comp, Object msg) {
        showErrMsg(comp, null, msg);
    }// showErrMsg()

    /**
     * Shows an error message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showErrMsgAsync(final Component comp, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showErrMsg(comp, msg);
            }// run()
        });
    }// showErrMsgAsync()

    /**
     * Shows an error message.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showErrMsg(String title, Object msg) {
        showErrMsg(null, title, msg);
    }// showErrMsg()

    /**
     * Shows an error message asynchronously.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showErrMsgAsync(final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showErrMsg(title, msg);
            }// run()
        });
    }// showErrMsgAsync()

    /**
     * Shows an error message.
     * 
     * @param msg
     *            the message.
     */
    public static void showErrMsg(Object msg) {
        showErrMsg(null, null, msg);
    }// showErrMsg()

    /**
     * Shows an error message asynchronously.
     * 
     * @param msg
     *            the message.
     */
    public static void showErrMsgAsync(final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showErrMsg(msg);
            }// run()
        });
    }// showErrMsgAsync()

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
        String msg = Messages.getString(R.string.pmsg_exception, e.getClass()
                .getName(), e.getMessage());
        showErrMsg(comp, title, msg);
    }// showException()

    /**
     * Shows an exception message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param e
     *            the exception.
     */
    public static void showExceptionAsync(final Component comp,
            final String title, final Exception e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showException(comp, title, e);
            }// run()
        });
    }// showExceptionAsync()

    /**
     * Shows an exception message.
     * 
     * @param comp
     *            the root component.
     * @param e
     *            the exception.
     */
    public static void showException(Component comp, Exception e) {
        showException(comp, null, e);
    }// showException()

    /**
     * Shows an exception message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param e
     *            the exception.
     */
    public static void showExceptionAsync(final Component comp,
            final Exception e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showException(comp, e);
            }// run()
        });
    }// showExceptionAsync()

    /**
     * Shows an exception message.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param e
     *            the exception.
     */
    public static void showException(String title, Exception e) {
        showException(null, title, e);
    }// showException()

    /**
     * Shows an exception message asynchronously.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param e
     *            the exception.
     */
    public static void showExceptionAsync(final String title, final Exception e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showException(title, e);
            }// run()
        });
    }// showExceptionAsync()

    /**
     * Shows an exception message.
     * 
     * @param e
     *            the exception.
     */
    public static void showException(Exception e) {
        showException(null, null, e);
    }// showException()

    /**
     * Shows an exception message asynchronously.
     * 
     * @param e
     *            the exception.
     */
    public static void showExceptionAsync(final Exception e) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showException(e);
            }// run()
        });
    }// showExceptionAsync()

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
        JOptionPane.showMessageDialog(
                comp,
                msg,
                (Texts.isEmpty(title) ? Messages
                        .getString(R.string.information) : title),
                JOptionPane.INFORMATION_MESSAGE);
    }// showInfoMsg()

    /**
     * Shows an information message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showInfoMsgAsync(final Component comp,
            final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showInfoMsg(comp, title, msg);
            }// run()
        });
    }// showInfoMsgAsync()

    /**
     * Shows an information message.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showInfoMsg(String title, Object msg) {
        showInfoMsg(null, title, msg);
    }// showInfoMsg()

    /**
     * Shows an information message asynchronously.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     */
    public static void showInfoMsgAsync(final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showInfoMsg(title, msg);
            }// run()
        });
    }// showInfoMsgAsync()

    /**
     * Shows an information message.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showInfoMsg(Component comp, Object msg) {
        showInfoMsg(comp, null, msg);
    }// showInfoMsg()

    /**
     * Shows an information message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showInfoMsgAsync(final Component comp, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showInfoMsg(comp, msg);
            }// run()
        });
    }// showInfoMsgAsync()

    /**
     * Shows an information message.
     * 
     * @param msg
     *            the message.
     */
    public static void showInfoMsg(Object msg) {
        showInfoMsg(null, null, msg);
    }// showInfoMsg()

    /**
     * Shows an information message asynchronously.
     * 
     * @param msg
     *            the message.
     */
    public static void showInfoMsgAsync(final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showInfoMsg(msg);
            }// run()
        });
    }// showInfoMsgAsync()

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
     * Shows a huge information message asynchronously. The dialog size will be
     * hardcoded with {@code width} and {@code height}.
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
    public static void showHugeInfoMsgAsync(final Component comp,
            final String title, final String msg, final int width,
            final int height) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showHugeInfoMsg(comp, title, msg, width, height);
            }// run()
        });
    }// showHugeInfoMsgAsync()

    /**
     * Shows a huge information message. The dialog size will be hardcoded with
     * {@code width} and {@code height}.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsg(String title, String msg, int width,
            int height) {
        showHugeInfoMsg(null, title, msg, width, height);
    }// showHugeInfoMsg()

    /**
     * Shows a huge information message asynchronously. The dialog size will be
     * hardcoded with {@code width} and {@code height}.
     * 
     * @param title
     *            the title. If {@code null}, default will be used.
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsgAsync(final String title,
            final String msg, final int width, final int height) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showHugeInfoMsg(title, msg, width, height);
            }// run()
        });
    }// showHugeInfoMsgAsync()

    /**
     * Shows a huge information message. The dialog size will be hardcoded with
     * {@code width} and {@code height}.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsg(Component comp, String msg, int width,
            int height) {
        showHugeInfoMsg(comp, null, msg, width, height);
    }// showHugeInfoMsg()

    /**
     * Shows a huge information message asynchronously. The dialog size will be
     * hardcoded with {@code width} and {@code height}.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsgAsync(final Component comp,
            final String msg, final int width, final int height) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showHugeInfoMsg(comp, msg, width, height);
            }// run()
        });
    }// showHugeInfoMsgAsync()

    /**
     * Shows a huge information message. The dialog size will be hardcoded with
     * {@code width} and {@code height}.
     * 
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsg(String msg, int width, int height) {
        showHugeInfoMsg(null, null, msg, width, height);
    }// showHugeInfoMsg()

    /**
     * Shows a huge information message asynchronously. The dialog size will be
     * hardcoded with {@code width} and {@code height}.
     * 
     * @param msg
     *            the message.
     * @param width
     *            the dialog width.
     * @param height
     *            the dialog height.
     */
    public static void showHugeInfoMsgAsync(final String msg, final int width,
            final int height) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showHugeInfoMsg(msg, width, height);
            }// run()
        });
    }// showHugeInfoMsgAsync()

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
                Texts.isEmpty(title) ? Messages.getString(R.string.warning)
                        : title, JOptionPane.WARNING_MESSAGE);
    }// showWarningMsg()

    /**
     * Shows a warning message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title.
     * @param msg
     *            the message.
     */
    public static void showWarningMsgAsync(final Component comp,
            final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showWarningMsg(comp, title, msg);
            }// run()
        });
    }// showWarningMsgAsync()

    /**
     * Shows a warning message.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showWarningMsg(Component comp, Object msg) {
        showWarningMsg(comp, null, msg);
    }// showWarningMsg()

    /**
     * Shows a warning message asynchronously.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     */
    public static void showWarningMsgAsync(final Component comp,
            final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showWarningMsg(comp, msg);
            }// run()
        });
    }// showWarningMsgAsync()

    /**
     * Shows a warning message.
     * 
     * @param title
     *            the title.
     * @param msg
     *            the message.
     */
    public static void showWarningMsg(String title, Object msg) {
        showWarningMsg(null, title, msg);
    }// showWarningMsg()

    /**
     * Shows a warning message asynchronously.
     * 
     * @param title
     *            the title.
     * @param msg
     *            the message.
     */
    public static void showWarningMsgAsync(final String title, final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showWarningMsg(title, msg);
            }// run()
        });
    }// showWarningMsgAsync()

    /**
     * Shows a warning message.
     * 
     * @param msg
     *            the message.
     */
    public static void showWarningMsg(Object msg) {
        showWarningMsg(null, null, msg);
    }// showWarningMsg()

    /**
     * Shows a warning message asynchronously.
     * 
     * @param msg
     *            the message.
     */
    public static void showWarningMsgAsync(final Object msg) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                showWarningMsg(msg);
            }// run()
        });
    }// showWarningMsgAsync()

    /**
     * Shows a yes-no confirmation dialog.
     * 
     * @param comp
     *            the root component.
     * @param title
     *            the title.
     * @param msg
     *            the message.
     * @param defaultYes
     *            {@code true} to make button "Yes" selected as default.
     *            {@code false} for button "No".
     * @return {@code true} if the user chose "Yes", otherwise {@code false}.
     */
    public static boolean confirmYesNo(Component comp, String title,
            Object msg, boolean defaultYes) {
        Object[] options = { Messages.getString(R.string.yes),
                Messages.getString(R.string.no) };
        int opt = JOptionPane.showOptionDialog(
                comp,
                msg,
                Texts.isEmpty(title) ? Messages
                        .getString(R.string.confirmation) : title,
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                options, options[defaultYes ? 0 : 1]);
        return opt == 0;
    }// confirmYesNo()

    /**
     * Shows a yes-no confirmation dialog.
     * 
     * @param title
     *            the title.
     * @param msg
     *            the message.
     * @param defaultYes
     *            {@code true} to make button "Yes" selected as default.
     *            {@code false} for button "No".
     * @return {@code true} if the user chose "Yes", otherwise {@code false}.
     */
    public static boolean confirmYesNo(String title, Object msg,
            boolean defaultYes) {
        return confirmYesNo(null, title, msg, defaultYes);
    }// confirmYesNo()

    /**
     * Shows a yes-no confirmation dialog.
     * 
     * @param comp
     *            the root component.
     * @param msg
     *            the message.
     * @param defaultYes
     *            {@code true} to make button "Yes" selected as default.
     *            {@code false} for button "No".
     * @return {@code true} if the user chose "Yes", otherwise {@code false}.
     */
    public static boolean confirmYesNo(Component comp, Object msg,
            boolean defaultYes) {
        return confirmYesNo(comp, null, msg, defaultYes);
    }// confirmYesNo()

    /**
     * Shows a yes-no confirmation dialog.
     * 
     * @param msg
     *            the message.
     * @param defaultYes
     *            {@code true} to make button "Yes" selected as default.
     *            {@code false} for button "No".
     * @return {@code true} if the user chose "Yes", otherwise {@code false}.
     */
    public static boolean confirmYesNo(Object msg, boolean defaultYes) {
        return confirmYesNo(null, null, msg, defaultYes);
    }// confirmYesNo()

}
