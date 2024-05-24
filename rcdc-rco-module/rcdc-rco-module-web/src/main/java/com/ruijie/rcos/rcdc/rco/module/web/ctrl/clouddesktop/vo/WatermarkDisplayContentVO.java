package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.TextShort;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */
public class WatermarkDisplayContentVO {

    @NotEmpty
    private String[] fieldArr;

    @TextShort
    private String customContent;

    public String[] getFieldArr() {
        return fieldArr;
    }

    public void setFieldArr(String[] fieldArr) {
        this.fieldArr = fieldArr;
    }

    public String getCustomContent() {
        return customContent;
    }

    public void setCustomContent(String customContent) {
        this.customContent = customContent;
    }
}
