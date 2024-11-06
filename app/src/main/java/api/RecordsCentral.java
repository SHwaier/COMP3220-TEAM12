package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RecordsCentral {

    /**
     * Parse the data from the given file path
     * 
     * @param filePath the path to the file to parse
     * @return a list of records parsed from the file
     * @throws IOException if the file is not found or cannot be read
     */
    public static List<Record> parseData(String filePath) {
        InputStream is = RecordsCentral.class.getClassLoader().getResourceAsStream(filePath);
        List<Record> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line = br.readLine(); // Read the header line and ignore it

            // Column headers in order (for mapping)
            String[] headers = line.split(",");

            // Read each subsequent line (each year)
            while ((line = br.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");

                // First element is the year, others are amounts for each place
                String year = values[0];

                for (int i = 1; i < values.length; i++) {
                    // in some files, empty fields are represented by "..", so we handle that here
                    int amount = values[i].equals("..") ? 0 : Integer.parseInt(values[i]);
                    records.add(new Record(year, headers[i], amount));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Get the total amount of electricity generated in a given year
     * 
     * @param records the list of records to search
     * @param year    the year to search for
     * @return the total amount of electricity generated in the given year in
     *         megawatts
     */
    public static long getTotalForYear(List<Record> records, String year) {
        if (records == null || year == null) {
            throw new IllegalArgumentException("Records and year must not be null");
        }
        if (!year.matches("\\d+")) {
            throw new IllegalArgumentException("Year must be a valid number");
        }

        return records.stream().filter(r -> r.getYear().equals(year)).mapToLong(Record::getAmount).sum();

    }

    /**
     * Get the total amount of electricity generated in a given place
     * 
     * @param records the list of records to search
     * @param place   the place to search for
     * @return the total amount of electricity generated in the given place in
     *         megawatts
     */
    public static long getTotalForPlace(List<Record> records, String place) {
        if (records == null || place == null) {
            throw new IllegalArgumentException("Records and place must not be null");
        }

        return records.stream().filter(r -> r.getPlace().equals(place)).mapToLong(Record::getAmount).sum();
    }

    /**
     * Get the total amount of electricity generated in a given year and place
     * 
     * @param records the list of records to search
     * @param year    the year to search for
     * @param place   the place to search for
     * @return the total amount of electricity generated in the given year and place
     */
    public static int getTotalForYearAndPlace(List<Record> records, String year, String place) {
        if (records == null || year == null || place == null) {
            throw new IllegalArgumentException("Records, year, and place must not be null");
        }
        if (!year.matches("\\d+")) {
            throw new IllegalArgumentException("Year must be a valid number");
        }

        return records.stream().filter(r -> r.getYear().equals(year) && r.getPlace().equals(place))
                .mapToInt(Record::getAmount).sum();
    }

    /**
     * Get the records for a given year
     * 
     * @param records list of records to query from
     * @param year    the year to search for
     * @return a list of records for the given year
     */
    public static List<Record> getRecordsForYear(List<Record> records, String year) {
        if (records == null || year == null) {
            throw new IllegalArgumentException("Records and year must not be null");
        }
        if (!year.matches("\\d+")) {
            throw new IllegalArgumentException("Year must be a valid number");
        }

        return records.stream().filter(r -> r.getYear().equals(year)).toList();
    }

    /**
     * Get the records for a given place
     * 
     * @param records list of records to query from
     * @param place   the place to search for
     * @return a list of records for the given place
     */
    public static List<Record> getRecordsForPlace(List<Record> records, String place) {
        if (records == null || place == null) {
            throw new IllegalArgumentException("Records and place must not be null");
        }

        return records.stream().filter(r -> r.getPlace().equals(place)).toList();
    }
}
