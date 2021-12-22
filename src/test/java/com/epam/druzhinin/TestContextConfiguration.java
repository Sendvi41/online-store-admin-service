package com.epam.druzhinin;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.druzhinin.config.RabbitMQConfig;
import com.epam.druzhinin.repositories.ProductRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestContextConfiguration {
    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private RabbitMQConfig rabbitMQConfig;

    @MockBean
    private AmazonS3 amazonS3Client;
}
