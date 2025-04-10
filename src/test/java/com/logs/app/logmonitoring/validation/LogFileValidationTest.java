package com.logs.app.logmonitoring.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import org.junit.jupiter.api.Test;

class LogFileValidationTest {
    @Test
    public void givenValidLogEntry_whenValidating_thenNoExceptionIsThrown() throws InvalidLogEntryException {
        String logEntry = "11:35:23, scheduled task 032, START, 37980";
        LogFileValidation.validateEntry(logEntry);
    }

    @Test
    public void givenLogEntryWithIncorrectComponentCount_whenValidating_thenExceptionIsThrown() {
        String logEntry = "11:35:23, scheduled task 032, START";
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> LogFileValidation.validateEntry(logEntry));
        assertEquals("Log entry must have exactly four components: 11:35:23, scheduled task 032, START",
                exception.getMessage());
    }

    @Test
    public void givenLogEntryWithInvalidTimestamp_whenValidating_thenExceptionIsThrown() {
        String logEntry = "invalid_timestamp, scheduled task 032, START, 37980";
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> LogFileValidation.validateEntry(logEntry));
        assertEquals("Invalid timestamp format: invalid_timestamp", exception.getMessage());
    }

    @Test
    public void givenLogEntryWithInvalidJobDescription_whenValidating_thenExceptionIsThrown() {
        String logEntry = "11:35:23, invalid job description, START, 37980";
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> LogFileValidation.validateEntry(logEntry));
        assertEquals("Job description must match the required format: invalid job description", exception.getMessage());
    }

    @Test
    public void givenLogEntryWithInvalidStatus_whenValidating_thenExceptionIsThrown() {
        String logEntry = "11:35:23, scheduled task 032, INVALID_STATUS, 37980";
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> LogFileValidation.validateEntry(logEntry));
        assertEquals("Status must be either 'START' or 'END': INVALID_STATUS", exception.getMessage());
    }

    @Test
    public void givenLogEntryWithInvalidPid_whenValidating_thenExceptionIsThrown() {
        String logEntry = "11:35:23, scheduled task 032, START, invalidPID";
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> LogFileValidation.validateEntry(logEntry));
        assertEquals("PID must be a 5-digit number: invalidPID", exception.getMessage());
    }
}
