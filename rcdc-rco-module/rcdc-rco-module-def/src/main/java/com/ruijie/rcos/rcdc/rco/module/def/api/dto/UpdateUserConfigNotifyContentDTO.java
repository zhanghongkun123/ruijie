package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月03日
 *
 * @author zhanghongkun
 */
public class UpdateUserConfigNotifyContentDTO implements Serializable {

    /**
     * 动态标签页
     */
    @Nullable
    private Boolean hasOtpCodeTab;

    /**
     * 动态口令开关
     */
    @Nullable
    private Boolean openOtp;

    /**
     * 用户id
     */
    @NotNull
    private UUID userId;

    public Boolean getHasOtpCodeTab() {
        return hasOtpCodeTab;
    }

    public void setHasOtpCodeTab(Boolean hasOtpCodeTab) {
        this.hasOtpCodeTab = hasOtpCodeTab;
    }

    public Boolean getOpenOtp() {
        return openOtp;
    }

    public void setOpenOtp(Boolean openOtp) {
        this.openOtp = openOtp;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
