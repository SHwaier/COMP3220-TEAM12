package project.filters;

import api.Record;
import api.RecordFactory;
import api.RecordsList;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class FilterManagerTest {

    private FilterManager filterManager;

    @Before
    public void setUp() {
        filterManager = new FilterManager();
    }

    @Test
    public void testAddFilter() {
        Filter filter = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForYear("2020");
            }
        };

        filterManager.addFilter(filter);
        assertEquals(1, getFiltersCount(filterManager));
    }

    @Test
    public void testApplyFiltersSingleFilter() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2021", "USA", 200);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2));

        // Add a filter to retain only records for year "2020"
        Filter filter = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForYear("2020");
            }
        };
        filterManager.addFilter(filter);

        // Apply filters and verify result
        List<Record> result = filterManager.applyFilters(recordsList);
        assertEquals(1, result.size());
        assertEquals("2020", result.get(0).getYear());
    }

    @Test
    public void testApplyFiltersMultipleFilters() {
        // Create test records
        Record record1 = RecordFactory.createRecord("2020", "Canada", 100);
        Record record2 = RecordFactory.createRecord("2020", "USA", 200);
        Record record3 = RecordFactory.createRecord("2021", "Canada", 300);
        RecordsList recordsList = new RecordsList(Arrays.asList(record1, record2, record3));

        // Add a filter to retain records for year "2020"
        Filter filterYear = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForYear("2020");
            }
        };

        // Add a filter to retain records for place "Canada"
        Filter filterPlace = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForPlace("Canada");
            }
        };

        filterManager.addFilter(filterYear);
        filterManager.addFilter(filterPlace);

        // Apply filters and verify result
        List<Record> result = filterManager.applyFilters(recordsList);
        assertEquals(1, result.size());
        assertEquals("2020", result.get(0).getYear());
        assertEquals("Canada", result.get(0).getPlace());
    }

    @Test
    public void testClearFilters() {
        // Add multiple filters
        Filter filter1 = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForYear("2020");
            }
        };

        Filter filter2 = new Filter() {
            @Override
            public List<Record> apply(RecordsList records) {
                return records.getRecordsForPlace("Canada");
            }
        };

        filterManager.addFilter(filter1);
        filterManager.addFilter(filter2);

        // Clear filters
        filterManager.clearFilters();

        // Verify the filters list is empty
        assertEquals(0, getFiltersCount(filterManager));
    }

    // Utility method to access private filter list size for verification
    private int getFiltersCount(FilterManager manager) {
        try {
            java.lang.reflect.Field field = FilterManager.class.getDeclaredField("filters");
            field.setAccessible(true);
            List<?> filters = (List<?>) field.get(manager);
            return filters.size();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Reflection failed", e);
        }
    }
}
