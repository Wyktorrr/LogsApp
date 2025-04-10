package com.logs.app.logmonitoring.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

//Used for generating large amount of logs - for concurrent testing of the app
public class LogGenerator {
    public static void generateLogs() {
        String filePath = "src/main/resources/massive_logs.logs";
        int numberOfProcesses = 1000; // Specify how many unique processes to generate
        Set<Integer> usedPIDs = new HashSet<>(); // Track used PIDs

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            Random random = new Random();
            String[] jobDescriptions = {
                    "scheduled task 001", "scheduled task 002", "scheduled task 003", "background job abc",
                    "background job def", "scheduled task 004", "background job ghi"
            };

            while (usedPIDs.size() < numberOfProcesses) {
                String jobDescription = jobDescriptions[random.nextInt(jobDescriptions.length)];
                int pid;

                // Generate a unique PID
                do {
                    pid = 10000 + random.nextInt(90000); // Generate random PID between 10000 and 99999
                } while (usedPIDs.contains(pid)); // Keep generating until we find a unique PID

                usedPIDs.add(pid); // Add the unique PID to the set

                // Generate a random start time
                LocalTime startTime = LocalTime.of(random.nextInt(24), random.nextInt(60), random.nextInt(60));

                // Write START log entry
                writer.write(String.format("%s, %s, START, %d%n", startTime, jobDescription, pid));

                // Generate an END time which is after the START time
                // Ensure random duration within reasonable limits (1 second to 20 minutes)
                int durationInSeconds = 1 + random.nextInt(1200); // Between 1 second and 20 minutes
                LocalTime endTime = startTime.plusSeconds(durationInSeconds);

                // Write END log entry
                writer.write(String.format("%s, %s, END, %d%n", endTime, jobDescription, pid));
            }

            System.out.println("Log file generated: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing log file: " + e.getMessage());
        }
    }
}
