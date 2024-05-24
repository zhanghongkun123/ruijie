package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import java.util.UUID;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopRole;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * 编辑云桌面角色WEB请求对象
 * <br>
 * Description: Function Description <br>
 * Copyright: Copyright (c) 2017 <br>
 * Company: Ruijie Co., Ltd. <br>
 * Create Time: 2019年7月12日 <br>
 * 
 * @author dan
 */
public class EditDesktopRoleWebRequest implements WebRequest {

    @NotNull
    private UUID id;

    @NotNull
    private DesktopRole desktopRole;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public DesktopRole getDesktopRole() {
        return desktopRole;
    }

    public void setDesktopRole(DesktopRole desktopRole) {
        this.desktopRole = desktopRole;
    }

}
