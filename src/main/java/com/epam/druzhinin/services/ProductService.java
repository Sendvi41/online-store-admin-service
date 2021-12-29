package com.epam.druzhinin.services;

import com.epam.druzhinin.config.RabbitMQConfig;
import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.dto.ProductQueueDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.enums.QueueTitle;
import com.epam.druzhinin.exception.NotFoundException;
import com.epam.druzhinin.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    private final RabbitTemplate rabbitTemplate;

    private final RabbitMQConfig rabbitMQConfig;

    @Autowired
    public ProductService(ModelMapper modelMapper,
                          ProductRepository productRepository,
                          RabbitTemplate rabbitTemplate,
                          RabbitMQConfig rabbitMQConfig) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    public Page<ProductEntity> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public ProductEntity createProduct(ProductDto productDto) {
        log.info("Starting to create the product [productDto={}]", productDto);
        ProductEntity product = modelMapper.map(productDto, ProductEntity.class);
        ProductEntity savedProduct = productRepository.save(product);
        log.info("Product is saved [id={}]", savedProduct.getId());
        sendToQueue(new ProductQueueDto()
                .setQueueTitle(QueueTitle.CREATE)
                .setProductEntity(savedProduct)
        );
        log.info("Product was send to exchange[exchange={}]", rabbitMQConfig.getExchange());
        return savedProduct;
    }

    public ProductEntity findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
    }

    public ProductEntity updateProduct(ProductDto productDto, Long id) {
        log.info("Starting to update the product [id={}]", id);
        productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
        ProductEntity entity = modelMapper.map(productDto, ProductEntity.class);
        entity.setId(id);
        ProductEntity updatedProduct = productRepository.save(entity);
        sendToQueue(new ProductQueueDto()
                .setQueueTitle(QueueTitle.UPDATE)
                .setProductEntity(updatedProduct)
        );
        log.info("Product is updated [id={}]", id);
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        log.info("Starting to delete the product [id={}]", id);
        productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
        productRepository.deleteById(id);
        sendToQueue(new ProductQueueDto()
                .setQueueTitle(QueueTitle.DELETE)
                .setProductEntity(new ProductEntity().setId(id))
        );
        log.info("Product is deleted [id={}]", id);
    }

    private void sendToQueue(ProductQueueDto productQueueDto) {
        rabbitTemplate.convertAndSend(
                rabbitMQConfig.getExchange(),
                rabbitMQConfig.getRoutingKey(),
                productQueueDto);
    }
}