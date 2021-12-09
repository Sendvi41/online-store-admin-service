package com.epam.druzhinin.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "goods")
public class GoodsEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "category")
    private String category;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;
}
