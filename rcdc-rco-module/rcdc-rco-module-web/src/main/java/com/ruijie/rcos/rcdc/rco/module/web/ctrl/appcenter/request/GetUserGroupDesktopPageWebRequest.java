package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/10 19:54
 *
 * @author coderLee23
 */
public class GetUserGroupDesktopPageWebRequest extends PageWebRequest {

    /**
     * 用户组id
     */
    @ApiModelProperty(value = " 用户组id", required = false)
    @Nullable
    private UUID userGroupId;

    /**
     * 交付组id
     */
    @ApiModelProperty(value = "交付组id")
    @Nullable
    private UUID deliveryGroupId;

    /**
     * 规格：操作系统类型
     */
    @ApiModelProperty(value = "操作系统类型")
    @Nullable
    private CbbOsType osType;

    /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = "镜像类型")
    @Nullable
    private CbbImageType cbbImageType;

    public UUID getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UUID userGroupId) {
        this.userGroupId = userGroupId;
    }

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    public CbbOsType getOsType() {
        return osType;
    }

    public void setOsType(CbbOsType osType) {
        this.osType = osType;
    }

    public CbbImageType getCbbImageType() {
        return cbbImageType;
    }

    public void setCbbImageType(CbbImageType cbbImageType) {
        this.cbbImageType = cbbImageType;
    }
}
