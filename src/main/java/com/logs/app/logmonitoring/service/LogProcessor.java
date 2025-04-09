package com.logs.app.logmonitoring.service;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class LogProcessor {
    private final Map<Integer, List<ProcessJob>> processMap = new HashMap<>();

    public void parseLogs(String filePath) throws IOException, InvalidLogEntryException {
        List<String> logEntries = Files.readAllLines(Paths.get(filePath));

        // First iteration: Validate and store entries
        for (String logEntry : logEntries) {
            if (logEntry.trim().isEmpty()) {
                throw new InvalidLogEntryException("Log entry cannot be empty or whitespace.");
            }

            String[] parts = logEntry.split(",\\s*"); // Uses regex to split by comma and whitespace

            if (parts.length != 4) {
                throw new InvalidLogEntryException("Log entry must have exactly four components: " + logEntry);
            }

            LocalTime timestamp;
            try {
                timestamp = LocalTime.parse(parts[0]);
            } catch (Exception e) {
                throw new InvalidLogEntryException("Invalid timestamp format: " + parts[0] + ". Entry: " + logEntry);
            }

            String jobDescription = parts[1];
            String status = parts[2];
            int pid;

            try {
                pid = Integer.parseInt(parts[3]);
            } catch (NumberFormatException e) {
                throw new InvalidLogEntryException("PID must be a 5-digit number: " + parts[3]);
            }

            ProcessJob process = ProcessJob.builder()
                    .jobDescription(jobDescription)
                    .pid(pid)
                    .startTime(status.equals("START") ? timestamp : null)
                    .endTime(status.equals("END") ? timestamp : null)
                    .duration(null)  // Initially set to null
                    .status(status)
                    .build();

            processMap.computeIfAbsent(pid, k -> new ArrayList<>()).add(process);
        }

        // Second iteration: Compute durations and determine statuses
        for (List<ProcessJob> processes : processMap.values()) {
            ProcessJob startProcess = null;
            ProcessJob endProcess = null;

            for (ProcessJob process : processes) {
                if ("START".equals(process.getStatus())) {
                    startProcess = process;
                } else if ("END".equals(process.getStatus())) {
                    endProcess = process;
                }
            }

            // Compute duration only if both start and end processes are found
            if (startProcess != null && endProcess != null) {
                Duration duration = Duration.between(startProcess.getStartTime(), endProcess.getEndTime());
                startProcess.setDuration(duration);
                endProcess.setDuration(duration);

                long seconds = duration.getSeconds(); // Get the total duration in seconds

                // Status determination logic based on total seconds
                if (seconds > 600) {  // More than 10 minutes
                    startProcess.setStatus("ERROR");
                    endProcess.setStatus("ERROR");
                } else if (seconds > 300) { // More than 5 minutes
                    startProcess.setStatus("WARNING");
                    endProcess.setStatus("WARNING");
                } else {
                    startProcess.setStatus("COMPLETED");
                    endProcess.setStatus("COMPLETED");
                }
            }
        }
    }

    public void generateReport() {
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
