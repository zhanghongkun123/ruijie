package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/03 14:56
 *
 * @author coderLee23
 */
public class HomePageSearchDeliveryGroupDTO extends BasePermissionDTO {

    /**
     * 跳转使用
     */
    private UUID id;

    /**
     * 支持应用名称模糊查询，忽略大小写
     */
    private String deliveryGroupName;


    /**
     *  应用交付类型
     */
    private AppDeliveryTypeEnum appDeliveryType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDeliveryGroupName() {
        return deliveryGroupName;
    }

    public void setDeliveryGroupName(String deliveryGroupName) {
        this.deliveryGroupName = deliveryGroupName;
    }

    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }
}
