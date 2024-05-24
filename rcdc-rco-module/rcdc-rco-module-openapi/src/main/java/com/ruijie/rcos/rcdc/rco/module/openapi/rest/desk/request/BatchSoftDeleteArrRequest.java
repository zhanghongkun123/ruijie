package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;

/**
 * Description: 批量云桌面软删除请求参数
 *
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author zqj
 */
public class BatchSoftDeleteArrRequest {

    @NotNull
    private SoftDeleteInfoRequest[] deskInfoArr;

    public SoftDeleteInfoRequest[] getDeskInfoArr() {
        return deskInfoArr;
    }

    public void setDeskInfoArr(SoftDeleteInfoRequest[] deskInfoArr) {
        this.deskInfoArr = deskInfoArr;
    }
}
