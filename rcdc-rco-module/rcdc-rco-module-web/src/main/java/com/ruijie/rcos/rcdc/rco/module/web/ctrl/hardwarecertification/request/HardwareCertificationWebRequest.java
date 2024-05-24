package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *
 * Description: 硬件特征码策略请求
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author zhang.zhiwen
 */
@ApiModel("硬件特征码配置信息请求体")
public class HardwareCertificationWebRequest {

    /**
     * 硬件特征码
     */
    @ApiModelProperty(value = "硬件特征码配置对象", required = true)
    @NotNull
    private HardwareDTO hardwareStrategy;

    public HardwareDTO getHardwareStrategy() {
        return hardwareStrategy;
    }

    public void setHardwareStrategy(HardwareDTO hardwareStrategy) {
        this.hardwareStrategy = hardwareStrategy;
    }
}
