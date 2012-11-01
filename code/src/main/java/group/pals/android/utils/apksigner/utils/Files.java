/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.android.utils.apksigner.utils;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Hai Bison
 */
public class Files {

    public static final String TitleOpenFile = "Choose File...";
    public static final String TitleOpenDir = "Choose Folder...";
    public static final String TitleSaveFile = "Save File As...";

    public static File chooseFile(File currentDir) {
        JFileChooserEx fc = new JFileChooserEx(currentDir);
        fc.setDialogTitle(TitleOpenFile);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFile

    public static File chooseFile(File currentDir, String regexFilenameFilter, String description) {
        JFileChooserEx fc = new JFileChooserEx(currentDir);
        fc.setDialogTitle(TitleOpenFile);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFilenameFilter(regexFilenameFilter, description);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFile

    public static File chooseDir(File currentDir) {
        JFileChooserEx fc = new JFileChooserEx(currentDir);
        fc.setDialogTitle(TitleOpenDir);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseDir

    public static File chooseFileToSave(File currentDir) {
        JFileChooserEx fc = new JFileChooserEx(currentDir);
        fc.setDialogTitle(TitleSaveFile);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (fc.showSaveDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFile

    private static class JFileChooserEx extends JFileChooser {

        public static final String PatternOverrideFile = "This file \"%s\" already exists.\nDo you want to replace it?";
        public static final String PatternFileNotExisted = "File \"%s\" does not exist";

        public JFileChooserEx(File currentDir) {
            super(currentDir);
        }
        
        public void setFilenameFilter(final String Regex, final String Description) {
            setFileFilter(new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (getFileSelectionMode() == DIRECTORIES_ONLY)
                        return f.getName().matches(Regex);
                    else if (f.isDirectory())
                        return true;
                    else
                        return f.getName().matches(Regex);
                }

                @Override
                public String getDescription() {
                    return Description;
                }
            });
        }
        
        @Override
        public void approveSelection() {
            switch (getDialogType()) {
                case SAVE_DIALOG: {
                    File file = getSelectedFile();
                    if ((file != null) && file.exists()) {
                        final String[] UsrOptions = {"Yes", "No"};
                        int usrOption = JOptionPane.showOptionDialog(this,
                                String.format(PatternOverrideFile, file.getName()),
                                MsgBox.TitleConfirmation, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
                                null, UsrOptions, UsrOptions[1]);
                        if (usrOption != 0) {
                            return;
                        }
                    }
                    break;
                }//case SAVE_DIALOG

                case OPEN_DIALOG: {
                    File file = getSelectedFile();
                    if ((file == null) || !file.exists()) {
                        JOptionPane.showMessageDialog(this,
                                String.format(PatternFileNotExisted, file.getName()),
                                MsgBox.TitleError, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                }//case OPEN_DIALOG
            }

            super.approveSelection();
        }
    }//JFileChooserEx
}
