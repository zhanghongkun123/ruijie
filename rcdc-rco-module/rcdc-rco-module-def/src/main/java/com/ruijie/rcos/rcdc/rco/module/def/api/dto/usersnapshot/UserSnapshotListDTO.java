package com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot;

import com.ruijie.rcos.sk.pagekit.api.Sort;

import java.util.UUID;

/**
 * Description: 用户自定义快照列表请求DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28
 *
 * @author liusd
 */
public class UserSnapshotListDTO {

    /**
     * 云桌面Id
     */
    private UUID deskId;

    /**
     * 分页数据 当前页
     */
    private Integer page;

    /**
     * 分页数据 每页数量
     */
    private Integer limit;

    /**
     * 排序条件
     */
    private Sort[] sortArr;

    /**
     * 用户Id
     */
    private UUID userId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return this.limit;
    }

    public Integer getPage() {
        return this.page;
    }

    public Sort[] getSortArr() {
        return this.sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
