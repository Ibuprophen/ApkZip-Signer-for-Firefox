/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */

package group.pals.desktop.app.apksigner;

import group.pals.desktop.app.apksigner.i18n.Messages;
import group.pals.desktop.app.apksigner.i18n.R;
import group.pals.desktop.app.apksigner.utils.L;
import group.pals.desktop.app.apksigner.utils.Preferences;
import group.pals.desktop.app.apksigner.utils.Sys;
import group.pals.desktop.app.apksigner.utils.ui.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.beans.Beans;
import java.util.List;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.muntjak.tinylookandfeel.Theme;
import de.muntjak.tinylookandfeel.ThemeDescription;
import de.muntjak.tinylookandfeel.TinyLookAndFeel;

/**
 * Splash dialog.
 * 
 * @author Hai Bison
 * @since v1.8 beta
 * 
 */
public class SplashDialog extends JDialog {

    /**
     * Auto-generated by Eclipse.
     */
    private static final long serialVersionUID = -5559071516714940599L;

    /**
     * @see javax.swing.plaf.basic.BasicLookAndFeel
     */
    private static final String[] FONT_KEYS = { "Button.font",
            "ToggleButton.font", "RadioButton.font", "CheckBox.font",
            "ColorChooser.font", "ComboBox.font", "Label.font", "List.font",
            "MenuBar.font", "MenuItem.font", "RadioButtonMenuItem.font",
            "CheckBoxMenuItem.font", "Menu.font", "PopupMenu.font",
            "OptionPane.font", "Panel.font", "ProgressBar.font",
            "ScrollPane.font", "Viewport.font", "Slider.font", "Spinner.font",
            "TabbedPane.font", "Table.font", "TableHeader.font",
            "TextField.font", "FormattedTextField.font", "PasswordField.font",
            "TextArea.font", "TextPane.font", "EditorPane.font",
            "TitledBorder.font", "ToolBar.font", "ToolTip.font", "Tree.font" };

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    final SplashDialog splashDialog = new SplashDialog();
                    splashDialog
                            .setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                    splashDialog.setVisible(true);
                    splashDialog.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }// run()
        });
    }// main()

    /*
     * CONTROLS
     */

    private JLabel mLabelInfo;

    /**
     * Create the dialog.
     */
    public SplashDialog() {
        setUndecorated(true);
        setIconImage(Assets.getIconLogo());
        getContentPane().setBackground(new Color(0, 0, 0, 0xcc));

        final ImageIcon logo = new ImageIcon(Assets.getIconSplash());

        mLabelInfo = new JLabel(logo, SwingConstants.CENTER);
        mLabelInfo.setVerticalAlignment(SwingConstants.TOP);
        mLabelInfo.setHorizontalTextPosition(SwingConstants.CENTER);
        mLabelInfo.setVerticalTextPosition(SwingConstants.BOTTOM);
        mLabelInfo.setForeground(new Color(0, 191, 255));
        mLabelInfo.setFont(mLabelInfo.getFont().deriveFont(
                mLabelInfo.getFont().getSize() - 3f));
        getContentPane().add(mLabelInfo, BorderLayout.CENTER);

        UI.setWindowCenterScreen(this, logo.getIconWidth(),
                logo.getIconHeight());
        pack();
    }// SplashDialog()

    /**
     * Starts the application.
     */
    public void start() {
        if (!Beans.isDesignTime())
            new Loader().execute();
    }// start()

    /**
     * Loads the application.
     * 
     * @author Hai Bison
     * 
     */
    private class Loader extends SwingWorker<Void, String> {

        @Override
        protected Void doInBackground() throws Exception {
            publish(Messages.getString(R.string.msg_html_loading));

            L.i(Messages.getString(R.string.pmsg_app_name, Sys.APP_NAME,
                    Sys.APP_VERSION_NAME));

            Locale.setDefault(Locale.forLanguageTag(Preferences.getInstance()
                    .getLocaleTag()));
            publish(Messages.getString(R.string.msg_html_loading_language));

            /*
             * THEME
             */

            publish(Messages.getString(R.string.msg_html_loading_fonts));
            for (String fontKey : FONT_KEYS)
                UIManager.put(fontKey, Assets.getDefaultFont());
            Assets.getDefaultMonoFont();

            publish(Messages.getString(R.string.msg_html_loading_theme));
            try {
                ThemeDescription[] availableThemes = Theme.getAvailableThemes();
                for (ThemeDescription td : availableThemes) {
                    if (td.getURL().toString().matches("(?i).*nightly.*")) {
                        Theme.loadTheme(td);
                        UIManager.setLookAndFeel(new TinyLookAndFeel());
                        // SwingUtilities.updateComponentTreeUI(mMainFrame);
                        break;
                    }// if
                }// for
            } catch (UnsupportedLookAndFeelException ex) {
                /*
                 * Ignore it.
                 */
            }

            /*
             * Start MainActivity.
             */

            publish(Messages.getString(R.string.msg_html_loading));

            MainActivity window = new MainActivity();
            setVisible(false);
            window.show();
            dispose();

            return null;
        }// doInBackground()

        @Override
        protected void process(List<String> strings) {
            mLabelInfo.setText(strings.get(strings.size() - 1));
            pack();
        }// process()

    }// Loader

}
