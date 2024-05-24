package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/18 11:38
 *
 * @author coderLee23
 */
public class SearchWebRequest implements WebRequest {

    /**
     * 搜索框内容
     */
    @ApiModelProperty(value = " 交付组id")
    @NotBlank
    private String searchContent;

    public String getSearchContent() {
        return searchContent;
    }

    public void setSearchContent(String searchContent) {
        this.searchContent = searchContent;
    }
}
