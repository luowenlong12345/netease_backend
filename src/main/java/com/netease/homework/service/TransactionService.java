package com.netease.homework.service;

import com.netease.homework.domain.Goods;
import com.netease.homework.domain.Transaction;
import com.netease.homework.domain.User;
import com.netease.homework.utils.page.PageResult;

import java.util.Map;
import java.util.List;

public interface TransactionService {
    Map<String, Object> details(Transaction transaction);
    void list(Integer buyerId, Integer sellerId, PageResult<Map<String, Object>> pageResult);
    Transaction delete(Integer transactionId);
    Transaction add(Integer goodsId, Integer number);
}
