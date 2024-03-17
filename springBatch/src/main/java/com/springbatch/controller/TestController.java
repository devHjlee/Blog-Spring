package com.springbatch.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequiredArgsConstructor
public class TestController {
    private final JobLauncher jobLauncher;
    private final JobRegistry jobRegistry;

    @RequestMapping("/test")
    public String handle(@RequestParam String jobName) throws Exception {
        Job processJob = jobRegistry.getJob(jobName);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        jobLauncher.run(processJob, jobParameters);

        return "Batch job has been invoked";
    }
}
