package com.logs.app.logmonitoring.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LogFileValidationTest {
    private static final String LOG_FILE_PATH = "src/test/resources/logs.log";

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testFileNotEmpty() throws IOException {
        // Given: A path to the log file
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));

        // When: Reading the first line
        String firstLine = reader.readLine();

        // Then: The log file should not be empty
        assertNotNull(firstLine, "The log file should not be empty");
        reader.close();
    }

    @Test
    public void testFileName() {
        // Given: The expected log file name
        String expectedFileName = "logs.log";

        // Then: The log file must be named 'logs.log'
        assertTrue(LOG_FILE_PATH.endsWith(expectedFileName),
                "The log file must be named '" + expectedFileName + "'");
    }

    @Test
    public void testEmptyValues() throws IOException {
        // Given: A file reader for the log file
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;

        // When: Iterating through each line in the file
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] parts = line.split(", ");
            for (String part : parts) {
                // Then: Each log entry should not have empty values
                assertFalse(part.trim().isEmpty(),
                        "Log entry on line " + lineNumber + " should not have empty values.");
            }
        }
        reader.close();
    }

    @Test
    public void testNullValues() throws IOException {
        // Given: A file reader for the log file
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;

        // When: Iterating through each line in the file
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] parts = line.split(", ");
            for (String part : parts) {
                // Then: Each log entry should not have null values
                assertNotNull(part,
                        "Log entry on line " + lineNumber + " should not have null values.");
            }
        }
        reader.close();
    }

    @Test
    public void testInputFormatConsistency() throws IOException {
        // Given: A file reader for the log file
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;

        // When: Iterating through each line in the file
        int lineNumber = 0;
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            String[] parts = line.split(",\\s*");

            // Then: Each log entry should have exactly four components
            assertEquals(4, parts.length,
                    "Log entry on line " + lineNumber + " should have exactly four components.");

            // Validate timestamp format HH:MM:SS
            try {
                LocalTime.parse(parts[0]); // Validate that parsing succeeds
            } catch (Exception e) {
                fail("Log entry on line " + lineNumber + " has an invalid timestamp format: " + parts[0]);
            }

            // Check job description format
            assertTrue(parts[1].matches("scheduled task \\d{3}|background job [a-z]{3}"),
                    "Log entry on line " + lineNumber + " job description must match the required format");

            // Check START/END status
            assertTrue("START".equals(parts[2]) || "END".equals(parts[2]),
                    "Log entry on line " + lineNumber + " status must be either 'START' or 'END'");

            // Check that PID is a 5-digit number
            assertTrue(parts[3].matches("\\d{5}"),
                    "Log entry on line " + lineNumber + " PID must be a 5-digit number");
        }
        reader.close();
    }

    @Test
    public void testDuplicateEntries() throws IOException {
        // Given: A file reader for the log file and a set to track entries
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        Set<String> entriesSet = new HashSet<>();
        String line;
        boolean hasDuplicates = false;
        int lineNumber = 0;

        // When: Iterating through each line in the file to check for duplicates
        while ((line = reader.readLine()) != null) {
            lineNumber++;
            if (!entriesSet.add(line.trim())) { // If the entry is already in the set, we have a duplicate
                hasDuplicates = true;
                break;
            }
        }

        // Then: The log file should not contain duplicate entries
        assertFalse(hasDuplicates, "The log file should not contain duplicate entries on line " + lineNumber);
        reader.close();
    }

    @Test
    public void testAllProcessesHaveBothStartAndEnd() throws IOException {
        // Given: A buffered reader for the log file to track START and END entries
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        Map<Integer, Boolean> hasStart = new HashMap<>();
        Map<Integer, Boolean> hasEnd = new HashMap<>();

        String line;

        // When: Iterating through each line in the log file
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",\\s*");
            int pid = Integer.parseInt(parts[3]);
            String status = parts[2];

            // Track if we have START and END for each PID
            if ("START".equals(status)) {
                hasStart.put(pid, true);
            } else if ("END".equals(status) && !hasStart.containsKey(pid)) {
                // Mark END without a corresponding START
                hasEnd.put(pid, false);
            } else {
                // Mark END as true only if there was a corresponding START
                hasEnd.put(pid, true);
            }
        }

        // Then: Verify that each process has both START and END entries
        for (Integer pid : hasStart.keySet()) {
            // Fail if there is no corresponding END for a valid START
            assertTrue(hasStart.get(pid), "Process with PID: " + pid + " must have a START entry.");
            assertTrue(hasEnd.getOrDefault(pid, false), "Process with PID: " + pid + " must have an END entry.");
        }

        // Additional check: If there's an END for a PID, there must also be a START for it
        for (Integer pid : hasEnd.keySet()) {
            if (!hasEnd.get(pid)) {
                fail("Process with PID: " + pid + " has an END entry without a corresponding START.");
            }
        }

        reader.close();
    }

    @Test
    public void testEndDateAfterStartDate() throws IOException {
        // Given: A file reader for the log file to track start and end dates
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        Map<Integer, LocalTime> startTimes = new HashMap<>();
        Map<Integer, LocalTime> endTimes = new HashMap<>();

        String line;

        // When: Iterating through each line in the log file
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",\\s*");
            int pid = Integer.parseInt(parts[3]);
            LocalTime timestamp = LocalTime.parse(parts[0]);
            String status = parts[2];

            if ("START".equals(status)) {
                startTimes.put(pid, timestamp);
            } else if ("END".equals(status)) {
                endTimes.put(pid, timestamp);
            }
        }

        // Then: Verify that the END time is after the START time for each process
        for (Integer pid : startTimes.keySet()) {
            LocalTime startTime = startTimes.get(pid);
            LocalTime endTime = endTimes.get(pid);

            assertNotNull(endTime, "Process with PID: " + pid + " must have an END time recorded.");
            assertTrue(endTime.isAfter(startTime), "The END time must be after the START time for PID: " + pid);
        }

        reader.close();
    }

    @Test
    public void testJobDescriptionAndPIDConsistency() throws IOException {
        // Given: A file reader for the log file to check job description and PID
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        Map<Integer, String> startJobDescriptions = new HashMap<>();
        Map<Integer, String> endJobDescriptions = new HashMap<>();
        String line;

        // When: Iterating through each line in the file
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",\\s*");
            int pid = Integer.parseInt(parts[3]);
            String jobDescription = parts[1];
            String status = parts[2];

            if ("START".equals(status)) {
                startJobDescriptions.put(pid, jobDescription);
            } else if ("END".equals(status)) {
                endJobDescriptions.put(pid, jobDescription);
            }
        }

        // Then: Verify that job descriptions match for START and END entries for each PID
        for (Integer pid : startJobDescriptions.keySet()) {
            String startJobDescription = startJobDescriptions.get(pid);
            String endJobDescription = endJobDescriptions.get(pid);

            assertEquals(startJobDescription, endJobDescription,
                    "The job description must be the same for START and END for PID: " + pid);
        }

        reader.close();
    }
}
