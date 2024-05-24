package com.ruijie.rcos.rcdc.rco.module.def.autologon.dto;

import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

/**
 * 全局windows密码配置开关DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月02日
 *
 * @author TD
 */
public class AutoLogonDTO {

    /**
     * 自动登录
     */
    @NotNull
    private Boolean autoLogon;

    /**
     * 自动登录类型
     */
    @Nullable
    private AutoLogonEnum autoLogonEnum;

    public Boolean getAutoLogon() {
        return autoLogon;
    }

    public void setAutoLogon(Boolean autoLogon) {
        this.autoLogon = autoLogon;
    }

    @Nullable
    public AutoLogonEnum getAutoLogonEnum() {
        return autoLogonEnum;
    }

    public void setAutoLogonEnum(@Nullable AutoLogonEnum autoLogonEnum) {
        this.autoLogonEnum = autoLogonEnum;
    }
}