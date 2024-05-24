package com.ruijie.rcos.rcdc.rco.module.openapi.rest.unifiedlogin.response;

import com.ruijie.rcos.rcdc.rco.module.def.constants.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedLoginResultDTO;

/**
 *
 * Description: rcdc统一登录openApi返回结果
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public class UnifiedLoginRestServerResponse extends UnifiedLoginResultDTO {

    /**
     * 接口结果状态码
     */
    private int resultCode = CommonMessageCode.SUCCESS;

    public UnifiedLoginRestServerResponse() {
    }

    public UnifiedLoginRestServerResponse(int resultCode) {
        super();
        this.resultCode = resultCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }
}
