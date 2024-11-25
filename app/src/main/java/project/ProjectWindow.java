package project;

import javax.swing.*;
import java.awt.*;

// project and api imports
import api.RecordsCentral;
import api.RecordsList;
import project.filters.FilterManager;
import project.filters.RegionFilter;
import project.filters.YearFilter;
import project.ui.FiltersPanel;
import project.ui.LineGraph;
import project.ui.PieChartPanel;

public class ProjectWindow extends JFrame {
        GridBagConstraints gbc = new GridBagConstraints();

        private JPanel container = new JPanel();

        private static JPanel overviewPanel;
        private PieChartPanel myPieChart;
        private LineGraph lineGraph;

        private RecordsList electricityGenerationRecords;
        private RecordsList electricityAvailableRecords;

        private RecordsList filteredRecords;

        public ProjectWindow() {
                super("COMP 3220 Project");
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setSize(800, 600);
                getContentPane().setPreferredSize(new Dimension(800, 600));
                setResizable(true);

                initializeData();

                initializeGraphs();

                container.setLayout(new BorderLayout(10, 10)); // Top-level container uses BorderLayout

                // Filters Panel (WEST) with BoxLayout for vertical alignment
                JPanel filtersPanel = new JPanel();
                filtersPanel.setLayout(new BoxLayout(filtersPanel, BoxLayout.Y_AXIS));
                filtersPanel.add(createFiltersPanel());
                container.add(filtersPanel, BorderLayout.WEST);

                // Title Label (NORTH)
                JLabel titleLabel = new JLabel("Electric Power Dashboard", SwingConstants.CENTER);
                titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
                container.add(titleLabel, BorderLayout.NORTH);

                // Overview Panel (CENTER)
                overviewPanel = createOverviewPanel();
                container.add(overviewPanel, BorderLayout.CENTER);

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
         * Helper method to initialize the data from the CSV files.
         */
        private void initializeData() {
                RecordsCentral recordsCentral = RecordsCentral.getInstance();
                electricityGenerationRecords = recordsCentral.parseData("data/ElectricityGeneration.csv");
                electricityAvailableRecords = recordsCentral.parseData("data/ElectricityAvailable.csv");
                filteredRecords = new RecordsList(electricityGenerationRecords.getAllRecords());
        }

        private void initializeGraphs() {
                lineGraph = createLineGraph();
                myPieChart = createPieChartPanel();
                lineGraph.setRecords(filteredRecords);
                myPieChart.setRecords(filteredRecords);
        }

        private JPanel createFiltersPanel() {
                return new FiltersPanel(
                                electricityGenerationRecords,
                                (selectedYear, selectedRegions) -> {
                                        FilterManager filterManager = new FilterManager();
                                        filterManager.addFilter(new YearFilter(selectedYear));
                                        filterManager.addFilter(new RegionFilter(selectedRegions));

                                        RecordsList filteredRecords = new RecordsList(
                                                        filterManager.applyFilters(electricityGenerationRecords));
                                        this.filteredRecords = filteredRecords;
                                        updateUIWithFilteredRecords(filteredRecords);
                                });
        }

        /**
         * Upddat relevant UI used in app with filtered records.
         * 
         * @param filteredRecords the records to update the UI with.
         */
        private void updateUIWithFilteredRecords(RecordsList filteredRecords) {
                // Update Pie Chart
                myPieChart.setRecords(filteredRecords);

                // Update Line Graph
                lineGraph.setRecords(filteredRecords);

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
                JLabel pieChartDesc = new JLabel("sample text.");
                pieChartLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                pieChartLabel.setHorizontalAlignment(SwingConstants.CENTER);
                pieChartPanel.add(pieChartDesc, gbc);
                // Adding pieChartPanel to the overview panel
                gbc.gridx = 1;
                gbc.gridy = 0;
                gbc.weightx = 0.5;
                gbc.weighty = 1.0;
                overviewPanel.add(pieChartPanel, gbc);

                return overviewPanel;
        }

}
