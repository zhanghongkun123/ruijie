package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformType;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.annotation.JSONField;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月23日
 *
 * @author zhk
 */
public class TaskGroupDesktopPageWebRequest extends PageWebRequest {

    /**
     * 终端组id
     */
    @ApiModelProperty(value = " 终端组id")
    @Nullable
    private UUID groupId;

    /**
     * 应用状态
     */
    @ApiModelProperty(value = " 云桌面状态", name = "deskStateList")
    @Nullable
    @JSONField(name = "deskState")
    private List<CbbCloudDeskState> deskStateList;

    @ApiModelProperty(value = "云平台状态", name = "platformStatus")
    @JSONField(name = "platformStatus")
    @Nullable
    private List<CloudPlatformStatus> platformStatusList;

    @ApiModelProperty(value = "云平台类型", name = "platformType")
    @JSONField(name = "platformType")
    @Nullable
    private List<CloudPlatformType> platformTypeList;

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public List<CbbCloudDeskState> getDeskStateList() {
        return deskStateList;
    }

    public void setDeskStateList(@Nullable List<CbbCloudDeskState> deskStateList) {
        this.deskStateList = deskStateList;
    }

    @Nullable
    public List<CloudPlatformStatus> getPlatformStatusList() {
        return platformStatusList;
    }

    public void setPlatformStatusList(@Nullable List<CloudPlatformStatus> platformStatusList) {
        this.platformStatusList = platformStatusList;
    }

    @Nullable
    public List<CloudPlatformType> getPlatformTypeList() {
        return platformTypeList;
    }

    public void setPlatformTypeList(@Nullable List<CloudPlatformType> platformTypeList) {
        this.platformTypeList = platformTypeList;
    }
}
