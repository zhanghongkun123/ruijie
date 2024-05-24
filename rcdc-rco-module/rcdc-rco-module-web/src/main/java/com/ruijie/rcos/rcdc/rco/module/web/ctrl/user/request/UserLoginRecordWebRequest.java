package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 导出下载参数
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-01 17:18:00
 *
 * @author zjy
 */
public class UserLoginRecordWebRequest extends TimePageWebRequest {
    @ApiModelProperty(value = "关键字")
    @Nullable
    private String searchKeyword;

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }
}
