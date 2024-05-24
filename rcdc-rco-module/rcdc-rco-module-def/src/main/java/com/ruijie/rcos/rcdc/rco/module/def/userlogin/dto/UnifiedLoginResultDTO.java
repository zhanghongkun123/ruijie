package com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * Description: 多集群统一登录校验结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/27
 *
 * @author linke
 */
public class UnifiedLoginResultDTO extends EqualsHashcodeSupport {

    private int authResultCode;

    private Boolean needUpdatePassword;

    /**
     * 存放上层业务返回值
     */
    private Object content;

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    public int getAuthResultCode() {
        return authResultCode;
    }

    public void setAuthResultCode(int authResultCode) {
        this.authResultCode = authResultCode;
    }

    public Boolean getNeedUpdatePassword() {
        return needUpdatePassword;
    }

    public void setNeedUpdatePassword(Boolean needUpdatePassword) {
        this.needUpdatePassword = needUpdatePassword;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public LoginResultMessageDTO getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessageDTO loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }
}
