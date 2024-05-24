package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: 获取ACPI信息请求DTO
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/31
 *
 * @author zqj
 */
public class GetCloudDesktopAcpiInfoRequest {

    @NotNull
    private UUID deskId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

}
