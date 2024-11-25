
package api;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a collection of records with utility methods for filtering and
 * aggregating.
 */
public class RecordsList {
    private final List<Record> records;

    public RecordsList(List<Record> records) {
        this.records = records;
    }

    public List<Record> getAllRecords() {
        return records;
    }

    public String[] getListOfYears() {
        return records.stream().map(Record::getYear).distinct().toArray(String[]::new);
    }
    public String[] getListOfPlaces() {
        return records.stream().map(Record::getPlace).distinct().toArray(String[]::new);
    }

    public List<Record> getRecordsForYear(String year) {
        return records.stream()
                .filter(record -> record.getYear().equals(year))
                .collect(Collectors.toList());
    }

    public List<Record> getRecordsForPlace(String place) {
        return records.stream()
                .filter(record -> record.getPlace().equals(place))
                .collect(Collectors.toList());
    }

    public long getTotalForYear(String year) {
        return records.stream()
                .filter(record -> record.getYear().equals(year))
                .mapToLong(Record::getAmount)
                .sum();
    }

    public long getTotalForPlace(String place) {
        return records.stream()
                .filter(record -> record.getPlace().equals(place))
                .mapToLong(Record::getAmount)
                .sum();
    }

    public int getTotalForYearAndPlace(String year, String place) {
        return records.stream()
                .filter(record -> record.getYear().equals(year) && record.getPlace().equals(place))
                .mapToInt(Record::getAmount)
                .sum();
    }
}
