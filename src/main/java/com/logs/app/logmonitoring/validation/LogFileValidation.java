package com.logs.app.logmonitoring.validation;

import static com.logs.app.logmonitoring.util.Constants.REQUIRED_NUMBER_OF_ENTRIES;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.util.Constants;

public class LogFileValidation {
    public static void validateEntry(String logEntry) throws InvalidLogEntryException {
        String[] parts = logEntry.split(",\\s*");

        if (parts.length != REQUIRED_NUMBER_OF_ENTRIES) {
            throw new InvalidLogEntryException(Constants.REQUIRED_NUMBER_OF_COMPONENTS_MSJ + logEntry);
        }

        InputFormatValidator.validateTimestamp(parts[0]);
        InputFormatValidator.validateJobDescription(parts[1]);
        InputFormatValidator.validateStatus(parts[2]);
        InputFormatValidator.validatePID(parts[3]);
    }
}
