package com.logs.app.logmonitoring.service;

import com.logs.app.logmonitoring.exception.InvalidLogEntryException;
import com.logs.app.logmonitoring.model.ProcessJob;
import java.io.IOException;

public interface LogProcessorService {

    void parseLogs(String filePath) throws IOException, InvalidLogEntryException;

    ProcessJob createProcessJob(String logEntry);

    void computeDurationsAndStatuses();

    void generateReport();
}
