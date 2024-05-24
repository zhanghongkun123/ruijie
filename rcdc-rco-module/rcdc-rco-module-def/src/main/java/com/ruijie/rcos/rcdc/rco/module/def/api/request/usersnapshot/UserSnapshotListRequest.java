package com.ruijie.rcos.rcdc.rco.module.def.api.request.usersnapshot;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.pagekit.kernel.request.criteria.DefaultSort;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 用户自定义快照列表请求DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/31 11:42
 *
 * @author lihengjing
 */
public class UserSnapshotListRequest {
    /**
     * 云桌面Id
     */
    @NotNull
    private UUID deskId;

    /**
     * 分页数据 当前页
     */
    @NotNull
    private Integer page;

    /**
     * 分页数据 每页数量
     */
    @NotNull
    private Integer limit;

    /**
     * 排序条件
     */
    @Nullable
    private Sort[] sortArr = new DefaultSort[0];

    /**
     * 用户Id
     */
    @NotNull
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

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable DefaultSort[] sortArr) {
        this.sortArr = sortArr;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
