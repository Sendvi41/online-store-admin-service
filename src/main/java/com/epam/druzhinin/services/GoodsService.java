package com.epam.druzhinin.services;

import com.epam.druzhinin.entity.GoodsEntity;
import com.epam.druzhinin.repositories.GoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Autowired
    private GoodsRepository goodsRepository;

    public List<GoodsEntity> getGoods() {
        return goodsRepository.findAll();
    }
}
