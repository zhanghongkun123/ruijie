package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/25 17:12
 *
 * @author ketb
 */
public class CreateDeskRemoteAssistDTO {

    @NotNull
    private UUID deskId;

    @NotNull
    private UUID adminId;

    @NotNull
    private String adminName;

    @Nullable
    private Boolean autoAgree;

    public CreateDeskRemoteAssistDTO(UUID deskId, UUID adminId, String adminName) {
        this.deskId = deskId;
        this.adminId = adminId;
        this.adminName = adminName;
    }

    public CreateDeskRemoteAssistDTO() {
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
