package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 查询应用池
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class SearchAppPoolWebRequest implements WebRequest {

    @ApiModelProperty(value = "应用池名",required = true)
    @TextShort
    @TextName
    @NotBlank
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
