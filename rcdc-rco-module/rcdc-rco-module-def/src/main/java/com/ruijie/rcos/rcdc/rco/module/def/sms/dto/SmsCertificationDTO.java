package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

/**
 * Description: 短信认证DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 *
 * @author TD
 */
public class SmsCertificationDTO extends SmsPwdRecoverDTO {

    /**
     * 是否自动绑定手机
     */
    private Boolean autoBindPhone;
    
    public Boolean getAutoBindPhone() {
        return autoBindPhone;
    }

    public void setAutoBindPhone(Boolean autoBindPhone) {
        this.autoBindPhone = autoBindPhone;
    }
}
