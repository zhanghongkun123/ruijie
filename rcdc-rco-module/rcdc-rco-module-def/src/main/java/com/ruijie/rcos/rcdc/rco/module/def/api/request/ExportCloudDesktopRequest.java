package com.ruijie.rcos.rcdc.rco.module.def.api.request;


import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.vo.Sort;

/**
 * 
 * Description: 导出云桌面信息回应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */
public class ExportCloudDesktopRequest {
    
    @NotNull
    private String userId;

    @NotNull
    private Sort[] sortArr;

    /**
     * 是否拥有全部组权限
     */
    @Nullable
    private Boolean enableAllGroupPermission;


    @Nullable
    private List<UUID> desktopIdList;

    @Nullable
    private ImageUsageTypeEnum imageUsage;

    public ExportCloudDesktopRequest()  { }

    public ExportCloudDesktopRequest(String userId, Sort[] sortArr, @Nullable List<UUID> desktopIdList) {
        this.userId = userId;
        this.sortArr = sortArr;
        this.desktopIdList = desktopIdList;
    }

    public Boolean getEnableAllGroupPermission() {
        return enableAllGroupPermission;
    }

    public void setEnableAllGroupPermission(Boolean enableAllGroupPermission) {
        this.enableAllGroupPermission = enableAllGroupPermission;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Nullable
    public Sort[] getSortArr() {
        return sortArr;
    }

    public void setSortArr(@Nullable Sort[] sortArr) {
        this.sortArr = sortArr;
    }

    @Nullable
    public List<UUID> getDesktopIdList() {
        return desktopIdList;
    }

    public void setDesktopIdList(@Nullable List<UUID> desktopIdList) {
        this.desktopIdList = desktopIdList;
    }

    @Nullable
    public ImageUsageTypeEnum getImageUsage() {
        return imageUsage;
    }

    public void setImageUsage(@Nullable ImageUsageTypeEnum imageUsage) {
        this.imageUsage = imageUsage;
    }
}
