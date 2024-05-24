package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;
import org.springframework.lang.Nullable;

/**
 *
 * Description: 导出请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/04/12
 *
 * @author guoyongxin
 */
public class ExportRequest extends TimePageRequest {

    @NotNull
    private String userId;

    /**
     * 数据总数量
     */
    @NotNull
    private Integer totalCount;

    /**
     * 导出数据总数量
     */
    @NotNull
    private Integer exportTotalCount;

    @NotNull
    private Sort[] sortArr;

    @Nullable
    private String searchKeyword;

    @NotNull
    private Integer sheetSize;

    public ExportRequest(String userId, Sort[] sortArr) {
        this.userId = userId;
        this.sortArr = sortArr;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getExportTotalCount() {
        return exportTotalCount;
    }

    public void setExportTotalCount(Integer exportTotalCount) {
        this.exportTotalCount = exportTotalCount;
    }

    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public Integer getSheetSize() {
        return sheetSize;
    }

    public void setSheetSize(Integer sheetSize) {
        this.sheetSize = sheetSize;
    }
}
