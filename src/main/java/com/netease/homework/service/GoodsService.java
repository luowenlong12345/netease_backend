package com.netease.homework.service;

import com.netease.homework.domain.Goods;
import com.netease.homework.utils.page.PageResult;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

public interface GoodsService {
    public Goods add(Goods goods, MultipartFile image);
    public Goods get(Integer goodsId);
//    public Goods delete(Goods goods);
    public Goods delete(Integer goodsId);
    public Goods update(Goods goods);
    public Goods update(Integer goodsId, MultipartFile image, String fileName);
    public Map<String, Object> details(Integer goodsId);
    public void goodsList(PageResult<Map<String, Object>> pageResult, Integer sellerId);


}
