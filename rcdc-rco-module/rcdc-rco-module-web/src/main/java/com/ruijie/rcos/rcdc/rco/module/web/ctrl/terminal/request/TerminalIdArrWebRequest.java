package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.request;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.Size;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/18
 *
 * @author Jarman
 */
public class TerminalIdArrWebRequest implements WebRequest {

    @ApiModelProperty(value = "ID数组", required = true)
    @NotEmpty
    @Size(min = 1)
    private String[] idArr;

    @ApiModelProperty(value = "保留镜像")
    @Nullable
    private Boolean retainImage;

    public TerminalIdArrWebRequest() {

    }

    public TerminalIdArrWebRequest(String[] idArr) {
        this.idArr = idArr;
    }

    public String[] getIdArr() {
        return idArr;
    }

    public void setIdArr(String[] idArr) {
        this.idArr = idArr;
    }

    @Nullable
    public Boolean getRetainImage() {
        return retainImage;
    }

    public void setRetainImage(@Nullable Boolean retainImage) {
        this.retainImage = retainImage;
    }
}
