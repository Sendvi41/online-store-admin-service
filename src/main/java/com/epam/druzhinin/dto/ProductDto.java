package com.epam.druzhinin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class ProductDto {
    @NotBlank
    private String name;
    @NotBlank
    private String category;
    @NotNull
    private Integer amount;
    @NotNull
    private Integer price;
    @NotNull
    private ZonedDateTime date;
}
