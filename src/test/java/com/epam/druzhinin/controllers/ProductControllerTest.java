package com.epam.druzhinin.controllers;

import com.epam.druzhinin.dto.MessageDto;
import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestMethod;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class ProductControllerTest {

    private static final String PRODUCT_END_POINT = "/products";

    private static final String ZONE_ID = "Europe/Moscow";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Container
    public GenericContainer postgres = new GenericContainer(DockerImageName.parse("postgres:13.4"))
            .withExposedPorts(5432)
            .withEnv("POSTGRES_PASSWORD", "password")
            .withEnv("POSTGRES_USER", "postgres");

    @BeforeTestMethod
    void setUp() {
        List<ProductEntity> listProductEntity = getListProductEntity();
        listProductEntity.forEach(entity -> productRepository.save(entity));
    }

    @AfterTestMethod
    void clearDb() {
        productRepository.deleteAll();
    }

    @Test
    void getProducts_shouldReturn200OkAndListProductEntity() throws Exception {
        //when
        String result = mockMvc.perform(get(PRODUCT_END_POINT))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        assertThat(result).isNotBlank();
    }

    @Test
    void createProduct_shouldReturn200OkAndProductEntity() throws Exception {
        //given
        ProductDto productDto = getValidProductDto();
        //when
        String result = mockMvc.perform(post(PRODUCT_END_POINT)
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ProductEntity actualEntity = objectMapper.readerFor(ProductEntity.class).readValue(result);

        assertThat(result).isNotBlank();
        assertThat(productDto.getPrice()).isEqualTo(actualEntity.getPrice());
        assertThat(productDto.getAmount()).isEqualTo(actualEntity.getAmount());
        assertThat(productDto.getCategory()).isEqualTo(actualEntity.getCategory());
        assertThat(productDto.getName()).isEqualTo(actualEntity.getName());
    }

    @Test
    void getProductById_shouldReturn200OkAndProductEntity() throws Exception {
        //given
        ProductEntity expectedEntity = productRepository.save(prepareProductEntity(it -> it.setName("stool")));
        //when
        String result = mockMvc.perform(get(PRODUCT_END_POINT + "/{id}", expectedEntity.getId()))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ProductEntity actualEntity = objectMapper.readerFor(ProductEntity.class).readValue(result);

        assertThat(result).isNotBlank();
        assertThat(expectedEntity.getId()).isEqualTo(actualEntity.getId());
        assertThat(expectedEntity.getPrice()).isEqualTo(actualEntity.getPrice());
        assertThat(expectedEntity.getAmount()).isEqualTo(actualEntity.getAmount());
        assertThat(expectedEntity.getCategory()).isEqualTo(actualEntity.getCategory());
        assertThat(expectedEntity.getName()).isEqualTo(actualEntity.getName());
    }

    @Test
    void updateProduct_shouldReturn200OkAndProductEntity() throws Exception {
        //given
        Long expectedId = productRepository.save(prepareProductEntity(it -> it.setName("stool"))).getId();
        ProductDto productDto = getValidProductDto();
        //when
        String result = mockMvc.perform(put(PRODUCT_END_POINT + "/{id}", expectedId)
                        .content(objectMapper.writeValueAsString(productDto))
                        .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ProductEntity actualEntity = objectMapper.readerFor(ProductEntity.class).readValue(result);

        assertThat(result).isNotBlank();
        assertThat(expectedId).isEqualTo(actualEntity.getId());
        assertThat(productDto.getPrice()).isEqualTo(actualEntity.getPrice());
        assertThat(productDto.getAmount()).isEqualTo(actualEntity.getAmount());
        assertThat(productDto.getCategory()).isEqualTo(actualEntity.getCategory());
        assertThat(productDto.getName()).isEqualTo(actualEntity.getName());
    }

    @Test
    void deleteProduct_shouldReturn200OkAndMessageDto() throws Exception {
        //given
        Long expectedId = productRepository.save(prepareProductEntity(it -> it.setName("stool"))).getId();
        //when
        String result = mockMvc.perform(delete(PRODUCT_END_POINT + "/{id}", expectedId))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        MessageDto messageDto = objectMapper.readerFor(MessageDto.class).readValue(result);

        assertThat(result).isNotBlank();
        assertThat(messageDto.getMessage()).isNotBlank();
        assertThat(productRepository.findById(expectedId).isPresent()).isFalse();
    }

    private static ProductDto getValidProductDto() {
        return new ProductDto().setAmount(1000)
                .setPrice(2000)
                .setDate(ZonedDateTime.now(ZoneId.of(ZONE_ID)))
                .setCategory("furniture")
                .setName("chair")
                .setAmount(4);
    }

    private static List<ProductEntity> getListProductEntity() {
        return List.of(
                prepareProductEntity(it -> it.setPrice(10000)),
                prepareProductEntity(it -> it.setPrice(1000)),
                prepareProductEntity(it -> it.setPrice(1500)),
                prepareProductEntity(it -> it.setAmount(23)),
                prepareProductEntity(it -> it.setName("chair")),
                prepareProductEntity(it -> it.setName("sofa"))
        );
    }

    private static ProductEntity prepareProductEntity(Consumer<ProductEntity> it) {
        ProductEntity productEntity = new ProductEntity()
                .setAmount(10)
                .setCategory("furniture")
                .setDate(ZonedDateTime.now(ZoneId.of(ZONE_ID)))
                .setPrice(4000)
                .setName("table");
        it.accept(productEntity);
        return productEntity;
    }
}
