package com.epam.druzhinin.controllers;

import com.epam.druzhinin.dto.MessageDto;
import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductEntity> getProducts(@PageableDefault(size = 4) Pageable pageable) {
        Page<ProductEntity> products = productService.getProducts(pageable);
        return products.toList();
    }

    @PostMapping
    public ProductEntity createProduct(@RequestBody ProductDto productDto) {
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
    public ResponseEntity<MessageDto> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(MessageDto.of("Product is deleted by id=" + id));
    }
}
