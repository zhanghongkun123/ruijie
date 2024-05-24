package com.ruijie.rcos.rcdc.rco.module.def.user.dto;

/**
 * Description: 认证结果
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月16日
 *
 * @author xwx
 */
public class CertificationResultDTO {

    /**
     * 认证结果
     */
    private boolean result;

    /**
     * 异常信息
     */
    private String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
