package com.ruijie.rcos.rcdc.rco.module.impl.api.dto;

/**
 * Description: 通知SHINE修改网卡工作模式请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/20 18:13
 *
 * @author yxq
 */
public class ShineConfigNicWorkModeDTO {

    /**
     * 网卡工作模式
     */
    private String netWorkModel;

    public ShineConfigNicWorkModeDTO(String netWorkModel) {
        this.netWorkModel = netWorkModel;
    }

    public String getNetWorkModel() {
        return netWorkModel;
    }

    public void setNetWorkModel(String netWorkModel) {
        this.netWorkModel = netWorkModel;
    }
}
