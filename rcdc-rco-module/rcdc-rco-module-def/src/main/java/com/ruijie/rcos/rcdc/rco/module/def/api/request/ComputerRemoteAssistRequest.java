package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 远程协助云桌面操作请求对象
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/22
 * 
 * @author ketb
 */
public class ComputerRemoteAssistRequest implements Request {

    @NotNull
    private UUID id;

    @NotNull
    private Boolean autoAgree;

    @Nullable
    private CbbCloudDeskType deskType;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getAutoAgree() {
        return autoAgree;
    }

    public void setAutoAgree(Boolean autoAgree) {
        this.autoAgree = autoAgree;
    }

    public CbbCloudDeskType getDeskType() {
        return deskType;
    }

    public void setDeskType(CbbCloudDeskType deskType) {
        this.deskType = deskType;
    }
    
}
