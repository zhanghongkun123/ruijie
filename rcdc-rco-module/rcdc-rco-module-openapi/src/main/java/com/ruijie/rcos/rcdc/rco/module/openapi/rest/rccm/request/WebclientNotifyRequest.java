package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.request;

import java.util.Map;

import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.constants.WebclientNotifyAction;
import com.ruijie.rcos.sk.base.annotation.NotNull;


/**
 * Description: ForwardRcdcRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class WebclientNotifyRequest {

    @NotNull
    private String action = WebclientNotifyAction.NOTIFY_REMOTE_ASSIST;

    @Nullable
    private Map<String,Object> requestBody;

    public WebclientNotifyRequest() {
    }

    public WebclientNotifyRequest(String action, @Nullable Map<String,Object> requestBody) {
        this.action = action;
        this.requestBody = requestBody;
    }


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Nullable
    public Map<String, Object> getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(@Nullable Map<String, Object> requestBody) {
        this.requestBody = requestBody;
    }
}
