package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.admin;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 *
 * Description: 结果管理员请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月01日
 * 
 * @author yxq
 */
public class UnlockAdminRequest implements WebRequest {

    @ApiModelProperty(value = "id数组", required = true)
    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }
}
