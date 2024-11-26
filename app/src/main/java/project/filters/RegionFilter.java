package project.filters;

import api.Record;
import api.RecordsList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A filter that filters records based on the region they are from, implemented using the Filter interface
 * @see Filter
 */
public class RegionFilter implements Filter {
    // List of regions to filter by
    private final List<String> regions;

    /**
     * Constructor for the RegionFilter
     * 
     * @param regions the regions to filter by
     */
    public RegionFilter(List<String> regions) {
        this.regions = regions;
    }

    @Override
    public List<Record> apply(RecordsList records) {
        if (regions == null || regions.isEmpty() || regions.contains("All Regions")) {
            // If no regions are selected or "All Regions" is included, return all records
            return records.getAllRecords();
        }
        // Filter records that match any of the selected regions
        return records.getAllRecords().stream()
                .filter(record -> regions.contains(record.getPlace()))
                .collect(Collectors.toList());
    }
}
