package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: ForwardRcdcRequest
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public class ForwardRcdcRequest {

    @NotNull
    private UUID clusterId;

    @NotNull
    private String url;

    @NotNull
    private String action = "POST";

    @Nullable
    private JSONObject requestBody;

    public ForwardRcdcRequest() {
    }

    public ForwardRcdcRequest(UUID clusterId, String url, String action, @Nullable JSONObject requestBody) {
        this.clusterId = clusterId;
        this.url = url;
        this.action = action;
        this.requestBody = requestBody;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
