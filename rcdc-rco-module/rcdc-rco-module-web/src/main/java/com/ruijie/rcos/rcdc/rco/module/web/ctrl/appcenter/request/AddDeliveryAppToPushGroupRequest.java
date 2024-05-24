package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 为推送安装包交付组添加交付应用
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 16:40
 *
 * @author coderLee23
 */
public class AddDeliveryAppToPushGroupRequest implements WebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID deliveryGroupId;

    /**
     * 应用appid列表
     */
    @ApiModelProperty(value = "appId列表", required = true)
    @NotEmpty
    private List<UUID> appIdList;

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<UUID> appIdList) {
        this.appIdList = appIdList;
    }
}
