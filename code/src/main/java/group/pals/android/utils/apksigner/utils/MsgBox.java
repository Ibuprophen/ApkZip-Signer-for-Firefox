/*
 *   Copyright 2012 Hai Bison
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package group.pals.android.utils.apksigner.utils;

import group.pals.android.utils.apksigner.panels.ui.JEditorPopupMenu;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Hai Bison
 */
public class MsgBox {

    public static final String TitleInformation = "Information";
    public static final String TitleWarning = "Warning";
    public static final String TitleConfirmation = "Confirmation";
    public static final String TitleError = "Error";

    public static void showErrMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg, title == null ? TitleError : title,
                JOptionPane.ERROR_MESSAGE);
    }//showErrMsg

    public static void showException(Component comp, String title, Exception ex) {
        String msg = String.format("Exception:\n%s\n\nMessage:\n%s",
                ex.getClass().getName(), ex.getMessage());
        showErrMsg(comp, title, msg);
    }//showException

    public static void showInfoMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg, (title == null ? TitleInformation : title),
                JOptionPane.INFORMATION_MESSAGE);
    }//showInfoMsg

    public static void showHugeInfoMsg(Component comp, String title, String msg,
            int width, int height) {
        JTextArea textArea = new JTextArea(msg);
        textArea.setEditable(false);
        textArea.setComponentPopupMenu(new JEditorPopupMenu());

        JScrollPane scrollPane = new JScrollPane(textArea);
        if (width > 0 && height > 0) {
            Dimension size = new Dimension(width, height);
            scrollPane.setMaximumSize(size);
            scrollPane.setMinimumSize(size);
            scrollPane.setPreferredSize(size);
        }

        showInfoMsg(comp, title, scrollPane);
    }//showHugeInfoMsg

    public static void showWarningMsg(Component comp, String title, Object msg) {
        JOptionPane.showMessageDialog(comp, msg, title == null ? TitleWarning : title,
                JOptionPane.WARNING_MESSAGE);
    }//showWarningMsg

    public static boolean confirmYesNo(Component comp, String title, Object msg, int defaultButton) {
        Object[] options = {"Yes", "No"};
        int opt = JOptionPane.showOptionDialog(comp,
                msg,
                title == null ? TitleConfirmation : title,
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options, options[defaultButton]);
        return opt == 0;
    }//confirmYesNo
}