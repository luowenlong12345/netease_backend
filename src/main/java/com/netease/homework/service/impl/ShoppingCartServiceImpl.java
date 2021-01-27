package com.netease.homework.service.impl;

import com.netease.homework.domain.Goods;
import com.netease.homework.domain.ShoppingCart;
import com.netease.homework.domain.User;
import com.netease.homework.repository.GoodsRepository;
import com.netease.homework.repository.ShoppingCartRepository;
import com.netease.homework.service.GoodsService;
import com.netease.homework.service.ShoppingCartService;
import com.netease.homework.service.TransactionService;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.exception.GlobalException;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Resource
    ShoppingCartRepository shoppingCartRepository;
    @Resource
    UserService userService;
    @Resource
    GoodsRepository goodsRepository;
    @Resource
    TransactionService transactionService;
    @Resource
    GoodsService goodsService;

    @Override
    public ShoppingCart add(Integer goodsId, Integer numbers) {
        User user = userService.getCurrentUser();
        ShoppingCart shoppingCart = shoppingCartRepository.findOneByGoodsIdAndBuyerId(goodsId, user.getId());
        if (shoppingCart != null)
        {
            shoppingCart.setNumber(shoppingCart.getNumber()+numbers);
            return shoppingCartRepository.save(shoppingCart);
        }
        shoppingCart = new ShoppingCart();
        shoppingCart.setNumber(numbers);
        shoppingCart.setBuyer(user);
        try{
            Goods goods = goodsRepository.getOne(goodsId);
            shoppingCart.setGoods(goods);
            return shoppingCartRepository.save(shoppingCart);
        }
        catch (Exception e){
            throw new GlobalException(ResultCode.ENTITY_NOT_EXIST);
        }


    }

    @Override
    public void delete(List<Integer> ids) {
        User user = userService.getCurrentUser();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByIdIn(ids);
        for(ShoppingCart s: shoppingCarts){
            if (s.getBuyer().getId().equals(user.getId()))
                shoppingCartRepository.delete(s);
        }
    }

    @Override
    public void buy(List<Integer> ids) {
        User user = userService.getCurrentUser();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByIdIn(ids);
        for(ShoppingCart s: shoppingCarts){
            if (s.getBuyer().getId().equals(user.getId())){
                transactionService.add(s.getGoods().getId(), s.getNumber());
                shoppingCartRepository.delete(s);
            }
        }
    }

    @Override
    public List<Map<String, Object>> list() {
        User user = userService.getCurrentUser();
        List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAllByBuyerId(user.getId());
        List<Map<String, Object>> res = new ArrayList<>();
        for (ShoppingCart shoppingCart: shoppingCarts){
            res.add(new HashMap<>(){{
                put("id", shoppingCart.getId());
                put("number", shoppingCart.getNumber());
                put("goods", goodsService.details(shoppingCart.getGoods().getId()));
                put("createTime", shoppingCart.getCreateTime());
            }});
        }
        return res;
    }
}
