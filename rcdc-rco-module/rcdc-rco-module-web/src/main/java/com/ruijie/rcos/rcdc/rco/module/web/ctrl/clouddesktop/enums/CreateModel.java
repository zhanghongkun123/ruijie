package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums;

/**
 * Description: CreateModel Description
 * Copyright: Copyright (c) 2017
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/04/21
 *
 * @author chixin
 */
public enum CreateModel {

    CLONE_EXIT(1),

    ISO_IMAGE_FILE(0);



    private Integer code;

    CreateModel(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
