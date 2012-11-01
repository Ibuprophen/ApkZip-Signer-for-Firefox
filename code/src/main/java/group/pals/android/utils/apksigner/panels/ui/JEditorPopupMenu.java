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
 * Editor popup menu for {@link JTextComponent}
 * @author Hai Bison
 */
public class JEditorPopupMenu extends JPopupMenu {

    public final static String ActionNameCut = "cut";
    public final static String ActionNameCopy = "copy";
    public final static String ActionNameCopyAll = "copy-all";
    public final static String ActionNamePaste = "paste";
    public final static String ActionNameClearAndPaste = "clear-and-paste";
    public final static String ActionNameClear = "clear";
    public final static String ActionNameDelete = "delete";
    public final static String ActionNameSelectAll = "select-all";

    public JEditorPopupMenu() {
        super();
        initMenuItems();
    }

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
    }//initMenuItems

    private void fireAfterActionPerformed(String actionName) {
        Component invoker = getInvoker();
        if (invoker != null) {
            invoker.firePropertyChange(actionName, 0, 1);
        }
    }//fireAfterActionPerformed

    /*
     * EDITOR ACTIONS
     */

    private class CutAction extends TextAction {

        public CutAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.cut();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameCut);
                    }
                }
            }
        }
    }//CutAction

    private class CopyAction extends TextAction {

        public CopyAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameCopy);
                    }
                }
            }
        }
    }//CopyAction

    private class CopyAllAction extends TextAction {

        public CopyAllAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.selectAll();
                        textComponent.copy();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameCopyAll);
                    }
                }
            }
        }
    }//CopyAllAction

    private class PasteAction extends TextAction {

        public PasteAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNamePaste);
                    }
                }
            }
        }
    }//PasteAction

    private class ClearAction extends TextAction {

        public ClearAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.setText(null);
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameClear);
                    }
                }
            }
        }
    }//ClearAction

    private class DeleteAction extends TextAction {

        public DeleteAction(String name) {
            super(name);
        }

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

                        fireAfterActionPerformed(ActionNameDelete);
                    }
                }
            }
        }
    }//DeleteAction

    private class ClearAndPasteAction extends TextAction {

        public ClearAndPasteAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.setText(null);
                        textComponent.paste();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameClearAndPaste);
                    }
                }
            }
        }
    }//ClearAndPasteAction

    private class SelectAllAction extends TextAction {

        public SelectAllAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JMenuItem) {
                if (((JMenuItem) e.getSource()).getParent() instanceof JPopupMenu) {
                    JPopupMenu popupMenu = (JPopupMenu) ((JMenuItem) e.getSource()).getParent();
                    if (popupMenu.getInvoker() instanceof JTextComponent) {
                        JTextComponent textComponent = (JTextComponent) popupMenu.getInvoker();
                        textComponent.selectAll();
                        textComponent.requestFocusInWindow();

                        fireAfterActionPerformed(ActionNameSelectAll);
                    }
                }
            }
        }
    }//SelectAllAction

    //check if invoker is enabled or not, then reset enabled-status of all menu items
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
    }//firePopupMenuWillBecomeVisible
}
