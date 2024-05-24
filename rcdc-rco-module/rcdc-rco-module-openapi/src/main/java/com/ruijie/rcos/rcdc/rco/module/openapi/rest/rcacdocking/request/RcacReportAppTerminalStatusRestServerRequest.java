package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * Description: RCAC通知应用客户端状态请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/3/18 15:55
 *
 * @author zhangyichi
 */
public class RcacReportAppTerminalStatusRestServerRequest {

    @NotNull
    private Integer rcaClientId;

    @Nullable
    private String status;

    public Integer getRcaClientId() {
        return rcaClientId;
    }

    public void setRcaClientId(Integer rcaClientId) {
        this.rcaClientId = rcaClientId;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }
}
