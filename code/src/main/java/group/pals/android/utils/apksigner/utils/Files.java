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
 * Helper class for working with files.
 *
 * @author Hai Bison
 */
public class Files {

    public static final String TITLE_CHOOSE_FILE = "Choose File...";
    public static final String TITLE_CHOOSE_DIRECTORY = "Choose Folder...";
    public static final String TITLE_SAVE_AS = "Save File As...";

    /**
     * Opens a dialog to choose a file.
     *
     * @param startupDir the startup directory.
     * @return the chosen file, or {@code null} if the user cancelled.
     */
    public static File chooseFile(File startupDir) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(TITLE_CHOOSE_FILE);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFile()

    /**
     * Opens a dialog to choose a file.
     *
     * @param startupDir the startup directory.
     * @param regexFilenameFilter the regex filename filter.
     * @param description the file filter description.
     * @return the chosen file, can be {@code null}.
     */
    public static File chooseFile(File startupDir, String regexFilenameFilter, String description) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(TITLE_CHOOSE_FILE);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFilenameFilter(regexFilenameFilter, description);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFile()

    /**
     * Opens a dialog to choose a directory.
     *
     * @param startupDir the startup directory.
     * @return the chosen directory, can be {@code null}.
     */
    public static File chooseDir(File startupDir) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(TITLE_CHOOSE_DIRECTORY);
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        switch (fc.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseDir()

    /**
     * Opens a dialog to choose a file to save.
     *
     * @param startupDir the startup directory.
     * @return the chosen file, can be {@code null}.
     */
    public static File chooseFileToSave(File startupDir) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(TITLE_SAVE_AS);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (fc.showSaveDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                return fc.getSelectedFile();
            default:
                return null;
        }
    }//chooseFileToSave()

    /**
     * Extended class of {@link JFileChooser}, which hacks some methods :-)
     */
    private static class JFileChooserEx extends JFileChooser {

        public static final String PMSG_OVERRIDE_FILE = "This file \"%s\" already exists.\n\nDo you want to replace it?";
        public static final String PMSG_FILE_NOT_EXISTED = "File \"%s\" does not exist";

        /**
         * Creates new instance.
         *
         * @param startupDir the startup directory.
         */
        public JFileChooserEx(File startupDir) {
            super(startupDir);
        }//JFileChooserEx()

        /**
         * Sets the regex file name filter.
         *
         * @param regex the regular expression.
         * @param description the description.
         */
        public void setFilenameFilter(final String regex, final String description) {
            setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    if (getFileSelectionMode() == DIRECTORIES_ONLY) {
                        return f.getName().matches(regex);
                    } else if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().matches(regex);
                    }
                }

                @Override
                public String getDescription() {
                    return description;
                }
            });
        }//setFilenameFilter()

        @Override
        public void approveSelection() {
            switch (getDialogType()) {
                case SAVE_DIALOG: {
                    File file = getSelectedFile();
                    if ((file != null) && file.exists()) {
                        final String[] UsrOptions = {"Yes", "No"};
                        int usrOption = JOptionPane.showOptionDialog(this,
                                String.format(PMSG_OVERRIDE_FILE, file.getName()),
                                MsgBox.TITLE_CONFIRMATION, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
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
                                String.format(PMSG_FILE_NOT_EXISTED, file.getName()),
                                MsgBox.TITLE_ERROR, JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    break;
                }//case OPEN_DIALOG
            }

            super.approveSelection();
        }//approveSelection()
    }//JFileChooserEx
}
