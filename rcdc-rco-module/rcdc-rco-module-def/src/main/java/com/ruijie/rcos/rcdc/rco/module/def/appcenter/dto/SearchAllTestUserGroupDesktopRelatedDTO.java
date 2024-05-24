package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.List;
import java.util.UUID;

/**
 * Description: 查询测试组下的用户组和桌面关联关系
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/16
 *
 * @author zhiweiHong
 */
public class SearchAllTestUserGroupDesktopRelatedDTO {

    @NotNull
    private List<UUID> userGroupIdList;

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

    public List<UUID> getUserGroupIdList() {
        return userGroupIdList;
    }

    public void setUserGroupIdList(List<UUID> userGroupIdList) {
        this.userGroupIdList = userGroupIdList;
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
