/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.services.BaseThread;
import group.pals.desktop.app.apksigner.services.INotification;
import group.pals.desktop.app.apksigner.services.ServiceManager;
import group.pals.desktop.app.apksigner.services.Updater;
import group.pals.desktop.app.apksigner.ui.prefs.DialogPreferences;
import group.pals.desktop.app.apksigner.utils.Files;
import group.pals.desktop.app.apksigner.utils.Preferences;
import group.pals.desktop.app.apksigner.utils.Sys;
import group.pals.desktop.app.apksigner.utils.Texts;
import group.pals.desktop.app.apksigner.utils.ui.Dlg;
import group.pals.desktop.app.apksigner.utils.ui.FileDrop;
import group.pals.desktop.app.apksigner.utils.ui.JEditorPopupMenu;
import group.pals.desktop.app.apksigner.utils.ui.UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.io.File;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

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

    /**
     * This key holds the last tab index.
     */
    private static final String PKEY_LAST_TAB_INDEX = CLASSNAME
            + ".last_tab_index";

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
     * Create the application.
     */
    public MainActivity() {
        initialize();

        UI.setWindowCenterScreen(mMainFrame, 65);
        JEditorPopupMenu.apply(mMainFrame);

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
                    Dlg.showInfoMsg(Messages
                            .getString(R.string.msg_restart_app_to_apply_new_language));
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
        new FileDrop(mTextJdkPath, BorderFactory.createTitledBorder(
                UI.BORDER_FILE_DROP,
                Messages.getString(R.string.desc_jdk_path),
                TitledBorder.LEADING, TitledBorder.TOP, null,
                UI.COLOUR_BORDER_FILE_DROP), mTextJdkPathFileDropListener);

        mMainFrame.addWindowListener(mMainFrameWindowAdapter);
        UI.initJTabbedPaneHeaderMouseWheelListener(mTabbedPane);

        initTabs();

        /*
         * START UPDATER SERVICE
         */

        ServiceManager.registerThread(mUpdater = new Updater());
        mUpdater.addNotification(mUpdaterServiceNotification);
        mUpdater.start();
    }// MainActivity()

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        mMainFrame = new JFrame();
        mMainFrame.setBounds(100, 100, 450, 300);
        mMainFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        mMainFrame.setIconImage(Assets.getIconLogo());

        JMenuBar mMenuBar = new JMenuBar();
        mMainFrame.setJMenuBar(mMenuBar);

        JMenu mMenuFile = new JMenu(Messages.getString(R.string.file)); //$NON-NLS-1$
        mMenuBar.add(mMenuFile);

        JMenuItem mMenuItemPreferences = new JMenuItem(
                Messages.getString(R.string.settings)); //$NON-NLS-1$
        mMenuItemPreferences
                .addActionListener(mMenuItemPreferencesActionListener);
        mMenuFile.add(mMenuItemPreferences);

        mMenuFile.addSeparator();

        JMenuItem mMenuItemExit = new JMenuItem(
                Messages.getString(R.string.exit)); //$NON-NLS-1$
        mMenuItemExit.addActionListener(mMenuItemExitActionListener);
        mMenuItemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
                InputEvent.CTRL_MASK));
        mMenuFile.add(mMenuItemExit);

        JMenu mMenuHelp = new JMenu(Messages.getString(R.string.help)); //$NON-NLS-1$
        mMenuBar.add(mMenuHelp);

        JMenuItem mMenuItemAbout = new JMenuItem(
                Messages.getString(R.string.about)); //$NON-NLS-1$
        mMenuItemAbout.addActionListener(mMenuItemAboutActionListener);
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
        mBtnChooseJdkPath.addActionListener(mBtnChooseJdkPathActionListener);
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

    /**
     * Initializes tabs.
     */
    private void initTabs() {
        if (Beans.isDesignTime())
            return;

        mTabbedPane.add(Messages.getString(R.string.key_generator),
                new PanelKeyGen());
        mTabbedPane.add(Messages.getString(R.string.signer), new PanelSigner());
        mTabbedPane.add(Messages.getString(R.string.apk_alignment),
                new PanelApkAlignment());
        mTabbedPane.add(Messages.getString(R.string.key_tools),
                new PanelKeyTools());

        /*
         * Select the last tab index.
         */

        int lastTabIndex = 0;
        try {
            lastTabIndex = Integer.parseInt(Preferences.getInstance().get(
                    PKEY_LAST_TAB_INDEX));
        } catch (Exception e) {
            /*
             * Ignore it.
             */
        }

        if (lastTabIndex >= mTabbedPane.getTabCount())
            lastTabIndex = mTabbedPane.getTabCount() - 1;
        if (lastTabIndex < 0)
            lastTabIndex = 0;

        mTabbedPane.setSelectedIndex(lastTabIndex);
    }// initTabs()

    /**
     * Shows main window.
     */
    public void show() {
        mMainFrame.setVisible(true);
    }// show()

    /**
     * Sets the JDK path.
     * 
     * @param file
     *            the JDK path to set.
     */
    private void setJdkPath(File file) {
        if (file != null && file.isDirectory() && file.canRead()) {
            Preferences.getInstance().set(PKEY_LAST_WORKING_DIR,
                    file.getParentFile().getAbsolutePath());
            Preferences.getInstance().setJdkPath(file);
            mTextJdkPath.setText(file.getAbsolutePath());
        } else {
            Preferences.getInstance().setJdkPath(null);
            mTextJdkPath.setText(null);
        }
    }// setJdkPath()

    /*
     * LISTENERS
     */

    private final WindowAdapter mMainFrameWindowAdapter = new WindowAdapter() {

        @Override
        public void windowClosing(WindowEvent e) {
            Preferences.getInstance().set(PKEY_LAST_TAB_INDEX,
                    Integer.toString(mTabbedPane.getSelectedIndex()));
            Preferences.getInstance().store();

            final List<BaseThread> activeThreads = ServiceManager
                    .getActiveThreads();
            if (activeThreads.isEmpty()) {
                mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            } else {
                StringBuilder serviceNames = new StringBuilder();
                for (BaseThread thread : activeThreads)
                    serviceNames.append(" - ").append(thread.getName())
                            .append('\n');
                if (Dlg.confirmYesNo(
                        String.format(
                                "%s\n\n%s\n%s",
                                Messages.getString(
                                        activeThreads.size() > 1 ? R.string.pmsg_there_are_x_services_running
                                                : R.string.pmsg_there_is_x_service_running,
                                        activeThreads.size()),
                                serviceNames,
                                Messages.getString(R.string.msg_do_you_want_to_exit)),
                        false)) {
                    for (BaseThread thread : activeThreads)
                        thread.interrupt();
                    mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }// confirmYesNo()
            }// There are active threads
        }// windowClosing()
    };// mMainFrameWindowAdapter

    private final INotification mUpdaterServiceNotification = new INotification() {

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
                            Dlg.showInfoMsg(msg.detailedMessage);
                    }// actionPerformed()
                });
                break;
            }// default
            }

            return false;
        }// onMessage()
    };// mUpdaterServiceNotification

    private final ActionListener mMenuItemAboutActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            final String msg = String
                    .format("<html><body topmargin=\"10px\" leftmargin=\"10px\" marginheight=\"10px\" marginwidth=\"10px\">"
                            + "<p><h3>%s</h3></p>"
                            + "<p>...by Hai Bison</p>"
                            + "<p><ul><li>License: MIT License</li>"
                            + "<li>Code page: <a href=\"https://code.google.com/p/apk-signer/\">https://code.google.com/p/apk-signer/</a></li>"
                            + "<li>Official site: <a href=\"http://www.haibison.com\">http://www.haibison.com</a></li></ul><p>"
                            + "<p>"
                            + "We sincerely thank:"
                            + "</p><ul>"
                            + "<li>All of our friends, who have been contributing to this project.</li>"
                            + "<li>The authors of external modules/ libraries which are used in this project.</li>"
                            + "</ul></p>"
                            + "<p>We hope this project will be always useful for everyone.</p>"
                            + ""
                            + "<p><h2>CREDITS</h2></p>"
                            + "<p><ul>"
                            + "<li>Hans Bickel (library <a href=\"http://www.muntjak.de/hans/java/tinylaf/index.html\">TinyLaF</a>)"
                            + "<ul>"
                            + "<li>License: <a href=\"http://www.gnu.org/licenses/lgpl.html\">GNU Lesser General Public License</a></li>"
                            + "</ul>"
                            + "</li>"
                            + "<li>Leo Chien (contributor)"
                            + "<ul><li><a href=\"https://plus.google.com/118055781130476825691?prsrc=2\">Google+ page</a></li></ul>"
                            + "</li>"
                            + "<li>Robert Harder and his friends (module <a href=\"http://www.iharder.net/current/java/filedrop/\">FileDrop</a>)"
                            + "<ul>"
                            + "<li>License: Public Domain</li>"
                            + "</ul>"
                            + "</li>"
                            + "<li>The Android Open Source Project (module <a href=\"https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/util/\">AOSP Base64</a>)"
                            + "<ul>"
                            + "<li>License: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache License, Version 2.0</a></li></ul>"
                            + "</li>"
                            + "<li>Paul D. Hunt (font Source Code Pro)"
                            + "<ul><li>License: <a href=\"http://scripts.sil.org/OFL\">SIL OPEN FONT LICENSE Version 1.1 - 26 February 2007</a></li></ul>"
                            + "</li>"
                            + "<li>Christian Robertson (font Roboto)"
                            + "<ul><li>License: <a href=\"http://www.apache.org/licenses/LICENSE-2.0\">Apache License, Version 2.0</a></li></ul>"
                            + "</li>" + "</ul></p>" + "</body></html>",
                            Messages.getString(R.string.pmsg_app_name,
                                    Sys.APP_NAME, Sys.APP_VERSION_NAME));

            JLabel label = new JLabel(new ImageIcon(Assets.getIconSplash()),
                    SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.TOP);
            label.setHorizontalTextPosition(SwingConstants.CENTER);
            label.setVerticalTextPosition(SwingConstants.BOTTOM);
            label.setText(msg);

            JScrollPane scrollPane = new JScrollPane(label);
            Dimension size = new Dimension(630, 270);
            scrollPane.setMaximumSize(size);
            scrollPane.setMinimumSize(size);
            scrollPane.setPreferredSize(size);

            Dlg.showInfoMsg(null, null, scrollPane);
        }// actionPerformed()
    };// mMenuItemAboutActionListener

    private final ActionListener mMenuItemPreferencesActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            DialogPreferences dialog = new DialogPreferences(mMainFrame);
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            UI.setWindowCenterScreen(dialog, 40);
            dialog.setVisible(true);
        }// actionPerformed()
    };// mMenuItemPreferencesActionListener

    private final ActionListener mMenuItemExitActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            mMainFrame.dispatchEvent(new WindowEvent(mMainFrame,
                    WindowEvent.WINDOW_CLOSING));
        }// actionPerformed()
    };// mMenuItemExitActionListener

    private final ActionListener mBtnChooseJdkPathActionListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            setJdkPath(Files.chooseDir(new File(Preferences.getInstance().get(
                    PKEY_LAST_WORKING_DIR, "/"))));
        }// actionPerformed()
    };// mBtnChooseJdkPathActionListener

    private final FileDrop.Listener mTextJdkPathFileDropListener = new FileDrop.Listener() {

        @Override
        public void onFilesDropped(File[] files) {
            setJdkPath(files[0]);
        }// onFilesDropped()
    };// mTextJdkPathFileDropListener
}
