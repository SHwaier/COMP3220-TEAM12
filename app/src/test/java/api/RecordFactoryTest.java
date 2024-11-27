package api;

import static org.junit.Assert.*;
import org.junit.Test;

public class RecordFactoryTest {

    @Test
    public void testCreateRecord_ValidInputs() {
        Record record = RecordFactory.createRecord("2020", "Place1", 100);

        assertNotNull(record);
        assertEquals("2020", record.getYear());
        assertEquals("Place1", record.getPlace());
        assertEquals(100, record.getAmount());
    }

    @Test
    public void testCreateRecord_ZeroAmount() {
        Record record = RecordFactory.createRecord("2021", "Place2", 0);

        assertNotNull(record);
        assertEquals("2021", record.getYear());
        assertEquals("Place2", record.getPlace());
        assertEquals(0, record.getAmount());
    }

    @Test
    public void testCreateRecord_NegativeAmount() {
        try {
            RecordFactory.createRecord("2020", "Place1", -100);
            fail("Expected IllegalArgumentException for negative amount");
        } catch (IllegalArgumentException e) {
            assertEquals("Amount must be a positive number", e.getMessage());
        }
    }

    @Test
    public void testCreateRecord_InvalidYear() {
        try {
            RecordFactory.createRecord("InvalidYear", "Place1", 100);
            fail("Expected IllegalArgumentException for invalid year");
        } catch (IllegalArgumentException e) {
            assertEquals("Year must be a valid number", e.getMessage());
        }
    }

    @Test
    public void testCreateRecord_EmptyPlace() {
        Record record = RecordFactory.createRecord("2020", "", 100);

        assertNotNull(record);
        assertEquals("2020", record.getYear());
        assertEquals("", record.getPlace());
        assertEquals(100, record.getAmount());
    }

    @Test
    public void testCreateRecord_ValidInputs_LongYear() {
        Record record = RecordFactory.createRecord("202012", "LongPlaceName", 200);

        assertNotNull(record);
        assertEquals("202012", record.getYear());
        assertEquals("LongPlaceName", record.getPlace());
        assertEquals(200, record.getAmount());
    }
}
