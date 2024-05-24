package com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.request;

import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: Web分页查询请求参数
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月03日
 *
 * @author TING
 */
public class CommonPageWebRequest extends PageWebRequest {

    @Nullable
    @ApiModelProperty(value = "是否不需要权限")
    private Boolean noPermission;

    @Nullable
    public Boolean getNoPermission() {
        return noPermission;
    }

    public void setNoPermission(@Nullable Boolean noPermission) {
        this.noPermission = noPermission;
    }
}
