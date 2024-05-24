package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/28 16:40
 *
 * @author coderLee23
 */
public class AddDeliveryObjectToDiskGroupRequest implements WebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID deliveryGroupId;

    /**
     * 云桌面id列表
     */
    @ApiModelProperty(value = "云桌面id列表", required = true)
    @NotEmpty
    private List<UUID> cloudDesktopIdList;


    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public List<UUID> getCloudDesktopIdList() {
        return cloudDesktopIdList;
    }

    public void setCloudDesktopIdList(List<UUID> cloudDesktopIdList) {
        this.cloudDesktopIdList = cloudDesktopIdList;
    }
}
