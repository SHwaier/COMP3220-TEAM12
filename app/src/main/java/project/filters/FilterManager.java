
package project.filters;

import java.util.ArrayList;
import java.util.List;

import api.RecordsList;
import api.Record;
public class FilterManager {
    private final List<Filter> filters = new ArrayList<>();

    public void addFilter(Filter filter) {
        filters.add(filter);
    }

    public List<Record> applyFilters(RecordsList records) {
        List<Record> result = records.getAllRecords();
        for (Filter filter : filters) {
            result = filter.apply(new RecordsList(result));
        }
        return result;
    }

    public void clearFilters() {
        filters.clear();
    }
}
