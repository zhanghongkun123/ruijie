package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 编辑应用主机最大会话数请求
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/8/22 13:51
 *
 * @author gaoxueyuan
 */
public class EditHostSessionTotalRequest implements WebRequest {

    @ApiModelProperty(value = "主机Id组", required = true)
    @NotEmpty
    @Size(min = 1)
    private UUID[] idArr;

    @NotNull
    @Range(min = "0", max = "200")
    private Integer sessionTotal;


    public UUID[] getIdArr() {
        return idArr;
    }

    public void setIdArr(UUID[] idArr) {
        this.idArr = idArr;
    }

    public Integer getSessionTotal() {
        return sessionTotal;
    }

    public void setSessionTotal(Integer sessionTotal) {
        this.sessionTotal = sessionTotal;
    }
}
