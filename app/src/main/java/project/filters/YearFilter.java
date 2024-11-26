package project.filters;

import api.Record;
import api.RecordsList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A filter that filters records based on the year they are from, implemented
 * using the Filter interface
 * 
 * @see Filter
 */
public class YearFilter implements Filter {
    // List of years to filter by
    private final List<String> years;

    /**
     * Constructor for the YearFilter
     * 
     * @param years the years to filter by
     */
    public YearFilter(List<String> years) {
        this.years = years;
    }

    @Override
    public List<Record> apply(RecordsList records) {
        if (years == null || years.isEmpty() || years.contains("All Years")) {
            // If no years are selected or "All Years" is selected, then return all records
            return records.getAllRecords();
        }
        // Filter records that match any of the selected years
        return records.getAllRecords().stream()
                .filter(record -> years.contains(record.getYear()))
                .collect(Collectors.toList());
    }
}
