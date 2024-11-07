package project;

import javax.swing.*;
import java.awt.*;
import java.io.File;

import api.Record;
import api.RecordsCentral;
import project.ui.BetterButton;
import project.ui.LineGraph;
import project.ui.PieChartPanel;

import java.util.ArrayList;
import java.util.List;

public class ProjectWindow extends JFrame {
    GridBagConstraints gbc = new GridBagConstraints();
    private JPanel container = new JPanel();
    private PieChartPanel myPieChart;
    private LineGraph lineGraph;
    private List<Record> ElectricityGenerationRecords = RecordsCentral.parseData("data/ElectricityGeneration.csv");
    private List<Record> ElectricityAvailableRecords = RecordsCentral.parseData("data/ElectricityAvailable.csv");
    private String SelectedYear = "2005";
    private static JPanel overviewPanel;

    public ProjectWindow() {
        super("COMP 3220 Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setPreferredSize(new Dimension(800, 600));
        setResizable(true);

        Record totalGeneration = new Record("Total Generation", RecordsCentral.getTotalForYearAndPlace(
                ElectricityGenerationRecords, SelectedYear, ElectricityGenerationRecords.get(0).getPlace()));
        Record totalAvailable = new Record("Total Available", RecordsCentral.getTotalForYearAndPlace(
                ElectricityAvailableRecords, SelectedYear, ElectricityAvailableRecords.get(0).getPlace()));
        api.RecordsCentral.getTotalForPlace(ElectricityGenerationRecords,
                ElectricityGenerationRecords.get(0).getPlace());
        myPieChart = new PieChartPanel(List.of(totalGeneration, totalAvailable));
        List<Record> overtimeRecords = new ArrayList<>(
                RecordsCentral.getRecordsForPlace(ElectricityAvailableRecords,
                        ElectricityAvailableRecords.get(0).getPlace()));
        lineGraph = new LineGraph(overtimeRecords);

        container.setLayout(new GridBagLayout());

        JPanel filtersPanel = createFiltersPanel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(filtersPanel, gbc);

        JLabel titleLabel = new JLabel("Electric Power Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        container.add(titleLabel, gbc);

        overviewPanel = createOverviewPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(overviewPanel, gbc);

        add(container);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                overviewPanel.revalidate();
                myPieChart.repaint();
                lineGraph.repaint();
            }
        });

        pack();
        setVisible(true);
        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

    }

    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new GridBagLayout());
        filtersPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Row 0: Filters Label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel filtersLabel = new JLabel("Filters");
        filtersLabel.setFont(new Font("Arial", Font.BOLD, 16));
        filtersPanel.add(filtersLabel, gbc);

        // Row 1: Year Label
        gbc.gridy = 1;
        JLabel yearLabel = new JLabel("Year");
        yearLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        filtersPanel.add(yearLabel, gbc);

        // Row 2: Year ComboBox
        gbc.gridy = 2;
        JComboBox<String> yearComboBox = new JComboBox<>(
                api.RecordsCentral.getListOfYears(ElectricityGenerationRecords)); // years

        filtersPanel.add(yearComboBox, gbc);

        // Row 3: Apply Filters Button
        gbc.gridy = 3;
        gbc.insets = new Insets(20, 10, 10, 10); // Add extra space above the button
        BetterButton applyFiltersButton = new BetterButton("Apply Filters");
        filtersPanel.add(applyFiltersButton, gbc);
        applyFiltersButton.addActionListener(e -> applyFilters(yearComboBox.getSelectedItem().toString()));

        // Row 4: Spacer to push export button down
        gbc.gridy = 4;
        gbc.weighty = 1.0; // This spacer takes up extra vertical space
        filtersPanel.add(Box.createVerticalGlue(), gbc);

        // Row 5: Export Button at the bottom
        gbc.gridy = 5;
        gbc.weighty = 0; // Reset vertical weight for the button
        gbc.insets = new Insets(10, 10, 20, 10); // Extra space below the export button
        gbc.anchor = GridBagConstraints.SOUTH; // Anchor at the bottom
        BetterButton exportButton = new BetterButton("Export");
        filtersPanel.add(exportButton, gbc);
        exportButton.addActionListener(e -> exportData());

        return filtersPanel;
    }

    /**
     * Create the overview panel with the pie chart and line graph.
     * 
     * @return a panel with the pie chart and line graph.
     */
    private JPanel createOverviewPanel() {
        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel lineGraphPanel = new JPanel(new GridBagLayout());

        lineGraphPanel.setBackground(Color.WHITE);
        // Setting up GridBagConstraints for components within pieChartPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; // Small weight for the label to take minimal space
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding for spacing

        // Add the label to the first row
        JLabel lineGraphLabel = new JLabel("Electricity Overview");
        lineGraphLabel.setFont(new Font("Arial", Font.BOLD, 16));
        lineGraphLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lineGraphPanel.add(lineGraphLabel, gbc);

        // Move to the next row for the line graph
        gbc.gridy++;
        gbc.weighty = 1.0; // Larger weight for the pie chart to take most of the space
        gbc.fill = GridBagConstraints.BOTH;
        lineGraphPanel.add(lineGraph, gbc);

        // Adding pieChartPanel to the overview panel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        overviewPanel.add(lineGraphPanel, gbc);

        // Setting up the pie chart panel with GridBagLayout
        JPanel pieChartPanel = new JPanel(new GridBagLayout());
        pieChartPanel.setBackground(Color.WHITE);

        // Setting up GridBagConstraints for components within pieChartPanel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; // Small weight for the label to take minimal space
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding for spacing

        // Add the label to the first row
        JLabel pieChartLabel = new JLabel("Electricity Generation vs. Available Electricity");
        pieChartLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pieChartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieChartPanel.add(pieChartLabel, gbc);

        // Move to the next row for the pie chart
        gbc.gridy++;
        gbc.weighty = 1.0; // Larger weight for the pie chart to take most of the space
        gbc.fill = GridBagConstraints.BOTH;
        pieChartPanel.add(myPieChart, gbc);
        gbc.gridy++;
        JLabel pieChartDesc = new JLabel(
                "Results show national electricity generation and available electricity for: " + SelectedYear);
        pieChartLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        pieChartLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pieChartPanel.add(pieChartDesc, gbc);
        // Adding pieChartPanel to the overview panel
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        overviewPanel.add(pieChartPanel, gbc);

        // JLabel

        return overviewPanel;
    }

    /**
     * Apply filters based on the selected year.
     *
     * @param selectedYear The year selected from the combo box.
     */
    private void applyFilters(String selectedYear) {
        this.SelectedYear = selectedYear;
        System.out.println("Applying filters for year: " + selectedYear);

        // Create updated records for the pie chart and line graph
        Record totalGeneration = new Record("Total Generation", RecordsCentral.getTotalForYearAndPlace(
                ElectricityGenerationRecords, SelectedYear, ElectricityGenerationRecords.get(0).getPlace()));
        Record totalAvailable = new Record("Total Available", RecordsCentral.getTotalForYearAndPlace(
                ElectricityAvailableRecords, SelectedYear, ElectricityAvailableRecords.get(0).getPlace()));

        myPieChart.setRecords(List.of(totalGeneration, totalAvailable));
        List<Record> overtimeRecords = new ArrayList<>(
                RecordsCentral.getRecordsForPlace(ElectricityAvailableRecords,
                        ElectricityAvailableRecords.get(0).getPlace()));
        lineGraph.setRecords(overtimeRecords);

        overviewPanel.removeAll();
        overviewPanel = createOverviewPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(overviewPanel, gbc);

        // Revalidate and repaint the overviewPanel to refresh the UI
        overviewPanel.revalidate();
        overviewPanel.repaint();
    }

    /**
     * Export data to a file or perform export action.
     */
    private void exportData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Export File");

        // Set file chooser to save mode
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);

        // Set the default file name
        fileChooser.setSelectedFile(new File("exported_data.csv")); // Default name, change extension if needed

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            System.out.println("Saving data to: " + fileToSave.getAbsolutePath());

            List<Record> recordsToExport = new ArrayList<>(
                    RecordsCentral.getRecordsForYear(ElectricityGenerationRecords, SelectedYear));
            // Call the method to perform export with the selected file path
            api.io.ExportRecords.exportToCSV(fileToSave.getAbsolutePath(), recordsToExport);
        }
    }

}
