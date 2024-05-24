package com.ruijie.rcos.rcdc.rco.module.def.sms.request;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacMessageBusinessType;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextName;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: 短信创建请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/16
 *
 * @author TD
 */
public class SmsCreateRequest {

    /**
     * 用户名称
     */
    @NotBlank
    @TextShort
    @TextName
    private String userName;

    /**
     * 业务类型
     */
    @NotNull
    private IacMessageBusinessType businessType;

    /**
     * 手机号码
     */
    @Nullable
    @TextShort
    private String phone;

    /**
     * 短信认证策略
     */
    @Nullable
    private SmsPwdRecoverDTO smsPwdRecoverDTO;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public IacMessageBusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(IacMessageBusinessType businessType) {
        this.businessType = businessType;
    }

    @Nullable
    public String getPhone() {
        return phone;
    }

    public void setPhone(@Nullable String phone) {
        this.phone = phone;
    }

    @Nullable
    public SmsPwdRecoverDTO getSmsPwdRecoverDTO() {
        return smsPwdRecoverDTO;
    }

    public void setSmsPwdRecoverDTO(@Nullable SmsPwdRecoverDTO smsPwdRecoverDTO) {
        this.smsPwdRecoverDTO = smsPwdRecoverDTO;
    }
}
