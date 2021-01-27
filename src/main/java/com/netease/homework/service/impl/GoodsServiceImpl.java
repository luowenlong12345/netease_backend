package com.netease.homework.service.impl;

import com.google.common.base.CaseFormat;
import com.netease.homework.domain.Goods;
import com.netease.homework.domain.Role;
import com.netease.homework.domain.Transaction;
import com.netease.homework.domain.User;
import com.netease.homework.repository.GoodsRepository;
import com.netease.homework.repository.TransactionRepository;
import com.netease.homework.repository.UserRepository;
import com.netease.homework.service.GoodsService;
import com.netease.homework.service.TransactionService;
import com.netease.homework.service.UserService;
import com.netease.homework.utils.exception.GlobalException;
import com.netease.homework.utils.page.PageResult;
import com.netease.homework.utils.result.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Resource
    private GoodsRepository goodsRepository;
    @Resource
    private UserRepository userRepository;
    @Value("${goods.image.path}")
    private String goodsImagePath;
    @Resource
    private TransactionRepository transactionRepository;
    @Resource
    private UserService userService;

    @Override
    public Goods add(Goods goods, MultipartFile image) {
        boolean hasRole = false;
        for (Role role : userService.getCurrentUser().getRoles())
            if (role.getName().equals("SELLER"))
            {
                hasRole = true; break;
            }
        if (!hasRole){
            throw new GlobalException(ResultCode.WITHOUT_ROLE.getCode(), "当前用户无商品上架权限！");
        }
        File parent = new File(goodsImagePath);
        if (!parent.exists()) {
            parent.mkdirs();
        }
        File destFile = new File(parent.getAbsolutePath() + File.separator + goods.getImagePath());
        try {
            image.transferTo(destFile);
        } catch (IOException e) {
            throw new GlobalException(ResultCode.FILE_UPLOAD_FAILED);
        }
        return goodsRepository.save(goods);
    }

    @Override
    public Goods get(Integer goodsId) {
        Optional<Goods> goods = goodsRepository.findById(goodsId);
        return goods.orElse(null);
    }

    @Override
    public Goods delete(Integer goodsId) {
        User user = userService.getCurrentUser();
        Goods goods = get(goodsId);
        if (goods == null)
            return null;
        if (!user.getId().equals(goods.getOwner().getId()))
            throw new GlobalException(ResultCode.NO_PERMISSION);
        goodsRepository.delete(goods);
        File image = new File(goodsImagePath + File.separator + goods.getImagePath());
        if (image.exists()) image.delete();
        List<Transaction> transactions = transactionRepository.findAllByGoods_Id(goodsId);
        for(Transaction transaction: transactions)
        {
            transaction.setGoods(null);
            transactionRepository.save(transaction);
        }
        return goods;
    }

    @Override
    public Goods update(Goods goods) {
        User user = userService.getCurrentUser();
        Goods old = goodsRepository.getOne(goods.getId());
        if (!old.getOwner().getId().equals(user.getId()))
            throw new GlobalException(ResultCode.NO_PERMISSION);
        goods.setOwner(old.getOwner());
        goods.setImagePath(old.getImagePath());
        goodsRepository.save(goods);
        return goods;
    }

    @Override
    public Goods update(Integer goodsId, MultipartFile image, String fileName) {
        Goods goods = goodsRepository.getOne(goodsId);
        if (!goods.getOwner().getId().equals(userService.getCurrentUser().getId()))
            throw new GlobalException(ResultCode.NO_PERMISSION);
        File oldImage = new File(goodsImagePath + File.separator + goods.getImagePath());

        File dir = new File(goodsImagePath);
        File newImage = new File(dir.getAbsolutePath() + File.separator + fileName);
        try {
            image.transferTo(newImage);
        } catch (IOException e) {
            throw new GlobalException(ResultCode.FILE_UPLOAD_FAILED);
        }
        if (oldImage.exists()) oldImage.delete();
        goods.setImagePath(fileName);
        goodsRepository.save(goods);
        return goods;
    }

    @Override
    public Map<String, Object> details(Integer goodsId) {
        Goods goods = get(goodsId);
        if (goods == null)
            throw new GlobalException(ResultCode.ENTITY_NOT_EXIST);
        return details(goods);
    }
    public Map<String, Object> details(Goods goods) {
        User user = userService.getCurrentUser();
        Integer buyNumbers = goodsRepository.getBuyNumbersByUserId(goods.getId(), user.getId());

        return new HashMap<>() {
            {
                put("createTime", goods.getCreateTime());
                put("image", "/goodsImages/" + goods.getImagePath());
                put("price", goods.getPrice());
                put("content", goods.getContent());
                put("id", goods.getId());
                put("sellNumbers", goods.getSellNumbers());
                put("remark", goods.getRemark());
                put("title", goods.getTitle());
                put("buyNumbers", buyNumbers);
                put("owner", new HashMap<String, Object>() {
                    {
                        put("id", goods.getOwner().getId());
                        put("username", goods.getOwner().getUsername());
                    }
                });
            }
        };
    }

    @Override
    public void goodsList(PageResult<Map<String, Object>> pageResult, Integer sellerId) {
        Sort sort = Sort.by(
                pageResult.getOrder().equals(PageResult.getOrderEnum().get(0)) ? Sort.Direction.DESC : Sort.Direction.ASC,
                CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, pageResult.getOrderBy())
        );
        Page<Goods> goods;
        if (sellerId == null)
            goods = goodsRepository.findAll(
                    PageRequest.of(
                            pageResult.getPage() - 1,
                            pageResult.getSize(),
                            sort
                    )
            );
        else{
            User user = userRepository.getOne(sellerId);
            goods = goodsRepository.findAllByOwner(
                    user.getId(),
                    PageRequest.of(
                            pageResult.getPage() - 1,
                            pageResult.getSize(),
                            sort
                    )
            );
        }

        pageResult.setTotalPages(goods.getTotalPages());
        pageResult.setTotalNumbers(goods.getTotalElements());
        List<Map<String, Object>> data = new ArrayList<>();
        goods.getContent().forEach(i -> {
            i.setSellNumbers(goodsRepository.getSellNumbers(i.getId()));
            data.add(getListInfo(i));
        });
        pageResult.setData(data);
    }

    private Map<String, Object> getListInfo(Goods goods){
        return details(goods);
    }
}