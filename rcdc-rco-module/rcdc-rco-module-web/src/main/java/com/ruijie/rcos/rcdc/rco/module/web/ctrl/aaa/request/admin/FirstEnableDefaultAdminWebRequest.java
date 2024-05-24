package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年02月06日
 * 
 * @author xiejian
 */
public class FirstEnableDefaultAdminWebRequest implements WebRequest {

    @ApiModelProperty(value = "管理员ID", required = true)
    @NotNull
    private UUID id;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank
    private String newPwd;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }
}
