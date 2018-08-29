package com.moka.job.config.batch;


public interface JobStepNameProvider {

    String jobName();

    String stepName(int n);
}
