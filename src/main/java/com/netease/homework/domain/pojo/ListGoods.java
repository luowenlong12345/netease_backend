package com.netease.homework.domain.pojo;

import java.math.BigDecimal;
import java.util.Date;

public class ListGoods{
    private Date createTime;
    private String imagePath;
    private BigDecimal price;
    private Integer id;
    private String title;
    private Integer sellNumbers;

    public ListGoods(Date createTime, String imagePath, BigDecimal price, Integer id, String title, Integer sellNumbers) {
        this.createTime = createTime;
        this.imagePath = "goodsImages/" + imagePath;
        this.price = price;
        this.id = id;
        this.title = title;
        this.sellNumbers = sellNumbers;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getSellNumbers() {
        return sellNumbers;
    }
}
