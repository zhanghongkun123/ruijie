package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.ProgressStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/05 14:05
 *
 * @author coderLee23
 */
public class GetDeliveryRecordPageWebRequest extends PageWebRequest {

    /**
     * 交付组id
     */
    @ApiModelProperty(value = " 交付组id", required = true)
    @NotNull
    private UUID deliveryGroupId;

    /**
     * /**
     * 规格：镜像类型
     */
    @ApiModelProperty(value = " 镜像类型", name = "cbbImageType")
    @Nullable
    @JSONField(name = "cbbImageType")
    private List<CbbImageType> cbbImageTypeList;

    /**
     * 云桌面类型
     */
    @ApiModelProperty(value = " 云桌面类型", name = "desktopType")
    @Nullable
    @JSONField(name = "desktopType")
    private List<CbbCloudDeskPattern> desktopTypeList;

    /**
     * 交付状态
     */
    @ApiModelProperty(value = "交付状态", name = "progressStatus")
    @Nullable
    @JSONField(name = "progressStatus")
    private List<ProgressStatusEnum> progressStatusList;

    /**
     * 云桌面类型
     */
    @ApiModelProperty(value = "云桌面类型", name = "deskType")
    @Nullable
    @JSONField(name = "deskType")
    private List<String> deskTypeList;

    public UUID getDeliveryGroupId() {
        return deliveryGroupId;
    }

    public void setDeliveryGroupId(UUID deliveryGroupId) {
        this.deliveryGroupId = deliveryGroupId;
    }

    @Nullable
    public List<CbbImageType> getCbbImageTypeList() {
        return cbbImageTypeList;
    }

    public void setCbbImageTypeList(@Nullable List<CbbImageType> cbbImageTypeList) {
        this.cbbImageTypeList = cbbImageTypeList;
    }

    @Nullable
    public List<CbbCloudDeskPattern> getDesktopTypeList() {
        return desktopTypeList;
    }

    public void setDesktopTypeList(@Nullable List<CbbCloudDeskPattern> desktopTypeList) {
        this.desktopTypeList = desktopTypeList;
    }

    @Nullable
    public List<ProgressStatusEnum> getProgressStatusList() {
        return progressStatusList;
    }

    public void setProgressStatusList(@Nullable List<ProgressStatusEnum> progressStatusList) {
        this.progressStatusList = progressStatusList;
    }

    @Nullable
    public List<String> getDeskTypeList() {
        return deskTypeList;
    }

    public void setDeskTypeList(@Nullable List<String> deskTypeList) {
        this.deskTypeList = deskTypeList;
    }
}
