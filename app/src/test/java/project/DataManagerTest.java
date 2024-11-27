package project;

import api.RecordFactory;
import api.RecordsList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DataManagerTest {

    private DataManager dataManager;

    @Before
    public void setUp() {
        dataManager = DataManager.getInstance();
        // Clear datasets before each test (workaround for singleton state retention)
        dataManager.addDataset("ClearAll", new RecordsList(Arrays.asList()));
        dataManager.setActiveDataset(null);
        dataManager.setFilteredRecords(null);
    }

    @Test
    public void testAddAndGetDataset() {
        // Create test records and datasets
        RecordsList dataset1 = new RecordsList(Arrays.asList(
                RecordFactory.createRecord("2020", "Canada", 100),
                RecordFactory.createRecord("2021", "USA", 200)
        ));
        RecordsList dataset2 = new RecordsList(Arrays.asList(
                RecordFactory.createRecord("2019", "Mexico", 150)
        ));

        // Add datasets to DataManager
        dataManager.addDataset("Dataset1", dataset1);
        dataManager.addDataset("Dataset2", dataset2);

        // Verify datasets can be retrieved correctly
        assertEquals(dataset1, dataManager.getDataset("Dataset1"));
        assertEquals(dataset2, dataManager.getDataset("Dataset2"));
        assertNull(dataManager.getDataset("NonExistentDataset"));
    }

    @Test
    public void testSetAndGetActiveDataset() {
        // Create test records and datasets
        RecordsList dataset = new RecordsList(Arrays.asList(
                RecordFactory.createRecord("2020", "Canada", 100),
                RecordFactory.createRecord("2021", "USA", 200)
        ));

        // Add dataset to DataManager and set it as active
        dataManager.addDataset("ActiveDataset", dataset);
        dataManager.setActiveDataset("ActiveDataset");

        // Verify the active dataset is set correctly
        assertEquals(dataset, dataManager.getActiveDataset());

        // Verify behavior when setting a non-existent dataset as active
        dataManager.setActiveDataset("NonExistentDataset");
        assertNull(dataManager.getActiveDataset());
    }

    @Test
    public void testSetAndGetFilteredRecords() {
        // Create test records and a dataset
        RecordsList filteredRecords = new RecordsList(Arrays.asList(
                RecordFactory.createRecord("2020", "Canada", 50),
                RecordFactory.createRecord("2021", "USA", 150)
        ));

        // Set and get filtered records
        dataManager.setFilteredRecords(filteredRecords);
        assertEquals(filteredRecords, dataManager.getFilteredRecords());

        // Verify behavior when filtered records are not set
        dataManager.setFilteredRecords(null);
        assertNull(dataManager.getFilteredRecords());
    }

    @Test
    public void testSingletonBehavior() {
        // Retrieve multiple instances of DataManager
        DataManager instance1 = DataManager.getInstance();
        DataManager instance2 = DataManager.getInstance();

        // Verify both instances are the same
        assertSame(instance1, instance2);
    }
}
