package project.io;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import api.RecordsList;
import project.ProjectWindow;

public class Export {

    /**
     * Exports the given records to a selected file type.
     * 
     * @param records The records to be exported.
     */
    public static void exportData(RecordsList records) {
        // Ensure we have records to export
        if (records == null || records.getAllRecords().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No data available to export.", "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Export File");

        // Add filters for CSV, JSON, and PDF
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV Files (*.csv)", "csv");
        FileNameExtensionFilter jsonFilter = new FileNameExtensionFilter("JSON Files (*.json)", "json");
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("PDF Files (*.pdf)", "pdf");
        fileChooser.addChoosableFileFilter(csvFilter);
        fileChooser.addChoosableFileFilter(jsonFilter);
        fileChooser.addChoosableFileFilter(pdfFilter);
        fileChooser.setFileFilter(csvFilter); // Set CSV as the default filter

        // Show save dialog
        int userSelection = fileChooser.showSaveDialog(ProjectWindow.getFrames()[0]); // `null` works, but a parent
                                                                                      // JFrame is better

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            String selectedExtension = fileChooser.getFileFilter().getDescription();

            // Ensure the file has the correct extension
            if (selectedExtension.contains("CSV") && !filePath.endsWith(".csv")) {
                filePath += ".csv";
            } else if (selectedExtension.contains("JSON") && !filePath.endsWith(".json")) {
                filePath += ".json";
            } else if (selectedExtension.contains("PDF") && !filePath.endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                // Call the appropriate export method based on the selected extension
                if (filePath.endsWith(".csv")) {
                    api.io.ExportRecords.exportToCSV(filePath, records);
                    JOptionPane.showMessageDialog(null, "CSV file saved successfully.", "Export Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (filePath.endsWith(".json")) {
                    api.io.ExportRecords.exportToJSON(filePath, records);
                    JOptionPane.showMessageDialog(null, "JSON file saved successfully.", "Export Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else if (filePath.endsWith(".pdf")) {
                    // Stub method for exporting to PDF
                    api.io.ExportRecords.exportToPDF(records.getAllRecords(), filePath);
                    JOptionPane.showMessageDialog(null, "PDF file saved successfully.", "Export Success",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Unsupported file type selected.", "Export Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Error during export: " + e.getMessage(), "Export Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
