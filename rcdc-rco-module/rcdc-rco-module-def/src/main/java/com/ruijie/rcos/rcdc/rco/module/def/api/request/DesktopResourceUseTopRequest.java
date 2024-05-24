package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/2/20
 *
 * @author Jarman
 */
public class DesktopResourceUseTopRequest implements Request {

    @NotNull
    private Integer top;

    @Nullable
    private UUID[] userGroupIdArr;
    
    @Nullable
    private UUID[] terminalGroupIdArr;

    public DesktopResourceUseTopRequest(Integer top) {
        this.top = top;
    }

    public Integer getTop() {
        return top;
    }

    public void setTop(Integer top) {
        this.top = top;
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
