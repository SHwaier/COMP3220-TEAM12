/**
 * This class is used to manage the datasets and records in the application.
 * It provides methods to add, get, set, and filter datasets and records.
 * The purpose of this class is to provide a central location for managing the data, without cluttering the UI classes.
 */
package project;

import java.util.HashMap;
import java.util.Map;

import api.RecordsList;

/**
 * Singleton class responsible for managing datasets and records.
 * Streamlines dataset and record management by providing a single point of access throughout the entire app.
 */
public class DataManager {
    private static DataManager instance;

    // Map to store different datasets
    private final Map<String, RecordsList> datasets = new HashMap<>();
    private RecordsList activeRecords = null;
    private RecordsList filteredRecords;

    // Private constructor to prevent direct instantiation
    private DataManager() {
    }

    // Public method to access the single instance
    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    /**
     * Adds a dataset to the DataManager.
     * 
     * @param name    The name of the dataset
     * @param records The records to be added
     */
    public void addDataset(String name, RecordsList records) {
        datasets.put(name, records);
    }

    /**
     * Gets a dataset by name.
     * 
     * @param name The name of the dataset
     * @return The dataset with the given name, or null if not found
     */
    public RecordsList getDataset(String name) {
        return datasets.get(name);
    }

    /**
     * Sets the active dataset to the one with the given name.
     * 
     * dataset has to exist within the datasets map to be added as an active dataset
     * 
     * @param name The name of the dataset to set as active
     */
    public void setActiveDataset(String name) {
        this.activeRecords = datasets.get(name);
    }

    /**
     * Gets the active dataset.
     * 
     * @return The active dataset
     */
    public RecordsList getActiveDataset() {
        return activeRecords;
    }

    /**
     * Sets the filtered records.
     * 
     * @param filteredRecords The filtered records
     */
    public void setFilteredRecords(RecordsList filteredRecords) {
        this.filteredRecords = filteredRecords;
    }

    /**
     * Gets the filtered records.
     * 
     * @return The filtered records
     */
    public RecordsList getFilteredRecords() {
        return filteredRecords;
    }
}
