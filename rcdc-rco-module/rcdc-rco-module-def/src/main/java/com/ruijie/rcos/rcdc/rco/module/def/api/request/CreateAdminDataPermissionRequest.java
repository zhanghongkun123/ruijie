package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AdminDataPermissionDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月21日
 *
 * @author linrenjian
 */
public class CreateAdminDataPermissionRequest implements Request {

    @NotNull
    private AdminDataPermissionDTO adminDataPermissionDTO;

    public AdminDataPermissionDTO getAdminDataPermissionDTO() {
        return adminDataPermissionDTO;
    }

    public void setAdminDataPermissionDTO(
        AdminDataPermissionDTO adminDataPermissionDTO) {
        this.adminDataPermissionDTO = adminDataPermissionDTO;
    }
}
