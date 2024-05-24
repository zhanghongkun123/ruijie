package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 批量编辑云桌面标签请求
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/29 13:44
 *
 * @author zdc
 */
public class BatchEditVDIDeskRemarkRequest {

    @NotNull
    private EditVDIDeskRemarkRequest[] deskInfoArr;

    public EditVDIDeskRemarkRequest[] getDeskInfoArr() {
        return deskInfoArr;
    }

    public void setDeskInfoArr(EditVDIDeskRemarkRequest[] deskInfoArr) {
        this.deskInfoArr = deskInfoArr;
    }
}