package com.netease.homework.controller;
import com.netease.homework.service.TransactionService;
import com.netease.homework.utils.page.PageResult;
import com.netease.homework.utils.result.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller()
@RequestMapping("/api/transaction")
@Validated
@Transactional
public class TransactionController {

    private final static List<String> listOrder = Arrays.asList("createTime", "unitPrice", "number");
    @Resource
    TransactionService transactionService;

    @ResponseBody
    @PostMapping("/add")
    public JsonResult<Object> add(
            @NotNull @Min(1) Integer number, @NotNull Integer goodsId) {
        transactionService.add(goodsId, number);
        return new JsonResult<>(true);
    }

    @ResponseBody
    @PostMapping("/delete")
    public JsonResult<Object> delete(
            @NotNull Integer id) {
        transactionService.delete(id);
        return new JsonResult<>(true);
    }

    @ResponseBody
    @GetMapping("/list")
    public JsonResult<Object> list(
            @Valid PageResult<Map<String, Object>> pageParams,
            Integer buyerId, Integer sellerId) {
        pageParams.setOrderByEnum(listOrder);
        transactionService.list(buyerId, sellerId, pageParams);

        return new JsonResult<>(true, pageParams);
    }
}
