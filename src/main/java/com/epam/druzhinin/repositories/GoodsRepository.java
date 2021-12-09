package com.epam.druzhinin.repositories;

import com.epam.druzhinin.entity.GoodsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsRepository extends JpaRepository<GoodsEntity, Integer> {
}
