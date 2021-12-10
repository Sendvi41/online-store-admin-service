package com.epam.druzhinin.entity;

import lombok.Data;
import lombok.experimental.Accessors;


import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "goods")
public class GoodsEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name="amount")
    private Integer amount;

    @Column(name="price")
    private Integer price;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;
}
