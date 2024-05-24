package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
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
public class GetDeliveryTestPageWebRequest extends PageWebRequest {

    /**
     * 测试任务id
     */
    @ApiModelProperty(value = "测试任务id")
    @Nullable
    private UUID id;

    /**
     * 交付应用id
     */
    @ApiModelProperty(value = "交付应用id")
    @Nullable
    private UUID appId;

    /**
     * 虚拟机类型
     */
    @ApiModelProperty(value = " 虚拟机类型", name = "appSoftwarePackageType")
    @Nullable
    @JSONField(name = "appSoftwarePackageType")
    private List<CbbImageType> appSoftwarePackageTypeList;

    /**
     * 测试状态
     */
    @ApiModelProperty(value = " 测试状态", name = "state")
    @Nullable
    @JSONField(name = "state")
    private List<TestTaskStateEnum> stateList;


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
    public List<CbbImageType> getAppSoftwarePackageTypeList() {
        return appSoftwarePackageTypeList;
    }

    public void setAppSoftwarePackageTypeList(@Nullable List<CbbImageType> appSoftwarePackageTypeList) {
        this.appSoftwarePackageTypeList = appSoftwarePackageTypeList;
    }

    @Nullable
    public List<TestTaskStateEnum> getStateList() {
        return stateList;
    }

    public void setStateList(@Nullable List<TestTaskStateEnum> stateList) {
        this.stateList = stateList;
    }
}
