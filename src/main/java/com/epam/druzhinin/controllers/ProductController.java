package com.epam.druzhinin.controllers;

import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.dto.MessageDTO;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<ProductEntity> getProducts() {
        return productService.getProducts();
    }

    @PostMapping
    public ProductEntity createProduct(ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    @GetMapping("/{id}")
    public ProductEntity getProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @PutMapping("/{id}")
    public ProductEntity updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto
    ) {
        return productService.updateProduct(productDto, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(MessageDTO.of("Product is deleted by id" + id));
    }
}
