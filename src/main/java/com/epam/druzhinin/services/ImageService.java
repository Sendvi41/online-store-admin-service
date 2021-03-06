package com.epam.druzhinin.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.epam.druzhinin.dto.MessageDto;
import com.epam.druzhinin.exception.InternalServerException;
import com.epam.druzhinin.exception.NotFoundException;
import com.epam.druzhinin.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@Slf4j
public class ImageService {
    @Value("${s3.image.service.name.bucket}")
    private String nameImageBucket;

    private final AmazonS3 amazonS3Client;

    private final ProductRepository productRepository;

    @Autowired
    public ImageService(AmazonS3 amazonS3Client, ProductRepository productRepository) {
        this.amazonS3Client = amazonS3Client;
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void init() {
        if (!amazonS3Client.doesBucketExistV2(nameImageBucket)) {
            amazonS3Client.createBucket(nameImageBucket);
        }
    }

    public MessageDto uploadProductImage(Long productId, MultipartFile image) {
        log.info("Starting to save the product image [productId={}]", productId);
        productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("Product is not found id=" + productId)
        );
        try {
            File file = convertMultiPartToFile(image);
            uploadFileTos3bucket(productId, file);
            file.delete();
        } catch (Exception e) {
            log.error("The product image couldn't be upload [ex = {}]", e.getMessage());
            throw new InternalServerException("The product image couldn't be upload productId=" + productId);
        }
        log.info("Product image was uploaded successfully [productId={}]", productId);
        return MessageDto.of("Product image was uploaded successfully productId=" + productId);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private void uploadFileTos3bucket(Long productId, File file) {
        amazonS3Client.putObject(new PutObjectRequest(nameImageBucket, productId.toString(), file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }
}
