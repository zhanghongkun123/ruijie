package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.test.enter;

import com.ruijie.rcos.sk.base.annotation.NotEmpty;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Description: 进入测试任务
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/13
 *
 * @author zhiweiHong
 */
@ApiModel("进入单桌面测试请求")
public class EnterDesktopTestWebRequest {

    @NotNull
    @ApiModelProperty(value = "测试id", required = true)
    private UUID testId;

    @NotEmpty
    @ApiModelProperty(value = "桌面Id", required = true)
    private UUID[] deskIdArr;

    public UUID getTestId() {
        return testId;
    }

    public void setTestId(UUID testId) {
        this.testId = testId;
    }

    public UUID[] getDeskIdArr() {
        return deskIdArr;
    }

    public void setDeskIdArr(UUID[] deskIdArr) {
        this.deskIdArr = deskIdArr;
    }
}
