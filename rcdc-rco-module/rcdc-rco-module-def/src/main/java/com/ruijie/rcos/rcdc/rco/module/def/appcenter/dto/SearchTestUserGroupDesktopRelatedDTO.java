package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

/**
 * Description: 查询测试组下的用户组和桌面关联关系
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/16
 *
 * @author zhiweiHong
 */
public class SearchTestUserGroupDesktopRelatedDTO extends BasePermissionDTO {

    private UUID userGroupId;

    /**
     * 支持云桌面和用户名模糊查询
     */
    private String searchName;

    /**
     * 操作系统类型
     */
    private CbbOsType osType;

    /**
     * 镜像类型
     */
    private CbbImageType cbbImageType;

    /**
     * 镜像类型
     */
    private DesktopTestStateEnum state;

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public DesktopTestStateEnum getState() {
        return state;
    }

    public void setState(DesktopTestStateEnum state) {
        this.state = state;
    }
}
