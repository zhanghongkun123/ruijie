package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 14:05
 *
 * @author coderLee23
 */
public class GetDeliveryAppDetailPageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID deliveryGroupId;

    /**
     * 应用id
     */
    @ApiModelProperty(value = " 应用id", required = true)
    @NotNull
    private UUID appId;

    /**
     * 云桌面状态
     */
    @ApiModelProperty(value = " 云桌面状态", name = "deskState")
    @Nullable
    @JSONField(name = "deskState")
    private List<CbbCloudDeskState> deskStateList;

    /**
     * 交付状态
     */
    @ApiModelProperty(value = " 交付状态", name = "deliveryStatus")
    @Nullable
    @JSONField(name = "deliveryStatus")
    private List<DeliveryStatusEnum> deliveryStatusList;


    /**
     * 云桌面状态
     */
    @ApiModelProperty(value = " 执行状态", name = "progressStatus")
    @Nullable
    @JSONField(name = "progressStatus")
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

    @Nullable
    public List<CbbCloudDeskState> getDeskStateList() {
        return deskStateList;
    }

    public void setDeskStateList(@Nullable List<CbbCloudDeskState> deskStateList) {
        this.deskStateList = deskStateList;
    }

    @Nullable
    public List<DeliveryStatusEnum> getDeliveryStatusList() {
        return deliveryStatusList;
    }

    public void setDeliveryStatusList(@Nullable List<DeliveryStatusEnum> deliveryStatusList) {
        this.deliveryStatusList = deliveryStatusList;
    }

    @Nullable
    public List<ProgressStatusEnum> getProgressStatusList() {
        return progressStatusList;
    }

    public void setProgressStatusList(@Nullable List<ProgressStatusEnum> progressStatusList) {
        this.progressStatusList = progressStatusList;
    }
}
