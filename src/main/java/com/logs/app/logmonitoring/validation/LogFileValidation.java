package com.logs.app.logmonitoring.validation;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;

public class LogFileValidation {
    public static void validateEntry(String logEntry) throws InvalidLogEntryException {
        String[] parts = logEntry.split(",\\s*");

        if (parts.length != 4) {
            throw new InvalidLogEntryException("Log entry must have exactly four components: " + logEntry);
        }

        InputFormatValidator.validateTimestamp(parts[0]);
        InputFormatValidator.validateJobDescription(parts[1]);
        InputFormatValidator.validateStatus(parts[2]);
        InputFormatValidator.validatePID(parts[3]);
    }
}
