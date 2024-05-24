package com.ruijie.rcos.rcdc.rco.module.web.ctrl.servermodel.response;

import io.swagger.annotations.ApiModelProperty;

/**
 * Description: HeartBeatWebResponse
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/4 15:46
 *
 * @author wjp
 */
public class HeartBeatWebResponse {

    @ApiModelProperty(value = "心跳健康标识， 成功为142857")
    private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

}
