package com.logs.app.logmonitoring.service;

import com.logs.app.logmonitoring.model.ProcessJob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogProcessingTest {
    private ProcessJob mockProcessLongWarning;
    private ProcessJob mockProcessLongError;

    @BeforeEach
    public void setUp() {
        // Create mock ProcessJob objects with appropriate durations
        mockProcessLongWarning = new ProcessJob("scheduled task 001", 12345,
                LocalTime.of(11, 0, 0), // 11:00:00
                LocalTime.of(11, 7, 30), // 11:07:30 (7 minutes + 30 seconds)
                null,  // Duration is to be calculated
                "START"
        );

        mockProcessLongError = new ProcessJob("scheduled task 002", 12346,
                LocalTime.of(11, 0, 0), // 11:00:00
                LocalTime.of(11, 11, 30), // 11:11:30 (11 minutes + 30 seconds)
                null,  // Duration is to be calculated
                "START"
        );
    }

    @Test
    public void testWarningStatus() {
        // Calculate the duration for mockProcessLongWarning
        Duration duration = Duration.between(mockProcessLongWarning.getStartTime(), mockProcessLongWarning.getEndTime());
        mockProcessLongWarning.setDuration(duration);

        // Check the duration is set correctly
        Duration expectedDuration = Duration.ofMinutes(7).plusSeconds(30);
        assertEquals(expectedDuration, mockProcessLongWarning.getDuration(), "The duration should be 5 minutes and 10 seconds.");

        // Determine and set the status based on the computed duration
        if (mockProcessLongWarning.getDuration().toMinutes() > 5 && mockProcessLongWarning.getDuration().toMinutes() <= 10) {
            mockProcessLongWarning.setStatus("WARNING");
        } else {
            mockProcessLongWarning.setStatus("COMPLETED");
        }

        // Assert that the status is set correctly
        assertEquals("WARNING", mockProcessLongWarning.getStatus(), "The status should be WARNING based on the duration.");
    }

    @Test
    public void testErrorStatus() {
        // Calculate the duration for mockProcessLongError
        Duration duration = Duration.between(mockProcessLongError.getStartTime(), mockProcessLongError.getEndTime());
        mockProcessLongError.setDuration(duration);

        // Set status based on duration for determining error
        if (mockProcessLongError.getDuration().toMinutes() > 10) {
            mockProcessLongError.setStatus("ERROR");
        } else {
            mockProcessLongError.setStatus("COMPLETED");
        }

        // Assert that the status has been set correctly
        assertEquals("ERROR", mockProcessLongError.getStatus(), "The status should be ERROR for exceeding 10 minutes.");
    }
}