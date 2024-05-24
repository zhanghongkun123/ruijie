package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto;

/**
 * Description: 水印自定义显示内容对象,这个对象定义的字段需要根据业务需求做修改
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/3
 *
 * @author jarman
 */

public class WatermarkDisplayContentDTO {

    private String[] fieldArr;

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
