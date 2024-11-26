
package project.filters;

import java.util.ArrayList;
import java.util.List;

import api.RecordsList;
import api.Record;
/**
 * Manages filters and applies them to records
 * @see Filter
 */
public class FilterManager {
    // List of filters
    private final List<Filter> filters = new ArrayList<>();

    /**
     * Add a filter to the list of filters
     * 
     * @param filter the filter to add
     */
    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    /**
     * Apply all filters to the records
     * 
     * @param records the records to filter
     * @return the filtered records
     */
    public List<Record> applyFilters(RecordsList records) {
        List<Record> result = records.getAllRecords();
        for (Filter filter : filters) {
            result = filter.apply(new RecordsList(result));
        }
        return result;
    }

    /**
     * Removes a filter from the list of filters that are applied
     */
    public void clearFilters() {
        filters.clear();
    }
}
