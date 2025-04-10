package com.logs.app.logmonitoring.model;

import com.logs.app.logmonitoring.util.Constants;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProcessJob {
    private final String jobDescription;  // e.g., "scheduled task 001"
    private final int pid;                // e.g., 37980
    private final LocalTime startTime;    // e.g., from timestamp HH:MM:SS
    private final LocalTime endTime;      // e.g., from timestamp HH:MM:SS
    private Duration duration;             // Duration of the process
    private String status;                 // e.g., "COMPLETED", "WARNING", "ERROR"

    public ProcessJob(String jobDescription, int pid, LocalTime startTime,
                      LocalTime endTime, Duration duration, String status) {
        this.jobDescription = Objects.requireNonNull(jobDescription, Constants.NOT_NULL_JOB_DESCRIPTION_MSJ);
        this.pid = pid;
        this.startTime = startTime; // Allowing null for start or end times for flexibility
        this.endTime = endTime;      // Allowing null for start or end times for flexibility
        this.duration = duration;     // Derived from start and end times
        this.status = status;        // Error, Warning, or Completed based on duration
    }
}
