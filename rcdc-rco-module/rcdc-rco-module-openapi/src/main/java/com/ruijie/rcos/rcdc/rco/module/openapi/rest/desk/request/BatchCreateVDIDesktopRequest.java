package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.request;


import com.ruijie.rcos.sk.base.annotation.NotNull;


/**
 * Description: 批量创建云桌面软请求参数
 *
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author zqj
 */
public class BatchCreateVDIDesktopRequest {

    @NotNull
    private CreateVDIDesktopRequest[] deskInfoArr;

    /**
     * 创建失败是否回滚已创建的云桌面
     */
    @NotNull
    private Boolean failRollback = false;

    public CreateVDIDesktopRequest[] getDeskInfoArr() {
        return deskInfoArr;
    }

    public void setDeskInfoArr(CreateVDIDesktopRequest[] deskInfoArr) {
        this.deskInfoArr = deskInfoArr;
    }

    public Boolean getFailRollback() {
        return failRollback;
    }

    public void setFailRollback(Boolean failRollback) {
        this.failRollback = failRollback;
    }
}
