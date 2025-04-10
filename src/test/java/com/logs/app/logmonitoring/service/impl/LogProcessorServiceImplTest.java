package com.logs.app.logmonitoring.service.impl;

import static com.logs.app.logmonitoring.validation.LogFileValidation.validateEntry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class LogProcessorServiceImplTest {

    private static Object[][] provideLogDurations() {
        return new Object[][]{
                {"10003", "COMPLETED"}, // Duration is less than 10 minutes
                {"10004", "COMPLETED"}, // Duration is less than 10 minutes
                {"10005", "ERROR"}, // Duration takes longer than 10 minutes
                {"62401", "ERROR"}, // Duration takes longer than 10 minutes
                {"87570", "WARNING"} // Duration between 5 and 10 minutes

        };
    }

    private LogProcessorServiceImpl logProcessor;
    String filePath = "src/test/resources/logs.log";

    @BeforeEach
    void setUp() {
        logProcessor = new LogProcessorServiceImpl();
    }

    @Test
    void testParseLogsValidLogEntries() throws IOException, InvalidLogEntryException {
        // Given
        // When
        logProcessor.parseLogs(filePath);

        // Then
        assertFalse(logProcessor.getProcessMap().isEmpty());
        assertEquals(43, logProcessor.getProcessMap().size());
    }

    @Test
    void testInvalidLogEntryValidation() {
        // Given
        String invalidLogEntry = "invalid log entry format";

        // When
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class, () -> {
            validateEntry(invalidLogEntry); // Ensure validateEntry is available for testing
        });

        // Then
        assertNotNull(exception);
        assertEquals("Log entry must have exactly four components: invalid log entry format",
                exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideLogDurations")
    void testComputeDurationsAndStatuses(String pid, String expectedStatus)
            throws IOException, InvalidLogEntryException {
        // Given
        String filePath = "src/test/resources/test_logs.log";

        // When
        logProcessor.parseLogs(filePath);
        List<ProcessJob> jobs = logProcessor.getProcessMap().get(Integer.parseInt(pid));

        assertNotNull(jobs);
        assertFalse(jobs.isEmpty());

        // Then
        assertEquals(expectedStatus, jobs.getLast().getStatus());
    }

    @Test
    void testParseLogsWithNoEntries() throws IOException, InvalidLogEntryException {
        // Given
        String emptyFilePath = "src/test/resources/empty_logs.log";

        // When
        logProcessor.parseLogs(emptyFilePath);

        // Then
        assertTrue(logProcessor.getProcessMap().isEmpty());
    }
}
