package com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.Assert;

/**
 * Description: CAS扫码返回DTO
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/21
 *
 * @author TD
 */
public class CasScanResultDTO {

    /**
     * 正确响应
     */
    private JSONObject successfulResponse;

    /**
     * 异常响应
     */
    private JSONObject errorResponse;

    public JSONObject getSuccessfulResponse() {
        return successfulResponse;
    }

    public void setSuccessfulResponse(JSONObject successfulResponse) {
        this.successfulResponse = successfulResponse;
    }

    public JSONObject getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(JSONObject errorResponse) {
        this.errorResponse = errorResponse;
    }

    public CasScanResultDTO(JSONObject successfulResponse, JSONObject errorResponse) {
        this.successfulResponse = successfulResponse;
        this.errorResponse = errorResponse;
    }

    /**
     * 构建正确返回数据
     * @param value 正确响应参数
     * @return 扫码通用返回
     */
    public static CasScanResultDTO successfulResponse(Object value) {
        Assert.notNull(value, "返回数据不能为空");
        return new CasScanResultDTO(JSON.parseObject(JSON.toJSONString(value)), null);
    }

    /**
     * 构建异常返回数据
     * @param value 异常提示语
     * @return 扫码通用返回
     */
    public static CasScanResultDTO errorResponse(String value) {
        Assert.notNull(value, "错误提示信息不能为空");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("errorMsg", value);
        return new CasScanResultDTO(null, jsonObject);
    }
}
