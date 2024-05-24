package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response;

import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: rcdc统一修改密码openApi返回结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/3/27
 *
 * @author ZouJian
 */
public class UnifiedChangePwdRestResponse {
    /**
     * 接口结果状态码
     */
    private int resultCode = CommonMessageCode.SUCCESS;

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    public UnifiedChangePwdRestResponse() {
    }

    public UnifiedChangePwdRestResponse(int resultCode) {
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public LoginResultMessageDTO getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(LoginResultMessageDTO loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }
}
