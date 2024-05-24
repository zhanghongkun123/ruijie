package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDTO;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/26 13:53
 *
 * @author conghaifeng
 */
public class IdvCreateTerminalGroupRequest extends CbbTerminalGroupDTO {

    /**
     *
     * 云桌面关联镜像模板id
     */
    @Nullable
    private UUID cbbIdvDesktopImageId;

    /**
     * 云桌面关联策略模板id
     */
    @Nullable
    private UUID cbbIdvDesktopStrategyId;

    @Nullable
    private UUID cbbVoiDesktopImageId;

    /**
     * VOI云桌面关联策略模板id
     */
    @Nullable
    private UUID cbbVoiDesktopStrategyId;

    @Nullable
    private List<WifiWhitelistDTO> wifiWhitelistDTOList;

    /**
     * IDV桌面用户配置策略
     */
    @Nullable
    private UUID cbbIdvUserProfileStrategyId;

    /**
     * VOI桌面用户配置策略
     */
    @Nullable
    private UUID cbbVoiUserProfileStrategyId;

    @Nullable
    private UUID idvSoftwareStrategyId;

    @Nullable
    private UUID voiSoftwareStrategyId;


    @Nullable
    public UUID getCbbIdvDesktopImageId() {
        return cbbIdvDesktopImageId;
    }

    public void setCbbIdvDesktopImageId(@Nullable UUID cbbIdvDesktopImageId) {
        this.cbbIdvDesktopImageId = cbbIdvDesktopImageId;
    }

    @Nullable
    public UUID getCbbIdvDesktopStrategyId() {
        return cbbIdvDesktopStrategyId;
    }

    public void setCbbIdvDesktopStrategyId(@Nullable UUID cbbIdvDesktopStrategyId) {
        this.cbbIdvDesktopStrategyId = cbbIdvDesktopStrategyId;
    }

    @Nullable
    public List<WifiWhitelistDTO> getWifiWhitelistDTOList() {
        return wifiWhitelistDTOList;
    }

    public void setWifiWhitelistDTOList(@Nullable List<WifiWhitelistDTO> wifiWhitelistDTOList) {
        this.wifiWhitelistDTOList = wifiWhitelistDTOList;
    }

    @Nullable
    public UUID getCbbVoiDesktopImageId() {
        return cbbVoiDesktopImageId;
    }

    public void setCbbVoiDesktopImageId(@Nullable UUID cbbVoiDesktopImageId) {
        this.cbbVoiDesktopImageId = cbbVoiDesktopImageId;
    }

    @Nullable
    public UUID getCbbVoiDesktopStrategyId() {
        return cbbVoiDesktopStrategyId;
    }

    public void setCbbVoiDesktopStrategyId(@Nullable UUID cbbVoiDesktopStrategyId) {
        this.cbbVoiDesktopStrategyId = cbbVoiDesktopStrategyId;
    }

    @Nullable
    public UUID getIdvSoftwareStrategyId() {
        return idvSoftwareStrategyId;
    }

    public void setIdvSoftwareStrategyId(@Nullable UUID idvSoftwareStrategyId) {
        this.idvSoftwareStrategyId = idvSoftwareStrategyId;
    }

    @Nullable
    public UUID getVoiSoftwareStrategyId() {
        return voiSoftwareStrategyId;
    }

    public void setVoiSoftwareStrategyId(@Nullable UUID voiSoftwareStrategyId) {
        this.voiSoftwareStrategyId = voiSoftwareStrategyId;
    }

    @Nullable
    public UUID getCbbIdvUserProfileStrategyId() {
        return cbbIdvUserProfileStrategyId;
    }

    public void setCbbIdvUserProfileStrategyId(@Nullable UUID cbbIdvUserProfileStrategyId) {
        this.cbbIdvUserProfileStrategyId = cbbIdvUserProfileStrategyId;
    }

    @Nullable
    public UUID getCbbVoiUserProfileStrategyId() {
        return cbbVoiUserProfileStrategyId;
    }

    public void setCbbVoiUserProfileStrategyId(@Nullable UUID cbbVoiUserProfileStrategyId) {
        this.cbbVoiUserProfileStrategyId = cbbVoiUserProfileStrategyId;
    }
}
