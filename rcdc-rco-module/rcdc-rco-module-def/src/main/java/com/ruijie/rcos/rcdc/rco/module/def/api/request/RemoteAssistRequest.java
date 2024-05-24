package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * 远程协助请求信息
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/23
 * 
 * @author artom
 */
public class RemoteAssistRequest implements Request {
    
    @NotNull
    private UUID deskId;
    
    @NotNull
    private UUID adminId;
    
    @Nullable
    private String adminName;

    @NotNull
    private Boolean autoAgree;
    
    public RemoteAssistRequest(UUID deskId, UUID adminId, String adminName) {
        Assert.notNull(deskId, "deskId must not be null.");
        Assert.notNull(adminId, "adminId must not be null.");
        Assert.hasText(adminName, "adminName can not be blank.");
        this.deskId = deskId;
        this.adminId = adminId;
        this.adminName = adminName;
        this.autoAgree = false;
    }
    
    public RemoteAssistRequest() {
        this.autoAgree = false;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Boolean getAutoAgree() {
        return autoAgree;
    }

    public void setAutoAgree(Boolean autoAgree) {
        this.autoAgree = autoAgree;
    }
}
