package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacAdGroupType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: ad域服务器组请求类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/16
 *
 * @author zqj
 */
public class AdServerGroupPageWebRequest implements WebRequest {


    @Range(min = "0")
    @ApiModelProperty(value = "页码", required = true)
    private Integer page = 0;

    @Range(min = "1", max = "1000")
    @ApiModelProperty(value = "大小", required = true)
    private Integer limit = 1;

    @NotBlank
    @ApiModelProperty(value = "域", required = true)
    private String domain;

    @Nullable
    @ApiModelProperty(value = "组类型")
    private IacAdGroupType adGroupType;

    @Nullable
    @ApiModelProperty(value = "筛选条件")
    private CbbDomainMatchRequest[] filterArr;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Nullable
    public CbbDomainMatchRequest[] getFilterArr() {
        return filterArr;
    }

    public void setFilterArr(@Nullable CbbDomainMatchRequest[] filterArr) {
        this.filterArr = filterArr;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    @Nullable
    public IacAdGroupType getAdGroupType() {
        return adGroupType;
    }

    public void setAdGroupType(@Nullable IacAdGroupType adGroupType) {
        this.adGroupType = adGroupType;
    }
}
