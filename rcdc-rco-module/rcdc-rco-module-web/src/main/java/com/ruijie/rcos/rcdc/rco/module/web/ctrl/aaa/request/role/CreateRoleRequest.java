package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
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
public class CreateRoleRequest implements WebRequest {

    @ApiModelProperty(value = "角色名称", required = true)
    @NotBlank
    @TextShort
    @TextName
    private String roleName;

    @ApiModelProperty(value = "权限标识列表")
    @Nullable
    private UUID[] permissionIdArr;

    @ApiModelProperty(value = "角色描述")
    @TextMedium
    private String describe;


    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public UUID[] getPermissionIdArr() {
        return permissionIdArr;
    }

    public void setPermissionIdArr(UUID[] permissionIdArr) {
        this.permissionIdArr = permissionIdArr;
    }

}
