package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 19:10
 *
 * @author coderLee23
 */
public class SearchDeliveryAppDTO extends BasePermissionDTO {

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用名称（支持模糊）
     */
    private String appName;

    /**
     * 应用类型
     */
    private List<AppTypeEnum> appTypeList;

    /**
     * 交付状态
     */
    private List<DeliveryStatusEnum> deliveryStatusList;

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public List<AppTypeEnum> getAppTypeList() {
        return appTypeList;
    }

    public void setAppTypeList(List<AppTypeEnum> appTypeList) {
        this.appTypeList = appTypeList;
    }

    public List<DeliveryStatusEnum> getDeliveryStatusList() {
        return deliveryStatusList;
    }

    public void setDeliveryStatusList(List<DeliveryStatusEnum> deliveryStatusList) {
        this.deliveryStatusList = deliveryStatusList;
    }
}
