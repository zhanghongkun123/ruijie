package com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto;

import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;

/**
 * Description: 统一查询r-center返回的修改密码信息
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/3/27
 *
 * @author ZouJian
 */
public class UnifiedChangePwdResultDTO {

    /**
     * 接口结果状态码
     */
    private int resultCode = CommonMessageCode.SUCCESS;

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    public UnifiedChangePwdResultDTO() {
    }

    public UnifiedChangePwdResultDTO(int resultCode) {
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
