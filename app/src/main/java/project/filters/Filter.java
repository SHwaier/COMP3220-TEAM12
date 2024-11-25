
package project.filters;

import java.util.List;
import api.Record;
import api.RecordsList;

public interface Filter {
    List<Record> apply(RecordsList records);
}
