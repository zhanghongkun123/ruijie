package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

import com.alibaba.fastjson.JSONObject;

/**
 * EstCommonActionRequest Est透传RCDC消息通道 请求DTO
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 * 
 * @author lihengjing
 */
public class EstCommonActionRequest {

    /**
     * 子动作Action 小写 例如 snapshot_list
     */
    private String subAction;

    /**
     * 子动作所需的业务参数
     */
    private JSONObject data;

    /**
     * 当前运行的云桌面的启动信息（前端传过来的信息）
     */
    private JSONObject currentVmData;

    public String getSubAction() {
        return subAction;
    }

    public void setSubAction(String subAction) {
        this.subAction = subAction;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public JSONObject getCurrentVmData() {
        return currentVmData;
    }

    public void setCurrentVmData(JSONObject currentVmData) {
        this.currentVmData = currentVmData;
    }

}
