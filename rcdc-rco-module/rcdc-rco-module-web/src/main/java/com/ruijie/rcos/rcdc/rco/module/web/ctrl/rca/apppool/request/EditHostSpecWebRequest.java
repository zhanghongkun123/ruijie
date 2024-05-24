package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;
import java.util.UUID;

/**
 * Description: 变更应用主机规格
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月25日
 *
 * @author zhengjingyong
 */
public class EditHostSpecWebRequest extends DeskSpecRequest {

    @ApiModelProperty(value = "应用主机id", required = true)
    @NotNull
    private List<UUID> hostIdList;

    public List<UUID> getHostIdList() {
        return hostIdList;
    }

    public void setHostIdList(List<UUID> hostIdList) {
        this.hostIdList = hostIdList;
    }
}
