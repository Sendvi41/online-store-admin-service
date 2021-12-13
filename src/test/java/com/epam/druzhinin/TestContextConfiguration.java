package com.epam.druzhinin;

import com.epam.druzhinin.repositories.ProductRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class TestContextConfiguration {
    @MockBean
    private ProductRepository productRepository;
}
