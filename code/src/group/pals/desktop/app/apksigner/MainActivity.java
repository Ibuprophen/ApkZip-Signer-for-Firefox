/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.services.INotification;
import group.pals.desktop.app.apksigner.services.Updater;
import group.pals.desktop.app.apksigner.ui.Dlg;
import group.pals.desktop.app.apksigner.ui.JEditorPopupMenu;
import group.pals.desktop.app.apksigner.utils.Files;
import group.pals.desktop.app.apksigner.utils.L;
import group.pals.desktop.app.apksigner.utils.Preferences;
import group.pals.desktop.app.apksigner.utils.Sys;
import group.pals.desktop.app.apksigner.utils.Texts;
import group.pals.desktop.app.apksigner.utils.UI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.TitledBorder;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.ThemeDescription;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;

/**
 * Main activity.
 * 
 * @author Hai Bison
 * @since v1.6 beta
 */
public class MainActivity {

    /**
     * The class name.
     */
    private static final String CLASSNAME = MainActivity.class.getName();

    /**
     * This key holds the last working directory.
     */
    private static final String PKEY_LAST_WORKING_DIR = CLASSNAME
            + ".last_working_dir";

    /*
     * FIELDS
     */

    private Updater mUpdater;

    /*
     * CONTROLS
     */

    private JMenuItem mMenuItemNotification;
    private JFrame mMainFrame;
    private JTextField mTextJdkPath;
    private JTabbedPane mTabbedPane;
    private JMenu mMenuLanguage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        L.i(Messages.getString(R.string.pmsg_app_name, Sys.APP_NAME,
                Sys.APP_VERSION_NAME));

        Locale.setDefault(Locale.forLanguageTag(Preferences.getInstance()
                .getLocaleTag()));

        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    MainActivity window = new MainActivity();
                    window.mMainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }// main()

    /**
     * Create the application.
     */
    public MainActivity() {
        initialize();

        /*
         * THEME
         */

        try {
            ThemeDescription[] availableThemes = Theme.getAvailableThemes();
            for (ThemeDescription td : availableThemes) {
                if (td.getURL().toString().matches("(?i).*nightly.*")) {
                    Theme.loadTheme(td);
                    UIManager.setLookAndFeel(new TinyLookAndFeel());
                    SwingUtilities.updateComponentTreeUI(mMainFrame);
                    break;
                }
            }
        } catch (UnsupportedLookAndFeelException ex) {
            /*
             * Ignore it.
             */
        }

        UI.setWindowCenterScreen(mMainFrame, 59);
        UI.setEditorPopupMenu(mMainFrame, new JEditorPopupMenu());

        /*
         * INITIALIZE CONTROLS
         */

        mMainFrame.setTitle(Messages.getString(R.string.pmsg_app_name,
                Sys.APP_NAME, Sys.APP_VERSION_NAME));

        /*
         * LANGUAGES
         */
        String localeTag = Preferences.getInstance().getLocaleTag();
        if (!Messages.AVAILABLE_LOCALES.containsKey(localeTag)) {
            Preferences.getInstance().setLocaleTag(Messages.DEFAULT_LOCALE);
            localeTag = Messages.DEFAULT_LOCALE;
        }
        ButtonGroup group = new ButtonGroup();
        for (final String tag : Messages.AVAILABLE_LOCALES.keySet()) {
            JRadioButtonMenuItem menuItem = new JRadioButtonMenuItem(
                    Messages.AVAILABLE_LOCALES.get(tag));
            menuItem.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Preferences.getInstance().setLocaleTag(tag);
                    Dlg.showInfoMsg(
                            null,
                            null,
                            Messages.getString(R.string.msg_restart_app_to_apply_new_language));
                }// actionPerformed()
            });

            if (localeTag.equals(tag))
                menuItem.setSelected(true);

            group.add(menuItem);

            mMenuLanguage.add(menuItem);
        }// for

        File jdkPath = Preferences.getInstance().getJdkPath();
        if (jdkPath != null && jdkPath.isDirectory())
            mTextJdkPath.setText(jdkPath.getAbsolutePath());

        mTabbedPane.add(Messages.getString(R.string.key_generator),
                new PanelKeyGen());
        mTabbedPane.add(Messages.getString(R.string.signer), new PanelSigner());
        mTabbedPane.add(Messages.getString(R.string.key_tools),
                new PanelKeyTools());

        mMainFrame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                Preferences.getInstance().store();

                /*
                 * In case the updater service finished while the confirmation
                 * dialog is still active...
                 */
                final Updater updater = mUpdater;
                if (updater != null && updater.isAlive()) {
                    if (Dlg.confirmYesNo(null, null, Messages
                            .getString(R.string.msg_app_updating_confirm_exit),
                            false)) {
                        updater.interrupt();
                        mMainFrame
                                .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    }
                } else
                    mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }// windowClosed()
        });

        /*
         * START UPDATER SERVICE
         */

        mUpdater = new Updater();
        mUpdater.setNotification(new INotification() {

            @Override
            public boolean onMessage(final Message msg) {
                switch (msg.id) {
                case Updater.MSG_DONE: {
                    mUpdater = null;
                    break;
                }// MSG_DONE

                default: {
                    mMenuItemNotification.setText(msg.shortMessage);
                    mMenuItemNotification.setAction(new AbstractAction(
                            msg.shortMessage) {

                        /**
                         * Auto-generated by Eclipse.
                         */
                        private static final long serialVersionUID = -7002312198404671927L;

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (!Texts.isEmpty(msg.detailedMessage))
                                Dlg.showInfoMsg(null, null, msg.detailedMessage);
                        }// actionPerformed()
                    });
                    break;
                }// default
                }

                return false;
            }// onMessage()
        });
        mUpdater.start();
    }// MainActivity()

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mMainFrame = new JFrame();
        mMainFrame.setBounds(100, 100, 450, 300);
        mMainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        JMenuBar mMenuBar = new JMenuBar();
        mMainFrame.setJMenuBar(mMenuBar);

        JMenu mMenuFile = new JMenu(Messages.getString(R.string.file)); //$NON-NLS-1$
        mMenuBar.add(mMenuFile);

        JMenuItem mMenuItemExit = new JMenuItem(
                Messages.getString(R.string.exit)); //$NON-NLS-1$
        mMenuItemExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                mMainFrame.dispatchEvent(new WindowEvent(mMainFrame,
                        WindowEvent.WINDOW_CLOSING));
            }// actionPerformed()
        });
        mMenuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));
        mMenuFile.add(mMenuItemExit);

        JMenu mMenuHelp = new JMenu(Messages.getString(R.string.help)); //$NON-NLS-1$
        mMenuBar.add(mMenuHelp);

        JMenuItem mMenuItemAbout = new JMenuItem(
                Messages.getString(R.string.about)); //$NON-NLS-1$
        mMenuItemAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String msg = String
                        .format("%s\n\n"
                                + "...by Hai Bison\n\n"
                                + " - License: MIT License\n"
                                + " - Code page: https://code.google.com/p/apk-signer/\n"
                                + " - Official site: http://www.haibison.com\n"
                                + "\n"
                                + "Special thanks to Hans Bickel for TinyLaF library:\n"
                                + " - http://www.muntjak.de/hans/java/tinylaf/index.html\n"
                                + " - License: GNU Lesser General Public License\n"
                                + "\n"
                                + "And thanks to our friends who have been contributing to this project:\n"
                                + " - Leo Chien (https://plus.google.com/118055781130476825691?prsrc=2)",
                                Messages.getString(R.string.pmsg_app_name,
                                        Sys.APP_NAME, Sys.APP_VERSION_NAME));
                Dlg.showHugeInfoMsg(null, null, msg, 630, 270);
            }// actionPerformed()
        });
        mMenuHelp.add(mMenuItemAbout);

        mMenuLanguage = new JMenu(Messages.getString(R.string.language));
        mMenuBar.add(mMenuLanguage);

        mMenuItemNotification = new JMenuItem();
        mMenuBar.add(mMenuItemNotification);
        mMenuItemNotification.setForeground(Color.yellow);

        SpringLayout springLayout = new SpringLayout();
        mMainFrame.getContentPane().setLayout(springLayout);

        mTextJdkPath = new JTextField();
        mTextJdkPath.setEditable(false);
        mTextJdkPath.setBorder(new TitledBorder(null, Messages
                .getString(R.string.desc_jdk_path), TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        springLayout.putConstraint(SpringLayout.NORTH, mTextJdkPath, 10,
                SpringLayout.NORTH, mMainFrame.getContentPane());
        springLayout.putConstraint(SpringLayout.WEST, mTextJdkPath, 10,
                SpringLayout.WEST, mMainFrame.getContentPane());
        mMainFrame.getContentPane().add(mTextJdkPath);

        JButton mBtnChooseJdkPath = new JButton(
                Messages.getString(R.string.choose_jdk_path)); //$NON-NLS-1$
        mBtnChooseJdkPath.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                File f = Files.chooseDir(new File(Preferences.getInstance()
                        .get(PKEY_LAST_WORKING_DIR, "/")));
                Preferences.getInstance().setJdkPath(f);
                if (f != null) {
                    Preferences.getInstance().set(PKEY_LAST_WORKING_DIR,
                            f.getParentFile().getAbsolutePath());
                    mTextJdkPath.setText(f.getAbsolutePath());
                } else
                    mTextJdkPath.setText(null);
            }// actionPerformed()
        });
        springLayout.putConstraint(SpringLayout.EAST, mTextJdkPath, -10,
                SpringLayout.WEST, mBtnChooseJdkPath);
        springLayout.putConstraint(SpringLayout.NORTH, mBtnChooseJdkPath, 10,
                SpringLayout.NORTH, mMainFrame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, mBtnChooseJdkPath, -10,
                SpringLayout.EAST, mMainFrame.getContentPane());
        mMainFrame.getContentPane().add(mBtnChooseJdkPath);

        mTabbedPane = new JTabbedPane(JTabbedPane.TOP);
        springLayout.putConstraint(SpringLayout.SOUTH, mBtnChooseJdkPath, -10,
                SpringLayout.NORTH, mTabbedPane);
        springLayout.putConstraint(SpringLayout.NORTH, mTabbedPane, 10,
                SpringLayout.SOUTH, mTextJdkPath);
        springLayout.putConstraint(SpringLayout.WEST, mTabbedPane, 5,
                SpringLayout.WEST, mMainFrame.getContentPane());
        springLayout.putConstraint(SpringLayout.SOUTH, mTabbedPane, -5,
                SpringLayout.SOUTH, mMainFrame.getContentPane());
        springLayout.putConstraint(SpringLayout.EAST, mTabbedPane, -5,
                SpringLayout.EAST, mMainFrame.getContentPane());
        mMainFrame.getContentPane().add(mTabbedPane);
    }// initialize()
}
