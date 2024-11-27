package project;

import javax.swing.*;

import java.awt.*;

// project and api imports
import api.RecordsCentral;
import api.RecordsList;
import project.filters.FilterManager;
import project.filters.RegionFilter;
import project.filters.YearFilter;
import project.ui.BetterButton;
import project.ui.FiltersPanel;
import project.ui.LineGraph;
import project.ui.PieChartPanel;

public class ProjectWindow extends JFrame {
        GridBagConstraints gbc = new GridBagConstraints();

        private JPanel container = new JPanel();

        private static JPanel overviewPanel;
        private PieChartPanel myPieChart;
        private LineGraph lineGraph;

        public ProjectWindow() {
                super("COMP 3220 Project");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                ImageIcon imageIcon = new ImageIcon(getClass().getResource("/logo.png"));
                setIconImage(imageIcon.getImage());
                setSize(1280, 720);
                this.setMinimumSize(new Dimension(1280, 720));
                getContentPane().setPreferredSize(new Dimension(1280, 720));
                setResizable(true);

                initializeData();

                initializeGraphs();

                container.setLayout(new GridBagLayout());

                JPanel filtersPanel = createFiltersPanel();
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.gridheight = 2;
                gbc.weightx = 0.1;
                gbc.fill = GridBagConstraints.BOTH;
                container.add(filtersPanel, gbc);

                // Title label and import button row
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.gridheight = 1;
                gbc.weightx = 0.8;
                gbc.insets = new Insets(20, 20, 10, 20);
                gbc.fill = GridBagConstraints.HORIZONTAL;

                // Create a panel for the title and button
                JPanel titlePanel = new JPanel(new BorderLayout());

                // Add title label to the left
                JLabel titleLabel = new JLabel("Electric Power Dashboard");
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                titlePanel.add(titleLabel, BorderLayout.WEST);

                // Add import button to the right
                BetterButton importButton = new BetterButton("Import Data");
                importButton.addActionListener(e -> this.importData());
                titlePanel.add(importButton, BorderLayout.EAST);

                // Add the title panel to the container
                container.add(titlePanel, gbc);
                gbc.insets = new Insets(10, 10, 10, 10);

                // Overview panel
                overviewPanel = createOverviewPanel();
                gbc.gridx = 1;
                gbc.gridy = 1;
                gbc.gridheight = 1;
                gbc.weightx = 0.8;
                gbc.weighty = 1.0;
                gbc.fill = GridBagConstraints.BOTH;
                container.add(overviewPanel, gbc);

                add(container);

                // This handles resizing of the frame, so that components adjust accordingly
                addComponentListener(new java.awt.event.ComponentAdapter() {
                        @Override
                        public void componentResized(java.awt.event.ComponentEvent e) {
                                // Signal layout manager to adjust the layout
                                overviewPanel.invalidate();

                                // Repaint dynamic components
                                lineGraph.repaint();
                                myPieChart.repaint();

                                // Ensure panel adjusts its layout
                                overviewPanel.revalidate();
                                overviewPanel.repaint();
                        }
                });

                pack();
                setVisible(true);
                setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        }

        /**
         * Helper method to initialize the data into the DataManager.
         */
        private void initializeData() {
                RecordsCentral recordsCentral = RecordsCentral.getInstance();
                DataManager dataManager = DataManager.getInstance();
                try {

                        dataManager.addDataset("ElectricityGeneration",
                                        recordsCentral.parseResourceData("data/ElectricityGeneration.csv"));
                        dataManager.addDataset("ElectricityAvailable",
                                        recordsCentral.parseResourceData("data/ElectricityAvailable.csv"));
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error loading data: " + e.getMessage(), "Data Load Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
                dataManager.setActiveDataset("ElectricityGeneration");
                dataManager.setFilteredRecords(dataManager.getActiveDataset());
        }

        /**
         * Helper method to initialize the graphs with the filtered records.
         */
        private void initializeGraphs() {
                DataManager dataManager = DataManager.getInstance();
                lineGraph = createLineGraph();
                myPieChart = createPieChartPanel();
                lineGraph.setRecords(dataManager.getFilteredRecords());
                myPieChart.setRecords(dataManager.getFilteredRecords());
        }

        /**
         * Create the filters panel containing the year and region filters.
         * 
         * @return the filters panel
         */
        private JPanel createFiltersPanel() {
                DataManager dataManager = DataManager.getInstance();

                return new FiltersPanel(
                                dataManager.getActiveDataset(),
                                (selectedYear, selectedRegions) -> {
                                        FilterManager filterManager = new FilterManager();
                                        filterManager.addFilter(new YearFilter(selectedYear));
                                        filterManager.addFilter(new RegionFilter(selectedRegions));

                                        RecordsList filteredRecords = new RecordsList(
                                                        filterManager.applyFilters(dataManager.getActiveDataset()));
                                        dataManager.setFilteredRecords(filteredRecords);
                                        updateUIWithFilteredRecords();
                                });
        }

        /**
         * Update relevant UI used in app with filtered records.
         * 
         * @param filteredRecords the records to update the UI with.
         */
        private void updateUIWithFilteredRecords() {
                DataManager dataManager = DataManager.getInstance();

                // Update Pie Chart
                myPieChart.setRecords(dataManager.getFilteredRecords());

                // Update Line Graph
                lineGraph.setRecords(dataManager.getFilteredRecords());

                // Refresh the overviewPanel without recreating it
                overviewPanel.revalidate();
                overviewPanel.repaint();
        }

        // if need to later on, we can use these methods to create the graph/chart with
        // some default data, for now left null
        private PieChartPanel createPieChartPanel() {
                return new PieChartPanel(null);
        }

        private LineGraph createLineGraph() {
                return new LineGraph(new RecordsList(null));
        }

        /**
         * Create the overview panel containing the line graph and pie chart.
         * 
         * @return the overview panel
         */
        private JPanel createOverviewPanel() {
                JPanel overviewPanel = new JPanel(new GridBagLayout());

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.BOTH;
                gbc.weightx = 0.5;
                gbc.weighty = 1.0;

                // Add Line Graph Panel
                JPanel lineGraphPanel = createChartPanel(
                                "Electricity Overview Over Time", lineGraph, Color.WHITE,
                                new Font("Arial", Font.BOLD, 16));
                gbc.gridx = 0;
                gbc.gridy = 0;
                overviewPanel.add(lineGraphPanel, gbc);

                // Add Pie Chart Panel
                JPanel pieChartPanel = createChartPanel(
                                "Breakdown By Region", myPieChart, Color.WHITE,
                                new Font("Arial", Font.BOLD, 16));
                JLabel pieChartDesc = new JLabel("Sample text.");
                pieChartDesc.setFont(new Font("Arial", Font.PLAIN, 14));
                pieChartDesc.setHorizontalAlignment(SwingConstants.CENTER);
                gbc.gridy++; // Add description below the pie chart
                pieChartPanel.add(pieChartDesc, gbc);

                gbc.gridx = 1;
                gbc.gridy = 0;
                overviewPanel.add(pieChartPanel, gbc);

                return overviewPanel;
        }

        /**
         * Create a panel containing a chart with a title.
         * 
         * @param title     the title of the chart
         * @param chart     the chart to display
         * @param bgColor   the background color of the panel
         * @param titleFont the font of the title
         * @return the panel containing the chart
         */
        private JPanel createChartPanel(String title, JComponent chart, Color bgColor, Font titleFont) {
                JPanel chartPanel = new JPanel(new GridBagLayout());
                chartPanel.setBackground(bgColor);

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.insets = new Insets(10, 10, 10, 10);
                gbc.fill = GridBagConstraints.BOTH;

                // Add Title
                gbc.gridx = 0;
                gbc.gridy = 0;
                gbc.weightx = 1.0;
                gbc.weighty = 0.1;
                gbc.anchor = GridBagConstraints.PAGE_START;

                JLabel titleLabel = new JLabel(title);
                titleLabel.setFont(titleFont);
                titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
                chartPanel.add(titleLabel, gbc);

                // Add Chart
                gbc.gridy++;
                gbc.weighty = 1.0;
                chartPanel.add(chart, gbc);

                return chartPanel;
        }

        /**
         * Import data from a CSV file.
         */
        private void importData() {
                try {
                        project.io.Import.importData();
                        // Update UI with the new data
                        updateUIWithFilteredRecords();

                        // Inform the user of success
                        JOptionPane.showMessageDialog(this, "Data imported successfully!", "Import Success",
                                        JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Error importing data: " + e.getMessage(), "Import Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }
        }

}
