package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModelProperty;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月08日
 *
 * @author zhk
 */
public class CompleteAppTestWebRequest implements WebRequest {

    /**
     * 测试结果
     */
    @NotNull
    @ApiModelProperty(value = "测试结果", required = true)
    private TestTaskStateEnum state;

    /**
     * 原因
     */
    @ApiModelProperty(value = "原因")
    @TextMedium
    @Nullable
    private String reason;

    /**
     * 测试ID
     */
    @NotNull
    @ApiModelProperty(value = "测试ID", required = true)
    private UUID id;

    public TestTaskStateEnum getState() {
        return state;
    }

    public void setState(TestTaskStateEnum state) {
        this.state = state;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
