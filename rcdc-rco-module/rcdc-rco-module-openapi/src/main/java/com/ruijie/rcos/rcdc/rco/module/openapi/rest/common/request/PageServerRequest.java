package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.Range;

/**
 * Description: 分页查询请求体
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/26 14:22
 *
 * @author lyb
 */
public class PageServerRequest {

    /**
     * 页码 page * limit不能超过2147483647
     */
    @Range(min = "0", max = "2147483")
    @Nullable
    private Integer page;

    /**
     * 每页数据条数
     */
    @Range(min = "1", max = "1000")
    @Nullable
    private Integer limit;

    @Nullable
    public Integer getPage() {
        return page;
    }

    public void setPage(@Nullable Integer page) {
        this.page = page;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(@Nullable Integer limit) {
        this.limit = limit;
    }
}
