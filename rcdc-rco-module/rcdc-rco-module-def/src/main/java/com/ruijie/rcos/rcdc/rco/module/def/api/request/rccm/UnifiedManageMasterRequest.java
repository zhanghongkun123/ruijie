package com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 发送云桌面策略数据给RCCM
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/17
 * @param <T> 泛型参数
 * @author TD
 */
public class UnifiedManageMasterRequest<T> {
    
    @NotNull
    private UUID identityId = UUID.randomUUID();

    @NotNull
    private UUID clusterId;

    @NotNull
    private String functionVersion;

    @NotNull
    private String functionSystem;

    @NotNull
    private String functionKey;

    @NotNull
    private UUID unifiedManageId;

    @NotNull
    private String unifiedManageName;

    @NotNull
    private String url;

    @NotNull
    private String action;

    @Nullable
    private T requestBody;

    public UUID getIdentityId() {
        return identityId;
    }

    public void setIdentityId(UUID identityId) {
        this.identityId = identityId;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public String getFunctionVersion() {
        return functionVersion;
    }

    public void setFunctionVersion(String functionVersion) {
        this.functionVersion = functionVersion;
    }

    public String getFunctionSystem() {
        return functionSystem;
    }

    public void setFunctionSystem(String functionSystem) {
        this.functionSystem = functionSystem;
    }

    public String getFunctionKey() {
        return functionKey;
    }

    public void setFunctionKey(String functionKey) {
        this.functionKey = functionKey;
    }

    public UUID getUnifiedManageId() {
        return unifiedManageId;
    }

    public void setUnifiedManageId(UUID unifiedManageId) {
        this.unifiedManageId = unifiedManageId;
    }

    public String getUnifiedManageName() {
        return unifiedManageName;
    }

    public void setUnifiedManageName(String unifiedManageName) {
        this.unifiedManageName = unifiedManageName;
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
    public T getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(@Nullable T requestBody) {
        this.requestBody = requestBody;
    }
}
