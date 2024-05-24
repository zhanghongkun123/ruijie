package com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.AllotDesktopPoolFaultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 创建连接失败记录请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/22 10:37
 *
 * @author yxq
 */
public class CreateConnectFaultLogRequest {
    /**
     * 用户id
     */
    private UUID userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 桌面池名称
     */
    private String desktopPoolName;

    /**
     * 关联类型：桌面池
     */
    private RelateTypeEnum relatedType;

    /**
     * 关联id：桌面池id，桌面组id
     */
    private UUID relatedId;

    /**
     * 故障类型：资源不足、其它
     */
    private AllotDesktopPoolFaultTypeEnum faultType;

    /**
     * 故障描述
     */
    private String faultDesc;

    /**
     * 故障时间
     */
    private Date faultTime;

    /**
     * 用户组id
     */
    private UUID userGroupId;

    /**
     * 用户组时间
     */
    private String userGroupName;

    /**
     * 桌面池模式（静态、动态）
     */
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型（VDI、第三方）
     */
    private CbbDesktopPoolType cbbDesktopPoolType;

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

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
    }

    public RelateTypeEnum getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(RelateTypeEnum relatedType) {
        this.relatedType = relatedType;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    public AllotDesktopPoolFaultTypeEnum getFaultType() {
        return faultType;
    }

    public void setFaultType(AllotDesktopPoolFaultTypeEnum faultType) {
        this.faultType = faultType;
    }

    public String getFaultDesc() {
        return faultDesc;
    }

    public void setFaultDesc(String faultDesc) {
        this.faultDesc = faultDesc;
    }

    public Date getFaultTime() {
        return faultTime;
    }

    public void setFaultTime(Date faultTime) {
        this.faultTime = faultTime;
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
}
