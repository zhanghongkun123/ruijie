package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * Description: IDV云桌面DTO
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月3日
 *
 * @author brq
 */
public class IDVCloudDesktopDTO {

    @Nullable
    private UUID deskId;

    @Nullable
    private UUID userId;

    @Nullable
    private String userName;

    @Nullable
    private UUID terminalGroupId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

    @NotNull
    private UUID strategyId;

    @NotNull
    private UUID imageId;

    @Nullable
    private String desktopName;

    @NotBlank
    private String terminalId;

    @NotBlank
    private String terminalMac;

    /**
     * 用户配置策略ID
     **/
    @Nullable
    private UUID userProfileStrategyId;

    /**
     * 软件管控策略ID
     */
    @Nullable
    private UUID softwareStrategyId;

    private UUID realUseImageId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
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

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    public void setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
    }

    public UUID getImageId() {
        return imageId;
    }

    public void setImageId(UUID imageId) {
        this.imageId = imageId;
    }

    public String getDesktopName() {
        return desktopName;
    }

    public void setDesktopName(String desktopName) {
        this.desktopName = desktopName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTerminalMac() {
        return terminalMac;
    }

    public void setTerminalMac(String terminalMac) {
        this.terminalMac = terminalMac;
    }

    @Nullable
    public UUID getSoftwareStrategyId() {
        return softwareStrategyId;
    }

    public void setSoftwareStrategyId(@Nullable UUID softwareStrategyId) {
        this.softwareStrategyId = softwareStrategyId;
    }

    @Nullable
    public UUID getUserProfileStrategyId() {
        return userProfileStrategyId;
    }

    public void setUserProfileStrategyId(@Nullable UUID userProfileStrategyId) {
        this.userProfileStrategyId = userProfileStrategyId;
    }

    public UUID getRealUseImageId() {
        return realUseImageId;
    }

    public void setRealUseImageId(UUID realUseImageId) {
        this.realUseImageId = realUseImageId;
    }
}
