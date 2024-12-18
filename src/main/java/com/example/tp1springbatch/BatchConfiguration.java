package com.example.tp1springbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import javax.xml.crypto.Data;

@Configuration
public class BatchConfiguration {
    @Bean
    public FlatFileItemReader<Product> reader(){
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(new ClassPathResource("product.csv"))
                .delimited()
                .names("name","price")
                .linesToSkip(1)
                .targetType(Product.class)
                .build();
    }
    @Bean
    public JdbcBatchItemWriter<Product> writer(DataSource dataSource){
        return new JdbcBatchItemWriterBuilder<Product>()
                .sql("INSERT INTO products(name,price) values (:name,:price)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }
    @Bean
    public ProductItemProcessor processor(){
        return new ProductItemProcessor();
    }
    @Bean
    public Step step1(JobRepository jobRepository, DataSourceTransactionManager transactionManager,FlatFileItemReader<Product> reader , ProductItemProcessor processor, JdbcBatchItemWriter<Product> writer){
        return  new StepBuilder("step1",jobRepository)
                .<Product,Product> chunk(3,transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importProductJob(JobRepository jobRepository , Step step1 , JobCompleteNotificationListener notificationListener){
        return  new JobBuilder("importProductJob",jobRepository)
                .listener(notificationListener)
                .start(step1)
                .build();
    }
}
