package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.rcdc.rco.module.def.constants.WebclientNotifyAction;
import org.springframework.lang.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.Map;



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
    private JSONObject requestBody;

    public WebclientNotifyRequest() {
    }

    public WebclientNotifyRequest(String action, @Nullable JSONObject requestBody) {
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
    public JSONObject getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(@Nullable JSONObject requestBody) {
        this.requestBody = requestBody;
    }
}
