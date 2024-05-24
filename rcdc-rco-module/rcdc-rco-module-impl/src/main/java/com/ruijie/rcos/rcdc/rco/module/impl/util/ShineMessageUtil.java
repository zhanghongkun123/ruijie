package com.ruijie.rcos.rcdc.rco.module.impl.util;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import org.springframework.util.Assert;


/**
 * Description: Shine消息构建工具类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/10 14:42
 *
 * @author zhangyichi
 */
public class ShineMessageUtil {

    private ShineMessageUtil() {
        throw new IllegalStateException("ShineMessageUtil Utility class");
    }

    private static final Integer SUCCESS = 0;


    /**
     * 构造shine应答消息
     *
     * @param request 请求参数
     * @param content 响应内容
     * @return shine应答消息
     */
    public static CbbResponseShineMessage buildResponseMessage(CbbDispatcherRequest request, Object content) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(content, "content can not be null");
        return buildResponseMessageWithContent(request, SUCCESS, content);
    }

    /**
     * 构造异常情况shine应答消息
     *
     * @param request 请求参数
     * @param code 异常码
     * @return shine应答消息
     */
    public static CbbResponseShineMessage buildErrorResponseMessage(CbbDispatcherRequest request, Integer code) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(code, "code can not be null");

        CbbResponseShineMessage responseMessage = new CbbResponseShineMessage();
        responseMessage.setAction(request.getDispatcherKey());
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setTerminalId(request.getTerminalId());
        responseMessage.setCode(code);

        return responseMessage;
    }

    /**
     * 构造带有异常信息的shine应答消息
     *
     * @param request 请求参数
     * @param code 异常码
     * @param content 异常信息
     * @return shine应答消息
     */
    public static CbbResponseShineMessage buildResponseMessageWithContent(CbbDispatcherRequest request, Integer code, Object content) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(code, "code can not be null");
        Assert.notNull(content, "content can not be null");

        CbbResponseShineMessage responseShineMessage = new CbbResponseShineMessage();
        responseShineMessage.setAction(request.getDispatcherKey());
        responseShineMessage.setRequestId(request.getRequestId());
        responseShineMessage.setTerminalId(request.getTerminalId());
        responseShineMessage.setCode(code);
        responseShineMessage.setContent(content);

        return responseShineMessage;
    }
}
