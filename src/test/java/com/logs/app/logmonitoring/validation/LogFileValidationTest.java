package com.logs.app.logmonitoring.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class LogFileValidationTest {
    private static final String LOG_FILE_PATH = "src/test/resources/logs.log";

    @BeforeEach
    public void setUp() {
        // Additional setup if needed
    }

    @Test
    public void testFileNotEmpty() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        assertNotNull(reader.readLine(), "The log file should not be empty");
        reader.close();
    }

    @Test
    public void testFileName() {
        assertTrue(LOG_FILE_PATH.endsWith("logs.log"), "The log file must be named 'logs.log'");
    }

    @Test
    public void testEmptyValues() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            for (String part : parts) {
                assertFalse(part.trim().isEmpty(), "Log entries should not have empty values");
            }
        }
        reader.close();
    }

    @Test
    public void testNullValues() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");
            for (String part : parts) {
                assertNotNull(part, "Log entries should not have null values");
            }
        }
        reader.close();
    }

    @Test
    public void testInputFormatConsistency() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            // Use regex to split by comma and any amount of whitespace
            String[] parts = line.split(",\\s*");

            assertEquals(4, parts.length, "Each log entry should have exactly four components");

            // Check timestamp format HH:MM:SS
            try {
                LocalTime.parse(parts[0]); // Validate that parsing succeeds
            } catch (Exception e) {
                fail("Timestamp format is invalid: " + parts[0]);
            }

            // Check job description format
            assertTrue(parts[1].matches("scheduled task \\d{3}|background job [a-z]{3}"),
                    "Job description must match the required format");

            // Check START/END status
            assertTrue("START".equals(parts[2]) || "END".equals(parts[2]),
                    "Status must be either 'START' or 'END'");

            // Check that PID is a 5-digit number
            assertTrue(parts[3].matches("\\d{5}"), "PID must be a 5-digit number");
        }
        reader.close();
    }

    @Test
    public void testDuplicateEntries() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        Set<String> entriesSet = new HashSet<>();
        String line;
        boolean hasDuplicates = false;

        while ((line = reader.readLine()) != null) {
            if (!entriesSet.add(line.trim())) { // If the entry is already in the set, we have a duplicate
                hasDuplicates = true;
                break;
            }
        }

        assertFalse(hasDuplicates, "The log file should not contain duplicate entries");
        reader.close();
    }

    @Test
    public void testSpacingConsistency() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH));
        String line;
        while ((line = reader.readLine()) != null) {
            // This split allows for varying spacing
            String[] parts = line.split(",\\s*"); // Split by "," and ignore spaces

            assertEquals(4, parts.length, "Each log entry should have exactly four components");

            // Ensure the third component must be "START" or "END" and validate its format
            assertTrue("START".equals(parts[2]) || "END".equals(parts[2]),
                    "The status must be either 'START' or 'END'");
        }
        reader.close();
    }
}
