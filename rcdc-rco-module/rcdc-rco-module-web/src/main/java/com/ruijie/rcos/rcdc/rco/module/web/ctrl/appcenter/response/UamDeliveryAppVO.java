package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;

import java.util.Date;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author 徐国祥
 */
public class UamDeliveryAppVO extends PlatformBaseInfoDTO {

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
