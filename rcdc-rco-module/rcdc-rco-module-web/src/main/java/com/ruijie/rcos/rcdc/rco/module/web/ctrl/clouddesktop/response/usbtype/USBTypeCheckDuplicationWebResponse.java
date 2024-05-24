package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.usbtype;

/**
 * 
 * Description: USB类型命名唯一性校验
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月14日
 * 
 * @author Ghang
 */
public class USBTypeCheckDuplicationWebResponse {

    private Boolean hasDuplication;

    public Boolean getHasDuplication() {
        return hasDuplication;
    }

    public void setHasDuplication(Boolean hasDuplication) {
        this.hasDuplication = hasDuplication;
    }
}
