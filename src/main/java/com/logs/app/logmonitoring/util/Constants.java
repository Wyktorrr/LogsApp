package com.logs.app.logmonitoring.util;

public class Constants {
    public static final String NOT_NULL_JOB_DESCRIPTION_MSJ = "Job description is null";
    public static final String NOT_NULL_TIMESTAMP_MSJ = "Timestamp cannot be empty.";
    public static final String INVALID_TIMESTAMP_FORMAT_MSJ = "Invalid timestamp format: ";
    public static final String JOB_DESCRIPTION_REGEX = "scheduled task \\d{3}|background job [a-z]{3}";
    public static final String JOB_DESCRIPTION_FORMAT_MSJ = "Job description must match the required format: ";
    public static final String REQUIRED_STATUS_MSJ = "Status must be either 'START' or 'END': ";
    public static final String PID_NOT_EMPTY_MSJ = "PID cannot be empty.";
    public static final String PID_FORMAT_MSJ = "PID must be a 5-digit number: ";
    public static final int REQUIRED_NUMBER_OF_ENTRIES = 4;
    public static final String REQUIRED_NUMBER_OF_COMPONENTS_MSJ = "Log entry must have exactly four components: ";
}
