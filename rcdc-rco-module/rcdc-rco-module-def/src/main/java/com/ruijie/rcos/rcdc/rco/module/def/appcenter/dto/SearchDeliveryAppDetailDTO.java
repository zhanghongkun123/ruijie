package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 19:10
 *
 * @author coderLee23
 */
public class SearchDeliveryAppDetailDTO {

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用id
     */
    private UUID appId;

    /**
     * 云桌面名称（支持模糊）
     */
    private String cloudDesktopName;

    /**
     * 云桌面状态
     */
    private List<CbbCloudDeskState> deskStateList;

    /**
     * 交付状态
     */
    private List<DeliveryStatusEnum> deliveryStatusList;

    /**
     * 云桌面状态
     */
    private List<ProgressStatusEnum> progressStatusList;

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public UUID getAppId() {
        return appId;
    }

    public void setAppId(UUID appId) {
        this.appId = appId;
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

    public List<ProgressStatusEnum> getProgressStatusList() {
        return progressStatusList;
    }

    public void setProgressStatusList(List<ProgressStatusEnum> progressStatusList) {
        this.progressStatusList = progressStatusList;
    }
}
