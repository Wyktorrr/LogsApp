package com.logs.app.logmonitoring.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.util.Constants;
import com.logs.app.logmonitoring.util.TimeStatusEnum;
import org.junit.jupiter.api.Test;

class InputFormatValidatorTest {
    @Test
    public void testValidateTimestampValidTimestamp() throws InvalidLogEntryException {
        InputFormatValidator.validateTimestamp("11:35:23");
    }

    @Test
    public void testValidateTimestampEmptyTimestamp() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validateTimestamp(""));
        assertEquals("Timestamp cannot be empty.", exception.getMessage());
    }

    @Test
    public void testValidateTimestampInvalidFormat() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validateTimestamp("invalid_timestamp"));
        assertEquals(Constants.INVALID_TIMESTAMP_FORMAT_MSJ + "invalid_timestamp", exception.getMessage());
    }

    @Test
    public void testValidateJobDescriptionValidJobDescription() throws InvalidLogEntryException {
        InputFormatValidator.validateJobDescription("scheduled task 032");
        InputFormatValidator.validateJobDescription("background job abc");
    }

    @Test
    public void testValidateJobDescriptionInvalidJobDescription() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validateJobDescription("invalid job description"));
        assertEquals(Constants.JOB_DESCRIPTION_FORMAT_MSJ + "invalid job description", exception.getMessage());
    }

    @Test
    public void testValidateStatusValidStatus() throws InvalidLogEntryException {
        InputFormatValidator.validateStatus(TimeStatusEnum.START.name());
        InputFormatValidator.validateStatus(TimeStatusEnum.END.name());
    }

    @Test
    public void testValidateStatusInvalidStatus() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validateStatus("INVALID"));
        assertEquals(Constants.REQUIRED_STATUS_MSJ + "INVALID", exception.getMessage());
    }

    @Test
    public void testValidatePidValidPid() throws InvalidLogEntryException {
        InputFormatValidator.validatePid("12345");
    }

    @Test
    public void testValidatePidEmptyPid() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validatePid(""));
        assertEquals(Constants.PID_NOT_EMPTY_MSJ, exception.getMessage());
    }

    @Test
    public void testValidatePidInvalidFormat() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validatePid("abc"));
        assertEquals(Constants.PID_FORMAT_MSJ + "abc", exception.getMessage());
    }

    @Test
    public void testValidatePidNot5Digit() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validatePid("1234"));
        assertEquals(Constants.PID_FORMAT_MSJ + "1234", exception.getMessage());
    }

    @Test
    public void testValidatePidTooLongPid() {
        InvalidLogEntryException exception = assertThrows(InvalidLogEntryException.class,
                () -> InputFormatValidator.validatePid("123456"));
        assertEquals(Constants.PID_FORMAT_MSJ + "123456", exception.getMessage());
    }
}
