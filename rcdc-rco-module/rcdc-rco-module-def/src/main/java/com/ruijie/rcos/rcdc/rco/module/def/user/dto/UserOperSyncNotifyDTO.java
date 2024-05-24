package com.ruijie.rcos.rcdc.rco.module.def.user.dto;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherKey;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年5月15日
 * 
 * @author wjp
 */
public class UserOperSyncNotifyDTO {

    @NotBlank
    @DispatcherKey
    private String dispatcherKey;

    @NotBlank
    private String oper;

    @Nullable
    private String childOper;

    @NotNull
    private Long timestamp;

    @NotNull
    private UUID relatedId;

    @Nullable
    private String relatedName;

    /**
     *  默认需要数据同步
     */
    @Nullable
    private Boolean enableSyncData = Boolean.TRUE;

    public String getDispatcherKey() {
        return dispatcherKey;
    }

    public void setDispatcherKey(String dispatcherKey) {
        this.dispatcherKey = dispatcherKey;
    }

    public String getOper() {
        return oper;
    }

    public void setOper(String oper) {
        this.oper = oper;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public UUID getRelatedId() {
        return relatedId;
    }

    public void setRelatedId(UUID relatedId) {
        this.relatedId = relatedId;
    }

    @Nullable
    public String getChildOper() {
        return childOper;
    }

    public void setChildOper(@Nullable String childOper) {
        this.childOper = childOper;
    }

    @Nullable
    public String getRelatedName() {
        return relatedName;
    }

    public void setRelatedName(@Nullable String relatedName) {
        this.relatedName = relatedName;
    }

    public Boolean getEnableSyncData() {
        return enableSyncData;
    }

    public void setEnableSyncData(Boolean enableSyncData) {
        this.enableSyncData = enableSyncData;
    }
}
