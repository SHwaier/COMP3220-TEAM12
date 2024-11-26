package project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.BiConsumer;

import api.RecordsList;
import project.DataManager;

/**
 * A JPanel that contains filters for the data visualizations.
 */
public class FiltersPanel extends JPanel {
    private final MultiSelectComboBox yearComboBox;
    private final MultiSelectComboBox placesComboBox;
    // Font for consistent styling
    private static final Font LABEL_FONT = new Font("Arial", Font.BOLD, 16);
    private static final Font COMBOBOX_FONT = new Font("Arial", Font.PLAIN, 16);

    /**
     * Creates a new FiltersPanel with the given RecordsList and callback for when
     * filters are applied.
     * 
     * @param records          The RecordsList to filter
     * @param onFiltersApplied The callback to run when filters are applied
     */
    public FiltersPanel(RecordsList records,
            BiConsumer<List<String>, List<String>> onFiltersApplied) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0: Radio Buttons for Dataset Selection
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel datasetLabel = new JLabel("Select Dataset:");
        datasetLabel.setFont(LABEL_FONT);
        add(datasetLabel, gbc);

        // Create radio buttons for datasets
        JRadioButton generationButton = new JRadioButton("Generation");
        JRadioButton availableButton = new JRadioButton("Available");
        JRadioButton importedButton = new JRadioButton("Imported Data");

        // Set fonts for the radio buttons
        generationButton.setFont(COMBOBOX_FONT);
        availableButton.setFont(COMBOBOX_FONT);
        importedButton.setFont(COMBOBOX_FONT);

        // Group the radio buttons
        ButtonGroup datasetGroup = new ButtonGroup();
        datasetGroup.add(generationButton);
        datasetGroup.add(availableButton);
        datasetGroup.add(importedButton);

        // Add the radio buttons to a panel for better layout
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        radioPanel.add(generationButton);
        radioPanel.add(availableButton);
        radioPanel.add(importedButton);

        // Set the default selection
        generationButton.setSelected(true);

        gbc.gridy = 1;
        add(radioPanel, gbc);

        // Row 2: Year Label
        gbc.gridy = 2;
        JLabel yearLabel = new JLabel("Year");
        add(yearLabel, gbc);

        // Row 3: Year ComboBox
        gbc.gridy = 3;
        yearComboBox = new MultiSelectComboBox(records.getListOfYears());
        yearComboBox.addItem("All Years");
        yearComboBox.setFont(COMBOBOX_FONT);
        add(yearComboBox, gbc);

        // Row 4: Region Label
        gbc.gridy = 4;
        JLabel placesLabel = new JLabel("Places");
        add(placesLabel, gbc);

        // Row 5: Multi-Select ComboBox for Places
        gbc.gridy = 5;
        placesComboBox = new MultiSelectComboBox(records.getListOfPlaces());
        placesComboBox.addItem("All Regions");
        placesComboBox.setFont(COMBOBOX_FONT);
        add(placesComboBox, gbc);

        // Row 6: Spacer
        gbc.gridy = 6;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        // Row 7: Apply Filters Button
        gbc.gridy = 7;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        BetterButton applyFiltersButton = new BetterButton("Apply Filters");
        add(applyFiltersButton, gbc);

        // Row 8: Export Button
        gbc.gridy = 8;
        BetterButton exportButton = new BetterButton("Export Data");
        add(exportButton, gbc);

        // ActionListeners for all interactive components
        applyFiltersButton.addActionListener(e -> {
            List<String> selectedYear = yearComboBox.getSelectedItems();
            List<String> selectedRegions = placesComboBox.getSelectedItems();

            // Notify the main window with the selected filters
            onFiltersApplied.accept(selectedYear, selectedRegions);
        });
        exportButton.addActionListener(e -> {
            project.io.Export.exportData(DataManager.getInstance().getActiveDataset());
        });
        // Add ActionListeners to the radio buttons to switch datasets
        generationButton
                .addActionListener(e -> {
                    DataManager.getInstance().setActiveDataset("ElectricityGeneration");
                    updateFilters(DataManager.getInstance().getDataset("ElectricityGeneration"));
                });
        availableButton
                .addActionListener(e -> {
                    DataManager.getInstance().setActiveDataset("ElectricityAvailable");
                    updateFilters(DataManager.getInstance().getDataset("ElectricityAvailable"));
                });
        importedButton.addActionListener(e -> {
            DataManager.getInstance().setActiveDataset("ImportedData");
            updateFilters(DataManager.getInstance().getDataset("ImportedData"));
        });
        updateFilters(records);

    }

    /**
     * Populates the yearComboBox and placesComboBox with the data from the given RecordsList.
     * Used to update the filters when the dataset is changed.
     * @param newRecords The new RecordsList to update the filters with
     */
    public void updateFilters(RecordsList newRecords) {
        if (newRecords == null) {

            JOptionPane.showMessageDialog(this, "The selected option doesn't have any data loaded", "No Data",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Update yearComboBox options
        yearComboBox.removeAllItems();
        String[] years = newRecords.getListOfYears();
        for (String year : years) {
            yearComboBox.addItem(year);
        }
        yearComboBox.addItem("All Years"); // Optionally re-add "All Years"

        // Update placesComboBox options
        placesComboBox.removeAllItems();
        String[] places = newRecords.getListOfPlaces();
        for (String place : places) {
            placesComboBox.addItem(place);
        }
        placesComboBox.addItem("All Regions"); // Optionally re-add "All Regions"

        // Optionally reset selected items
        yearComboBox.clearSelection();
        placesComboBox.clearSelection();
    }

}
