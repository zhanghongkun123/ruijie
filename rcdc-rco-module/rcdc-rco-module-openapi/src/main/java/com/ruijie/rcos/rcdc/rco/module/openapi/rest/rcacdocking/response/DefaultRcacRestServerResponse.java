package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.response;

import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rcacdocking.enums.RcacRestResponseResultEnum;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import org.springframework.util.Assert;

/**
 * Description: Rcac对接rest接口默认响应
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/5/7 16:03
 *
 * @author zhangyichi
 */
public class DefaultRcacRestServerResponse {

    private RcacRestResponseResultEnum result;

    private String message;

    public RcacRestResponseResultEnum getResult() {
        return result;
    }

    public void setResult(RcacRestResponseResultEnum result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 成功
     * @return 响应
     */
    public static DefaultRcacRestServerResponse success() {
        DefaultRcacRestServerResponse response = new DefaultRcacRestServerResponse();
        response.setResult(RcacRestResponseResultEnum.SUCCESS);
        return response;
    }

    /**
     * 失败
     * @param message 失败信息
     * @return 响应
     */
    public static DefaultRcacRestServerResponse fail(String message) {
        Assert.hasText(message, "message cannt be null!");

        DefaultRcacRestServerResponse response = new DefaultRcacRestServerResponse();
        response.setResult(RcacRestResponseResultEnum.FAIL);
        response.setMessage(message);
        return response;
    }

    /**
     * 失败
     * @param key 国际化key
     * @param args 国际化参数
     * @return 响应
     */
    public static DefaultRcacRestServerResponse failWithLocale(String key, String... args) {
        Assert.hasText(key, "key cannot be null!");
        Assert.notNull(args, "args cannot be null!");

        DefaultRcacRestServerResponse response = new DefaultRcacRestServerResponse();
        response.setResult(RcacRestResponseResultEnum.FAIL);
        response.setMessage(LocaleI18nResolver.resolve(key, args));
        return response;
    }
}
