package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 应用池批量分配用户分组分配情况
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月24日
 *
 * @author zhengjingyong
 */
public class RcaGroupBindAppGroupWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用分组id")
    @Nullable
    private UUID groupId;

    @ApiModelProperty(value = "应用分组名称")
    @Nullable
    private String groupName;

    @ApiModelProperty(value = "应用id列表")
    @Nullable
    private List<UUID> appIdList;

    @ApiModelProperty(value = "绑定关系")
    @NotNull
    private RcaUpdateBindWebRequest updateBindRequest;

    @Nullable
    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(@Nullable UUID groupId) {
        this.groupId = groupId;
    }

    @Nullable
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(@Nullable String groupName) {
        this.groupName = groupName;
    }

    @Nullable
    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(@Nullable List<UUID> appIdList) {
        this.appIdList = appIdList;
    }

    public RcaUpdateBindWebRequest getUpdateBindRequest() {
        return updateBindRequest;
    }

    public void setUpdateBindRequest(RcaUpdateBindWebRequest updateBindRequest) {
        this.updateBindRequest = updateBindRequest;
    }
}
