package com.logs.app.logmonitoring.service;

import static com.logs.app.logmonitoring.validation.LogFileValidation.validateEntry;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import com.logs.app.logmonitoring.util.ReportStatusEnum;
import com.logs.app.logmonitoring.util.TimeStatusEnum;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

@Getter
public class LogProcessor {
    private final Map<Integer, List<ProcessJob>> processMap = new ConcurrentHashMap<>();

    public void parseLogs(String filePath) throws IOException {
        long startTime = System.currentTimeMillis(); // Start timer
        List<String> logEntries = Files.readAllLines(Paths.get(filePath));

        // Create a ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        // Execute the ForkJoinTask
        forkJoinPool.invoke(new LogEntryTask(logEntries));

        computeDurationsAndStatuses();
        long endTime = System.currentTimeMillis(); // End timer

        System.out.println("Time taken for ForkJoin processing: " + (endTime - startTime) + " ms");
    }

    private class LogEntryTask extends RecursiveTask<Void> {
        private static final int THRESHOLD = 100; // Define a threshold
        private final List<String> logEntries;

        public LogEntryTask(List<String> logEntries) {
            this.logEntries = logEntries;
        }

        @Override
        protected Void compute() {
            if (logEntries.size() <= THRESHOLD) {
                // Process directly if below threshold
                logEntries.forEach(logEntry -> {
                    try {
                        validateEntry(logEntry);
                        ProcessJob process = createProcessJob(logEntry);
                        processMap.computeIfAbsent(process.getPid(), k -> Collections.synchronizedList(new ArrayList<>())).add(process);
                    } catch (InvalidLogEntryException e) {
                        // Handle the exception according to your application's needs
                        e.printStackTrace();
                    }
                });
            } else {
                // Split the task into two subtasks
                int mid = logEntries.size() / 2;
                LogEntryTask leftTask = new LogEntryTask(logEntries.subList(0, mid));
                LogEntryTask rightTask = new LogEntryTask(logEntries.subList(mid, logEntries.size()));

                // Fork the left task and compute the right task in the current thread
                leftTask.fork();
                rightTask.compute();

                // Join the left task
                leftTask.join();
            }
            return null;
        }
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
                if (TimeStatusEnum.START.name().equals(process.getStatus())) {
                    startProcess = process;
                } else if (TimeStatusEnum.END.name().equals(process.getStatus())) {
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
                    startProcess.setStatus(ReportStatusEnum.ERROR.name());
                    endProcess.setStatus(ReportStatusEnum.ERROR.name());
                } else if (seconds >= 300) { // More than 5 minutes
                    startProcess.setStatus(ReportStatusEnum.WARNING.name());
                    endProcess.setStatus(ReportStatusEnum.WARNING.name());
                } else {
                    startProcess.setStatus(ReportStatusEnum.COMPLETED.name());
                    endProcess.setStatus(ReportStatusEnum.COMPLETED.name());
                }
            }
        }
    }

    public void generateReport() {
        ReportGenerator.generateReport(processMap);
    }
}
