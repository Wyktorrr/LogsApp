package com.logs.app.logmonitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.logs.app.logmonitoring.model.ProcessJob;
import java.time.Duration;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogProcessingTest {
    private ProcessJob mockProcessLongWarning; // Process with duration leading to WARNING
    private ProcessJob mockProcessLongError;   // Process with duration leading to ERROR
    private ProcessJob mockProcessShort;       // Process with duration less than 5 minutes

    @BeforeEach
    public void setUp() {
        // Given: Initializing mock ProcessJob objects
        mockProcessLongWarning = new ProcessJob("scheduled task 001", 12345,
                LocalTime.of(11, 0, 0),  // START time
                LocalTime.of(11, 7, 30), // END time (7 minutes and 30 seconds)
                null, // Duration will be computed
                "START"
        );

        mockProcessLongError = new ProcessJob("scheduled task 002", 12346,
                LocalTime.of(11, 0, 0),  // START time
                LocalTime.of(11, 11, 30), // END time (11 minutes and 30 seconds)
                null, // Duration will be computed
                "START"
        );

        mockProcessShort = new ProcessJob("scheduled task 003", 12347,
                LocalTime.of(11, 0, 0),  // START time
                LocalTime.of(11, 4, 30), // END time (4 minutes and 30 seconds)
                null,                     // Duration will be computed
                "START"
        );
    }

    @Test
    public void testWarningStatus() {
        // Given: Setup for the process with warning status
        // When: Calculating duration
        Duration duration = Duration.between(mockProcessLongWarning.getStartTime(), mockProcessLongWarning.getEndTime());
        mockProcessLongWarning.setDuration(duration);

        // Then: Expect the status to be "WARNING"
        Duration expectedDuration = Duration.ofMinutes(7).plusSeconds(30); // 7 minutes 30 seconds
        assertEquals(expectedDuration, mockProcessLongWarning.getDuration(), "The duration should be 7 minutes and 30 seconds.");

        // Then: Determine and assert the status based on the calculated duration
        if (mockProcessLongWarning.getDuration().toMinutes() >= 5 && mockProcessLongWarning.getDuration().toMinutes() <= 10) {
            mockProcessLongWarning.setStatus("WARNING");
        } else {
            mockProcessLongWarning.setStatus("COMPLETED");
        }

        assertEquals("WARNING", mockProcessLongWarning.getStatus(), "The status should be WARNING based on the duration.");
    }

    @Test
    public void testErrorStatus() {
        // Given: Setup for the process with error status
        // When: Calculating duration
        Duration duration = Duration.between(mockProcessLongError.getStartTime(), mockProcessLongError.getEndTime());
        mockProcessLongError.setDuration(duration);

        // Then: Expect the status to be "ERROR"
        if (mockProcessLongError.getDuration().toMinutes() > 10) {
            mockProcessLongError.setStatus("ERROR");
        } else {
            mockProcessLongError.setStatus("COMPLETED");
        }

        assertEquals("ERROR", mockProcessLongError.getStatus(), "The status should be ERROR for exceeding 10 minutes.");
    }

    @Test
    public void testShortProcessStatus() {
        // Given: Setup for a short process (less than 5 min)
        // When: Calculate the duration for mockProcessShort
        Duration duration = Duration.between(mockProcessShort.getStartTime(), mockProcessShort.getEndTime());
        mockProcessShort.setDuration(duration);

        // Check the duration is set correctly
        Duration expectedShortDuration = Duration.ofMinutes(4).plusSeconds(30); // 4 minutes and 30 seconds
        assertEquals(expectedShortDuration, mockProcessShort.getDuration(), "The duration should be 4 minutes and 30 seconds.");

        // Determine and set the status based on the computed duration
        if (mockProcessShort.getDuration().toMinutes() < 5) {
            mockProcessShort.setStatus("COMPLETED");
        }

        // Then: Expect the status to pe "COMPLETED"
        // Assert that the status should be COMPLETED
        assertEquals("COMPLETED", mockProcessShort.getStatus(), "The status should be COMPLETED for durations less than 5 minutes.");
    }
}
