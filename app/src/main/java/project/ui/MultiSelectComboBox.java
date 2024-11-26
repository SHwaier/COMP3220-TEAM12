package project.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A JComboBox that allows multiple items to be selected.
 * Each item is displayed with a checkbox.
 */
public class MultiSelectComboBox extends JComboBox<String> {
    private final String[] items;
    private final List<String> selectedItems = new ArrayList<>();

    /**
     * Create a new MultiSelectComboBox with the given items.
     * 
     * @param items The items to display in the combo box
     */
    public MultiSelectComboBox(String[] items) {
        super(items);
        this.items = items;
        setRenderer(new CheckBoxRenderer());
        setEditable(true);

        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1), // Outer border
                BorderFactory.createEmptyBorder(0, 0, 0, 0) // Padding inside the border
        ));

        // Custom UI to control popup visibility
        setUI(new BasicComboBoxUI() {
            @Override
            public void setPopupVisible(JComboBox c, boolean v) {
                // Do nothing to avoid auto-close
            }
        });

        // Add ActionListener to toggle selection
        addActionListener(e -> {
            Object selected = getSelectedItem();
            if (selected instanceof String item) {
                toggleSelection(item);
            }
        });

        addPopupBehavior();
    }

    /**
     * Add a mouse listener to the popup to prevent it from auto-closing.
     */
    private void addPopupBehavior() {
        BasicComboPopup popup = getPopup();
        popup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume(); // Prevent popup auto-close
            }
        });
    }

    /**
     * Get the BasicComboPopup from the UI.
     * 
     * @return The BasicComboPopup
     */
    private BasicComboPopup getPopup() {
        return (BasicComboPopup) getUI().getAccessibleChild(this, 0);
    }

    /**
     * Toggle the selection of the given item.
     * 
     * @param item The item to toggle
     */
    private void toggleSelection(String item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
            this.selectedItems.remove(item);
        } else {
            selectedItems.add(item);
            this.selectedItems.add(item);
        }
        repaint();
    }

    /**
     * Get the list of selected items.
     * 
     * @return The list of selected items
     */
    public List<String> getSelectedItems() {
        return new ArrayList<>(selectedItems);
    }

    // Custom Renderer to display checkboxes
    private class CheckBoxRenderer extends JCheckBox implements ListCellRenderer<Object> {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {
            if (value instanceof String item) {
                setText(item);
                setSelected(selectedItems.contains(item));
            }
            setBackground(isSelected ? Color.LIGHT_GRAY : Color.WHITE);

            return this;
        }
    }

    /**
     * Clear the selection of all items.
     */
    public void clearSelection() {
        selectedItems.clear();
        this.selectedItems.clear();
        repaint();
    }
}
