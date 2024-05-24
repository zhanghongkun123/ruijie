package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 重置VDI云桌面MAC地址请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/29 10:24
 *
 * @author yxq
 */
public class ResetCloudDeskMacRequest implements WebRequest {

    @ApiModelProperty("云桌面id")
    @NotNull
    private UUID id;

    @ApiModelProperty("mac地址")
    @Nullable
    private String mac;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
