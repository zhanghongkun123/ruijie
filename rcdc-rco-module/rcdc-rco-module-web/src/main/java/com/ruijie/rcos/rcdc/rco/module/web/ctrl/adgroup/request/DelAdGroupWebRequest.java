package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13
 *
 * @author zqj
 */
public class DelAdGroupWebRequest implements WebRequest {

    /**
     * AD域组ID集合
     */
    @NotEmpty
    @ApiModelProperty(value = "AD域组ID集合", required = true)
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
