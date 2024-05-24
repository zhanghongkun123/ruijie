package com.ruijie.rcos.rcdc.rco.module.impl.connector.dto;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Code;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;

/**
 * Description: 校验用户动态口令响应
 * Copyright: Copyright (c) 2022
 * Company: RuiJie Co., Ltd.
 * Create Time: 2022/4/18 4:24 下午
 *
 * @author chenli
 */
public class CheckUserOtpCodeResponse extends Result {

    /**
     * 默认失败
     */
    private Boolean result = Boolean.FALSE;

    private String loginResultMessage;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getLoginResultMessage() {
        return loginResultMessage;
    }

    public void setLoginResultMessage(String loginResultMessage) {
        this.loginResultMessage = loginResultMessage;
    }
}
