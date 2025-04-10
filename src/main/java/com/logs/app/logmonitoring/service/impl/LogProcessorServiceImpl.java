package com.logs.app.logmonitoring.service.impl;

import static com.logs.app.logmonitoring.validation.LogFileValidation.validateEntry;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import com.logs.app.logmonitoring.service.LogProcessorService;
import com.logs.app.logmonitoring.util.ReportStatusEnum;
import com.logs.app.logmonitoring.util.TimeStatusEnum;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;

@Getter
public class LogProcessorServiceImpl implements LogProcessorService {
    private final Map<Integer, List<ProcessJob>> processMap = new HashMap<>();

    public void parseLogs(String filePath) throws IOException, InvalidLogEntryException {
        long startTime = System.currentTimeMillis(); // Start timer
        List<String> logEntries = Files.readAllLines(Paths.get(filePath));

        for (String logEntry : logEntries) {
            validateEntry(logEntry);
            ProcessJob process = createProcessJob(logEntry);
            processMap.computeIfAbsent(process.getPid(), k -> new ArrayList<>()).add(process);
        }

        computeDurationsAndStatuses();
        long endTime = System.currentTimeMillis(); // End timer

        System.out.println("Time taken for sequential processing: " + (endTime - startTime) + " ms");
    }

    public ProcessJob createProcessJob(String logEntry) {
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

    public void computeDurationsAndStatuses() {
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
        ReportGeneratorServiceImpl.generateReport(processMap);
    }
}
