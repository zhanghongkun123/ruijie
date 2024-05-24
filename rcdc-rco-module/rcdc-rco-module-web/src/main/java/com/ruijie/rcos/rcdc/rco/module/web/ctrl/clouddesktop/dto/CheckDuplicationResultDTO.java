package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

/**
 * 
 * Description: 命名唯一性校验
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 * 
 * @author Ghang
 */
public class CheckDuplicationResultDTO {
    private Boolean hasDuplication;

    private String errorMsg;

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
