package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.openapi.exception.OpenAPIDesktopBindTerminalException;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.StartVmMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.StartVmResponse;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/6
 *
 * @author Jarman
 */
@Service
public class StartDesktopSPIHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartDesktopSPIHelper.class);

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    /**
     * 应答桌面连接信息给shine
     *
     * @param notifyRequest request
     * @return true 成功应答桌面连接信息
     * @throws Exception Exception
     */
    public boolean responseDeskConnectionInfoToShine(CbbDeskOperateNotifyRequest notifyRequest) throws Exception {
        Assert.notNull(notifyRequest, "notifyRequest cannot be null");
        UUID deskId = notifyRequest.getDeskId();
        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(deskId);
        if (desktopRequestDTO == null) {
            throw new IllegalStateException("找不到请求终端数据,deskId=" + deskId);
        }
        CbbDispatcherRequest dispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
        if (dispatcherRequest == null) {
            // 仅作为 Openapi 接口启动云桌面接口
            LOGGER.warn("对应桌面标识 [{}] 需要和终端进行绑定", deskId);
            throw new OpenAPIDesktopBindTerminalException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCD_RCO_SHINE_START_VM_NOT_BIND_TERMINAL);
        }

        String terminalId = dispatcherRequest.getTerminalId();
        // 检查终端是否已登录，未登录不允许进入桌面
        boolean hasLogin = hasUserLoginTerminal(desktopRequestDTO);
        if (!hasLogin) {
            LOGGER.warn("用户[{}]没有在终端[{}]登录,不允许操作桌面", desktopRequestDTO.getUserName(), terminalId);
            shineMessageHandler.response(dispatcherRequest, StartVmMessageCode.CODE_ERR_HAS_NOT_LOGIN);
            return false;
        }
        // 检查桌面是否启动成功，失败直接返回错误消息给shine
        if (!notifyRequest.getIsSuccess()) {
            // 从桌面组件获取错误消息发给shine
            String errorMsg = notifyRequest.getErrorMsg();
            if (StringUtils.isBlank(errorMsg)) {
                LOGGER.info("启动桌面，云桌面组件没有返回具体异常原因，指定默认异常信息，deskId={}", deskId);
                errorMsg = LocaleI18nResolver.resolve(BusinessKey.RCD_RCO_SHINE_START_VM_SYSTEM_ERR);
            }

            int errorCode = StartVmMessageCode.CODE_ERR_OTHER;
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.
                    RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(notifyRequest.getErrorKey())) {
                errorCode = StartVmMessageCode.CODE_ERR_WAKE_UP_FAIL_BY_RESOURCE_INSUFFICIENT;
            }

            LOGGER.error("登录桌面失败，terminalId=" + terminalId + " ; deskId=" + notifyRequest.getDeskId() + " ; errorMsg=" + errorMsg);
            shineMessageHandler.responseMessage(dispatcherRequest, errorCode, errorMsg);
            // 云桌面启动失败, 清除缓存
            desktopOperateRequestCache.removeCache(deskId);
            return false;
        }
        // 成功，发送桌面连接信息给shine
        StartVmResponse resp = new StartVmResponse(notifyRequest.getDeskId());
        shineMessageHandler.responseSuccessContent(dispatcherRequest, resp);
        return true;
    }

    private boolean hasUserLoginTerminal(DesktopRequestDTO desktopRequestDTO) {
        CbbDispatcherRequest dispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
        String terminalId = dispatcherRequest.getTerminalId();
        return userLoginSession.hasLogin(desktopRequestDTO.getUserName(), terminalId);
    }
}
