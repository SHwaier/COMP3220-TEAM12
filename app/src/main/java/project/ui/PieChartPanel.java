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
 * A JPanel that displays a responsive pie chart of the given records.
 */
public class PieChartPanel extends JPanel {

    private RecordsList records;

    // Dynamically generated colors for chart slices
    private final List<Color> colors = generateColors();

    public PieChartPanel(RecordsList records) {
        this.records = consolidateRecords(records);
    }

    public void setRecords(RecordsList records) {
        this.records = consolidateRecords(records);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.WHITE);
        drawPieChart(g);
        drawLegend(g);
    }

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

    private void drawLegend(Graphics g) {
        if (records == null || records.getAllRecords().isEmpty()) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;

        int width = getWidth();
        int height = getHeight();
        int legendY = height / 2 + (int) (Math.min(width, height) * 0.3) + 20;
        int legendX = 20;

        int fontSize = Math.max(10, (int) (width * 0.012));
        g2d.setFont(new Font("Helvetica", Font.PLAIN, fontSize));

        double totalAmount = records.getAllRecords().stream().mapToDouble(Record::getAmount).sum();

        for (int i = 0; i < records.getAllRecords().size(); i++) {
            Record record = records.getAllRecords().get(i);
            double percentage = record.getAmount() / totalAmount;

            String label = record.getPlace() + " (" + String.format("%.1f", percentage * 100) + "%)";
            if (label.length() > 20) {
                label = label.substring(0, 17) + "...";
            }

            g2d.setColor(colors.get(i % colors.size()));
            g2d.fillRect(legendX, legendY + i * (fontSize + 5), 10, 10);

            g2d.setColor(Color.BLACK);
            g2d.drawString(label, legendX + 15, legendY + i * (fontSize + 5) + fontSize);
        }
    }

    /**
     * Consolidates records with the same place into a single record.
     * Uses the RecordFactory to create new records with an average amount.
     *
     * @param originalRecords the original RecordsList
     * @return a consolidated RecordsList
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

    private List<Color> generateColors() {
        List<Color> colorPalette = new ArrayList<>();
        colorPalette.add(Color.ORANGE);
        colorPalette.add(new Color(0, 102, 204));
        colorPalette.add(Color.MAGENTA);
        colorPalette.add(Color.CYAN);
        colorPalette.add(Color.RED);
        colorPalette.add(Color.BLUE);
        colorPalette.add(Color.GREEN);
        colorPalette.add(Color.PINK);
        colorPalette.add(Color.YELLOW);

        for (int i = 0; i < 50; i++) {
            colorPalette.add(new Color((int) (Math.random() * 0x1000000)));
        }
        return colorPalette;
    }
}
