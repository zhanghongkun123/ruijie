package com.ruijie.rcos.rcdc.rco.module.impl.spi.response;

/**
 *
 * Description: 网络模式回应
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/10/18
 *
 * @author zhiweiHong
 */
public class ShineNicWorkModeResponse {

    private String netWorkModel;

    public ShineNicWorkModeResponse() {
    }

    public ShineNicWorkModeResponse(String netWorkModel) {
        this.netWorkModel = netWorkModel;
    }

    public String getNetWorkModel() {
        return netWorkModel;
    }

    public void setNetWorkModel(String netWorkModel) {
        this.netWorkModel = netWorkModel;
    }
}
