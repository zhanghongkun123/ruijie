package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月22日
 * 
 * @author wjp
 */
public class RemoteAssistNotifyContentDTO {

    @NotNull
    private UUID deskId;

    @NotNull
    private UUID adminId;

    public RemoteAssistNotifyContentDTO(UUID deskId, UUID adminId) {
        this.deskId = deskId;
        this.adminId = adminId;
    }

    public RemoteAssistNotifyContentDTO() {

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
}
