package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018/12/7
 *
 * @author Jarman
 */
public class DeskTypeRequest implements Request {

    @NotNull
    private CbbCloudDeskType desktopType;

    @Nullable
    private UUID[] userGroupIdArr;

    @Nullable
    private UUID[] terminalGroupIdArr;

    public CbbCloudDeskType getDesktopType() {
        return desktopType;
    }

    public void setDesktopType(CbbCloudDeskType desktopType) {
        this.desktopType = desktopType;
    }

    public UUID[] getUserGroupIdArr() {
        return userGroupIdArr;
    }

    public void setUserGroupIdArr(UUID[] userGroupIdArr) {
        this.userGroupIdArr = userGroupIdArr;
    }

    public UUID[] getTerminalGroupIdArr() {
        return terminalGroupIdArr;
    }

    public void setTerminalGroupIdArr(UUID[] terminalGroupIdArr) {
        this.terminalGroupIdArr = terminalGroupIdArr;
    }
}
