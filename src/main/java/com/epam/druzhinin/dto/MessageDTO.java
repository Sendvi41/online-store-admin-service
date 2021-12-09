package com.epam.druzhinin.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MessageDTO {
    private String message;
}
