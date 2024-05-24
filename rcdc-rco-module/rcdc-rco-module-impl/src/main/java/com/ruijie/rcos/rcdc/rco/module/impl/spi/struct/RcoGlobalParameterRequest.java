package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;


/**
 * Description: t_rco_global_parameter请求类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/7
 * @param <E> 请求参数类型
 * @author TD
 */
public class RcoGlobalParameterRequest<E> {

    /**
     * 请求key
     */
    private String paramKey;

    /**
     * 请求参数内容
     */
    private E requestContent;

    /**
     * 默认内容：如果从RCDC获取失败，或者无需获取直接给定
     */
    private String defaultValue;

    public RcoGlobalParameterRequest(String paramKey, E requestContent, String defaultValue) {
        this.paramKey = paramKey;
        this.requestContent = requestContent;
        this.defaultValue = defaultValue;
    }

    /**
     * 是否保存到配置文件：默认保存到dataPub.properties文件中
     * 键：paramKey，值为：defaultValue或者返回response
     * isSave默认 true
     */
    private Boolean isSave = Boolean.TRUE;



    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public E getRequestContent() {
        return requestContent;
    }

    public void setRequestContent(E requestContent) {
        this.requestContent = requestContent;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
