package com.logs.app.logmonitoring.service;

import com.logs.app.logmonitoring.model.ProcessJob;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ReportGenerator {
    public static void generateReport(Map<Integer, List<ProcessJob>> processMap) {
        System.out.println("Job Report");
        System.out.println("---------------------------------------------------------");
        System.out.println("| Job Description            | PID    | Start Time | End Time  | Duration | Status    |");
        System.out.println("---------------------------------------------------------");

        for (List<ProcessJob> processes : processMap.values()) {
            for (ProcessJob process : processes) {
                System.out.printf("| %-24s | %-6d | %-10s | %-10s | %-8s | %-9s |%n",
                        process.getJobDescription(),
                        process.getPid(),
                        process.getStartTime() != null ? process.getStartTime().toString() : "N/A",
                        process.getEndTime() != null ? process.getEndTime().toString() : "N/A",
                        process.getDuration() != null ? process.getDuration().toString() : Duration.ZERO,
                        process.getStatus());
            }
        }
        System.out.println("---------------------------------------------------------");
        System.out.println("Total Jobs Processed: " + processMap.size());
    }
}
