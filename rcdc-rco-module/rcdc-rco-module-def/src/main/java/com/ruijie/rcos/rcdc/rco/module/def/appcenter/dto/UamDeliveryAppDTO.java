package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;

/**
 * Description: 交付应用
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 16:15
 *
 * @author coderLee23
 */
public class UamDeliveryAppDTO {

    private UUID id;

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 应用id
     */
    private UUID appId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用类型
     */
    private AppTypeEnum appType;

    /**
     * 交付状态
     */
    private DeliveryStatusEnum deliveryStatus;

    /**
     * 交付时间
     */
    private Date createTime;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public AppTypeEnum getAppType() {
        return appType;
    }

    public void setAppType(AppTypeEnum appType) {
        this.appType = appType;
    }

    public DeliveryStatusEnum getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatusEnum deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
