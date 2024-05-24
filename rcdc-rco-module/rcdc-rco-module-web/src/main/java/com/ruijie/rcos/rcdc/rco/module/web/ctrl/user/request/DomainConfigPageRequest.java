package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import java.io.Serializable;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdUserSyncTypeEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月09日
 *
 * @author zhanghongkun
 */
@ApiModel("域用户请求参数")
public class DomainConfigPageRequest extends DomainConfigBaseWebRequest implements Serializable {
    private static final long serialVersionUID = 5786676047314332388L;

    @ApiModelProperty(value = "组id", required = true)
    @NotNull
    private String orgDn;

    @ApiModelProperty(value = "查询类型：组、用户和所有", required = true)
    @NotNull
    private IacAdUserSyncTypeEnum type;

    @ApiModelProperty(value = "每页大小")
    @Nullable
    private Integer limit;

    @ApiModelProperty(value = "页数")
    @Nullable
    private Integer page;

    @ApiModelProperty(value = "用户名查询")
    @Nullable
    private String searchName;


    @Nullable
    public String getOrgDn() {
        return orgDn;
    }

    public void setOrgDn(@Nullable String orgDn) {
        this.orgDn = orgDn;
    }

    public IacAdUserSyncTypeEnum getType() {
        return type;
    }

    public void setType(IacAdUserSyncTypeEnum type) {
        this.type = type;
    }

    @Nullable
    public Integer getLimit() {
        return limit;
    }

    public void setLimit(@Nullable Integer limit) {
        this.limit = limit;
    }

    @Nullable
    public Integer getPage() {
        return page;
    }

    public void setPage(@Nullable Integer page) {
        this.page = page;
    }

    @Nullable
    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(@Nullable String searchName) {
        this.searchName = searchName;
    }
}
