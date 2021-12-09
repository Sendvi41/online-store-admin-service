package com.epam.druzhinin.controllers;

import com.epam.druzhinin.entity.GoodsEntity;
import com.epam.druzhinin.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping
    public List<GoodsEntity> getGoods() {
        return goodsService.getGoods();
    }
}
