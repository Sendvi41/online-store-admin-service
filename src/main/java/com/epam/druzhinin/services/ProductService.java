package com.epam.druzhinin.services;

import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.exception.NotFoundException;
import com.epam.druzhinin.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {

    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    public ProductEntity createProduct(ProductDto productDto) {
        log.info("Starting to create the product [productDto={}]", productDto);
        ProductEntity product = modelMapper.map(productDto, ProductEntity.class);
        ProductEntity savedProduct = productRepository.save(product);
        log.info("Product is saved [id={}]", savedProduct.getId());
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
        log.info("Product is updated [id={}]", id);
        return updatedProduct;
    }

    public void deleteProduct(Long id) {
        log.info("Starting to delete the product [id={}]", id);
        productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
        productRepository.deleteById(id);
        log.info("Product is deleted [id={}]", id);
    }
}
