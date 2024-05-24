package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面会话使用记录DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 19:26
 *
 * @author yxq
 */
public class DesktopSessionLogDTO {

    private UUID id;

    /**
     * 桌面id
     */
    private UUID desktopId;

    /**
     * 桌面名称
     */
    private String desktopName;

    /**
     * 桌面池名称
     */
    private String desktopPoolName;

    /**
     * 桌面池模式（静态、动态）
     */
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型（VDI、第三方）
     */
    private CbbDesktopPoolType cbbDesktopPoolType;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 退出时间
     */
    private Date logoutTime;

    /**
     * 用户组id
     */
    private UUID userGroupId;

    /**
     * 用户id
     */
    private UUID userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户组名称
     */
    private String userGroupName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 桌面池、桌面组id
     */
    private UUID relatedId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public CbbDesktopPoolType getCbbDesktopPoolType() {
        return cbbDesktopPoolType;
    }

    public void setCbbDesktopPoolType(CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLogoutTime() {
        return logoutTime;
    }

    public void setLogoutTime(Date logoutTime) {
        this.logoutTime = logoutTime;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }
}
