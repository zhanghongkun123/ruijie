package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
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
public class GetDeliveryGroupPageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = "交付组id")
    @Nullable
    private UUID id;

    /**
     * 交付应用id
     */
    @ApiModelProperty(value = "交付应用id")
    @Nullable
    private UUID appId;

    @ApiModelProperty(value = "应用交付类型")
    @Nullable
    private AppDeliveryTypeEnum appDeliveryType;

    /**
     * 虚拟机类型
     */
    @ApiModelProperty(value = " 虚拟机类型", name = "cbbImageType")
    @Nullable
    @JSONField(name = "cbbImageType")
    private List<CbbImageType> cbbImageTypeList;

    @Nullable
    public UUID getId() {
        return id;
    }

    public void setId(@Nullable UUID id) {
        this.id = id;
    }

    @Nullable
    public UUID getAppId() {
        return appId;
    }

    public void setAppId(@Nullable UUID appId) {
        this.appId = appId;
    }

    @Nullable
    public AppDeliveryTypeEnum getAppDeliveryType() {
        return appDeliveryType;
    }

    public void setAppDeliveryType(@Nullable AppDeliveryTypeEnum appDeliveryType) {
        this.appDeliveryType = appDeliveryType;
    }

    @Nullable
    public List<CbbImageType> getCbbImageTypeList() {
        return cbbImageTypeList;
    }

    public void setCbbImageTypeList(@Nullable List<CbbImageType> cbbImageTypeList) {
        this.cbbImageTypeList = cbbImageTypeList;
    }
}
