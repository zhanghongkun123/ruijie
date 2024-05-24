package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import java.util.UUID;

import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * 
 * Description: 导出第三方应用主机
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/16
 *
 * @author zhiweiHong
 */
public class RcaHostExportWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池ID数组")
    @Nullable
    private UUID[] appPoolIdArr;

    @Nullable
    public UUID[] getAppPoolIdArr() {
        return appPoolIdArr;
    }

    public void setAppPoolIdArr(@Nullable UUID[] appPoolIdArr) {
        this.appPoolIdArr = appPoolIdArr;
    }
}
