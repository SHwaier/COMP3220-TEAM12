package project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import api.Record;

/**
 * A JPanel that displays a responsive pie chart of the given records.
 */
public class PieChartPanel extends JPanel {

    // List of records to display in the pie chart
    private List<Record> records;

    // Colors for the chart slices
    private final Color[] colors = { Color.ORANGE, new Color(0, 102, 204), Color.MAGENTA, Color.CYAN, Color.RED,
            Color.BLUE,
            Color.GREEN, Color.PINK, Color.YELLOW };

    /**
     * Create a new PieChartPanel with the given records
     * 
     * @param records the records to display in the pie chart
     */
    public PieChartPanel(List<Record> records) {
        this.records = records;
    }

    /**
     * Set the records to display in the pie chart
     * 
     * @param records the records to display
     */
    public void setRecords(List<Record> records) {
        this.records = records;
    }

    /**
     * Paint the pie chart and legend on the panel
     * 
     * @param g the Graphics object to paint on
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        drawPieChart(g);
        drawLegend(g);
    }

    /**
     * Draw the pie chart on the panel.
     * 
     * @param g the Graphics object to paint on
     */
    private void drawPieChart(Graphics g) {
        if (records == null || records.isEmpty()) {
            return; // No data to display
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Set diameter to be 50% of the panel's smaller dimension for better
        // responsiveness
        int diameter = (int) (Math.min(width, height) * 0.5);
        int x = (width - diameter) / 2;
        int y = height / 6; // Position in the top third of the panel

        // Calculate total amount for scaling
        double totalAmount = records.stream().mapToDouble(Record::getAmount).sum();

        // Start angle for first slice
        double startAngle = 0;

        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            double percentage = record.getAmount() / totalAmount;
            double angle = percentage * 360 + 0.5;

            // Set color for the slice
            g2d.setColor(colors[i % colors.length]);

            // Draw the arc for the slice
            g2d.fillArc(x, y, diameter, diameter, (int) startAngle, (int) angle);

            // Update start angle for next slice
            startAngle += angle;
        }
    }

    /**
     * Draw the legend on the panel.
     * 
     * @param g the Graphics object to paint on
     */
    private void drawLegend(Graphics g) {
        if (records == null || records.isEmpty()) {
            return; // No data to display
        }

        Graphics2D g2d = (Graphics2D) g;

        // Position the legend dynamically below the pie chart
        int width = getWidth();
        int height = getHeight();
        int legendY = height / 2 + (int) (Math.min(width, height) * 0.3) + 20; // Adjusted to start below the pie chart
        int legendX = width / 2 - 50; // Center the legend horizontally

        // Font size relative to panel size
        int fontSize = Math.max(10, (int) (width * 0.012)); // Scaled font size
        g2d.setFont(new Font("Helvetica", Font.PLAIN, fontSize));

        // Color array for the legend boxes
        for (int i = 0; i < records.size(); i++) {
            Record record = records.get(i);
            double percentage = record.getAmount() / records.stream().mapToDouble(Record::getAmount).sum();

            // Draw legend color box
            g2d.setColor(colors[i % colors.length]);
            g2d.fillRect(legendX, legendY + i * (fontSize + 5), 10, 10);

            // Draw legend text
            g2d.setColor(Color.BLACK);
            g2d.drawString(
                    record.getPlace() + " (" + String.format("%.1f", percentage * 100) + "%)",
                    legendX + 15,
                    legendY + i * (fontSize + 5) + fontSize);
        }
    }
}
