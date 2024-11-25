package project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Comparator;
import api.Record;
import api.RecordsList;

public class LineGraph extends JPanel {
    private RecordsList records;
    private static final double SCALE_FACTOR = 1_000_000.0; // Scale down values by 1 million

    public LineGraph(RecordsList records) {
        this.records = records;
    }

    /**
     * Set the records to display on the line graph
     * @param records the records to display
     */
    public void setRecords(RecordsList records) {
        this.records = records;
    }

    @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    setBackground(Color.WHITE);

    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Define base colors for the lines
    Color[] baseColors = { new Color(0, 102, 204), Color.BLUE, Color.ORANGE, Color.RED, Color.GREEN, Color.MAGENTA };

    // Track unique labels and assign colors to them
    Map<String, Color> labelColorMap = new HashMap<>();
    int colorIndex = 0; // Initialize colorIndex here

    // Sort records by year for better line continuity
    records.getAllRecords().sort(Comparator.comparingInt(r -> Integer.parseInt(r.getYear())));

    // Calculate range of years for the X-axis
    int minYear = records.getAllRecords().stream().mapToInt(r -> Integer.parseInt(r.getYear())).min().orElse(0);
    int maxYear = records.getAllRecords().stream().mapToInt(r -> Integer.parseInt(r.getYear())).max().orElse(1);
    int yearRange = maxYear - minYear;

    // Responsiveness: Adjust spacing based on panel size
    int panelWidth = getWidth();
    int panelHeight = getHeight();
    int leftPadding = 70;
    int maxGraphHeight = panelHeight - 150;
    int pointSpacing = (panelWidth - leftPadding - 50) / (yearRange + 1);

    // Find the maximum data value for scaling, scaled down by SCALE_FACTOR
    double maxDataValue = records.getAllRecords().stream().mapToDouble(r -> r.getAmount() / SCALE_FACTOR).max().orElse(1.0);

    // Draw Y-axis grid lines and labels
    int gridLines = 10;
    g2d.setColor(Color.LIGHT_GRAY);
    for (int i = 0; i <= gridLines; i++) {
        int y = panelHeight - 100 - i * maxGraphHeight / gridLines;
        g2d.drawLine(leftPadding, y, panelWidth - 50, y);
        String label = String.format("%.1fM", i * maxDataValue / gridLines);
        g2d.setColor(Color.BLACK);
        g2d.drawString(label, leftPadding - 30, y + 5);
        g2d.setColor(Color.LIGHT_GRAY);
    }

    // Draw X-axis year labels
    g2d.setColor(Color.BLACK);
    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
    FontMetrics fm = g2d.getFontMetrics();
    for (int year = minYear; year <= maxYear; year++) {
        int x = leftPadding + (year - minYear) * pointSpacing;
        String yearLabel = String.valueOf(year);
        if (pointSpacing > fm.stringWidth(yearLabel) * 2 || year == minYear || year == maxYear || (year - minYear) % 2 == 0) {
            g2d.drawString(yearLabel, x - fm.stringWidth(yearLabel) / 2, panelHeight - 70);
        }
    }

    // Draw X-axis label
    g2d.setFont(new Font("Arial", Font.BOLD, 12));
    g2d.drawString("Time (Year)", panelWidth / 2 - fm.stringWidth("Time (Year)") / 2, panelHeight - 40);

    // Draw Y-axis label rotated
    g2d.rotate(-Math.PI / 2);
    g2d.drawString("Power Generated (Megawatts)", -panelHeight / 2 - fm.stringWidth("Power Generated (Megawatts)") / 2, 20);
    g2d.rotate(Math.PI / 2);

    // Draw the line graph points and lines for each unique label
    Map<String, List<Point>> pointsMap = new HashMap<>();

    for (Record record : records.getAllRecords()) {
        int year = Integer.parseInt(record.getYear());
        int x = leftPadding + (year - minYear) * pointSpacing;
        int y = panelHeight - 100 - (int) (((double) record.getAmount() / SCALE_FACTOR / maxDataValue) * maxGraphHeight);

        // Assign a color to the label if it doesn't already have one
        if (!labelColorMap.containsKey(record.getPlace())) {
            labelColorMap.put(record.getPlace(), baseColors[colorIndex % baseColors.length]);
            colorIndex++; // Increment colorIndex only when assigning a new color
        }

        g2d.setColor(labelColorMap.get(record.getPlace()));

        // Draw the point
        g2d.fillOval(x - 4, y - 4, 8, 8);

        // Store points for line drawing
        pointsMap.computeIfAbsent(record.getPlace(), k -> new ArrayList<>()).add(new Point(x, y));
    }

    // Draw lines connecting points with the same label
    for (Map.Entry<String, List<Point>> entry : pointsMap.entrySet()) {
        g2d.setColor(labelColorMap.get(entry.getKey()));
        List<Point> points = entry.getValue();
        for (int i = 0; i < points.size() - 1; i++) {
            Point p1 = points.get(i);
            Point p2 = points.get(i + 1);
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    // Draw the legend below the chart
    int legendY = panelHeight - 20;
    int legendX = leftPadding;
    g2d.setFont(new Font("Arial", Font.PLAIN, 12));
    for (Map.Entry<String, Color> entry : labelColorMap.entrySet()) {
        g2d.setColor(entry.getValue());
        g2d.fillRect(legendX, legendY - 10, 10, 10);
        g2d.setColor(Color.BLACK);
        g2d.drawString(entry.getKey(), legendX + 15, legendY);
        legendX += 50 + fm.stringWidth(entry.getKey());
    }
}

}
