package com.epam.druzhinin;

import com.epam.druzhinin.repositories.ProductRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContextConfiguration {
    @MockBean
    public ProductRepository productRepository;
}
