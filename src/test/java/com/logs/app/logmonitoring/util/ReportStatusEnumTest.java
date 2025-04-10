package com.logs.app.logmonitoring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ReportStatusEnumTest {
    @Test
    public void testStatusValues() {
        List<String> expectedStatuses = Arrays.asList("COMPLETED", "ERROR", "WARNING");

        // Iterate through the enum values and check their status
        for (ReportStatusEnum statusEnum : ReportStatusEnum.values()) {
            assertEquals(expectedStatuses.get(statusEnum.ordinal()), statusEnum.name(),
                    "Unexpected status for enum value: " + statusEnum);
        }
    }

    @Test
    public void testEnumValues() {
        ReportStatusEnum[] expectedValues =
                { ReportStatusEnum.COMPLETED, ReportStatusEnum.ERROR, ReportStatusEnum.WARNING };
        assertEquals(expectedValues.length, ReportStatusEnum.values().length);
        for (int i = 0; i < expectedValues.length; i++) {
            assertEquals(expectedValues[i], ReportStatusEnum.values()[i]);
        }
    }
}
