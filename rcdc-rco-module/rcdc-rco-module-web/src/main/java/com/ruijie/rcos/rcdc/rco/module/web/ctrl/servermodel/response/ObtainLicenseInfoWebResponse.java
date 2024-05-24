package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: ObtainLicenseInfoWebResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/4 15:46
 *
 * @author wjp
 */
public class ObtainLicenseInfoWebResponse {

    @ApiModelProperty(value = "获取服务器CPU证书到期时间， 格式为yyyy-MM-dd HH:mm:ss")
    private String trialRemainder;

    @ApiModelProperty(value = "是否全部授权到期， true：已到期， false：未到期")
    private boolean hasAllExpire;

    @ApiModelProperty(value = "是否部分授权到期， true：已到期， false：未到期")
    private boolean hasPartExpire;

    public String getTrialRemainder() {
        return trialRemainder;
    }

    public void setTrialRemainder(String trialRemainder) {
        this.trialRemainder = trialRemainder;
    }

    public boolean isHasAllExpire() {
        return hasAllExpire;
    }

    public void setHasAllExpire(boolean hasAllExpire) {
        this.hasAllExpire = hasAllExpire;
    }

    public boolean isHasPartExpire() {
        return hasPartExpire;
    }

    public void setHasPartExpire(boolean hasPartExpire) {
        this.hasPartExpire = hasPartExpire;
    }
}
