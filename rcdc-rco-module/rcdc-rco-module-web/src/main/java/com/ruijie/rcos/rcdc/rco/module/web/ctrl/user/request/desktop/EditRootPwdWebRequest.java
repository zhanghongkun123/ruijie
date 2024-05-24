package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.desktop;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: web请求
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/10/11 9:26
 *
 * @author yxq
 */
public class EditRootPwdWebRequest {

    @ApiModelProperty(value = "桌面id列表", required = true)
    @NotEmpty
    private UUID[] idArr;

    @ApiModelProperty(value = "是否展示密码", required = true)
    @NotNull
    private Boolean showRootPwd;

    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public Boolean getShowRootPwd() {
        return showRootPwd;
    }

    public void setShowRootPwd(Boolean showRootPwd) {
        this.showRootPwd = showRootPwd;
    }
}
