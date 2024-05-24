package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * 
 * Description: 导出第三方应用主机
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */
public class ExportRcaHostRequest {

    @NotNull
    private String userId;

    @Nullable
    private List<UUID> appPoolIdList;

    /**
     * 是否拥有全部组权限
     */
    @NotNull
    private Boolean enableAllDataPermission;

    @Nullable
    private List<UUID> hasPermissionAppPoolIdList;

    /**
     * 是否是应用池的第三方应用主机列表
     */
    @NotNull
    private Boolean rcaPoolPage;

    public ExportRcaHostRequest()  { }

    public ExportRcaHostRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getEnableAllDataPermission() {
        return enableAllDataPermission;
    }

    public void setEnableAllDataPermission(Boolean enableAllDataPermission) {
        this.enableAllDataPermission = enableAllDataPermission;
    }

    @Nullable
    public List<UUID> getAppPoolIdList() {
        return appPoolIdList;
    }

    public void setAppPoolIdList(@Nullable List<UUID> appPoolIdList) {
        this.appPoolIdList = appPoolIdList;
    }

    public Boolean getRcaPoolPage() {
        return rcaPoolPage;
    }

    public void setRcaPoolPage(Boolean rcaPoolPage) {
        this.rcaPoolPage = rcaPoolPage;
    }

    @Nullable
    public List<UUID> getHasPermissionAppPoolIdList() {
        return hasPermissionAppPoolIdList;
    }

    public void setHasPermissionAppPoolIdList(@Nullable List<UUID> hasPermissionAppPoolIdList) {
        this.hasPermissionAppPoolIdList = hasPermissionAppPoolIdList;
    }
}
