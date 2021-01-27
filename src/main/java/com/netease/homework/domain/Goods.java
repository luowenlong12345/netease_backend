package com.netease.homework.domain;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table
public class Goods extends BaseDomain{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    @NotNull
    private BigDecimal price;
    @Column(nullable = false, length = 280)
    @NotNull
    private String title;
    @Column(length = 2140)
    private String remark;
    private String imagePath;
    @Column(columnDefinition = "text")
    private String content;

    @Transient
    private Integer sellNumbers;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @ManyToOne()
    private User owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getSellNumbers() {
        return sellNumbers;
    }

    public void setSellNumbers(Integer sellNumbers) {
        this.sellNumbers = sellNumbers;
    }
}
