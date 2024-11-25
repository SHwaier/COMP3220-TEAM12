package project.ui;

import javax.swing.*;

import java.awt.*;

import java.util.List;
import java.util.function.BiConsumer;

import api.RecordsList;
import project.io.Export;

public class FiltersPanel extends JPanel {
    private final MultiSelectComboBox yearComboBox;
    private final MultiSelectComboBox placesComboBox;
    private final RecordsList filteredRecords;

    public FiltersPanel(RecordsList electricityGenerationRecords,
            BiConsumer<List<String>, List<String>> onFiltersApplied) {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);
        filteredRecords = new RecordsList(electricityGenerationRecords.getAllRecords());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0: Year Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel yearLabel = new JLabel("Year");
        add(yearLabel, gbc);

        // Row 1: Year ComboBox
        gbc.gridy = 1;
        yearComboBox = new MultiSelectComboBox(electricityGenerationRecords.getListOfYears());
        yearComboBox.addItem("All Years");
        add(yearComboBox, gbc);

        // Row 2: Region Label
        gbc.gridy = 2;
        JLabel placesLabel = new JLabel("Places");
        add(placesLabel, gbc);

        // Row 3: Multi-Select ComboBox for Places
        gbc.gridy = 3;
        placesComboBox = new MultiSelectComboBox(electricityGenerationRecords.getListOfPlaces());
        placesComboBox.addItem("All Regions");
        add(placesComboBox, gbc);

        // Row 4: Spacer
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        // Row 5: Apply Filters Button
        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.SOUTH;
        BetterButton applyFiltersButton = new BetterButton("Apply Filters");
        add(applyFiltersButton, gbc);

        applyFiltersButton.addActionListener(e -> {
            List<String> selectedYear = yearComboBox.getSelectedItems();
            List<String> selectedRegions = placesComboBox.getSelectedItems();

            // Notify the main window with the selected filters
            onFiltersApplied.accept(selectedYear, selectedRegions);
        });

        // Row 6: Export Button
        gbc.gridy = 6;
        BetterButton exportButton = new BetterButton("Export Data");
        add(exportButton, gbc);

        exportButton.addActionListener(e -> {
            project.io.Export.exportData(filteredRecords);
        });
    }
}
