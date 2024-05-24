package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageSessionRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.AdminLoginExceptionEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;



/**
 * Description: VDI终端编辑镜像，消息处理抽象类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/20 16:32
 *
 * @param <T> 传入消息类型
 * @author zhangyichi
 */
public abstract class AbstractVDIEditImageHandlerSPIImpl<T extends VDIEditImageSessionRequestDTO> implements CbbDispatcherHandlerSPI {

    /**
     * 会话不存在
     */
    private static final Integer SESSION_NOT_EXIST = -97;

    /**
     * 管理员被禁用
     */
    private static final Integer ADMIN_DISABLED = -197;

    private static final Integer FAILURE = -1;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;


    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractVDIEditImageHandlerSPIImpl.class);


    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");

        String dataJsonString = request.getData();
        Assert.hasText(dataJsonString, "data string cannot be blank!");
        T requestDTO = resolveJsonString(dataJsonString);
        // 查询会话ID是否有效
        AdminLoginOnTerminalCache loginCache = cacheManager.getIfPresent(requestDTO.getAdminSessionId());
        if (loginCache == null) {
            CbbResponseShineMessage errorShineMessage =
                    ShineMessageUtil.buildResponseMessageWithContent(request, SESSION_NOT_EXIST, new JSONObject());
            messageHandlerAPI.response(errorShineMessage);
            return;
        }


        try {
            // 获取管理员信息
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(loginCache.getAdminId());
            // 管理员是禁用状态
            if (!baseAdminDTO.getEnabled()) {
                CbbResponseShineMessage errorShineMessage = ShineMessageUtil.buildResponseMessageWithContent(request,
                        AdminLoginExceptionEnum.ADMIN_IS_DISABLE.getCode(), new JSONObject());
                messageHandlerAPI.response(errorShineMessage);
                return;
            }
        } catch (Exception e) {
            LOGGER.error("终端{}获取管理员信息失败", request.getTerminalId(), e);
            CbbResponseShineMessage errorShineMessage =
                    ShineMessageUtil.buildResponseMessageWithContent(request, Constants.FAILURE, new JSONObject());
            messageHandlerAPI.response(errorShineMessage);
            return;
        }


        CbbResponseShineMessage successShineMessage = getResponseMessage(request, requestDTO, loginCache.getAdminId());
        messageHandlerAPI.response(successShineMessage);
    }

    abstract CbbResponseShineMessage getResponseMessage(CbbDispatcherRequest request, T requestDTO, UUID adminId);

    abstract T resolveJsonString(String dataJsonString);

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

        return ShineMessageUtil.buildResponseMessage(request, content);
    }

    /**
     * 构造异常情况shine应答消息
     *
     * @param request 请求参数
     * @return shine应答消息
     */
    public static CbbResponseShineMessage buildErrorResponseMessage(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        return ShineMessageUtil.buildErrorResponseMessage(request, FAILURE);
    }
}
