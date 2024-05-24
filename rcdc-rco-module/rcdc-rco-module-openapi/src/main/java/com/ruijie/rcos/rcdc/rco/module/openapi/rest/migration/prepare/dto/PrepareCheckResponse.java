package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.dto;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.enums.PreparationCheckResultEnum;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/7
 *
 * @author zhangsiming
 */

public class PrepareCheckResponse {

    private String code;

    private String msg;

    public PrepareCheckResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PrepareCheckResponse(PreparationCheckResultEnum checkResultEnum) {
        this(checkResultEnum.getCode(), checkResultEnum.getMsg());
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
