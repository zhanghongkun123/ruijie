package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
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
public class GetDeliveryObjectPageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID deliveryGroupId;

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

    @ApiModelProperty(value = "云平台状态", name = "platformStatus")
    @JSONField(name = "platformStatus")
    @Nullable
    private List<CloudPlatformStatus> platformStatusList;


    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
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
    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(@Nullable List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }
}
