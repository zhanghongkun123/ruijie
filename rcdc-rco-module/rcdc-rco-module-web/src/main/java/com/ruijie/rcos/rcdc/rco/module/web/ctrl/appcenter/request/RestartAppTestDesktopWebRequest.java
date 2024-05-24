package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年01月05日
 *
 * @author zhk
 */
@ApiModel("重新发起桌面测试")
public class RestartAppTestDesktopWebRequest implements WebRequest {

    @NotEmpty
    @ApiModelProperty(value = "桌面id", required = true)
    private UUID deskId;

    @NotNull
    @ApiModelProperty(value = "测试任务id", required = true)
    private UUID testId;

    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(UUID deskId) {
        this.deskId = deskId;
    }

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }
}
