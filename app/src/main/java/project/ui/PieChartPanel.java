package project.ui;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;
import api.Record;
import api.RecordFactory;
import api.RecordsList;
import java.util.List;

/**
 * A JPanel that displays a pie chart of the records in the RecordsList.
 */
public class PieChartPanel extends JPanel {

    private RecordsList records;

    // Dynamically generated colors for chart slices
    private final List<Color> colors = generateColors();
    private JPanel legendContainer;

    /**
     * Creates a new PieChartPanel with the given RecordsList.
     * 
     * @param records the records to display
     */
    public PieChartPanel(RecordsList records) {
        this.records = consolidateRecords(records);
        setLayout(new BorderLayout()); // Use BorderLayout to separate pie chart and legend

        // Create a scrollable legend panel
        JScrollPane legendScrollPane = createLegendScrollPane();
        add(legendScrollPane, BorderLayout.SOUTH); // Move legend below the pie chart

        // Create a pie chart panel
        JPanel pieChartCanvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.WHITE);
                drawPieChart(g);
            }
        };
        pieChartCanvas.setPreferredSize(new Dimension(400, 400)); // Fixed size for the pie chart
        add(pieChartCanvas, BorderLayout.CENTER);
    }

    /**
     * Set the records to display on the pie chart
     * 
     * @param records the records to display
     */
    public void setRecords(RecordsList records) {
        this.records = consolidateRecords(records);
        refreshLegend(); // Update the legend whenever data changes
        repaint();
    }

    /**
     * Draw the pie chart on the given Graphics object.
     * 
     * @param g the Graphics object to draw on
     */
    private void drawPieChart(Graphics g) {
        if (records == null || records.getAllRecords().isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int diameter = (int) (Math.min(width, height) * 0.5);
        int x = (width - diameter) / 2;
        int y = height / 6;

        double totalAmount = records.getAllRecords().stream().mapToDouble(Record::getAmount).sum();
        if (totalAmount == 0) {
            g2d.setColor(Color.BLACK);
            g2d.drawString("No Data Available", width / 2 - 50, height / 2);
            return;
        }

        double startAngle = 0;

        for (int i = 0; i < records.getAllRecords().size(); i++) {
            Record record = records.getAllRecords().get(i);
            double percentage = record.getAmount() / totalAmount;
            double angle = percentage * 360;

            g2d.setColor(colors.get(i % colors.size()));
            g2d.fillArc(x, y, diameter, diameter, (int) startAngle, (int) angle);

            startAngle += angle;
        }
    }

    /**
     * Create a scrollable panel to display the legend of the pie chart.
     * @return a JScrollPane containing the legend
     */
    private JScrollPane createLegendScrollPane() {
        legendContainer = new JPanel();
        legendContainer.setLayout(new BoxLayout(legendContainer, BoxLayout.Y_AXIS));
        legendContainer.setBackground(Color.WHITE);

        refreshLegend(); // Populate the legend initially

        JScrollPane scrollPane = new JScrollPane(legendContainer);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        scrollPane.setPreferredSize(new Dimension(400, 200)); // Fixed height for the legend
        return scrollPane;
    }

    /**
     * Refresh the legend panel with the latest data.
     */
    private void refreshLegend() {
        legendContainer.removeAll();

        if (records == null || records.getAllRecords().isEmpty()) {
            return;
        }

        double totalAmount = records.getAllRecords().stream().mapToDouble(Record::getAmount).sum();

        for (int i = 0; i < records.getAllRecords().size(); i++) {
            Record record = records.getAllRecords().get(i);
            double percentage = record.getAmount() / totalAmount;

            JPanel legendItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
            legendItem.setBackground(Color.WHITE);

            JLabel colorBox = new JLabel();
            colorBox.setOpaque(true);
            colorBox.setBackground(colors.get(i % colors.size()));
            colorBox.setPreferredSize(new Dimension(20, 20));

            JLabel label = new JLabel(record.getPlace() + " (" + String.format("%.1f", percentage * 100) + "%)");
            label.setFont(new Font("Helvetica", Font.PLAIN, 14));

            legendItem.add(colorBox);
            legendItem.add(Box.createHorizontalStrut(10)); // Add spacing between color and label
            legendItem.add(label);

            legendContainer.add(legendItem);
        }

        legendContainer.revalidate();
        legendContainer.repaint();
    }

    /**
     * Consolidate the records by averaging the amounts for each place.
     * Used when multiple records of the same place are present.
     * @param originalRecords the original records to consolidate
     * @return a new RecordsList with consolidated records
     */
    private RecordsList consolidateRecords(RecordsList originalRecords) {
        if (originalRecords == null || originalRecords.getAllRecords().isEmpty()) {
            return new RecordsList(List.of());
        }

        Map<String, List<Record>> groupedByPlace = originalRecords.getAllRecords().stream()
                .collect(Collectors.groupingBy(Record::getPlace));

        List<Record> consolidatedRecords = groupedByPlace.entrySet().stream()
                .map(entry -> {
                    String place = entry.getKey();
                    double averageAmount = entry.getValue().stream().mapToDouble(Record::getAmount).average()
                            .orElse(0.0);
                    return RecordFactory.createRecord("0", place, (int) Math.round(averageAmount));
                })
                .collect(Collectors.toList());

        return new RecordsList(consolidatedRecords);
    }

    /**
     * Generate a list of random colors for the pie chart slices.
     * @return a list of random colors
     */
    private List<Color> generateColors() {
        List<Color> colorPalette = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            float hue = random.nextFloat();
            float saturation = 0.5f + random.nextFloat() * 0.5f;
            float brightness = 0.7f + random.nextFloat() * 0.4f;
            colorPalette.add(Color.getHSBColor(hue, saturation, brightness));
        }
        return colorPalette;
    }
}
