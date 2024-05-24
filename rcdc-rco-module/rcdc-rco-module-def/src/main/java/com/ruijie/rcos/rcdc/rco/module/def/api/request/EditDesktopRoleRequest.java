package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;

/**
 * 编辑云桌面角色
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年7月16日 <br>
 * 
 * @author dan
 */
public class EditDesktopRoleRequest extends IdRequest {

    @NotNull
    private DesktopRole desktopRole;

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }

}
