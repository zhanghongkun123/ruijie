package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 短信认证返回体
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class SmsCertificationResponse extends SmsPwdRecoverStrategyResponse {

    /**
     * 是否自动绑定手机
     */
    @ApiModelProperty(value = "是否自动绑定手机")
    private Boolean autoBindPhone;

    public Boolean getAutoBindPhone() {
        return autoBindPhone;
    }

    public void setAutoBindPhone(Boolean autoBindPhone) {
        this.autoBindPhone = autoBindPhone;
    }
}
