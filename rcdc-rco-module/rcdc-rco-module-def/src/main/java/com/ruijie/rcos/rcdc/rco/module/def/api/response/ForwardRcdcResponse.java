package com.ruijie.rcos.rcdc.rco.module.def.api.response;


import com.alibaba.fastjson.JSONObject;

/**
 * Description: ForwardRcdcRequest
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-22
 *
 * @author zqj
 */
public class ForwardRcdcResponse {

    private int resultCode;

    /**
     * 存放上层业务返回值
     */
    private JSONObject content;

    /**
     * 信息
     */
    private String message;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public JSONObject getContent() {
        return content;
    }

    public void setContent(JSONObject content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
