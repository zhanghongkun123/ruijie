package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/3/18
 *
 * @author Jarman
 */
public class VdiDesktopDetailResponse extends DefaultResponse {

    private UUID desktopImageId;

    private String desktopImageName;

    private UUID desktopStrategyId;

    private String desktopStrategyName;

    private UUID desktopNetworkId;

    private String desktopNetworkName;
    
    private Boolean canEdit;

    public UUID getDesktopImageId() {
        return desktopImageId;
    }

    public void setDesktopImageId(UUID desktopImageId) {
        this.desktopImageId = desktopImageId;
    }

    public String getDesktopImageName() {
        return desktopImageName;
    }

    public void setDesktopImageName(String desktopImageName) {
        this.desktopImageName = desktopImageName;
    }

    public UUID getDesktopStrategyId() {
        return desktopStrategyId;
    }

    public void setDesktopStrategyId(UUID desktopStrategyId) {
        this.desktopStrategyId = desktopStrategyId;
    }

    public String getDesktopStrategyName() {
        return desktopStrategyName;
    }

    public void setDesktopStrategyName(String desktopStrategyName) {
        this.desktopStrategyName = desktopStrategyName;
    }

    public UUID getDesktopNetworkId() {
        return desktopNetworkId;
    }

    public void setDesktopNetworkId(UUID desktopNetworkId) {
        this.desktopNetworkId = desktopNetworkId;
    }

    public String getDesktopNetworkName() {
        return desktopNetworkName;
    }

    public void setDesktopNetworkName(String desktopNetworkName) {
        this.desktopNetworkName = desktopNetworkName;
    }

    public Boolean getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Boolean canEdit) {
        this.canEdit = canEdit;
    }
    
}
