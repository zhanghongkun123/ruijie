package com.ruijie.rcos.rcdc.rco.module.def.api.response;

import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;

import java.util.UUID;

/**
 * Description: 功能列表自定义列数据响应
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public class FunctionListCustomResponse extends DefaultResponse {

    private UUID id;

    private UUID adminId;

    private String functionType;

    private String[] columnTextArr;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }

    public String getFunctionType() {
        return functionType;
    }

    public void setFunctionType(String functionType) {
        this.functionType = functionType;
    }

    public String[] getColumnTextArr() {
        return columnTextArr;
    }

    public void setColumnTextArr(String[] columnTextArr) {
        this.columnTextArr = columnTextArr;
    }
}
