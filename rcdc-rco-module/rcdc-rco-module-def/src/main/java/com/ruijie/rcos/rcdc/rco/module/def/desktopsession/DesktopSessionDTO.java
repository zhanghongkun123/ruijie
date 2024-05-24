package com.ruijie.rcos.rcdc.rco.module.def.desktopsession;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.enums.SessionStatueEnum;
import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Version;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月27日
 *
 * @author wangjie9
 */
public class DesktopSessionDTO extends EqualsHashcodeSupport implements Serializable {

    private UUID id;

    /**
     * 桌面id
     */
    private UUID deskId;

    /**
     * 桌面名称
     */
    private String desktopName;

    /**
     * 终端IP
     */
    private String terminalIp;

    /**
     * 桌面池id
     */
    private UUID desktopPoolId;

    /**
     * 桌面池名称
     */
    private String desktopPoolName;

    /**
     * 桌面池模式
     */
    private String desktopPoolModel;

    /**
     * 会话状态：ONLINE 在线 DESTROYING 注销中
     */
    private SessionStatueEnum sessionStatus;

    private CbbCloudDeskState deskState;

    /**
     * 用户ID
     */
    private UUID userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户姓名
     */
    private String realName;

    /**
     * 用户组ID
     */
    private UUID userGroupId;

    /**
     * 用户组名
     */
    private String userGroupName;

    /**
     * 会话ID
     */
    private Integer sessionId;

    /**
     * 会话最近建立时间
     */
    private Date lastCreateTime;

    /**
     * 会话最近建立时间 距今秒时间
     */
    private Long lastCreateTimeSecond;

    /**
     * 会话最近空闲开始时间
     */
    private Date lastIdleTime;

    /**
     * 会话最近空闲开始时间 距今秒时间
     */
    private Long lastIdleTimeSecond;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
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

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Date getLastCreateTime() {
        return lastCreateTime;
    }

    public void setLastCreateTime(Date lastCreateTime) {
        this.lastCreateTime = lastCreateTime;
    }

    public Long getLastCreateTimeSecond() {
        return lastCreateTimeSecond;
    }

    public void setLastCreateTimeSecond(Long lastCreateTimeSecond) {
        this.lastCreateTimeSecond = lastCreateTimeSecond;
    }

    public Date getLastIdleTime() {
        return lastIdleTime;
    }

    public void setLastIdleTime(Date lastIdleTime) {
        this.lastIdleTime = lastIdleTime;
    }

    public Long getLastIdleTimeSecond() {
        return lastIdleTimeSecond;
    }

    public void setLastIdleTimeSecond(Long lastIdleTimeSecond) {
        this.lastIdleTimeSecond = lastIdleTimeSecond;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public SessionStatueEnum getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(SessionStatueEnum sessionStatus) {
        this.sessionStatus = sessionStatus;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public String getDesktopPoolModel() {
        return desktopPoolModel;
    }

    public void setDesktopPoolModel(String desktopPoolModel) {
        this.desktopPoolModel = desktopPoolModel;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    @Override
    public String toString() {
        return "DesktopSessionDTO{" +
                "id=" + id +
                ", deskId=" + deskId +
                ", desktopName='" + desktopName + '\'' +
                ", terminalIp='" + terminalIp + '\'' +
                ", desktopPoolId=" + desktopPoolId +
                ", desktopPoolName='" + desktopPoolName + '\'' +
                ", desktopPoolModel='" + desktopPoolModel + '\'' +
                ", sessionStatus=" + sessionStatus +
                ", userId=" + userId +
                ", userName='" + userName + '\'' +
                ", realName='" + realName + '\'' +
                ", userGroupId=" + userGroupId +
                ", userGroupName='" + userGroupName + '\'' +
                ", sessionId=" + sessionId +
                ", lastCreateTime=" + lastCreateTime +
                ", lastCreateTimeSecond=" + lastCreateTimeSecond +
                ", lastIdleTime=" + lastIdleTime +
                ", lastIdleTimeSecond=" + lastIdleTimeSecond +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
