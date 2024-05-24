package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 短信认证策略请求参数
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
public class SmsCertificationRequest extends SmsPwdRecoverStrategyRequest {

    /**
     * 是否自动绑定手机
     */
    @Nullable
    @ApiModelProperty(value = "是否自动绑定手机", required = true)
    private Boolean autoBindPhone;

    public Boolean getAutoBindPhone() {
        return autoBindPhone;
    }

    public void setAutoBindPhone(Boolean autoBindPhone) {
        this.autoBindPhone = autoBindPhone;
    }
    
}
