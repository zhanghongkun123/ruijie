package com.ruijie.rcos.rcdc.rco.module.web.ctrl.certificationstrategy.request.certificationstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import org.springframework.lang.Nullable;

/**
 *
 * Description: 启用防暴力破解
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月01日
 *
 * @author zhang.zhiwen
 */
public class PreventsBruteForceDTO {

    /**
     * 是否启用防暴力破解：0不启用，1启用
     */
    @NotNull
    private Boolean preventsBruteForce;

    /**
     * 最大错误次数，超过锁定用户
     */
    @Range(min = "5", max = "100")
    @Nullable
    private Integer userLockedErrorTimes;

    /**
     * 锁定时长：0否 1是
     */
    @Range(min = "0", max = "1")
    @Nullable
    private Integer isOpenUserLockTime;

    /**
     * 锁定时间，单位分钟
     */
    @Range(min = "10", max = "9999")
    @Nullable
    private Integer userLockTime;

    public Boolean getPreventsBruteForce() {
        return preventsBruteForce;
    }

    public void setPreventsBruteForce(Boolean preventsBruteForce) {
        this.preventsBruteForce = preventsBruteForce;
    }

    public Integer getUserLockedErrorTimes() {
        return userLockedErrorTimes;
    }

    public void setUserLockedErrorTimes(Integer userLockedErrorTimes) {
        this.userLockedErrorTimes = userLockedErrorTimes;
    }

    public Integer getIsOpenUserLockTime() {
        return isOpenUserLockTime;
    }

    public void setIsOpenUserLockTime(Integer isOpenUserLockTime) {
        this.isOpenUserLockTime = isOpenUserLockTime;
    }

    public Integer getUserLockTime() {
        return userLockTime;
    }

    public void setUserLockTime(Integer userLockTime) {
        this.userLockTime = userLockTime;
    }
}
