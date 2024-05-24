package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto;

import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.LoginResultMessageDTO;

/**
 * Description: 向rccm请求统一修改密码校验的结果
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/3/27
 *
 * @author ZouJian
 */
public class RccmUnifiedChangePwdResultDTO {
    /**
     * 接口结果状态码
     */
    private int resultCode = CommonMessageCode.SUCCESS;

    /**
     * 结果提示信息
     */
    private LoginResultMessageDTO loginResultMessage;

    public RccmUnifiedChangePwdResultDTO() {
    }

    public RccmUnifiedChangePwdResultDTO(int resultCode) {
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
