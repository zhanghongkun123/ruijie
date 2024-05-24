package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.Date;
import java.util.UUID;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.PlatformBaseInfoDTO;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/04 16:15
 *
 * @author coderLee23
 */
public class UamDeliveryObjectDetailDTO extends PlatformBaseInfoDTO {

    private UUID id;

    /**
     * 交付组id
     */
    private UUID deliveryGroupId;

    /**
     * 云桌面id
     */
    private UUID cloudDesktopId;

    /**
     * 交付应用id
     */
    private UUID appId;

    /**
     * 云桌面名称
     */
    private String cloudDesktopName;

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
     * 进度状态
     */
    private ProgressStatusEnum progressStatus;

    /**
     * 进度描述
     */
    private String progressDesc;

    /**
     * 安装时间
     */
    private Date installTime;

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

    public UUID getCloudDesktopId() {
        return cloudDesktopId;
    }

    public void setCloudDesktopId(UUID cloudDesktopId) {
        this.cloudDesktopId = cloudDesktopId;
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

    public Date getInstallTime() {
        return installTime;
    }

    public void setInstallTime(Date installTime) {
        this.installTime = installTime;
    }

    public ProgressStatusEnum getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(ProgressStatusEnum progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getProgressDesc() {
        return progressDesc;
    }

    public void setProgressDesc(String progressDesc) {
        this.progressDesc = progressDesc;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
