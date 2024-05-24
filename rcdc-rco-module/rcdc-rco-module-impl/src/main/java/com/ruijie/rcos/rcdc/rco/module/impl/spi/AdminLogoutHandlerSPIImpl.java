package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.vdiedit.VDIEditImageSessionRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.util.ShineMessageUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 管理员在终端登出请求
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/12 17:57
 *
 * @author zhangyichi
 */
@DispatcherImplemetion(Constants.SEND_ADMIN_LOGOUT)
public class AdminLogoutHandlerSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLogoutHandlerSPIImpl.class);

    private static final Integer FAILURE = -1;

    @Autowired
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request cannot be null!");
        String dataJsonString = request.getData();
        Assert.hasText(dataJsonString, "data string cannot be blank!");

        CbbResponseShineMessage shineMessage;
        try {
            VDIEditImageSessionRequestDTO requestDTO = JSONObject.parseObject(dataJsonString, VDIEditImageSessionRequestDTO.class);
            Assert.notNull(requestDTO, "request parse error!");

            AdminLoginOnTerminalCache sessionCache = cacheManager.getIfPresent(requestDTO.getAdminSessionId());
            if (sessionCache != null) {
                cacheManager.invalidate(requestDTO.getAdminSessionId());

                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(sessionCache.getTerminalId());
                LOGGER.info("管理员[{}]在终端[{}]退出登录", sessionCache.getAdminName(), sessionCache.getTerminalId());
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGOUT_ON_TERMINAL_LOG_KEY,
                        sessionCache.getAdminName(), terminalDTO.getUpperMacAddrOrTerminalId());
            }
            shineMessage = ShineMessageUtil.buildResponseMessage(request, new Object());
        } catch (Exception e) {
            LOGGER.error("管理员在终端登出失败", e);
            shineMessage = ShineMessageUtil.buildErrorResponseMessage(request, FAILURE);
        }
        messageHandlerAPI.response(shineMessage);
    }
}
