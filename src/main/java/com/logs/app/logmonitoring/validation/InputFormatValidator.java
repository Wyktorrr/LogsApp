package com.logs.app.logmonitoring.validation;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.util.Constants;
import com.logs.app.logmonitoring.util.TimeStatusEnum;

import java.time.LocalTime;

public class InputFormatValidator {
    public static void validateTimestamp(String timestamp) throws InvalidLogEntryException {
        if (timestamp.trim().isEmpty()) {
            throw new InvalidLogEntryException(Constants.NOT_NULL_TIMESTAMP_MSJ);
        }
        try {
            LocalTime.parse(timestamp);
        } catch (Exception e) {
            throw new InvalidLogEntryException(Constants.INVALID_TIMESTAMP_FORMAT_MSJ + timestamp);
        }
    }

    public static void validateJobDescription(String jobDescription) throws InvalidLogEntryException {
        if (!jobDescription.matches(Constants.JOB_DESCRIPTION_REGEX)) {
            throw new InvalidLogEntryException(Constants.JOB_DESCRIPTION_FORMAT_MSJ + jobDescription);
        }
    }

    public static void validateStatus(String status) throws InvalidLogEntryException {
        if (!TimeStatusEnum.START.name().equals(status) && !TimeStatusEnum.END.name().equals(status)) {
            throw new InvalidLogEntryException(Constants.REQUIRED_STATUS_MSJ + status);
        }
    }

    public static void validatePID(String pidStr) throws InvalidLogEntryException {
        if (pidStr.trim().isEmpty()) {
            throw new InvalidLogEntryException(Constants.PID_NOT_EMPTY_MSJ);
        }
        try {
            int pid = Integer.parseInt(pidStr);
            if (String.valueOf(pid).length() != 5) {
                throw new InvalidLogEntryException(Constants.PID_FORMAT_MSJ + pidStr);
            }
        } catch (NumberFormatException e) {
            throw new InvalidLogEntryException(Constants.PID_FORMAT_MSJ + pidStr);
        }
    }
}
