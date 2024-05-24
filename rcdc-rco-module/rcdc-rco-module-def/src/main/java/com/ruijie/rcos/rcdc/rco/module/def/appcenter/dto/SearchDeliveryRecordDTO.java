package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 19:10
 *
 * @author coderLee23
 */
public class SearchDeliveryRecordDTO {

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用名称（支持模糊）
     */
    private String cloudDesktopName;

    /**
     * /**
     * 规格：镜像类型
     */
    private List<CbbImageType> cbbImageTypeList;

    /**
     * 云桌面类型
     */
    private List<CbbCloudDeskPattern> desktopTypeList;

    /**
     * 交付状态
     */
    private List<ProgressStatusEnum> progressStatusList;

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public String getCloudDesktopName() {
        return cloudDesktopName;
    }

    public void setCloudDesktopName(String cloudDesktopName) {
        this.cloudDesktopName = cloudDesktopName;
    }

    public List<CbbImageType> getCbbImageTypeList() {
        return cbbImageTypeList;
    }

    public void setCbbImageTypeList(List<CbbImageType> cbbImageTypeList) {
        this.cbbImageTypeList = cbbImageTypeList;
    }

    public List<CbbCloudDeskPattern> getDesktopTypeList() {
        return desktopTypeList;
    }

    public void setDesktopTypeList(List<CbbCloudDeskPattern> desktopTypeList) {
        this.desktopTypeList = desktopTypeList;
    }

    public List<ProgressStatusEnum> getProgressStatusList() {
        return progressStatusList;
    }

    public void setProgressStatusList(List<ProgressStatusEnum> progressStatusList) {
        this.progressStatusList = progressStatusList;
    }
}
