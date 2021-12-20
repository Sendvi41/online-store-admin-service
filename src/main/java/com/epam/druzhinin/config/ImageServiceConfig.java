package com.epam.druzhinin.config;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageServiceConfig {
    @Bean
    public AmazonS3 amazonS3(
            @Value("${s3.image.service.endpoint}") String endpoint,
            @Value("${s3.image.service.region}") String region) {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        endpoint,
                        region
                ))
                .withPathStyleAccessEnabled(true)
                .build();
    }
}
