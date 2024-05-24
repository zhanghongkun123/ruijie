package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 应用池总览查询条件
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月03日
 *
 * @author zhengjingyong
 */
public class AppPoolOverviewWebRequest implements WebRequest {

    @ApiModelProperty(value = "池类型")
    @Nullable
    private RcaEnum.PoolType poolType;

    @ApiModelProperty(value = "主机来源")
    @Nullable
    private RcaEnum.HostSourceType hostSourceType;

    @ApiModelProperty(value = "会话类型")
    @Nullable
    private RcaEnum.HostSessionType sessionType;

    @Nullable
    public RcaEnum.PoolType getPoolType() {
        return poolType;
    }

    public void setPoolType(@Nullable RcaEnum.PoolType poolType) {
        this.poolType = poolType;
    }

    @Nullable
    public RcaEnum.HostSourceType getHostSourceType() {
        return hostSourceType;
    }

    public void setHostSourceType(@Nullable RcaEnum.HostSourceType hostSourceType) {
        this.hostSourceType = hostSourceType;
    }

    @Nullable
    public RcaEnum.HostSessionType getSessionType() {
        return sessionType;
    }

    public void setSessionType(@Nullable RcaEnum.HostSessionType sessionType) {
        this.sessionType = sessionType;
    }
}
