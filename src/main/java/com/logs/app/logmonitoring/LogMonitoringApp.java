package com.logs.app.logmonitoring;

import static com.logs.app.logmonitoring.service.LogGenerator.generateLogs;

import com.logs.app.logmonitoring.service.LogProcessor;

public class LogMonitoringApp {
    public static void main(String[] args) {
        LogProcessor logProcessor = new LogProcessor();

        // Specify the path to desired logs.log file
        //String logFilePath = "src/main/resources/logs.log";
        generateLogs();
        String logFilePath = "src/main/resources/massive_logs.logs";

        try {
            logProcessor.parseLogs(logFilePath);
            logProcessor.generateReport();
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }
}
