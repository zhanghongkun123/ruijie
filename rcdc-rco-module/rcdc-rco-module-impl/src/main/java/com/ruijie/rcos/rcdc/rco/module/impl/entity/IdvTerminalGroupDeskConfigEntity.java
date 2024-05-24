package com.ruijie.rcos.rcdc.rco.module.impl.entity;

import java.util.UUID;

import javax.persistence.*;

import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 14:33
 *
 * @author conghaifeng
 */
@Entity
@Table(name = "t_rco_user_terminal_group_desk_config")
public class IdvTerminalGroupDeskConfigEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * 终端组ID
     */
    private UUID cbbTerminalGroupId;

    @Version
    private Integer version;

    private UUID cbbIdvDesktopStrategyId;

    private UUID cbbIdvDesktopImageId;

    /**
     *桌面类型 IDV|VOI|VDI
     */
    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums deskType;

    /**
     * 用户配置策略ID
     **/
    private UUID userProfileStrategyId;


    /**
     * 软件管控策略ID
     */
    private UUID softwareStrategyId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public CbbTerminalPlatformEnums getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbTerminalPlatformEnums deskType) {
        this.deskType = deskType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getCbbTerminalGroupId() {
        return cbbTerminalGroupId;
    }

    public void setCbbTerminalGroupId(UUID cbbTerminalGroupId) {
        this.cbbTerminalGroupId = cbbTerminalGroupId;
    }

    public UUID getCbbIdvDesktopStrategyId() {
        return cbbIdvDesktopStrategyId;
    }

    public void setCbbIdvDesktopStrategyId(UUID cbbIdvDesktopStrategyId) {
        this.cbbIdvDesktopStrategyId = cbbIdvDesktopStrategyId;
    }

    public UUID getCbbIdvDesktopImageId() {
        return cbbIdvDesktopImageId;
    }

    public void setCbbIdvDesktopImageId(UUID cbbIdvDesktopImageId) {
        this.cbbIdvDesktopImageId = cbbIdvDesktopImageId;
    }

    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }
}
