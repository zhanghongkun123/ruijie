package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopSessionLogState;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 云桌面会话使用记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 19:26
 *
 * @author yxq
 */
@Entity
@Table(name = "t_rco_desktop_session_log")
public class DesktopSessionLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
     * 桌面池、桌面组id
     */
    private UUID relatedId;

    /**
     * 桌面池名称
     */
    private String desktopPoolName;

    /**
     * 桌面池模式（动态、静态）
     */
    @Enumerated(EnumType.STRING)
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型（VDI、第三方）
     */
    @Enumerated(EnumType.STRING)
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
     * 版本号
     */
    @Version
    private int version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 会话id
     */
    private Long connectionId;

    private String terminalId;

    /**
     * 会话类型
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType desktopSessionType;

    @Enumerated(EnumType.STRING)
    private DesktopSessionLogState state;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID deskId) {
        this.desktopId = deskId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String deskName) {
        this.desktopName = deskName;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType deskPoolType) {
        this.desktopPoolType = deskPoolType;
    }

    public CbbDesktopPoolType getCbbDesktopPoolType() {
        return cbbDesktopPoolType;
    }

    public void setCbbDesktopPoolType(CbbDesktopPoolType cbbDesktopPoolType) {
        this.cbbDesktopPoolType = cbbDesktopPoolType;
    }

    public CbbDesktopSessionType getDesktopSessionType() {
        return desktopSessionType;
    }

    public void setDesktopSessionType(CbbDesktopSessionType desktopSessionType) {
        this.desktopSessionType = desktopSessionType;
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

    public void setUserGroupId(UUID groupId) {
        this.userGroupId = groupId;
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

    public void setUserGroupName(String groupName) {
        this.userGroupName = groupName;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public void setRelatedId(UUID desktopPoolId) {
        this.relatedId = desktopPoolId;
    }

    public Long getConnectionId() {
        return connectionId;
    }

    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public DesktopSessionLogState getState() {
        return state;
    }

    public void setState(DesktopSessionLogState state) {
        this.state = state;
    }
}
