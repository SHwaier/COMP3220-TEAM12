package project;

import javax.swing.*;
import java.awt.*;

public class ProjectWindow extends JFrame {
    GridBagConstraints gbc = new GridBagConstraints();
    private JPanel container = new JPanel();
    private MyPieChart myPieChart;

    public ProjectWindow() {
        super("COMP 3220 Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        getContentPane().setPreferredSize(new Dimension(800, 600));
        setResizable(false);

        myPieChart = new MyPieChart();
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

        JPanel overviewPanel = createOverviewPanel();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridheight = 1;
        gbc.weightx = 0.8;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        container.add(overviewPanel, gbc);

        JButton exportButton = new JButton("Export");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        container.add(exportButton, gbc);

        add(container);

        pack();
        setVisible(true);
    }

    private JPanel createFiltersPanel() {
        JPanel filtersPanel = new JPanel();
        filtersPanel.setLayout(new GridBagLayout());
        filtersPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        JLabel filtersLabel = new JLabel("Filters");
        filtersLabel.setFont(new Font("Arial", Font.BOLD, 16));

        filtersPanel.add(filtersLabel, gbc);
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 70);
        filtersPanel.add(new JLabel("Year"), gbc);
        gbc.gridy = 2;
        JComboBox<String> yearComboBox = new JComboBox<>(new String[]{"2023"}); //years
        filtersPanel.add(yearComboBox, gbc);

        gbc.gridy = 3;
        filtersPanel.add(new JLabel("Sector"), gbc);
        gbc.gridy = 4;
        JComboBox<String> sectorComboBox = new JComboBox<>(new String[]{"All"}); //sectos
        filtersPanel.add(sectorComboBox, gbc);

        gbc.gridy = 5;
        filtersPanel.add(new JLabel("Region"), gbc);
        gbc.gridy = 6;
        JComboBox<String> regionComboBox = new JComboBox<>(new String[]{"All Regions"}); //regions
        filtersPanel.add(regionComboBox, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(10, 5, 5, 5);
        JButton applyFiltersButton = new JButton("Apply Filters");
        filtersPanel.add(applyFiltersButton, gbc);

        gbc.gridy = 8;
        gbc.weighty = 1.0;
        filtersPanel.add(Box.createVerticalGlue(), gbc);
        return filtersPanel;
    }

    class BarChart extends JPanel {
        private int[] data;
        private String[] labels;

        public BarChart(int[] data, String[] labels) {
            this.data = data;
            this.labels = labels;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);

            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.LIGHT_GRAY);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            int barWidth = 30;
            int spacing = 10;
            Color[] colors = {Color.GREEN, Color.BLUE, Color.ORANGE, Color.RED};

            int maxDataValue = 250;

            for (int i = 0; i <= 10; i++) {
                int y = getHeight() - 30 - i * (getHeight() - 50) / 10;
                g2d.drawLine(45, y, getWidth() - 290, y);
            }

            for (int i = 0; i < data.length; i++) {
                int x = 45 + (barWidth + spacing) * i;
                g2d.drawLine(x, 20, x, getHeight() - 30);
            }

            for (int i = 0; i < data.length; i++) {
                g2d.setColor(colors[i % colors.length]);
                int x = 50 + (barWidth + spacing) * i;
                int height = (int) (((double) data[i] / maxDataValue) * (getHeight() - 50 - 30));
                int y = getHeight() - height - 30;

                g2d.fillRect(x, y, barWidth, height);
            }

            g2d.setColor(Color.BLACK);
            for (int i = 0; i <= 10; i++) {
                int y = getHeight() - 30 - i * (getHeight() - 50) / 10;
                String label = String.valueOf(i * maxDataValue / 10) + "m";
                g2d.drawString(label, 10, y + 5);
            }
        }
    }

    class MyPieChart extends JPanel {
        private int arcValue1, arcValue2;

        public MyPieChart(int arcValue1, int arcValue2) {
            this.arcValue1 = arcValue1;
            this.arcValue2 = arcValue2;
            repaint();
        }

        public MyPieChart() {
            this(170, 190);// pie values
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.WHITE);
            Graphics2D g2d = (Graphics2D) g;

            g2d.setColor(Color.ORANGE);
            g2d.fillArc(50, 50, 200, 200, 90, arcValue1);
            g2d.setColor(Color.GREEN);
            g2d.fillArc(50, 50, 200, 200, 90+arcValue1, arcValue2);
        }
    }


    private JPanel createOverviewPanel() {
        JPanel overviewPanel = new JPanel();
        overviewPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel barChartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barChartPanel.setBackground(Color.WHITE);

        JLabel barChartLabel = new JLabel("Electric Overview");
        barChartPanel.add(barChartLabel);

        int[] data = {230, 200, 30, 10};// bar values
        String[] labels = {"Total Generation", "Total Sales", "Exports to US", "Imports from US"};
        BarChart barChart = new BarChart(data, labels);
        barChart.setPreferredSize(new Dimension(500, 300));

        barChartPanel.add(barChart);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 1.0;
        overviewPanel.add(barChartPanel, gbc);

        JPanel pieChartPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pieChartPanel.setBackground(Color.WHITE);

        JLabel pieChartLabel = new JLabel("Electricity Sales Distribution");
        pieChartLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        pieChartPanel.add(pieChartLabel);

        myPieChart.setPreferredSize(new Dimension(500, 500));
        pieChartPanel.add(myPieChart);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        overviewPanel.add(pieChartPanel, gbc);

        return overviewPanel;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProjectWindow::new);
    }
}
