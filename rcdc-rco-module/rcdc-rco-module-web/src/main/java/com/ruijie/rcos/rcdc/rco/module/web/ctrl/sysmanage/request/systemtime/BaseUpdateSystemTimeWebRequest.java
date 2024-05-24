package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.request.systemtime;

import org.springframework.lang.Nullable;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.enums.SystemTimeConfigType;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

/**
 * Description: 更新系统时间web请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月29日
 *
 * @author fyq
 */
public class BaseUpdateSystemTimeWebRequest implements WebRequest {

    @NotNull
    private SystemTimeConfigType type;

    @Nullable
    private Long time;

    @Nullable
    private String ntpServer;

    public SystemTimeConfigType getType() {
        return type;
    }

    public void setType(SystemTimeConfigType type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getNtpServer() {
        return ntpServer;
    }

    public void setNtpServer(String ntpServer) {
        this.ntpServer = ntpServer;
    }
}
