package com.epam.druzhinin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.ZonedDateTime;

@Data
@Accessors(chain = true)
public class GoodsDTO {
    private String name;
    private String category;
    private Integer amount;
    private Integer price;
    private ZonedDateTime date;
}
