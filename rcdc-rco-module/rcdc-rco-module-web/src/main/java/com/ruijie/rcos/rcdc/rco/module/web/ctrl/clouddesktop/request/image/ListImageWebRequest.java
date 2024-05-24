package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.image;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.Sort;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/1 11:18
 *
 * @author linrenjian
 */
public class ListImageWebRequest implements PageQueryRequest {

    @Nullable
    @ApiModelProperty(value = "是否不需要权限")
    private Boolean noPermission;

    @NotNull
    @Range(min = "0", max = "2147483647")
    private Integer page = 0;

    @NotNull
    @Range(min = "1", max = "2147483647")
    private Integer limit = 1;


    @Nullable
    private String searchKeyword;

    @Nullable
    private ExactMatch[] exactMatchArr;

    @Nullable
    private Sort[] sortArr;

    // fixme sk前端框架不支持复杂的分页查询，如果使用matchArr会导致分页框架刷新与下一页上一页按钮查询条件丢失，因此要求修改字段名称
    @Nullable
    private Match[] customMatchArr;

    @Nullable
    private UUID platformId;

    @Nullable
    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(@Nullable Boolean noPermission) {
        this.noPermission = noPermission;
    }

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public Match[] getMatchArr() {
        return customMatchArr;
    }

    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    @Nullable
    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(@Nullable String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    @Nullable
    public ExactMatch[] getExactMatchArr() {
        return exactMatchArr;
    }

    public void setExactMatchArr(@Nullable ExactMatch[] exactMatchArr) {
        this.exactMatchArr = exactMatchArr;
    }

    @Override
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public Match[] getCustomMatchArr() {
        return customMatchArr;
    }

    public void setCustomMatchArr(@Nullable Match[] customMatchArr) {
        this.customMatchArr = customMatchArr;
    }

    @Nullable
    public UUID getPlatformId() {
        return platformId;
    }

    public void setPlatformId(UUID platformId) {
        this.platformId = platformId;
    }
}
