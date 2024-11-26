package project.io;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import api.RecordsCentral;
import api.RecordsList;
import project.DataManager;

/**
 * Handles importing data from a CSV file for user imports.
 */
public class Import {

    /**
     * Imports data from a CSV file and adds it to the DataManager as "ImportedData"
     * that can be called later on through DataManager.
     */
    public static void importData() {

        DataManager dataManager = DataManager.getInstance();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Import Data File");

        // Add a file filter for CSV files
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Parse the new data
                RecordsCentral recordsCentral = RecordsCentral.getInstance();

                RecordsList importedData = recordsCentral
                        .parseFileData(selectedFile.getAbsolutePath());
                // Add the new data to the DataManager
                dataManager.addDataset("ImportedData", importedData);

                // Set the active dataset to the imported data
                dataManager.setActiveDataset("ImportedData");

            } catch (Exception ex) {
                // Handle errors (e.g., parsing issues)
                JOptionPane.showMessageDialog(null, "Failed to import data: " + ex.getMessage(),
                        "Import Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
