package com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/12 17:08
 *
 * @author coderLee23
 */
public class BasePermissionDTO {

    /**
     *
     * 是否有全部权限
     *
     */
    private Boolean hasAllPermission;

    /**
     * 权限id 超级管理员可以为空
     */
    private UUID adminId;

    public Boolean getHasAllPermission() {
        return hasAllPermission;
    }

    public void setHasAllPermission(Boolean hasAllPermission) {
        this.hasAllPermission = hasAllPermission;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }
}
