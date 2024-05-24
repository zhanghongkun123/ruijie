package com.ruijie.rcos.rcdc.rco.module.impl.entity;


import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

/**
 * 终端持久化实体
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年10月30日
 * 
 * @author chenzj
 */
@Entity
@Table(name = "t_rco_user_terminal")
public class UserTerminalEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String terminalId;

    private UUID userId;

    private boolean hasLogin;

    private Date createTime;

    @Version
    private int version;

    private UUID bindUserId;

    private Date bindUserTime;

    private String bindUserName;

    private UUID bindDeskId;

    private Boolean enableVisitorLogin;

    private Boolean enableAutoLogin;

    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums terminalMode;

    /**
     * 开机模式
     */
    private String bootType;

    @Enumerated(EnumType.STRING)
    private CbbTerminalPlatformEnums terminalPlatform;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public boolean isHasLogin() {
        return hasLogin;
    }

    public void setHasLogin(boolean hasLogin) {
        this.hasLogin = hasLogin;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public UUID getBindUserId() {
        return bindUserId;
    }

    public void setBindUserId(UUID bindUserId) {
        this.bindUserId = bindUserId;
    }

    public Date getBindUserTime() {
        return bindUserTime;
    }

    public void setBindUserTime(Date bindUserTime) {
        this.bindUserTime = bindUserTime;
    }

    public String getBindUserName() {
        return bindUserName;
    }

    public void setBindUserName(String bindUserName) {
        this.bindUserName = bindUserName;
    }

    public UUID getBindDeskId() {
        return bindDeskId;
    }

    public void setBindDeskId(UUID bindDeskId) {
        this.bindDeskId = bindDeskId;
    }

    public Boolean getEnableVisitorLogin() {
        return enableVisitorLogin;
    }

    public void setEnableVisitorLogin(Boolean enableVisitorLogin) {
        this.enableVisitorLogin = enableVisitorLogin;
    }

    public Boolean getEnableAutoLogin() {
        return enableAutoLogin;
    }

    public void setEnableAutoLogin(Boolean enableAutoLogin) {
        this.enableAutoLogin = enableAutoLogin;
    }

    public IdvTerminalModeEnums getTerminalMode() {
        return terminalMode;
    }

    public void setTerminalMode(IdvTerminalModeEnums terminalMode) {
        this.terminalMode = terminalMode;
    }

    public String getBootType() {
        return bootType;
    }

    public void setBootType(String bootType) {
        this.bootType = bootType;
    }

    public CbbTerminalPlatformEnums getTerminalPlatform() {
        return terminalPlatform;
    }

    public void setTerminalPlatform(CbbTerminalPlatformEnums terminalPlatform) {
        this.terminalPlatform = terminalPlatform;
    }
}

