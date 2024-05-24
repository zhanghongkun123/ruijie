package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.log;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.webmvc.api.request.DownloadWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.WebRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 下载日志请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
@ApiModel("下载日志请求")
public class DownloadLogTestWebRequest implements DownloadWebRequest {

    @NotNull
    @ApiModelProperty(value = "测试id", required = true)
    private UUID testId;

    @NotNull
    @ApiModelProperty(value = "桌面Id", required = true)
    private UUID desktopId;

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }
}
