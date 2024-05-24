package com.ruijie.rcos.rcdc.rco.module.def.api.dto.uws;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月19日
 *
 * @author xgx
 */
public class CbbQrCodeConfigDTO extends CbbBaseQrCodeReqDTO {
    @NotNull
    private Integer order = 0;

    @NotNull
    private Boolean openSwitch;

    @Range(min = "0")
    @Nullable
    private Long expireTime;

    @Size(max = 1024)
    @Nullable
    private String contentPrefix;

    @Nullable
    private String advanceConfig;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Boolean getOpenSwitch() {
        return openSwitch;
    }

    public void setOpenSwitch(Boolean openSwitch) {
        this.openSwitch = openSwitch;
    }

    @Nullable
    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(@Nullable Long expireTime) {
        this.expireTime = expireTime;
    }

    @Nullable
    public String getAdvanceConfig() {
        return advanceConfig;
    }

    public void setAdvanceConfig(@Nullable String advanceConfig) {
        this.advanceConfig = advanceConfig;
    }

    @Nullable
    public String getContentPrefix() {
        return contentPrefix;
    }

    public void setContentPrefix(@Nullable String contentPrefix) {
        this.contentPrefix = contentPrefix;
    }
}
