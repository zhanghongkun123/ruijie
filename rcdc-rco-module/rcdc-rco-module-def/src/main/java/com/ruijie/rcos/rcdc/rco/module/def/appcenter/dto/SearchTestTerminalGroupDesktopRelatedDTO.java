package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;

import java.util.UUID;

/**
 * Description: 查询测试组下的终端组和桌面关联关系
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/16
 *
 * @author zhiweiHong
 */
public class SearchTestTerminalGroupDesktopRelatedDTO extends BasePermissionDTO {

    private UUID terminalGroupId;

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

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }
}
