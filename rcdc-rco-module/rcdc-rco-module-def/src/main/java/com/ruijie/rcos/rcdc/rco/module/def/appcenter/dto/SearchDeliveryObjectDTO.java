package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 19:10
 *
 * @author coderLee23
 */
public class SearchDeliveryObjectDTO {

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用名称（支持模糊）
     */
    private String cloudDesktopName;


    private List<CbbCloudDeskState> deskStateList;


    private List<DeliveryStatusEnum> deliveryStatusList;

    private List<CloudPlatformStatus> platformStatusList;

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

    public List<CbbCloudDeskState> getDeskStateList() {
        return deskStateList;
    }

    public void setDeskStateList(List<CbbCloudDeskState> deskStateList) {
        this.deskStateList = deskStateList;
    }

    public List<DeliveryStatusEnum> getDeliveryStatusList() {
        return deliveryStatusList;
    }

    public void setDeliveryStatusList(List<DeliveryStatusEnum> deliveryStatusList) {
        this.deliveryStatusList = deliveryStatusList;
    }

    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
