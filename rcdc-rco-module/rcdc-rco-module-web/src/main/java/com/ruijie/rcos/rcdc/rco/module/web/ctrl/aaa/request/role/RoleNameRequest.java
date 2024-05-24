package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 创建角色的请求 Function Description <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021年06月23日 14:17 <br>
 *
 * @author linrenjian
 */
public class RoleNameRequest implements WebRequest {

    @ApiModelProperty(value = "角色名称")
    @NotBlank
    @TextShort
    @TextName
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
