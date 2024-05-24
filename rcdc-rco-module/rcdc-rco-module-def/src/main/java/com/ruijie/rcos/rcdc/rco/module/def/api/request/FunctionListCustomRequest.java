package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 功能列表自定义请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public class FunctionListCustomRequest implements Request {

    /**
     * 管理员id
     */
    @NotNull
    private UUID adminId;

    /**
     * 功能模块类型
     */
    @NotBlank
    private String functionType;

    /**
     * 列数据
     */
    @Nullable
    private String[] columnTextArr;

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
