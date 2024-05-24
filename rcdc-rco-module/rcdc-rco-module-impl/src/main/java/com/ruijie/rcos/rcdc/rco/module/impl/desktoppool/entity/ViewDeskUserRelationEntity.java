package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import java.util.UUID;

/**
 * Description: 桌面关联的所有用户关系视图
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月15日
 *
 * @author wangjie9
 */
@Entity
@Table(name = "v_rco_desk_user_relation")
public class ViewDeskUserRelationEntity {

    /**
     * 云桌面id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID deskId;

    /**
     * 桌面池id
     */
    private UUID desktopPoolId;

    /**
     * 云桌面状态
     */
    @Enumerated(EnumType.STRING)
    private CbbCloudDeskState deskState;

    /**
     * 关联用户id
     */
    private UUID userId;

    /**
     * 会话类型
     */
    @Enumerated(EnumType.STRING)
    private CbbDesktopSessionType sessionType;

    /**
     * 桌面池类型
     */
    @Enumerated(EnumType.STRING)
    private DesktopPoolType desktopPoolType;

    @Version
    private Integer version;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getDesktopPoolId() {
        return desktopPoolId;
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }

    public CbbCloudDeskState getDeskState() {
        return deskState;
    }

    public void setDeskState(CbbCloudDeskState deskState) {
        this.deskState = deskState;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public CbbDesktopSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(CbbDesktopSessionType sessionType) {
        this.sessionType = sessionType;
    }

    public DesktopPoolType getDesktopPoolType() {
        return desktopPoolType;
    }

    public void setDesktopPoolType(DesktopPoolType desktopPoolType) {
        this.desktopPoolType = desktopPoolType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
