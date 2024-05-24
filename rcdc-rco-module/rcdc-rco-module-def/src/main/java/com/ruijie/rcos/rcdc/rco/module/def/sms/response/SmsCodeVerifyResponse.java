package com.ruijie.rcos.rcdc.rco.module.def.sms.response;

import java.util.UUID;

/**
 * Description: 短信验证码校验返回
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/26
 *
 * @author TD
 */
public class SmsCodeVerifyResponse {

    /**
     * 返回码
     */
    private Integer businessCode;

    /**
     * 当前错误次数
     */
    private Integer errorNumber;

    /**
     * 允许最大错误次数
     */
    private Integer maxErrorNumber;

    /**
     * token：有效期5分钟，重启失效
     */
    private UUID token;

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

    public Integer getMaxErrorNumber() {
        return maxErrorNumber;
    }

    public void setMaxErrorNumber(Integer maxErrorNumber) {
        this.maxErrorNumber = maxErrorNumber;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }
}
