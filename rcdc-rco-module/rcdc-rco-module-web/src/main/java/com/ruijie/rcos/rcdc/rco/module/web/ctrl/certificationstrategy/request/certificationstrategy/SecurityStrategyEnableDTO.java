package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.certificationstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 *
 * Description: 启用密码安全策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月01日
 *
 * @author zhang.zhiwen
 */
public class SecurityStrategyEnableDTO {

    /**
     * 是否启用密码安全策略：0不启用，1启用
     */
    @NotNull
    private Boolean securityStrategyEnable;

    /**
     * 密码有效期：0永久，1自定义
     */
    @Range(min = "0", max = "1")
    @Nullable
    private Integer isOpenUpdateDays;

    /**
     * 每隔（）天用户必须修改密码
     */
    @Range(min = "3", max = "365")
    @Nullable
    private Integer updateDays;

    /**
     * 密码剩余天数提醒
     */
    @Range(min = "1", max = "60")
    @Nullable
    private Integer pwdExpireRemindDays;

    /**
     * 密码复杂度等级
     */
    @Nullable
    private String pwdLevel;

    /**
     * 首次登录强制修改密码(无论是否符合密码规则)
     */
    @Nullable
    private Boolean enableForceUpdatePassword;


    public Boolean getSecurityStrategyEnable() {
        return securityStrategyEnable;
    }

    public void setSecurityStrategyEnable(Boolean securityStrategyEnable) {
        this.securityStrategyEnable = securityStrategyEnable;
    }

    public Integer getIsOpenUpdateDays() {
        return isOpenUpdateDays;
    }

    public void setIsOpenUpdateDays(Integer isOpenUpdateDays) {
        this.isOpenUpdateDays = isOpenUpdateDays;
    }

    public Integer getUpdateDays() {
        return updateDays;
    }

    public void setUpdateDays(Integer updateDays) {
        this.updateDays = updateDays;
    }

    public String getPwdLevel() {
        return pwdLevel;
    }

    public void setPwdLevel(String pwdLevel) {
        this.pwdLevel = pwdLevel;
    }

    public Integer getPwdExpireRemindDays() {
        return pwdExpireRemindDays;
    }

    public void setPwdExpireRemindDays(Integer pwdExpireRemindDays) {
        this.pwdExpireRemindDays = pwdExpireRemindDays;
    }

    @Nullable
    public Boolean getEnableForceUpdatePassword() {
        return enableForceUpdatePassword;
    }

    public void setEnableForceUpdatePassword(@Nullable Boolean enableForceUpdatePassword) {
        this.enableForceUpdatePassword = enableForceUpdatePassword;
    }
}
