package com.ruijie.rcos.rcdc.rco.module.web.ctrl.advanced.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 重置密码时，检查用户是否支持随机密码生成
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/25
 *
 * @author liusd
 */
@ApiModel("检查用户是否支持随机密码生成")
public class CheckRandomPasswordWebRequest implements WebRequest {

    @ApiModelProperty(value = "用户标识")
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
