package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.AllotDesktopPoolFaultTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.RelateTypeEnum;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * Description: 分配云桌面失败记录表
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13 19:26
 *
 * @author yxq
 */
@Entity
@Table(name = "t_rco_user_connect_desktop_fault_log")
public class UserConnectDesktopFaultLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

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
    @Enumerated(EnumType.STRING)
    private RelateTypeEnum relatedType;

    /**
     * 关联id：桌面池id，桌面组id
     */
    private UUID relatedId;

    /**
     * 故障类型：资源不足、其它
     */
    @Enumerated(EnumType.STRING)
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
     * 版本号
     */
    @Version
    private Integer version;

    /**
     * 桌面池模式（静态、动态）
     */
    @Enumerated(EnumType.STRING)
    private DesktopPoolType desktopPoolType;

    /**
     * 桌面池类型（VDI、第三方）
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopPoolType cbbDesktopPoolType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setUserGroupId(UUID groupId) {
        this.userGroupId = groupId;
    }

    public String getUserGroupName() {
        return userGroupName;
    }

    public void setUserGroupName(String groupName) {
        this.userGroupName = groupName;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getDesktopPoolName() {
        return desktopPoolName;
    }

    public void setDesktopPoolName(String desktopPoolName) {
        this.desktopPoolName = desktopPoolName;
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