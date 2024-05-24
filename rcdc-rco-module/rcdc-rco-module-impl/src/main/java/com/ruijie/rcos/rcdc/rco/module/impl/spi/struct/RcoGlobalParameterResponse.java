package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/7
 *
 * @author TD
 */
public class RcoGlobalParameterResponse {

    /**
     * 返回key
     */
    private String paramKey;

    /**
     * 返回内容
     */
    private String responseContent;

    public RcoGlobalParameterResponse(String paramKey, String responseContent) {
        this.paramKey = paramKey;
        this.responseContent = responseContent;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }
}
