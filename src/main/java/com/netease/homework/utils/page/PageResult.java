package com.netease.homework.utils.page;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

public class PageResult<T> {
    @JsonIgnore
    private static final List<String> orderEnum = Arrays.asList("desc", "asc");
    @JsonIgnore
    private List<String> orderByEnum;
    @Min(1)
    private Integer page = 1;

    public Integer getTotalPages() {
        return totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    private List<T> data;

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    private Integer totalPages = 0;

    public Long getTotalNumbers() {
        return totalNumbers;
    }

    public void setTotalNumbers(Long totalNumbers) {
        this.totalNumbers = totalNumbers;
    }

    private Long totalNumbers = 0L;
    @Min(1)
    @Max(10)
    private Integer size = 10;
    private String order = orderEnum.get(0);
    private String orderBy;

    public static List<String> getOrderEnum() {
        return orderEnum;
    }

    public List<String> getOrderByEnum() {
        return orderByEnum;
    }

    public void setOrderByEnum(List<String> orderByEnum) {
        this.orderByEnum = orderByEnum;
        if (orderBy == null || !orderByEnum.contains(orderBy))
            orderBy = orderByEnum.get(0);
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
}
