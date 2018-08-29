package com.moka.job.work.test;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.moka.job.base.BasePageItemReader;
import com.moka.job.config.batch.JobLauncherDetails;
import com.moka.job.utils.MapperUtils;
import com.moka.job.work.test.mapper.TestDataMapper;
import com.moka.job.work.test.model.po.TestData;
import com.moka.job.work.test.processer.TestItemProcesser;
import com.moka.job.work.test.writer.TestItemWriter;
@Configuration
public class TestJob extends JobLauncherDetails{
	private static final String testDataMapperNamespace = TestDataMapper.class.getName();
	@Bean("testbReader")
    public ItemReader<TestData> reader() {
		BasePageItemReader reader = new BasePageItemReader();
		reader.setSqlSessionFactory(sqlSessionFactory());
		reader.setQueryId(MapperUtils.statement(testDataMapperNamespace, "selectAll"));
        return reader;
    }

	@Bean("testProcessor")
    public ItemProcessor<TestData, TestData> processor() {
		
        return new TestItemProcesser();
    }

    @Bean("testWriter")
    public ItemWriter<TestData> writer() {
    	TestItemWriter writer = new TestItemWriter();
        writer.setSqlSessionFactory(sqlSessionFactory());
        writer.setAssertUpdates(false);
        return writer;
    }
    
    @Bean("testbatchJob")
    public Job job() {
        return jobBuilderFactory.get(jobName())
                .incrementer(new RunIdIncrementer())
                .flow(step1())
                .end()
                .build();

    }
    @Bean("testStep1")
    public Step step1() {
        return stepBuilderFactory.get(stepName(1))
                .<TestData, TestData> chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }
}
