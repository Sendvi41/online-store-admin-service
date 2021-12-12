package com.epam.druzhinin.controllers;

import com.epam.druzhinin.dto.MessageDto;
import com.epam.druzhinin.dto.ProductDto;
import com.epam.druzhinin.entity.ProductEntity;
import com.epam.druzhinin.repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
})
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

    @Test
    void getProducts_shouldReturn200OkAndListProductEntity() throws Exception {
        //given
        List<ProductEntity> listEntities = getListProductEntity();
        when(productRepository.findAll()).thenReturn(listEntities);
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
        ProductEntity entity = modelMapper.map(productDto, ProductEntity.class);
        when(productRepository.save(any())).thenReturn(entity.setId(1L));
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
        ProductEntity expectedEntity = prepareProductEntity(it -> it.setId(1L));
        when(productRepository.findById(any())).thenReturn(Optional.of(expectedEntity));
        //when
        String result = mockMvc.perform(get(PRODUCT_END_POINT + "/{id}", 1L))
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
        Long expectedId = 1L;
        ProductDto productDto = getValidProductDto();
        ProductEntity entity = modelMapper.map(productDto, ProductEntity.class);
        when(productRepository.findById(any())).thenReturn(Optional.of(entity.setId(expectedId)));
        when(productRepository.save(any())).thenReturn(entity.setId(expectedId));
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
        Long expectedId = 1L;
        ProductEntity deletedEntity = prepareProductEntity(it -> it.setId(1L));
        when(productRepository.findById(expectedId)).thenReturn(Optional.of(deletedEntity.setId(expectedId)));
        //when
        String result = mockMvc.perform(delete(PRODUCT_END_POINT + "/{id}", expectedId))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        MessageDto messageDto = objectMapper.readerFor(MessageDto.class).readValue(result);

        assertThat(result).isNotBlank();
        assertThat(messageDto.getMessage()).isNotBlank();
        verify(productRepository, times(1)).findById(expectedId);
        verify(productRepository, times(1)).deleteById(expectedId);
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
                prepareProductEntity(it -> it.setPrice(10000).setId(2L)),
                prepareProductEntity(it -> it.setPrice(1000).setId(3L)),
                prepareProductEntity(it -> it.setPrice(1500).setId(4L)),
                prepareProductEntity(it -> it.setAmount(23).setId(5L)),
                prepareProductEntity(it -> it.setName("chair").setId(6L)),
                prepareProductEntity(it -> it.setName("sofa").setId(7L))
        );
    }

    private static ProductEntity prepareProductEntity(Consumer<ProductEntity> it) {
        ProductEntity productEntity = new ProductEntity().setId(1L)
                .setAmount(10)
                .setCategory("furniture")
                .setDate(ZonedDateTime.now(ZoneId.of(ZONE_ID)))
                .setPrice(4000)
                .setName("table");
        it.accept(productEntity);
        return productEntity;
    }
}
