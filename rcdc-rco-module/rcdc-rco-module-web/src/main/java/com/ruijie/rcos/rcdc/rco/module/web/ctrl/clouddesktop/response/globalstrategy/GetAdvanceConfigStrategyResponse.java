package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 *
 * @author fyq
 */
public class GetAdvanceConfigStrategyResponse {

    /**
     * 是否开启评测功能
     */
    private Boolean enableEvaluation;

    /**
     * 是否开启虚拟应用
     */
    private Boolean enableVirtualApplication;

    public Boolean getEnableEvaluation() {
        return enableEvaluation;
    }

    public void setEnableEvaluation(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }

    public Boolean getEnableVirtualApplication() {
        return enableVirtualApplication;
    }

    public void setEnableVirtualApplication(Boolean enableVirtualApplication) {
        this.enableVirtualApplication = enableVirtualApplication;
    }
}
