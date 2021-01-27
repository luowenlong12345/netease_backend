package com.netease.homework.repository;

import com.netease.homework.domain.Role;
import com.netease.homework.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    ShoppingCart findOneByGoodsIdAndBuyerId(Integer goodsId, Integer buyerId);
    List<ShoppingCart> findAllByIdIn(List<Integer> ids);
    List<ShoppingCart> findAllByBuyerId(Integer buyerId);
    Integer countAllByBuyerId(Integer buyerId);
}
