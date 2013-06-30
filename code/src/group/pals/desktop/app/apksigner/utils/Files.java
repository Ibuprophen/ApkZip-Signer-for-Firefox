/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner.utils;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.ui.Dlg;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * File utilities.
 * 
 * @author Hai Bison
 * 
 */
public class Files {

    /**
     * Removes invalid characters...
     * 
     * @param name
     *            the name to fix.
     * @return the "fresh" name :-)
     */
    public static String fixFilename(String name) {
        return name == null ? null : name.replaceAll("[\\\\/?%*:|\"<>]+", "")
                .trim();
    }// fixFilename()

    /**
     * Appends {@code suffix} to {@code fileName}, makes sure the {@code suffix}
     * is placed before the file's extension (if there is one).
     * 
     * @param fileName
     *            the original file name.
     * @param suffix
     *            the suffix.
     * @return the new file name.
     */
    public static String appendFilename(String fileName, String suffix) {
        if (fileName.matches("(?si).+\\.[^ \t]+")) {
            final int iPeriod = fileName.lastIndexOf(KeyEvent.VK_PERIOD);
            return fileName.substring(0, iPeriod) + suffix
                    + (char) KeyEvent.VK_PERIOD
                    + fileName.substring(iPeriod + 1);
        }

        return fileName + suffix;
    }// appendFilename()

    /**
     * Opens a dialog to choose a file.
     * 
     * @param startupDir
     *            the startup directory.
     * @return the chosen file, or {@code null} if the user cancelled.
     */
    public static File chooseFile(File startupDir) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(Messages.getString(R.string.choose_file));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (fc.showOpenDialog(null)) {
        case JFileChooser.APPROVE_OPTION:
            return fc.getSelectedFile();
        default:
            return null;
        }
    }// chooseFile()

    /**
     * Opens a dialog to choose a file.
     * 
     * @param startupDir
     *            the startup directory.
     * @param regexFilenameFilter
     *            the regex filename filter.
     * @param description
     *            the file filter description.
     * @return the chosen file, can be {@code null}.
     */
    public static File chooseFile(File startupDir, String regexFilenameFilter,
            String description) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(Messages.getString(R.string.choose_file));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.addFilenameFilter(regexFilenameFilter, description, true);

        switch (fc.showOpenDialog(null)) {
        case JFileChooser.APPROVE_OPTION:
            return fc.getSelectedFile();
        default:
            return null;
        }
    }// chooseFile()

    /**
     * Opens a dialog to choose a directory.
     * 
     * @param startupDir
     *            the startup directory.
     * @return the chosen directory, can be {@code null}.
     */
    public static File chooseDir(File startupDir) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(Messages.getString(R.string.choose_directory));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        switch (fc.showOpenDialog(null)) {
        case JFileChooser.APPROVE_OPTION:
            return fc.getSelectedFile();
        default:
            return null;
        }
    }// chooseDir()

    /**
     * Opens a dialog to choose a file to save.
     * 
     * @param startupDir
     *            the startup directory.
     * @return the chosen file, can be {@code null}.
     */
    public static File chooseFileToSave(File startupDir) {
        return chooseFileToSave(startupDir, null, null, null);
    }// chooseFileToSave()

    /**
     * Opens a dialog to choose a file to save.
     * 
     * @param startupDir
     *            the startup directory.
     * @param defaultFileExt
     *            the default file extension.
     * @param regexFilenameFilter
     *            the regex filename filter.
     * @param description
     *            the file filter description.
     * @return the chosen file, can be {@code null}.
     */
    public static File chooseFileToSave(File startupDir, String defaultFileExt,
            String regexFilenameFilter, String description) {
        JFileChooserEx fc = new JFileChooserEx(startupDir);
        fc.setDialogTitle(Messages.getString(R.string.save_as));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDefaultFileExt(defaultFileExt);
        if (regexFilenameFilter != null)
            fc.addFilenameFilter(regexFilenameFilter, description, true);

        switch (fc.showSaveDialog(null)) {
        case JFileChooser.APPROVE_OPTION:
            return fc.getSelectedFile();
        default:
            return null;
        }
    }// chooseFileToSave()

    /**
     * Creates new file filter.
     * 
     * @param fileSelectionMode
     *            one of {@link JFileChooser#FILES_ONLY},
     *            {@link JFileChooser#DIRECTORIES_ONLY},
     *            {@link JFileChooser#FILES_AND_DIRECTORIES}.
     * @param regex
     *            the regex string to filter filenames.
     * @param description
     *            the description.
     * @return the {@link FileFilter} object.
     */
    public static FileFilter newFileFilter(final int fileSelectionMode,
            final String regex, final String description) {
        return new FileFilter() {

            @Override
            public boolean accept(File f) {
                if (fileSelectionMode == JFileChooser.DIRECTORIES_ONLY) {
                    return f.getName().matches(regex);
                } else if (f.isDirectory()) {
                    return true;
                } else {
                    return f.getName().matches(regex);
                }
            }// accept()

            @Override
            public String getDescription() {
                return description;
            }// getDescription()
        };
    }// newFileFilter()

    /**
     * Extended class of {@link JFileChooser}, which hacks some methods :-)
     */
    public static class JFileChooserEx extends JFileChooser {

        /**
         * Auto-generated by Eclipse.
         */
        private static final long serialVersionUID = -8249130783203341207L;

        private String mDefaultFileExt;

        /**
         * Creates new instance.
         * 
         * @param startupDir
         *            the startup directory.
         */
        public JFileChooserEx(File startupDir) {
            super(startupDir);
        }// JFileChooserEx()

        /**
         * Adds the regex file name filter.
         * 
         * @param regex
         *            the regular expression.
         * @param description
         *            the description.
         * @return the {@link FileFilter}.
         */
        public FileFilter addFilenameFilter(final String regex,
                final String description) {
            return addFilenameFilter(regex, description, false);
        }// addFilenameFilter()

        /**
         * Adds the regex file name filter.
         * 
         * @param regex
         *            the regular expression.
         * @param description
         *            the description.
         * @param setAsMainFilter
         *            {@code true} if you want to set the main filter to this
         *            one.
         * @return the {@link FileFilter}.
         */
        public FileFilter addFilenameFilter(final String regex,
                final String description, boolean setAsMainFilter) {
            final FileFilter filter = new FileFilter() {

                @Override
                public boolean accept(File f) {
                    if (getFileSelectionMode() == DIRECTORIES_ONLY) {
                        return f.getName().matches(regex);
                    } else if (f.isDirectory()) {
                        return true;
                    } else {
                        return f.getName().matches(regex);
                    }
                }// accept()

                @Override
                public String getDescription() {
                    return description;
                }// getDescription()
            };

            addChoosableFileFilter(filter);
            if (setAsMainFilter)
                setFileFilter(filter);

            return filter;
        }// addFilenameFilter()

        /**
         * Sets default file extension in {@link #SAVE_DIALOG} mode.
         * 
         * @param fileExt
         *            the default file extension to set.
         * @return the instance of this class, to allow chaining multiple calls
         *         into a single statement.
         */
        public JFileChooserEx setDefaultFileExt(String fileExt) {
            mDefaultFileExt = fileExt;
            return this;
        }// setDefaultFileExt()

        @Override
        public void approveSelection() {
            switch (getDialogType()) {
            case SAVE_DIALOG: {
                if (getCurrentDirectory() == null
                        || !getCurrentDirectory().canWrite()) {
                    Dlg.showErrMsg(Messages
                            .getString(R.string.msg_cannot_save_a_file_here));
                    return;
                }

                File file = getSelectedFile();
                if (file != null && mDefaultFileExt != null) {
                    if (!file.getName().matches(
                            "(?si).+" + Pattern.quote(mDefaultFileExt))) {
                        file = new File(file.getParent() + File.separator
                                + file.getName() + mDefaultFileExt);
                        setSelectedFile(file);
                    }
                }
                if ((file != null) && file.exists()) {
                    final String[] userOptions = {
                            Messages.getString(R.string.yes),
                            Messages.getString(R.string.no) };
                    int usrOption = JOptionPane.showOptionDialog(this, Messages
                            .getString(R.string.pmsg_override_file,
                                    file.getName()), Messages
                            .getString(R.string.confirmation),
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE, null, userOptions,
                            userOptions[1]);
                    if (usrOption != 0)
                        return;
                }
                break;
            }// case SAVE_DIALOG

            case OPEN_DIALOG: {
                File file = getSelectedFile();
                if (file == null || !file.exists()) {
                    Dlg.showErrMsg(Messages.getString(
                            R.string.pmsg_file_not_exist, file == null ? ""
                                    : file.getName()));
                    return;
                }
                break;
            }// case OPEN_DIALOG
            }

            super.approveSelection();
        }// approveSelection()
    }// JFileChooserEx
}
