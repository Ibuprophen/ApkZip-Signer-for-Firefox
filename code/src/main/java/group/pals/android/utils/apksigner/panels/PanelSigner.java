/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

/*
 * PanelSigner.java
 *
 * Created on Feb 15, 2012, 6:57:44 PM
 */
package group.pals.android.utils.apksigner.panels;

import group.pals.android.utils.apksigner.MainFrame;
import group.pals.android.utils.apksigner.panels.ui.JEditorPopupMenu;
import group.pals.android.utils.apksigner.utils.Files;
import group.pals.android.utils.apksigner.utils.MsgBox;
import group.pals.android.utils.apksigner.utils.Signer;
import group.pals.android.utils.apksigner.utils.UI;
import group.pals.android.utils.apksigner.utils.prefs.Prefs;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import javax.swing.AbstractAction;

/**
 * The APK signer panel.
 *
 * @author Hai Bison
 */
public class PanelSigner extends javax.swing.JPanel {

    private final MainFrame mMainFrame;

    /**
     * Creates new form PanelSigner.
     *
     * @param mainFrame the main frame.
     */
    public PanelSigner(MainFrame mainFrame) {
        mMainFrame = mainFrame;

        initComponents();
        setKeyFile(null);
        setApkFile(null);

        btnLoadKeyFile.addActionListener(mBtnLoadKeyFileListener);
        btnLoadApkFile.addActionListener(mBtnLoadApkFileListener);
        btnSignFile.setAction(mActionSign);

        txtPwd.addKeyListener(mTxtPwdKeyListener);
        UI.setEditorPopupMenu(this, new JEditorPopupMenu());
    }//PanelSigner()

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        btnLoadKeyFile = new javax.swing.JButton();
        txtPwd = new javax.swing.JPasswordField();
        txtAlias = new javax.swing.JTextField();
        txtAliasPwd = new javax.swing.JPasswordField();
        btnLoadApkFile = new javax.swing.JButton();
        btnSignFile = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        btnLoadKeyFile.setText("jButton1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 1.0;
        add(btnLoadKeyFile, gridBagConstraints);

        txtPwd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPwd.setBorder(javax.swing.BorderFactory.createTitledBorder("Password:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(txtPwd, gridBagConstraints);

        txtAlias.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAlias.setBorder(javax.swing.BorderFactory.createTitledBorder("Alias:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.5;
        add(txtAlias, gridBagConstraints);

        txtAliasPwd.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtAliasPwd.setBorder(javax.swing.BorderFactory.createTitledBorder("Alias password:"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(txtAliasPwd, gridBagConstraints);

        btnLoadApkFile.setText("jButton1");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        add(btnLoadApkFile, gridBagConstraints);

        btnSignFile.setText("Sign!");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.weighty = 1.0;
        add(btnSignFile, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLoadApkFile;
    private javax.swing.JButton btnLoadKeyFile;
    private javax.swing.JButton btnSignFile;
    private javax.swing.JTextField txtAlias;
    private javax.swing.JPasswordField txtAliasPwd;
    private javax.swing.JPasswordField txtPwd;
    // End of variables declaration//GEN-END:variables
    /**
     * Preferences
     */
    private final Prefs mPrefs = Prefs.getInstance();
    private static final String KEY_KEYFILE_LAST_WORKING_DIR = PanelSigner.class.getName() + ".key-file-last-working-dir";
    private static final String KEY_APK_FILE_LAST_WORKING_DIR = PanelSigner.class.getName() + ".apk-file-last-working-dir";
    private File mKeyFile;

    /**
     * Gets the keystore file.
     *
     * @return the key file
     */
    public File getKeyFile() {
        return mKeyFile;
    }//getKeyFile()

    /**
     * Sets the keystore file.
     *
     * @param file the key file to set.
     */
    public void setKeyFile(File file) {
        mKeyFile = file;
        btnLoadKeyFile.setText(file == null ? "Load key-file..." : String.format("[ %s ]", file.getName()));
        btnLoadKeyFile.setForeground(file == null ? Color.cyan : UI.COLOUR_SELECTED_FILE);
    }//setKeyFile()
    private File mApkFile;

    /**
     * Gets the APK file.
     *
     * @return the APK file.
     */
    public File getApkFile() {
        return mApkFile;
    }//getApkFile()

    /**
     * Sets the APK file.
     *
     * @param file the APK file to set.
     */
    public void setApkFile(File file) {
        mApkFile = file;
        btnLoadApkFile.setText(file == null ? "Load apk-file..." : String.format("[ %s ]", file.getName()));
        btnLoadApkFile.setForeground(file == null ? Color.cyan : UI.COLOUR_SELECTED_FILE);
    }//setApkFile()

    /*
     * LISTENERS
     */
    private final ActionListener mBtnLoadKeyFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File f = Files.chooseFile(new File(mPrefs.get(KEY_KEYFILE_LAST_WORKING_DIR, "/")));
            if (f != null) {
                setKeyFile(f);
                mPrefs.set(KEY_KEYFILE_LAST_WORKING_DIR, f.getParent());
            }
        }//actionPerformed()
    };//mBtnLoadKeyFileListener
    private final ActionListener mBtnLoadApkFileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File f = Files.chooseFile(new File(mPrefs.get(KEY_APK_FILE_LAST_WORKING_DIR, "/")), "(?si).*apk", "APK files");
            if (f != null) {
                setApkFile(f);
                mPrefs.set(KEY_APK_FILE_LAST_WORKING_DIR, f.getParent());
            }
        }//actionPerformed()
    };//mBtnLoadApkFileListener
    private final AbstractAction mActionSign = new AbstractAction("Sign!") {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (getKeyFile() == null || !getKeyFile().isFile()) {
                MsgBox.showErrMsg(null, null, "Key-file does not exist");
                return;
            }
            if (getApkFile() == null || !getApkFile().isFile()) {
                MsgBox.showErrMsg(null, null, "APK file does not exist");
                return;
            }
            String alias = String.valueOf(txtAlias.getText());
            if (alias.isEmpty()) {
                MsgBox.showErrMsg(null, null, "Alias is empty");
                return;
            }

            try {
                String info = Signer.sign(mMainFrame.getJdkDir(), getApkFile(), getKeyFile(), new String(txtPwd.getPassword()), alias, new String(txtAliasPwd.getPassword()));
                if (info == null || info.isEmpty()) {
                    MsgBox.showInfoMsg(null, null, "APK is signed");
                } else {
                    MsgBox.showHugeInfoMsg(null, null, info, 800, 450);
                }
            } catch (Exception ex) {
                MsgBox.showErrMsg(null, null, "Error while signing file. Please try again.\n\nDetails:\n\n" + ex);
            }
        }//actionPerformed()
    };//mActionSign
    /**
     * TODO: load aliases automatically, and let user to be able to choose them
     */
    private final KeyListener mTxtPwdKeyListener = new KeyAdapter() {
        @Override
        public void keyTyped(KeyEvent e) {
//            try {
//                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
//
//                char[] pwd = txtPwd.getPassword();
//                
//                java.io.FileInputStream fis = null;
//                try {
//                    fis = new java.io.FileInputStream("keyStoreName");
//                    ks.load(fis, pwd);
//
//                    // get my private key
//                    KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) ks.getEntry("privateKeyAlias", pwd);
//                    PrivateKey myPrivateKey = pkEntry.getPrivateKey();
//                } finally {
//                    if (fis != null) {
//                        fis.close();
//                    }
//                }
//            } catch (Exception ex) {
//            }
        }
    };//mTxtPwdKeyListener
}
