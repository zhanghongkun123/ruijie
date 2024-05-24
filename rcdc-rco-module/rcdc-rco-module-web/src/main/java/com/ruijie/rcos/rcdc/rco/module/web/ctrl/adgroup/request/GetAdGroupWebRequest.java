package com.ruijie.rcos.rcdc.rco.module.web.ctrl.adgroup.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/13
 *
 * @author zqj
 */
public class GetAdGroupWebRequest implements WebRequest {

    /**
     * AD域组ObjectGuid集合
     */
    @NotEmpty
    @ApiModelProperty(value = "AD域组ObjectGuid集合", required = true)
    private String[] objectGuidArr;

    public String[] getObjectGuidArr() {
        return objectGuidArr;
    }

    public void setObjectGuidArr(String[] objectGuidArr) {
        this.objectGuidArr = objectGuidArr;
    }
}
