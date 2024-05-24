package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月4日
 * 
 * @author zhuangchenwu
 */
public class ValidateAdminPwdWebRequest implements WebRequest {

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String pwd;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
