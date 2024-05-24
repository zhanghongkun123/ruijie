package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desksnapshot.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 创建云桌面快照Web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月04日
 *
 * @author luojianmo
 */
public class CreateDeskSnapshotRequest implements WebRequest {

    /**
     * 快照名称
     */
    @ApiModelProperty(value = "快照名称，通用名称格式", required = true)
    @NotBlank
    @Size(min = 1, max = 64)
    @TextName
    private String name;

    /**
     * 云桌面ID
     */
    @ApiModelProperty(value = "云桌面ID", required = true)
    @NotNull
    private UUID deskId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }
}
