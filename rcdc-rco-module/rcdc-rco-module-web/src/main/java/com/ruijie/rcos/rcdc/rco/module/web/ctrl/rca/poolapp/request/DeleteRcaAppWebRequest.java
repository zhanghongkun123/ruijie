package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 删除应用分组应用
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author zhengjingyong
 */
public class DeleteRcaAppWebRequest implements WebRequest {

    @ApiModelProperty(value = "分组id",required = true)
    @NotNull
    private UUID groupId;

    @ApiModelProperty(value = "应用id列表",required = true)
    @NotEmpty
    private List<UUID> appIdList;

    public UUID getGroupId() {
        return groupId;
    }

    public void setGroupId(UUID groupId) {
        this.groupId = groupId;
    }

    public List<UUID> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<UUID> appIdList) {
        this.appIdList = appIdList;
    }
}
