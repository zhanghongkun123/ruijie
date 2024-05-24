package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import org.springframework.lang.Nullable;
import java.util.UUID;

/**
 * Description: 获取远程协助信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/11/9
 *
 * @author chenjiehui
 */
public class RemoteAssistInfoRequest implements WebRequest {

    /**
     * 桌面id
     */
    @NotNull
    private UUID id;

    /**
     * 业务标识
     */
    @NotNull
    private String component;

    /**
     * 平台（cloudDesk 或 pc ）
     */
    @Nullable
    private String platform;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
