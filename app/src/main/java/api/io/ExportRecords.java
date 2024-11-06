package api.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import api.Record;

/**
 * Provides methods to export records to different file formats.
 */
public class ExportRecords {

    /**
     * Exports a list of records to a PDF file.
     *
     * @param records  the list of records to export
     * @param filePath the path to the PDF file to be created
     */
    public static void exportToPDF(List<Record> records, String filePath) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            float startY = 700; // Starting Y position for content
            float margin = 50;
            float yPosition = startY;

            // Title section
            try (PDPageContentStream titleStream = new PDPageContentStream(document, page)) {
                titleStream.beginText();
                titleStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
                titleStream.newLineAtOffset(220, 750);
                titleStream.showText("Electricity Generation Report");
                titleStream.endText();
            }

            // Draw table headers and content
            for (Record record : records) {
                // Create a new page and content stream if we run out of space on the current
                // page
                if (yPosition < 50) {
                    page = new PDPage();
                    document.addPage(page);
                    yPosition = startY;
                }

                // Content stream for table data
                try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                    contentStream.setFont(PDType1Font.HELVETICA, 12);

                    // Write each record's information
                    contentStream.beginText();
                    contentStream.newLineAtOffset(margin, yPosition);
                    contentStream.showText(record.getYear());
                    contentStream.newLineAtOffset(100, 0);
                    contentStream.showText(record.getPlace());
                    contentStream.newLineAtOffset(250, 0);
                    contentStream.showText(String.valueOf(record.getAmount()));
                    contentStream.endText();
                }

                yPosition -= 15; // Move down for the next row
            }

            // Save the PDF
            document.save(filePath);
            System.out.println("PDF file created successfully at " + filePath);

        } catch (IOException e) {
            System.err.println("Error writing to PDF file: " + e.getMessage());
        }
    }

    public static void exportToCSV(String filePath, List<Record> records) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the CSV header
            writer.write("Year,Place,Amount");
            writer.newLine();

            // Write each record as a CSV line
            for (Record record : records) {
                writer.write(record.getYear() + "," + record.getPlace() + "," + record.getAmount());
                writer.newLine();
            }

            System.out.println("CSV file created successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
