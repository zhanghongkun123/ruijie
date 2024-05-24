package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

import org.springframework.util.Assert;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 分页查询结果抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/24 15:41
 *
 * @param <T> T
 * @param <O> O
 *
 * @author lyb
 */
public abstract class AbstractPageQueryResponse<T, O> {
    /**
     * 总条数
     */
    public Long total;

    /**
     * 数据条目集合
     */
    public T[] itemArr;

    /**
     * 构造方法
     */
    public AbstractPageQueryResponse() {
        setTotal(0L);
        setItemArr(null);
    }

    /**
     * 转换方法
     * 
     * @param defaultPageResponse 输入参数
     */
    public void convert(DefaultPageResponse<O> defaultPageResponse) {
        Assert.notNull(defaultPageResponse, "defaultPageResponse is not null");
        setTotal(defaultPageResponse.getTotal());
        setItemArr(convertItems(defaultPageResponse));
    }

    /**
     * 将业务层返回的DTO对象转为OpenAPI对外输出格式，由子类实现
     * 
     * @param defaultPageResponse 业务层分页响应类
     * @return 转换后的数据条目集合
     */
    public abstract T[] convertItems(DefaultPageResponse<O> defaultPageResponse);

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public T[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(T[] itemArr) {
        this.itemArr = itemArr;
    }
}
