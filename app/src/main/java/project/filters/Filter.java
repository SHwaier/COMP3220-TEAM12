
package project.filters;

import java.util.List;
import api.Record;
import api.RecordsList;

/**
 * Interface for filters
 */
public interface Filter {
    /**
     * Apply the filter to the records
     * 
     * @param records the records to filter
     * @return the filtered records
     */
    List<Record> apply(RecordsList records);
}
