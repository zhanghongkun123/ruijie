package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.wifi.dto.WifiWhitelistConfigDTO;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;


/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/11/27 10:13
 *
 * @author conghaifeng
 */
public class IdvTerminalGroupDetailResponse extends DefaultResponse {


    private UUID id;

    private String name;

    private UUID parentId;

    private String parentName;

    /**
     *
     * IDV云桌面关联镜像模板id
     */
    private UUID idvDesktopImageId;

    private String idvDesktopImageName;

    /**
     * IDV云桌面关联策略模板id
     */
    private UUID idvDesktopStrategyId;

    private String idvDesktopStrategyName;

    private WifiWhitelistConfigDTO wifiWhitelistConfigDTO;

    /**
     *
     * VOI云桌面关联镜像模板id
     */
    private UUID voiDesktopImageId;

    /**
     * VOI 云桌面关 镜像名称
     */
    private String voiDesktopImageName;

    /**
     * VOI云桌面关联策略模板id
     */
    private UUID voiDesktopStrategyId;

    /**
     * VOI云桌面关联策略模板名称
     */
    private String voiDesktopStrategyName;

    /**
     * IDV用户配置策略ID
     */
    private UUID idvUserProfileStrategyId;

    /**
     * IDV用户配置策略名称
     */
    private String idvUserProfileStrategyName;

    /**
     * VOI用户配置策略ID
     */
    private UUID voiUserProfileStrategyId;

    /**
     * VOI用户配置策略名称
     */
    private String voiUserProfileStrategyName;

    /**
     * 软件管控策略ID
     */
    private UUID idvSoftwareStrategyId;

    /**
     * 软件管控策略名称
     */
    private String idvSoftwareStrategyName;

    /**
     * 软件管控策略ID
     */
    private UUID voiSoftwareStrategyId;

    /**
     * 软件管控策略名称
     */
    private String voiSoftwareStrategyName;


    public UUID getVoiDesktopImageId() {
        return voiDesktopImageId;
    }

    public void setVoiDesktopImageId(UUID voiDesktopImageId) {
        this.voiDesktopImageId = voiDesktopImageId;
    }

    public String getVoiDesktopImageName() {
        return voiDesktopImageName;
    }

    public void setVoiDesktopImageName(String voiDesktopImageName) {
        this.voiDesktopImageName = voiDesktopImageName;
    }

    public UUID getVoiDesktopStrategyId() {
        return voiDesktopStrategyId;
    }

    public void setVoiDesktopStrategyId(UUID voiDesktopStrategyId) {
        this.voiDesktopStrategyId = voiDesktopStrategyId;
    }

    public String getVoiDesktopStrategyName() {
        return voiDesktopStrategyName;
    }

    public void setVoiDesktopStrategyName(String voiDesktopStrategyName) {
        this.voiDesktopStrategyName = voiDesktopStrategyName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public UUID getIdvDesktopImageId() {
        return idvDesktopImageId;
    }

    public void setIdvDesktopImageId(UUID idvDesktopImageId) {
        this.idvDesktopImageId = idvDesktopImageId;
    }

    public String getIdvDesktopImageName() {
        return idvDesktopImageName;
    }

    public void setIdvDesktopImageName(String idvDesktopImageName) {
        this.idvDesktopImageName = idvDesktopImageName;
    }

    public UUID getIdvDesktopStrategyId() {
        return idvDesktopStrategyId;
    }

    public void setIdvDesktopStrategyId(UUID idvDesktopStrategyId) {
        this.idvDesktopStrategyId = idvDesktopStrategyId;
    }

    public String getIdvDesktopStrategyName() {
        return idvDesktopStrategyName;
    }

    public void setIdvDesktopStrategyName(String idvDesktopStrategyName) {
        this.idvDesktopStrategyName = idvDesktopStrategyName;
    }

    public WifiWhitelistConfigDTO getWifiWhitelistConfigDTO() {
        return wifiWhitelistConfigDTO;
    }

    public void setWifiWhitelistConfigDTO(WifiWhitelistConfigDTO wifiWhitelistConfigDTO) {
        this.wifiWhitelistConfigDTO = wifiWhitelistConfigDTO;
    }

    public UUID getIdvUserProfileStrategyId() {
        return idvUserProfileStrategyId;
    }

    public void setIdvUserProfileStrategyId(UUID idvUserProfileStrategyId) {
        this.idvUserProfileStrategyId = idvUserProfileStrategyId;
    }

    public String getIdvUserProfileStrategyName() {
        return idvUserProfileStrategyName;
    }

    public void setIdvUserProfileStrategyName(String idvUserProfileStrategyName) {
        this.idvUserProfileStrategyName = idvUserProfileStrategyName;
    }

    public UUID getVoiUserProfileStrategyId() {
        return voiUserProfileStrategyId;
    }

    public void setVoiUserProfileStrategyId(UUID voiUserProfileStrategyId) {
        this.voiUserProfileStrategyId = voiUserProfileStrategyId;
    }

    public String getVoiUserProfileStrategyName() {
        return voiUserProfileStrategyName;
    }

    public void setVoiUserProfileStrategyName(String voiUserProfileStrategyName) {
        this.voiUserProfileStrategyName = voiUserProfileStrategyName;
    }

    public UUID getVoiSoftwareStrategyId() {
        return voiSoftwareStrategyId;
    }

    public void setVoiSoftwareStrategyId(UUID voiSoftwareStrategyId) {
        this.voiSoftwareStrategyId = voiSoftwareStrategyId;
    }

    public String getVoiSoftwareStrategyName() {
        return voiSoftwareStrategyName;
    }

    public void setVoiSoftwareStrategyName(String voiSoftwareStrategyName) {
        this.voiSoftwareStrategyName = voiSoftwareStrategyName;
    }

    public UUID getIdvSoftwareStrategyId() {
        return idvSoftwareStrategyId;
    }

    public void setIdvSoftwareStrategyId(UUID idvSoftwareStrategyId) {
        this.idvSoftwareStrategyId = idvSoftwareStrategyId;
    }

    public String getIdvSoftwareStrategyName() {
        return idvSoftwareStrategyName;
    }

    public void setIdvSoftwareStrategyName(String idvSoftwareStrategyName) {
        this.idvSoftwareStrategyName = idvSoftwareStrategyName;
    }
}
