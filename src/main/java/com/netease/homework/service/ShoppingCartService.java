package com.netease.homework.service;

import com.netease.homework.domain.Goods;
import com.netease.homework.domain.ShoppingCart;
import java.util.List;
import java.util.Map;

public interface ShoppingCartService {
    ShoppingCart add(Integer goodsId, Integer numbers);
    void delete(List<Integer> ids);
    void buy(List<Integer> ids);
    List<Map<String, Object>> list();
}
