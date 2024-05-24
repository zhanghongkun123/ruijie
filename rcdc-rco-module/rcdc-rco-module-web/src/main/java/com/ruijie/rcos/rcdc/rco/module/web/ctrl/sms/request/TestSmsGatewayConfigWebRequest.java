package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 测试短信网关配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class TestSmsGatewayConfigWebRequest extends UpdateSmsGatewayConfigWebRequest {

    @NotBlank
    @TextShort
    @ApiModelProperty(value = "待测试的手机号", required = true)
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
