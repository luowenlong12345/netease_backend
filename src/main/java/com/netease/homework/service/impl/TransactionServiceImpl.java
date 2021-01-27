package com.netease.homework.service.impl;

import com.google.common.base.CaseFormat;
import com.netease.homework.domain.Goods;
import com.netease.homework.domain.Role;
import com.netease.homework.domain.Transaction;
import com.netease.homework.domain.User;
import com.netease.homework.repository.GoodsRepository;
import com.netease.homework.repository.TransactionRepository;
import com.netease.homework.repository.UserRepository;
import com.netease.homework.service.TransactionService;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.exception.GlobalException;
import com.netease.homework.utils.page.PageResult;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Resource
    TransactionRepository transactionRepository;
    @Resource
    UserService userService;
    @Resource
    UserRepository userRepository;
    @Resource
    GoodsRepository goodsRepository;
    @Override
    public Map<String, Object> details(Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        map.put("createTime", transaction.getCreateTime());
        map.put("buyer", new HashMap<>(){{
            put("id", transaction.getBuyer().getId());
            put("username", transaction.getBuyer().getUsername());
        }});
        map.put("id", transaction.getId());
        map.put("unitPrice", transaction.getUnitPrice());
        map.put("number", transaction.getNumber());
        Goods goods = transaction.getGoods();
        if (goods == null){
            map.put("goodsHasDeleted", true);
            return map;
        }

        map.put("goodsHasDeleted", false);
        map.put("seller", new HashMap<String, Object>(){{
            put("username", goods.getOwner().getUsername());
            put("id", goods.getOwner().getId());
        }});
        map.put("currentPrice", goods.getPrice());
        map.put("imagePath", "goodsImages/" + goods.getImagePath());
        map.put("title", goods.getTitle());
        map.put("remark", goods.getRemark());
        map.put("content", goods.getContent());
        map.put("goodsId", goods.getId());
        return map;
    }

    @Override
    public void list(Integer buyerId, Integer sellerId, PageResult<Map<String, Object>> pageResult) {
        User user = userService.getCurrentUser();
        if (!user.getId().equals(buyerId) && !user.getId().equals(sellerId))
            throw new GlobalException(ResultCode.NO_PERMISSION);
        Sort sort = Sort.by(
                pageResult.getOrder().equals(PageResult.getOrderEnum().get(0)) ? Sort.Direction.DESC : Sort.Direction.ASC,
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pageResult.getOrderBy())
        );
        PageRequest pageRequest = PageRequest.of(pageResult.getPage() - 1,
                pageResult.getSize(),
                sort);
        Page<Transaction> transactions;
        if (buyerId != null && sellerId != null)
            transactions = transactionRepository.findAllByGoods_OwnerIdAndBuyerId(
                    pageRequest, sellerId, buyerId
            );
        else if(sellerId != null)
            transactions = transactionRepository.findAllByGoods_OwnerId(
                    pageRequest, sellerId
            );
        else if (buyerId != null)
            transactions = transactionRepository.findAllByBuyerId(
                    pageRequest, buyerId
            );
        else
            throw new GlobalException(ResultCode.PARAM_IS_BLANK);
        pageResult.setTotalPages(transactions.getTotalPages());
        pageResult.setTotalNumbers(transactions.getTotalElements());
        List<Map<String, Object>> data = new ArrayList<>();
        transactions.getContent().forEach(i -> {
            data.add(details(i));
        });
        pageResult.setData(data);

    }


    @Override
    public Transaction delete(Integer transactionId) {
        Transaction transaction = transactionRepository.getOne(transactionId);
        User user = userService.getCurrentUser();
        if (!transaction.getBuyer().getId().equals(user.getId()))
            throw new GlobalException(ResultCode.NO_PERMISSION);
        transactionRepository.delete(transaction);
        return transaction;
    }

    @Override
    public Transaction add(Integer goodsId, Integer number) {

        User user = userService.getCurrentUser();
        boolean hasRole = false;
        for (Role role : user.getRoles())
            if (role.getName().equals("BUYER"))
            {
                hasRole = true; break;
            }
        if (!hasRole){
            throw new GlobalException(ResultCode.WITHOUT_ROLE.getCode(), "当前用户无购买权限！");
        }
        Transaction transaction = new Transaction();
        transaction.setBuyer(user);
        Goods goods = goodsRepository.getOne(goodsId);
        transaction.setGoods(goods);
        transaction.setNumber(number);
        transaction.setUnitPrice(goods.getPrice());
        user.setMoney(user.getMoney().subtract(
                goods.getPrice().multiply(new BigDecimal(number))
        ));
        if (user.getMoney().compareTo(new BigDecimal(0)) < 0)
            throw new GlobalException(ResultCode.WITHOUT_ENOUGH_MONEY);
        transactionRepository.save(transaction);
        userRepository.save(user);
        goods.getOwner().setMoney(
                goods.getOwner().getMoney().add(
                        goods.getPrice().multiply(new BigDecimal(number))
                )
        );
        return transaction;
    }
}
