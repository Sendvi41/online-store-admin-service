package com.epam.druzhinin.controllers;

import com.epam.druzhinin.dto.GoodsDTO;
import com.epam.druzhinin.dto.MessageDTO;
import com.epam.druzhinin.entity.GoodsEntity;
import com.epam.druzhinin.services.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public GoodsEntity createGoods(GoodsDTO goodsDTO) {
        return goodsService.createGoods(goodsDTO);
    }

    @GetMapping("/{id}")
    public GoodsEntity getGoodsById(@PathVariable Long id) {
        return goodsService.findGoodsById(id);
    }

    @PutMapping("/{id}")
    public GoodsEntity updateGoods(
            @PathVariable Long id,
            @RequestBody GoodsDTO goodsDTO
    ) {
        return goodsService.updateGoods(goodsDTO, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageDTO> deleteGoods(@PathVariable Long id) {
        goodsService.deleteGoods(id);
        return ResponseEntity.ok(MessageDTO.of("Goods is deleted by id" + id));
    }
}
