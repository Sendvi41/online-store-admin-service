package com.epam.druzhinin.services;

import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.exception.NotFoundException;
import com.epam.druzhinin.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class ProductService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductEntity> getProducts() {
        return productRepository.findAll();
    }

    public ProductEntity createProduct(ProductDto productDto) {
        ProductEntity goods = modelMapper.map(productDto, ProductEntity.class);
        return productRepository.save(goods);
    }

    public ProductEntity findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
    }

    public ProductEntity updateProduct(ProductDto productDto, Long id) {
        productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + id)
        );
        ProductEntity entity = modelMapper.map(productDto, ProductEntity.class);
        entity.setId(id);
        ProductEntity updatedGoods = productRepository.save(entity);
        log.info("Product is updated [id={}]", id);
        return updatedGoods;
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Product is not found" + id)
        );
        productRepository.deleteById(id);
    }
}
