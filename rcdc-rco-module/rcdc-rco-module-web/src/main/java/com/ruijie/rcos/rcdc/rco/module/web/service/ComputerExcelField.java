package com.ruijie.rcos.rcdc.rco.module.web.service;

/**
 * Description: excel单元格数据列
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/19
 *
 * @author zqj
 */
public enum ComputerExcelField {

    GROUP_NAME("终端分组名称", 0),
    IP("IP地址", 1),
    USER_NAME("用户名", 2),
    DESK_STRATEGY("第三方桌面策略", 3),
    REMARK("备注", 4);

    private String header;

    private Integer index;

    ComputerExcelField(String header, Integer index) {
        this.header = header;
        this.index = index;
    }

    public String getHeader() {
        return header;
    }

    public Integer getIndex() {
        return index;
    }

}
