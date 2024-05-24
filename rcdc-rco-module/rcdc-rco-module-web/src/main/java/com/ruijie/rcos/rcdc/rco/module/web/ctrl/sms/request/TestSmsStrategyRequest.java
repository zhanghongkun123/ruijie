package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextShort;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 测试短信策略请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class TestSmsStrategyRequest extends SmsPwdRecoverStrategyRequest {

    @NotBlank
    @TextShort
    @ApiModelProperty(value = "待测试的手机号", required = true)
    private String phone;

    /**
     * 业务类型
     */
    @NotNull
    @ApiModelProperty(value = "业务类型：SMS_AUTH，PWD_RECOVER", required = true)
    private MessageBusinessType businessType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public MessageBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(MessageBusinessType businessType) {
        this.businessType = businessType;
    }
}
