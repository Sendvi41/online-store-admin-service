package com.epam.druzhinin.dto;

import com.epam.druzhinin.enums.QueueTitle;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProductQueueDto {
    private QueueTitle queueTitle;
    private Long productId;
}
