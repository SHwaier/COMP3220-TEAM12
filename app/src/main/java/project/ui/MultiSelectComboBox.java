package project.ui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class MultiSelectComboBox extends JComboBox<String> {
    private final String[] items;
    private final List<String> selectedItems = new ArrayList<>();

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

    private void addPopupBehavior() {
        BasicComboPopup popup = getPopup();
        popup.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume(); // Prevent popup auto-close
            }
        });
    }

    private BasicComboPopup getPopup() {
        return (BasicComboPopup) getUI().getAccessibleChild(this, 0);
    }

    private void toggleSelection(String item) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
        } else {
            selectedItems.add(item);
        }
        repaint();
    }

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
}
