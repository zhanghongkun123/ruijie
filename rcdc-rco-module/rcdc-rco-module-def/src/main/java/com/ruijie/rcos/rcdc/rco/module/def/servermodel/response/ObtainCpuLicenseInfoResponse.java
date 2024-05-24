package com.ruijie.rcos.rcdc.rco.module.def.servermodel.response;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/12/15
 *
 * @author wjp
 */
public class ObtainCpuLicenseInfoResponse {

    private String trialRemainder;

    private boolean hasAllExpire;

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
