package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.Range;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

/**
 * Description: 高级配置请求
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 *
 * @author fyq
 */
@ApiModel("编辑高级配置请求")
public class UpdateAdvanceConfigRequest implements WebRequest {

    /**
     * 是否开启评测功能
     */
    @ApiModelProperty(value = "是否开启评测功能")
    @Nullable
    private Boolean enableEvaluation;

    /**
     * 是否开启虚拟应用
     */
    @ApiModelProperty(value = "是否开启虚拟应用")
    @Nullable
    private Boolean enableVirtualApplication;

    public Boolean getEnableEvaluation() {
        return enableEvaluation;
    }

    public void setEnableEvaluation(Boolean enableEvaluation) {
        this.enableEvaluation = enableEvaluation;
    }

    @Nullable
    public Boolean getEnableVirtualApplication() {
        return enableVirtualApplication;
    }

    public void setEnableVirtualApplication(@Nullable Boolean enableVirtualApplication) {
        this.enableVirtualApplication = enableVirtualApplication;
    }
}
