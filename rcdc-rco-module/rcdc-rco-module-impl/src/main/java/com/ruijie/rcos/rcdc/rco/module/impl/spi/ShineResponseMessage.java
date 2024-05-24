package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/21
 *
 * @author chen zj
 */
public class ShineResponseMessage {

    private ShineResponseMessage() {
        throw new IllegalStateException("ShineResponseMessage Utility class");
    }
    /**
     * 构建成功CbbDispatcherRequest消息
     *
     * @param request CbbDispatcherRequest
     * @return CbbResponseShineMessage
     */

    protected static <T> CbbResponseShineMessage success(CbbDispatcherRequest request, T content) {
        Assert.notNull(request, "Param [cbbDispatcherRequest] must not be null");

        CbbResponseShineMessage responseShineMessage = buildShineMessage(request, CommonMessageCode.SUCCESS);
        responseShineMessage.setContent(content);

        return responseShineMessage;
    }

    /**
     * 构建失败消息
     *
     * @param request CbbDispatcherRequest
     * @return CbbResponseShineMessage
     */
    protected static CbbResponseShineMessage fail(CbbDispatcherRequest request) {
        Assert.notNull(request, "Param [cbbDispatcherRequest] must not be null");

        return buildShineMessage(request, CommonMessageCode.CODE_ERR_OTHER);
    }

    /**
     * 构建失败消息
     *
     * @param request CbbDispatcherRequest
     * @return CbbResponseShineMessage
     */
    protected static CbbResponseShineMessage failWhitCode(CbbDispatcherRequest request, int code) {
        Assert.notNull(request, "Param [cbbDispatcherRequest] must not be null");

        return buildShineMessage(request, code);
    }


    /**
     * 从CbbDispatcherRequest拼接CbbResponseShineMessage信息
     *
     * @param request CbbDispatcherRequest
     * @param code 错误吗
     * @return CbbResponseShineMessage
     */
    private static CbbResponseShineMessage buildShineMessage(CbbDispatcherRequest request, int code) {
        CbbResponseShineMessage responseMessage = new CbbResponseShineMessage();
        responseMessage.setAction(request.getDispatcherKey());
        responseMessage.setRequestId(request.getRequestId());
        responseMessage.setTerminalId(request.getTerminalId());
        responseMessage.setCode(code);

        return responseMessage;
    }
}
