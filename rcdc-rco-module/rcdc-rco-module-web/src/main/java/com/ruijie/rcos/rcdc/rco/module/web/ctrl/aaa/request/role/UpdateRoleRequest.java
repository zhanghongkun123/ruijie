package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.role;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 更新角色请求 <br>
 * Copyright: Copyright (c) 2021 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2021年07月19日 14:17 <br>
 *
 * @author linrenjian
 */
public class UpdateRoleRequest implements WebRequest {

    @NotNull
    private UUID id;

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

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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