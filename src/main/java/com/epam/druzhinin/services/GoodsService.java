package com.epam.druzhinin.services;

import com.epam.druzhinin.dto.GoodsDTO;
import com.epam.druzhinin.entity.GoodsEntity;
import com.epam.druzhinin.repositories.GoodsRepository;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
public class GoodsService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private GoodsRepository goodsRepository;

    public List<GoodsEntity> getGoods() {
        return goodsRepository.findAll();
    }

    public GoodsEntity createGoods(GoodsDTO goodsDTO) {
        GoodsEntity goods = modelMapper.map(goodsDTO, GoodsEntity.class);
        return goodsRepository.save(goods);
    }

    public GoodsEntity findGoodsById(Long id) {
        return goodsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Goods is not found")
        );
    }

    public GoodsEntity updateGoods(GoodsDTO goodsDTO, Long id) {
        goodsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Goods is not found")
        );
        GoodsEntity entity = modelMapper.map(goodsDTO, GoodsEntity.class);
        entity.setId(id);
        GoodsEntity updatedGoods = goodsRepository.save(entity);
        log.info("Goods is updated [goodsId={}]", id);
        return updatedGoods;
    }

    public void deleteGoods(Long id) {
        goodsRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Goods is not found")
        );
        goodsRepository.deleteById(id);
    }
}
