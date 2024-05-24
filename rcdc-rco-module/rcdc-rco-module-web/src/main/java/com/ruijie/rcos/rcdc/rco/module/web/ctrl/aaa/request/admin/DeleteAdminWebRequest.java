package com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.request.admin;

import java.util.UUID;
import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月21日
 * 
 * @author zhuangchenwu
 */
public class DeleteAdminWebRequest implements WebRequest {

    @ApiModelProperty(value = "管理员ID数组", required = true)
    @NotEmpty
    private UUID[] idArr;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

}
