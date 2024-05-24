package com.ruijie.rcos.rcdc.rco.module.def.sms.dto;

import com.ruijie.rcos.rcdc.rco.module.def.sms.request.HttpPairRequest;
import com.ruijie.rcos.sk.base.annotation.*;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * Description: http配置DTO
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/8
 *
 * @author TD
 */
public class HttpConfigDTO {

    /**
     * 请求url
     */
    @NotBlank
    @Size(max = 256)
    @URL
    private String url;

    /**
     * 方法类型
     */
    @NotNull
    private HttpMethod httpMethod;

    /**
     * 编码类型
     */
    @NotBlank
    @TextShort
    private String encodingType;

    /**
     * 请求头部
     */
    @Nullable
    private List<HttpPairRequest> headerList;

    /**
     * 是否隐藏请求内容
     */
    @NotNull
    private Boolean enableHide;

    /**
     * 请求内容
     */
    @NotBlank
    @Size(max = 1024)
    private String body;

    /**
     * 解析器开关
     */
    @NotNull
    private Boolean enableParser;

    /**
     * 请求返回解析器
     */
    @Nullable
    private HttpResultParserDTO httpResultParser;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }
    
    public String getEncodingType() {
        return encodingType;
    }

    public void setEncodingType(String encodingType) {
        this.encodingType = encodingType;
    }

    @Nullable
    public List<HttpPairRequest> getHeaderList() {
        return headerList;
    }

    public void setHeaderList(@Nullable List<HttpPairRequest> headerList) {
        this.headerList = headerList;
    }

    public Boolean getEnableHide() {
        return enableHide;
    }

    public void setEnableHide(Boolean enableHide) {
        this.enableHide = enableHide;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Boolean getEnableParser() {
        return enableParser;
    }

    public void setEnableParser(Boolean enableParser) {
        this.enableParser = enableParser;
    }

    public HttpResultParserDTO getHttpResultParser() {
        return httpResultParser;
    }

    public void setHttpResultParser(HttpResultParserDTO httpResultParser) {
        this.httpResultParser = httpResultParser;
    }
}
