package com.netease.homework.controller;

import com.netease.homework.domain.Goods;
import com.netease.homework.service.GoodsService;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.Utils;
import com.netease.homework.utils.page.PageResult;
import com.netease.homework.utils.result.JsonResult;
import com.netease.homework.utils.result.ResultCode;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

@Controller()
@RequestMapping("/api/goods")
@Validated
@Transactional()
public class GoodsController {
    @Resource
    private UserService userService;
    @Resource
    private GoodsService goodsService;

    private final static List<String> listOrder = Arrays.asList("createTime", "price", "sellNumbers", "title");

    @ResponseBody
    @PostMapping("/add")
    public JsonResult<Object> add(
            @Valid Goods goods,
            @NotNull MultipartFile image) {
        if (goods.getPrice().compareTo(new BigDecimal(0)) < 0 || image.isEmpty())
            return new JsonResult<>(false, ResultCode.PARAM_NOT_VALID);
        goods.setOwner(userService.getCurrentUser());
        String fileName = image.getOriginalFilename();  // 文件名
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        goods.setImagePath(UUID.randomUUID() + suffixName);
        if (!Utils.getImageSuffixes().contains(suffixName))
            return new JsonResult<>(false, ResultCode.PARAM_NOT_VALID, "image: NOT A IMAGE FILE");
        goodsService.add(goods, image);
        return new JsonResult<>(true);
    }

    @ResponseBody
    @PostMapping("/delete")
    public JsonResult<Object> delete(@NotNull Integer goodsId){
        goodsService.delete(goodsId);
        return new JsonResult<>(true);
    }

    @ResponseBody
    @GetMapping("/get")
    public JsonResult<Map<String, Object>> get(@NotNull Integer goodsId){
        Map<String, Object> details = goodsService.details(goodsId);
        return new JsonResult<>(true, details);
    }
    @ResponseBody
    @GetMapping("/list")
    public JsonResult<Object> list(
            @Valid PageResult<Map<String, Object>> pageParams, Integer sellerId
            ){
        pageParams.setOrderByEnum(listOrder);

        goodsService.goodsList(pageParams, sellerId);

        return new JsonResult<>(true, pageParams);
    }

    @ResponseBody
    @PostMapping("/update")
    public JsonResult<Object> update(
            @Valid Goods goods
    ){
        if (goods.getId() == null){
            return new JsonResult<>(false, new HashMap<>(){{
                put("id", "MUST BE GIVEN!");
            }});
        }
        goodsService.update(goods);
        return new JsonResult<>(true);
    }
    @ResponseBody
    @PostMapping("/updateImage")
    public JsonResult<Object> updateImage(
            @NotNull Integer goodsId, @NotNull MultipartFile image
    ){
        if (image.isEmpty()){
            return new JsonResult<>(false, new HashMap<>(){{
                put("image", "IS EMPTY");
            }});
        }
        String fileName = image.getOriginalFilename();  // 文件名
        assert fileName != null;
        String suffixName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
        fileName = UUID.randomUUID() + suffixName;
        goodsService.update(goodsId, image, fileName);
        return new JsonResult<>(true);
    }
}
