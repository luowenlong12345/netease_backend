package com.netease.homework.controller;

import com.netease.homework.domain.Goods;
import com.netease.homework.service.ShoppingCartService;
import com.netease.homework.utils.Utils;
import com.netease.homework.utils.result.JsonResult;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/api/cart")
@Validated
@Transactional()
public class ShoppingCartController {
    @Resource
    ShoppingCartService shoppingCartService;

    @ResponseBody
    @PostMapping("/add")
    public JsonResult<Object> add(
            @NotNull Integer goodsId,
            @NotNull @Min(1) Integer number) {
        shoppingCartService.add(goodsId, number);
        return new JsonResult<>(true);
    }
    @ResponseBody
    @PostMapping("/delete")
        public JsonResult<Object> delete(@NotNull @RequestParam(value = "ids") List<Integer> ids) {
        shoppingCartService.delete(ids);
        return new JsonResult<>(true);
    }

    @ResponseBody
    @PostMapping("/buy")
    public JsonResult<Object> buy(@NotNull @RequestParam(value = "ids") List<Integer> ids) {
        shoppingCartService.buy(ids);
        return new JsonResult<>(true);
    }
    @ResponseBody
    @GetMapping("/list")
    public JsonResult<List<Map<String, Object>>> list() {
        return new JsonResult<>(true, shoppingCartService.list());
    }

}
