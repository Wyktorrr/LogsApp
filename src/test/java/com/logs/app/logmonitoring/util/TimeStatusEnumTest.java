package com.logs.app.logmonitoring.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class TimeStatusEnumTest {
    @ParameterizedTest
    @EnumSource(TimeStatusEnum.class)
    public void testTimeStatusEnum(TimeStatusEnum status) {
        // Check the name of the enum
        if (status == TimeStatusEnum.START) {
            assertEquals("START", status.name());
        } else if (status == TimeStatusEnum.END) {
            assertEquals("END", status.name());
        }
    }
}
