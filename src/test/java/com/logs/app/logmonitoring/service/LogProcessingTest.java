package com.logs.app.logmonitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.logs.app.logmonitoring.model.ProcessJob;
import com.logs.app.logmonitoring.util.ReportStatusEnum;
import com.logs.app.logmonitoring.util.TimeStatusEnum;
import java.time.Duration;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LogProcessingTest {
    private ProcessJob mockProcessLongWarning;
    private ProcessJob mockProcessLongError;
    private ProcessJob mockProcessShort;

    @BeforeEach
    public void setUp() {
        mockProcessLongWarning = new ProcessJob("scheduled task 001", 12345,
                LocalTime.of(11, 0, 0),
                LocalTime.of(11, 7, 30),
                null,
                TimeStatusEnum.START.name()
        );

        mockProcessLongError = new ProcessJob("scheduled task 002", 12346,
                LocalTime.of(11, 0, 0),
                LocalTime.of(11, 11, 30),
                null,
                TimeStatusEnum.START.name()
        );

        mockProcessShort = new ProcessJob("scheduled task 003", 12347,
                LocalTime.of(11, 0, 0),
                LocalTime.of(11, 4, 30),
                null,
                TimeStatusEnum.START.name()
        );
    }

    @Test
    public void testWarningStatus() {
        Duration duration = Duration.between(
                mockProcessLongWarning.getStartTime(), mockProcessLongWarning.getEndTime());
        mockProcessLongWarning.setDuration(duration);

        Duration expectedDuration = Duration.ofMinutes(7).plusSeconds(30);
        assertEquals(expectedDuration, mockProcessLongWarning.getDuration(),
                "The duration should be 7 minutes and 30 seconds.");

        if (mockProcessLongWarning.getDuration().toMinutes() >= 5
                && mockProcessLongWarning.getDuration().toMinutes() <= 10) {
            mockProcessLongWarning.setStatus(ReportStatusEnum.WARNING.name());
        } else {
            mockProcessLongWarning.setStatus(ReportStatusEnum.COMPLETED.name());
        }

        assertEquals(ReportStatusEnum.WARNING.name(), mockProcessLongWarning.getStatus(),
                "The status should be WARNING based on the duration.");
    }

    @Test
    public void testErrorStatus() {
        Duration duration = Duration.between(mockProcessLongError.getStartTime(), mockProcessLongError.getEndTime());
        mockProcessLongError.setDuration(duration);

        if (mockProcessLongError.getDuration().toMinutes() > 10) {
            mockProcessLongError.setStatus(ReportStatusEnum.ERROR.name());
        } else {
            mockProcessLongError.setStatus(ReportStatusEnum.COMPLETED.name());
        }

        assertEquals(ReportStatusEnum.ERROR.name(), mockProcessLongError.getStatus(),
                "The status should be ERROR for exceeding 10 minutes.");
    }

    @Test
    public void testShortProcessStatus() {
        Duration duration = Duration.between(mockProcessShort.getStartTime(), mockProcessShort.getEndTime());
        mockProcessShort.setDuration(duration);

        Duration expectedShortDuration = Duration.ofMinutes(4).plusSeconds(30);
        assertEquals(expectedShortDuration, mockProcessShort.getDuration(),
                "The duration should be 4 minutes and 30 seconds.");

        if (mockProcessShort.getDuration().toMinutes() < 5) {
            mockProcessShort.setStatus(ReportStatusEnum.COMPLETED.name());
        }

        assertEquals(ReportStatusEnum.COMPLETED.name(), mockProcessShort.getStatus(),
                "The status should be COMPLETED for durations less than 5 minutes.");
    }
}
