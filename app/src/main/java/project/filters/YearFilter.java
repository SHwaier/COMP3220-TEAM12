package project.filters;

import api.Record;
import api.RecordsList;

import java.util.List;
import java.util.stream.Collectors;

public class YearFilter implements Filter {
    private final List<String> years;

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
