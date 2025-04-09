package com.logs.app.logmonitoring.validation;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;

import java.time.LocalTime;

public class InputFormatValidator {
    public static void validateTimestamp(String timestamp) throws InvalidLogEntryException {
        if (timestamp.trim().isEmpty()) {
            throw new InvalidLogEntryException("Timestamp cannot be empty.");
        }
        try {
            LocalTime.parse(timestamp);
        } catch (Exception e) {
            throw new InvalidLogEntryException("Invalid timestamp format: " + timestamp);
        }
    }

    public static void validateJobDescription(String jobDescription) throws InvalidLogEntryException {
        if (!jobDescription.matches("scheduled task \\d{3}|background job [a-z]{3}")) {
            throw new InvalidLogEntryException("Job description must match the required format: " + jobDescription);
        }
    }

    public static void validateStatus(String status) throws InvalidLogEntryException {
        if (!"START".equals(status) && !"END".equals(status)) {
            throw new InvalidLogEntryException("Status must be either 'START' or 'END': " + status);
        }
    }

    public static void validatePID(String pidStr) throws InvalidLogEntryException {
        if (pidStr.trim().isEmpty()) {
            throw new InvalidLogEntryException("PID cannot be empty.");
        }
        try {
            int pid = Integer.parseInt(pidStr);
            if (String.valueOf(pid).length() != 5) {
                throw new InvalidLogEntryException("PID must be a 5-digit number: " + pidStr);
            }
        } catch (NumberFormatException e) {
            throw new InvalidLogEntryException("PID must be a 5-digit number: " + pidStr);
        }
    }
}
