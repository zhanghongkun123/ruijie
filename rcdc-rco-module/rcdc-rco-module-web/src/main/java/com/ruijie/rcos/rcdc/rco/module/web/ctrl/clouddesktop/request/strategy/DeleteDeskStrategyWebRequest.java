package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月24日
 * 
 * @author zhfw
 */
public class DeleteDeskStrategyWebRequest implements WebRequest {

    @ApiModelProperty(value = "云桌面策略ID", required = true)
    @NotNull
    @Size(min = 1)
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
