package com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.request;

import java.util.Date;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月18日
 *
 * @author luoyuanbin
 */
public class ClientOpLogPageRequest extends PageSearchRequest {

    public ClientOpLogPageRequest(PageWebRequest webRequest) {
        super(webRequest);
    }

    @Nullable
    private Date startTime;

    @Nullable
    private Date endTime;

    @Nullable
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(@Nullable Date startTime) {
        this.startTime = startTime;
    }

    @Nullable
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(@Nullable Date endTime) {
        this.endTime = endTime;
    }
}
