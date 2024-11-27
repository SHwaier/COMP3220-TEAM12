package api;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class RecordsListTest {

    @Test
    public void testGetAllRecords() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200)
        ));

        List<Record> allRecords = recordsList.getAllRecords();
        assertEquals(2, allRecords.size());
    }

    @Test
    public void testGetAllRecords_Empty() {
        RecordsList recordsList = new RecordsList(List.of());
        List<Record> allRecords = recordsList.getAllRecords();
        assertTrue(allRecords.isEmpty());
    }

    @Test
    public void testGetListOfYears() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200),
                RecordFactory.createRecord("2020", "Place3", 300)
        ));

        String[] years = recordsList.getListOfYears();
        assertArrayEquals(new String[]{"2020", "2021"}, years);
    }

    @Test
    public void testGetListOfYears_Empty() {
        RecordsList recordsList = new RecordsList(List.of());
        String[] years = recordsList.getListOfYears();
        assertArrayEquals(new String[]{}, years);
    }

    @Test
    public void testGetRecordsForYear_ValidYear() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200),
                RecordFactory.createRecord("2020", "Place3", 300)
        ));

        List<Record> recordsForYear = recordsList.getRecordsForYear("2020");
        assertEquals(2, recordsForYear.size());
        assertEquals("Place1", recordsForYear.get(0).getPlace());
    }

    @Test
    public void testGetRecordsForYear_InvalidYear() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200)
        ));

        List<Record> recordsForYear = recordsList.getRecordsForYear("2019");
        assertTrue(recordsForYear.isEmpty());
    }

    @Test
    public void testGetRecordsForPlace_ValidPlace() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200),
                RecordFactory.createRecord("2020", "Place1", 300)
        ));

        List<Record> recordsForPlace = recordsList.getRecordsForPlace("Place1");
        assertEquals(2, recordsForPlace.size());
        assertEquals("2020", recordsForPlace.get(0).getYear());
    }

    @Test
    public void testGetRecordsForPlace_InvalidPlace() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200)
        ));

        List<Record> recordsForPlace = recordsList.getRecordsForPlace("NonExistentPlace");
        assertTrue(recordsForPlace.isEmpty());
    }

    @Test
    public void testGetTotalForYear() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200),
                RecordFactory.createRecord("2020", "Place3", 300)
        ));

        long totalForYear = recordsList.getTotalForYear("2020");
        assertEquals(400, totalForYear);
    }

    @Test
    public void testGetTotalForYear_InvalidYear() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200)
        ));

        long totalForYear = recordsList.getTotalForYear("2019");
        assertEquals(0, totalForYear);
    }

    @Test
    public void testGetTotalForPlace() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200),
                RecordFactory.createRecord("2020", "Place1", 300)
        ));

        long totalForPlace = recordsList.getTotalForPlace("Place1");
        assertEquals(400, totalForPlace);
    }

    @Test
    public void testGetTotalForPlace_InvalidPlace() {
        RecordsList recordsList = new RecordsList(List.of(
                RecordFactory.createRecord("2020", "Place1", 100),
                RecordFactory.createRecord("2021", "Place2", 200)
        ));

        long totalForPlace = recordsList.getTotalForPlace("NonExistentPlace");
        assertEquals(0, totalForPlace);
    }
}
