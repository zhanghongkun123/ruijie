package com.ruijie.rcos.rcdc.rco.module.web.response;


import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.webmvc.api.response.WebResponse;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/**
 * Description: 通用返回对象
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/09/23
 *
 * @param <T> 响应数据
 * @author luojianmo
 */
public class CommonWebResponse<T> implements WebResponse {

    private Status status;

    private String message;

    private T content;

    private String msgKey;

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

    protected CommonWebResponse() {
    }

    protected CommonWebResponse(Status status, String message, T content) {
        this.status = status;
        this.message = message;
        this.content = content;
    }

    /**
     * 成功返回结果
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> CommonWebResponse<T> success() {
        return new CommonWebResponse<>(Status.SUCCESS, null, null);
    }

    /**
     * 成功返回结果
     * @param content 响应数据
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> CommonWebResponse<T> success(T content) {
        Assert.notNull(content, "content is null");
        return new CommonWebResponse<>(Status.SUCCESS, null, content);
    }

    /**
     * 成功返回结果
     * @param msgKey 消息KEY
     * @param content 响应数据
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> CommonWebResponse<T> success(String msgKey, T content) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notNull(content, "content is null");
        CommonWebResponse<T> response = new CommonWebResponse<>();
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
    public static <T> CommonWebResponse<T> success(String msgKey, String[] msgArgs) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notNull(msgArgs, "msgArgArr is not empty");
        CommonWebResponse<T> response = new CommonWebResponse<>();
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
    public static <T> CommonWebResponse<T> success(String msgKey, String[] msgArgs, T content) {
        Assert.hasText(msgKey, "msgKey is not empty");
        Assert.notEmpty(msgArgs, "msgArgArr is not empty");
        Assert.notNull(content, "content is null");
        CommonWebResponse<T> response = new CommonWebResponse<>();
        response.status = Status.SUCCESS;
        response.content = content;
        response.message = LocaleI18nResolver.resolve(msgKey, msgArgs);
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }

    /**
     * 异常返回结果
     * @param msgKey 消息KEY
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> CommonWebResponse<T> fail(String msgKey) {
        Assert.hasText(msgKey, "msgKey is not empty");
        CommonWebResponse<T> response = new CommonWebResponse<>();
        response.status = Status.ERROR;
        response.message = LocaleI18nResolver.resolve(msgKey, new String[]{});
        response.msgKey = msgKey;
        return response;
    }

    /**
     * 异常返回结果
     * @param msgKey 消息KEY
     * @param msgArgs 消息填充值
     * @param <T> 响应数据
     * @return 响应
     */
    public static <T> CommonWebResponse<T> fail(String msgKey, @Nullable String[] msgArgs) {
        Assert.hasText(msgKey, "msgKey is not empty");
        CommonWebResponse<T> response = new CommonWebResponse<>();
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
    public static <T> CommonWebResponse<T> fail(Status status, String msgKey, String[] msgArgs) {
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
    public static <T> CommonWebResponse<T> fail(Status status, String msgKey, String[] msgArgs, String message) {
        Assert.notNull(status, "status is not empty");
        Assert.isTrue(Status.SUCCESS != status, "status is not success");
        Assert.hasText(msgKey, "msgkey is not empty");
        Assert.notNull(msgArgs, "msgArgArr is not empty");
        Assert.notNull(message, "message is not null");
        CommonWebResponse<T> response = new CommonWebResponse<>();
        response.status = status;
        response.message = message;
        response.msgKey = msgKey;
        response.msgArgArr = msgArgs;
        return response;
    }


}
