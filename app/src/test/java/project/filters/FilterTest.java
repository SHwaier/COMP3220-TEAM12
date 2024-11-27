package project.filters;

import api.Record;
import api.RecordFactory;
import api.RecordsList;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class FilterTest {

    @Test
    public void testYearFilterWithSpecificYears() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        Record record3 = RecordFactory.createRecord("2020", "Canada", 150);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2, record3));

        // Create a YearFilter for year "2020"
        YearFilter yearFilter = new YearFilter(Collections.singletonList("2020"));

        // Apply filter
        List<Record> filteredRecords = yearFilter.apply(recordsList);

        // Verify results
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.stream().allMatch(record -> record.getYear().equals("2020")));
    }

    @Test
    public void testYearFilterWithEmptyYears() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2));

        // Create a YearFilter with an empty list
        YearFilter yearFilter = new YearFilter(Collections.emptyList());

        // Apply filter
        List<Record> filteredRecords = yearFilter.apply(recordsList);

        // Verify all records are returned
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.containsAll(recordsList.getAllRecords()));
    }

    @Test
    public void testYearFilterWithAllYears() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2));

        // Create a YearFilter with "All Years"
        YearFilter yearFilter = new YearFilter(Collections.singletonList("All Years"));

        // Apply filter
        List<Record> filteredRecords = yearFilter.apply(recordsList);

        // Verify all records are returned
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.containsAll(recordsList.getAllRecords()));
    }

    @Test
    public void testRegionFilterWithSpecificRegions() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        Record record3 = RecordFactory.createRecord("2022", "Canada", 300);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2, record3));

        // Create a RegionFilter for region "Canada"
        RegionFilter regionFilter = new RegionFilter(Collections.singletonList("Canada"));

        // Apply filter
        List<Record> filteredRecords = regionFilter.apply(recordsList);

        // Verify results
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.stream().allMatch(record -> record.getPlace().equals("Canada")));
    }

    @Test
    public void testRegionFilterWithEmptyRegions() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2));

        // Create a RegionFilter with an empty list
        RegionFilter regionFilter = new RegionFilter(Collections.emptyList());

        // Apply filter
        List<Record> filteredRecords = regionFilter.apply(recordsList);

        // Verify all records are returned
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.containsAll(recordsList.getAllRecords()));
    }

    @Test
    public void testRegionFilterWithAllRegions() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2));

        // Create a RegionFilter with "All Regions"
        RegionFilter regionFilter = new RegionFilter(Collections.singletonList("All Regions"));

        // Apply filter
        List<Record> filteredRecords = regionFilter.apply(recordsList);

        // Verify all records are returned
        assertEquals(2, filteredRecords.size());
        assertTrue(filteredRecords.containsAll(recordsList.getAllRecords()));
    }
}
