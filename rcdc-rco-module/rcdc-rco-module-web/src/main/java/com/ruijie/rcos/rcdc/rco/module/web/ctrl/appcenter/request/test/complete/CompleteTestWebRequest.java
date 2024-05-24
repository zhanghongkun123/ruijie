package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.test.complete;

import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: 完成测试任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
@ApiModel("完成测试请求")
public class CompleteTestWebRequest implements WebRequest {

    @NotNull
    @ApiModelProperty(value = "测试id", required = true)
    private UUID testId;

    @Nullable
    @ApiModelProperty(value = "测试状态", required = true)
    private TestTaskStateEnum state;

    @Nullable
    @ApiModelProperty(value = "原因")
    private String reason;

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public TestTaskStateEnum getState() {
        return state;
    }

    public void setState(TestTaskStateEnum state) {
        this.state = state;
    }

    @Nullable
    public String getReason() {
        return reason;
    }

    public void setReason(@Nullable String reason) {
        this.reason = reason;
    }
}
