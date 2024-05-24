package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request;

import com.ruijie.rcos.sk.base.annotation.NotNull;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月22日
 *
 * @author zdc
 */
public class BatchUpdateHardwareCertificationRequest {

    /**
     * 用户终端绑定请求数组
     */
    @NotNull
    UpdateHardwareCertificationRequest[] updateBindingArr;

    public UpdateHardwareCertificationRequest[] getUpdateBindingArr() {
        return updateBindingArr;
    }

    public void setUpdateBindingArr(UpdateHardwareCertificationRequest[] updateBindingArr) {
        this.updateBindingArr = updateBindingArr;
    }
}
