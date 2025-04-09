package com.logs.app.logmonitoring.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.logs.app.logmonitoring.model.ProcessJob;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ReportGeneratorTest {

    @Test
    void testGenerateReport() {
        // Given
        Map<Integer, List<ProcessJob>> processMap = new HashMap<>(); // Use LinkedHashMap

        ProcessJob job1 = ProcessJob.builder()
                .jobDescription("test task 1")
                .pid(12345)
                .startTime(LocalTime.of(10, 0, 10))
                .endTime(LocalTime.of(10, 0, 30))
                .duration(Duration.ofSeconds(30))
                .status("COMPLETED")
                .build();

        ProcessJob job2 = ProcessJob.builder()
                .jobDescription("test task 2")
                .pid(67890)
                .startTime(LocalTime.of(10, 1, 10))
                .endTime(LocalTime.of(10, 2, 10))
                .duration(Duration.ofMinutes(1))
                .status("COMPLETED")
                .build();

        processMap.put(12345, Collections.singletonList(job1)); // Adding job1 first
        processMap.put(67890, Collections.singletonList(job2)); // Adding job2 second

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        // When
        ReportGenerator.generateReport(processMap);
        System.setOut(originalOut);

        // Expected output
        String expectedOutput = """
                Job Report
                ---------------------------------------------------------
                | Job Description            | PID    | Start Time | End Time  | Duration | Status    |
                ---------------------------------------------------------
                | test task 2               | 67890  | 10:01:10  | 10:02:10  | PT1M    | COMPLETED |
                | test task 1               | 12345  | 10:00:10  | 10:00:30  | PT30S   | COMPLETED |
                ---------------------------------------------------------
                Total Jobs Processed: 2""";

        // Then
        assertEquals(expectedOutput.trim().replaceAll("\\s+", " "),
                outputStream.toString().trim().replaceAll("\\s+", " "));
    }
}
