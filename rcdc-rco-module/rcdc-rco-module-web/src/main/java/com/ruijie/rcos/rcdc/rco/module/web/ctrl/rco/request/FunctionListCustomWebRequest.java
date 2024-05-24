package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request;

import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import org.springframework.lang.Nullable;

/**
 * Description: 功能列表自定义请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年2月28日
 *
 * @author brq
 */
public class FunctionListCustomWebRequest implements WebRequest {

    /**
     * 功能模块类型
     */
    @NotBlank
    @Size(max = 32)
    private String functionType;

    /**
     * 列数据
     */
    @Nullable
    private String[] columnTextArr;

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
