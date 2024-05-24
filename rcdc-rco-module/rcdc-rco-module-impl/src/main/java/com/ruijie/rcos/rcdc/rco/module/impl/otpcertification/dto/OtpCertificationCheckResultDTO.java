package com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月14日
 *
 * @author zhanghongkun
 */
public class OtpCertificationCheckResultDTO {
    /**
     * 校验结果
     */
    private Boolean enableCheck;

    /**
     * code的编码
     */
    private int code;

    public Boolean getEnableCheck() {
        return enableCheck;
    }

    public void setEnableCheck(Boolean enableCheck) {
        this.enableCheck = enableCheck;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
