package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.*;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import java.util.UUID;

/**
 * Description: 应用主机重名校验请求
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月30日
 *
 * @author liuwc
 */
public class CheckRcaHostNameDuplicateRequest implements WebRequest {

    @NotNull
    private UUID hostId;

    @NotNull
    @NotBlank
    @TextShort
    private String name;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
