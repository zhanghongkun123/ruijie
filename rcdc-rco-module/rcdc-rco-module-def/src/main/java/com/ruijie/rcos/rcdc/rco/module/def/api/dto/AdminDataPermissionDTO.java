package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月22日
 *
 * @author linrenjian
 */
public class AdminDataPermissionDTO {

    /**
     * 管理员ID
     */
    @NotNull
    private UUID adminId;

    /**
     * 数据ID
     */
    @NotNull
    private String  permissionDataId;

    /**
     * 数据类型
     */
    @NotNull
    private AdminDataPermissionType permissionDataType;

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getPermissionDataId() {
        return permissionDataId;
    }

    public void setPermissionDataId(String permissionDataId) {
        this.permissionDataId = permissionDataId;
    }

    public AdminDataPermissionType getPermissionDataType() {
        return permissionDataType;
    }

    public void setPermissionDataType(AdminDataPermissionType permissionDataType) {
        this.permissionDataType = permissionDataType;
    }
}
