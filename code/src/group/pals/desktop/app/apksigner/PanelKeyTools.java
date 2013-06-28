/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.ui.Dlg;
import group.pals.desktop.app.apksigner.ui.JEditorPopupMenu;
import group.pals.desktop.app.apksigner.utils.Files;
import group.pals.desktop.app.apksigner.utils.KeyTools;
import group.pals.desktop.app.apksigner.utils.Preferences;
import group.pals.desktop.app.apksigner.utils.Texts;
import group.pals.desktop.app.apksigner.utils.UI;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * Panel for keystore utilities.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class PanelKeyTools extends JPanel {

    /**
     * Auto-generated by Eclipse.
     */
    private static final long serialVersionUID = 416508787192252476L;

    /**
     * The class name.
     */
    private static final String CLASSNAME = PanelKeyTools.class.getName();

    /**
     * This key holds the last working directory.
     */
    private static final String PKEY_LAST_WORKING_DIR = CLASSNAME
            + ".last_working_dir";

    /*
     * FIELDS
     */

    private File mKeyfile;

    /*
     * CONTROLS
     */

    private JPasswordField mTextPassword;
    private JButton mBtnChooseKeyfile;
    private JButton mBtnListEntries;
    private JTextArea mTextInfo;

    /**
     * Create the panel.
     */
    public PanelKeyTools() {
        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] { 0, 0 };
        gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0 };
        gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
        gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 1.0,
                Double.MIN_VALUE };
        setLayout(gridBagLayout);

        mBtnChooseKeyfile = new JButton(
                Messages.getString(R.string.desc_load_key_file));
        mBtnChooseKeyfile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mKeyfile = Files.chooseFile(new File(Preferences.getInstance()
                        .get(PKEY_LAST_WORKING_DIR, "/")),
                        Texts.REGEX_KEYSTORE_FILES, Messages
                                .getString(R.string.desc_keystore_files));
                if (mKeyfile != null) {
                    mBtnChooseKeyfile.setText(mKeyfile.getName());
                    mBtnChooseKeyfile.setForeground(UI.COLOUR_SELECTED_FILE);
                    Preferences.getInstance().set(PKEY_LAST_WORKING_DIR,
                            mKeyfile.getParentFile().getAbsolutePath());
                    mTextPassword.requestFocus();
                } else {
                    mBtnChooseKeyfile.setText(Messages
                            .getString(R.string.desc_load_key_file));
                    mBtnChooseKeyfile.setForeground(UI.COLOUR_WAITING_CMD);
                }
            }// actionPerformed()
        });
        GridBagConstraints gbc_mBtnChooseKeyfile = new GridBagConstraints();
        gbc_mBtnChooseKeyfile.insets = new Insets(10, 3, 3, 3);
        gbc_mBtnChooseKeyfile.gridx = 0;
        gbc_mBtnChooseKeyfile.gridy = 0;
        add(mBtnChooseKeyfile, gbc_mBtnChooseKeyfile);

        mTextPassword = new JPasswordField();
        mTextPassword.setHorizontalAlignment(SwingConstants.CENTER);
        mTextPassword.setBorder(new TitledBorder(null, Messages
                .getString(R.string.password), TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        GridBagConstraints gbc_mTextPassword = new GridBagConstraints();
        gbc_mTextPassword.insets = new Insets(3, 3, 3, 3);
        gbc_mTextPassword.fill = GridBagConstraints.HORIZONTAL;
        gbc_mTextPassword.gridx = 0;
        gbc_mTextPassword.gridy = 1;
        add(mTextPassword, gbc_mTextPassword);

        mBtnListEntries = new JButton(Messages.getString(R.string.list_entries));
        mBtnListEntries.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields())
                    listEntries();
            }// actionPerformed()
        });
        GridBagConstraints gbc_mBtnListEntries = new GridBagConstraints();
        gbc_mBtnListEntries.insets = new Insets(3, 3, 3, 3);
        gbc_mBtnListEntries.gridx = 0;
        gbc_mBtnListEntries.gridy = 2;
        add(mBtnListEntries, gbc_mBtnListEntries);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(3, 3, 3, 3);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 3;
        add(scrollPane, gbc_scrollPane);

        mTextInfo = new JTextArea();
        mTextInfo.setEditable(false);
        mTextInfo.setFont(new Font("Monospaced",
                mTextInfo.getFont().getStyle(), mTextInfo.getFont().getSize()));
        mTextInfo.setMargin(new Insets(9, 9, 9, 9));
        mTextInfo.setTabSize(4);
        scrollPane.setViewportView(mTextInfo);

        UI.setEditorPopupMenu(this, new JEditorPopupMenu());
    }// PanelKeyTools()

    /**
     * Validates all fields.
     * 
     * @return {@code true} or {@code false}.
     */
    private boolean validateFields() {
        if (mKeyfile == null || !mKeyfile.isFile() || !mKeyfile.canRead()) {
            Dlg.showErrMsg(null, null,
                    Messages.getString(R.string.msg_keyfile_doesnt_exist));
            mBtnChooseKeyfile.requestFocus();
            return false;
        }

        if (mTextPassword.getPassword() == null
                || mTextPassword.getPassword().length == 0) {
            Dlg.showErrMsg(null, null,
                    Messages.getString(R.string.msg_password_is_empty));
            mTextPassword.requestFocus();
            return false;
        }

        return true;
    }// validateFields()

    /**
     * Lists all entries of a keyfile.
     * <p>
     * <b>Notes:</b> You should call {@link #validateFields()} first.
     * </p>
     */
    private void listEntries() {
        CharSequence result = KeyTools.listEntries(Preferences.getInstance()
                .getJdkPath(), mKeyfile, mTextPassword.getPassword());
        mTextInfo.setText(result.toString().trim());
    }// listEntries()
}
