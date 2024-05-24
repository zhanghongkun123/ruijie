package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/10 18:30
 *
 * @author coderLee23
 */
public class TerminalGroupDesktopRelatedDTO {

    /**
     * 云桌面id
     */
    private UUID cloudDesktopId;

    /**
     * 会话类型
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    /**
     * 终端组id
     */
    private UUID terminalGroupId;

    /**
     * 云桌面名称
     */
    private String desktopName;

    /**
     * 终端名称
     */
    private String terminalName;

    /**
     * 镜像类型
     */
    private CbbCloudDeskPattern desktopType;

    /**
     * 云桌面状态
     */
    private CbbCloudDeskState deskState;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 终端状态
     */
    private CbbTerminalStateEnums state;

    /**
     * 镜像模板名称
     */
    private String imageTemplateName;

    /**
     * 规格：操作系统类型
     */
    private CbbOsType osType;

    /**
     * 操作系统版本号
     */
    private String osVersion;

    /**
     * 规格：镜像类型
     */
    private CbbImageType cbbImageType;

    /**
     * 桌面类型
     */
    private String deskType;

    /**
     * 终端ip
     */
    private String ip;


    /**
     * 用户组id
     */
    private UUID userGroupId;

    /**
     *  用户名
     */
    private String userName;

    /**
     *  不可选提示
     */
    private String notOptionalTip;

    public UUID getCloudDesktopId() {
        return cloudDesktopId;
    }

    public void setCloudDesktopId(UUID cloudDesktopId) {
        this.cloudDesktopId = cloudDesktopId;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public CbbCloudDeskPattern getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskPattern desktopType) {
        this.desktopType = desktopType;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public CbbTerminalStateEnums getState() {
        return state;
    }

    public void setState(CbbTerminalStateEnums state) {
        this.state = state;
    }

    public String getImageTemplateName() {
        return imageTemplateName;
    }

    public void setImageTemplateName(String imageTemplateName) {
        this.imageTemplateName = imageTemplateName;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public String getDeskType() {
        return deskType;
    }

    public void setDeskType(String deskType) {
        this.deskType = deskType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNotOptionalTip() {
        return notOptionalTip;
    }

    public void setNotOptionalTip(String notOptionalTip) {
        this.notOptionalTip = notOptionalTip;
    }
}
