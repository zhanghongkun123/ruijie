package com.ruijie.rcos.rcdc.rco.module.impl.spi.struct;


import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;

/**
 * Description: EST跟RCDC透传接口 通用返回子对象 被CommonActionResponse包裹
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月31日
 *
 * @param <T> 响应参数
 * @author lihengjing
 */
public class EstCommonActionSubResponse<T> implements WebResponse {

    @Nullable
    private Status status;

    @Nullable
    private String message;

    @Nullable
    private T content;

    @Nullable
    private String msgKey;

    @Nullable
    private String[] msgArgArr;

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getMsgKey() {
        return msgKey;
    }

    @Override
    public String[] getMsgArgArr() {
        return msgArgArr;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }

    public void setMsgArgArr(String[] msgArgArr) {
        this.msgArgArr = msgArgArr;
    }

    protected EstCommonActionSubResponse() {
    }

    protected EstCommonActionSubResponse(Status status, String message, T content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    /**
     * 成功返回结果
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> success() {
        return new EstCommonActionSubResponse<T>(Status.SUCCESS, null, null);
    }

    /**
     * 成功返回结果
     * @param content 响应数据
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> success(T content) {
        Assert.notNull(content, "content is null");
        return new EstCommonActionSubResponse<T>(Status.SUCCESS, null, content);
    }

    /**
     * 成功返回结果
     * @param msgKey 消息KEY
     * @param content 响应数据
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> success(String msgKey, T content) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notNull(content, "content is null");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.SUCCESS;
        response.content = content;
        response.msgKey = msgKey;
        response.msgArgArr = new String[0];
        response.message = LocaleI18nResolver.resolve(msgKey, response.msgArgArr);
        return response;
    }

    /**
     * 成功返回结果
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> success(String msgKey, String[] msgArgs) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notNull(msgArgs, "msgArgArr is not empty");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.SUCCESS;
        response.message = LocaleI18nResolver.resolve(msgKey, msgArgs);
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }

    /**
     * 成功返回结果
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param content 响应数据
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> success(String msgKey, String[] msgArgs, T content) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notEmpty(msgArgs, "msgArgArr is not empty");
        Assert.notNull(content, "content is null");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.SUCCESS;
        response.content = content;
        response.message = LocaleI18nResolver.resolve(msgKey, msgArgs);
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }

    /**
     * 异常返回结果
     * @param msg 消息KEY
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> fail(@Nullable String msg) {
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.ERROR;
        response.message = LocaleI18nResolver.resolve(msg);
//        response.msgKey = msg;
        return response;
    }

    /**
     * 异常返回结果
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> fail(String msgKey, @Nullable String[] msgArgs) {
        Assert.hasText(msgKey, "msgKey is not empty");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.ERROR;
        response.message = LocaleI18nResolver.resolve(msgKey, msgArgs);
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }

    /**
     * 异常返回结果
     * @param status 响应状态
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> fail(Status status, String msgKey, String[] msgArgs) {
        Assert.notNull(status, "status is not empty");
        Assert.isTrue(Status.SUCCESS != status, "status is not success");
        Assert.hasText(msgKey, "msgkey is not empty");
        Assert.notNull(msgArgs, "msgArgArr is not empty");
        String message = LocaleI18nResolver.resolve(msgKey, msgArgs);
        return fail(status, msgKey, msgArgs, message);
    }

    /**
     * 异常返回结果
     * @param status 响应状态
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param message 提示信息
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> fail(Status status, String msgKey, String[] msgArgs, String message) {
        Assert.notNull(status, "status is not empty");
        Assert.isTrue(Status.SUCCESS != status, "status is not success");
        Assert.hasText(msgKey, "msgkey is not empty");
        Assert.notNull(msgArgs, "msgArgArr is not empty");
        Assert.notNull(message, "message is not null");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = status;
        response.message = message;
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }



    /**
     * 异常返回结果
     * @param message 提示信息
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> EstCommonActionSubResponse<T> failWithMsg(String message) {
        Assert.notNull(message, "message is not null");
        EstCommonActionSubResponse<T> response = new EstCommonActionSubResponse<>();
        response.status = Status.ERROR;
        response.message = message;
        return response;
    }

}
