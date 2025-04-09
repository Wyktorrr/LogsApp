package com.logs.app.logmonitoring.service;

import static com.logs.app.logmonitoring.validation.LogFileValidation.validateEntry;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Getter
public class LogProcessor {
    Map<Integer, List<ProcessJob>> processMap = Collections.synchronizedMap(new HashMap<>());
    private final ExecutorService executorService = Executors.newFixedThreadPool(4);

    public void parseLogs(String filePath) throws IOException, InvalidLogEntryException {
        long startTime = System.currentTimeMillis();
        List<String> logEntries = Files.readAllLines(Paths.get(filePath));
        List<Future<Void>> futures = new ArrayList<>();

        for (String logEntry : logEntries) {
            futures.add(executorService.submit(() -> {
                validateEntry(logEntry);
                ProcessJob process = createProcessJob(logEntry);
                processMap.computeIfAbsent(process.getPid(), k -> new ArrayList<>()).add(process);
                return null;
            }));
        }

        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new InvalidLogEntryException("Log processing was interrupted.");
        } catch (Exception e) {
            throw new InvalidLogEntryException("Error processing log entry: " + e.getMessage());
        }

        computeDurationsAndStatuses();
        long endTime = System.currentTimeMillis(); // End timer

        System.out.println("Time taken for concurrent processing: " + (endTime - startTime) + " ms");
        executorService.shutdown(); // Shutdown the executor service
    }

    private ProcessJob createProcessJob(String logEntry) {
        String[] parts = logEntry.split(",\\s*");
        LocalTime timestamp = LocalTime.parse(parts[0]);
        String jobDescription = parts[1];
        String status = parts[2];
        int pid = Integer.parseInt(parts[3]);

        return ProcessJob.builder()
                .jobDescription(jobDescription)
                .pid(pid)
                .startTime(status.equals("START") ? timestamp : null)
                .endTime(status.equals("END") ? timestamp : null)
                .duration(null)
                .status(status)
                .build();
    }

    private void computeDurationsAndStatuses() {
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

            // Compute duration if both start and end processes are found
            if (startProcess != null && endProcess != null) {
                Duration duration = Duration.between(startProcess.getStartTime(), endProcess.getEndTime());
                startProcess.setDuration(duration);
                endProcess.setDuration(duration);

                // Set status based on total seconds
                long seconds = duration.getSeconds();
                if (seconds > 600) {  // More than 10 minutes
                    startProcess.setStatus("ERROR");
                    endProcess.setStatus("ERROR");
                } else if (seconds >= 300) { // More than 5 minutes
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
        ReportGenerator.generateReport(processMap);
    }
}
