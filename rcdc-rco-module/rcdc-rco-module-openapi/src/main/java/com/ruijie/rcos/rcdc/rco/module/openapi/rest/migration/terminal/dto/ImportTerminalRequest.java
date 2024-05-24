package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.terminal.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.common.ServerBusinessName;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import org.springframework.lang.Nullable;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author linhj
 */
@ServerBusinessName("终端信息导入")
public class ImportTerminalRequest {

    @NotNull
    private IdvTerminalModeEnums idvTerminalMode;

    @NotNull
    private Long id;

    @NotNull
    private Long terminalGroupId;

    @NotBlank
    @TextShort
    private String terminalName;

    @NotBlank
    @TextShort
    private String terminalIp;

    @NotBlank
    @TextShort
    private String terminalMac;

    @NotBlank
    private String cpuType;

    @NotBlank
    @TextShort
    private String terminalGroupName;

    @Nullable
    private Long userId;

    @Nullable
    private String userName;

    @Nullable
    private String productType;

    @Nullable
    private String productId;

    @Nullable
    private String serialNumber;

    @Nullable
    private String computerName;

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public Long getUserId() {
        return userId;
    }

    public void setUserId(@Nullable Long userId) {
        this.userId = userId;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(Long terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public String getTerminalIp() {
        return terminalIp;
    }

    public void setTerminalIp(String terminalIp) {
        this.terminalIp = terminalIp;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    public String getTerminalGroupName() {
        return terminalGroupName;
    }

    public void setTerminalGroupName(String terminalGroupName) {
        this.terminalGroupName = terminalGroupName;
    }

    @Nullable
    public String getProductType() {
        return productType;
    }

    public void setProductType(@Nullable String productType) {
        this.productType = productType;
    }

    @Nullable
    public String getProductId() {
        return productId;
    }

    public void setProductId(@Nullable String productId) {
        this.productId = productId;
    }

    @Nullable
    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(@Nullable String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getComputerName() {
        return computerName;
    }

    public void setComputerName(String computerName) {
        this.computerName = computerName;
    }
}
