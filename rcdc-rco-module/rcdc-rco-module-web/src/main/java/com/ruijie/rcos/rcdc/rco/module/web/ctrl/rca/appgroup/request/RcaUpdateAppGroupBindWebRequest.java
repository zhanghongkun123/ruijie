package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.appgroup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 更新应用池分组绑定关系请求体
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月09日
 *
 * @author zhengjingyong
 */
public class RcaUpdateAppGroupBindWebRequest extends RcaUpdateBindWebRequest {

    @ApiModelProperty(value = "应用分组Id列表", required = true)
    @NotEmpty
    private List<UUID> appGroupIdList;

    public List<UUID> getAppGroupIdList() {
        return appGroupIdList;
    }

    public void setAppGroupIdList(List<UUID> appGroupIdList) {
        this.appGroupIdList = appGroupIdList;
    }

}
