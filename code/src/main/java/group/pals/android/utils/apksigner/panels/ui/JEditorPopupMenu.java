/*
 *    Copyright (C) 2012 Hai Bison
 *
 *    See the file LICENSE at the root directory of this project for copying
 *    permission.
 */
package group.pals.android.utils.apksigner.panels.ui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;
import javax.swing.text.TextAction;

/**
 * Editor popup menu for {@link JTextComponent}.
 *
 * @author Hai Bison
 */
public class JEditorPopupMenu extends JPopupMenu {

    public final static String ACTION_NAME_CUT = "cut";
    public final static String ACTION_NAME_COPY = "copy";
    public final static String ACTION_NAME_COPY_ALL = "copy-all";
    public final static String ACTION_NAME_PASTE = "paste";
    public final static String ACTION_NAME_CLEAR_AND_PASTE = "clear-and-paste";
    public final static String ACTION_NAME_CLEAR = "clear";
    public final static String ACTION_NAME_DELETE = "delete";
    public final static String ACTION_NAME_SELECT_ALL = "select-all";

    /**
     * Creates new instance.
     */
    public JEditorPopupMenu() {
        super();
        initMenuItems();
    }//JEditorPopupMenu()

    /**
     * Initializes all menu items.
     */
    private void initMenuItems() {
        final String ItemSeparator = "-";
        final String[] ItemTitles = {"Cut", "Copy", "Copy All", "Paste", ItemSeparator,
            "Clear and Paste", "Clear", "Delete", ItemSeparator, "Select All"};
        final Action[] ItemActions = {new CutAction("Cut"), new CopyAction("Copy"),
            new CopyAllAction("Copy All"), new PasteAction("Paste"), null,
            new ClearAndPasteAction("Clear and Paste"), new ClearAction("Clear"),
            new DeleteAction("Delete"), null, new SelectAllAction("Select All")};
        for (int i = 0; i < ItemTitles.length; i++) {
            if (ItemTitles[i].equals(ItemSeparator)) {
                Separator separator = new JPopupMenu.Separator();
                this.add(separator);
            } else {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(ItemActions[i]);
                menuItem.setText(ItemTitles[i]);
                menuItem.setToolTipText(ItemTitles[i]);
                this.add(menuItem);
            }
        }
    }//initMenuItems()

    /**
     * Fires an action... (TODO ???)
     *
     * @param actionName the action name.
     */
    private void fireAfterActionPerformed(String actionName) {
        Component invoker = getInvoker();
        if (invoker != null) {
            invoker.firePropertyChange(actionName, 0, 1);
        }
    }//fireAfterActionPerformed()

    /*
     * EDITOR ACTIONS
     */
    /**
     * The CUT action.
     */
    private class CutAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public CutAction(String name) {
            super(name);
        }//CutAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.cut();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CUT);
                    }
                }
            }
        }//actionPerformed()
    }//CutAction

    /**
     * The COPY action.
     */
    private class CopyAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public CopyAction(String name) {
            super(name);
        }//CopyAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_COPY);
                    }
                }
            }
        }//actionPerformed()
    }//CopyAction

    /**
     * The COPY ALL action.
     */
    private class CopyAllAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public CopyAllAction(String name) {
            super(name);
        }//CopyAllAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.selectAll();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_COPY_ALL);
                    }
                }
            }
        }//actionPerformed()
    }//CopyAllAction

    private class PasteAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public PasteAction(String name) {
            super(name);
        }//PasteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_PASTE);
                    }
                }
            }
        }//actionPerformed()
    }//PasteAction

    private class ClearAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public ClearAction(String name) {
            super(name);
        }//ClearAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.setText(null);
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CLEAR);
                    }
                }
            }
        }//actionPerformed()
    }//ClearAction

    private class DeleteAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public DeleteAction(String name) {
            super(name);
        }//DeleteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        int start = textComponent.getSelectionStart();
                        int end = textComponent.getSelectionEnd();
                        if (end > start) {
                            try {
                                String text = textComponent.getText();
                                if (end <= text.length()) {
                                    text = text.substring(0, start) + text.substring(end);
                                    textComponent.setText(text);
                                    textComponent.setCaretPosition(start);
                                }
                            } catch (Exception ex) {
                                return;
                            }
                        }
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_DELETE);
                    }
                }
            }
        }//actionPerformed()
    }//DeleteAction

    private class ClearAndPasteAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public ClearAndPasteAction(String name) {
            super(name);
        }//ClearAndPasteAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.setText(null);
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_CLEAR_AND_PASTE);
                    }
                }
            }
        }//actionPerformed()
    }//ClearAndPasteAction

    private class SelectAllAction extends TextAction {

        /**
         * Creates new instance.
         *
         * @param name the action name.
         */
        public SelectAllAction(String name) {
            super(name);
        }//SelectAllAction()

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.selectAll();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ACTION_NAME_SELECT_ALL);
                    }
                }
            }
        }//actionPerformed()
    }//SelectAllAction()

    /**
     * Checks if invoker is enabled or not, then reset enabled-status of all
     * menu items.
     */
    @Override
    protected void firePopupMenuWillBecomeVisible() {
        final Component Invoker = getInvoker();
        if (Invoker != null) {
            boolean isEditable = true;
            if (Invoker instanceof JTextComponent) {
                isEditable = ((JTextComponent) Invoker).isEditable();
            }
            final boolean IsEnabled = Invoker.isEnabled();
            for (int i = 0; i < getComponentCount(); i++) {
                boolean enabled = IsEnabled;
                Component comp = getComponent(i);
                if (IsEnabled && !isEditable) {
                    if (comp instanceof JMenuItem) {
                        Action action = ((JMenuItem) comp).getAction();
                        enabled = (action instanceof CopyAction) || (action instanceof CopyAllAction)
                                || (action instanceof SelectAllAction);
                    }
                }

                comp.setEnabled(enabled);
            }
        }
    }//firePopupMenuWillBecomeVisible()
}
