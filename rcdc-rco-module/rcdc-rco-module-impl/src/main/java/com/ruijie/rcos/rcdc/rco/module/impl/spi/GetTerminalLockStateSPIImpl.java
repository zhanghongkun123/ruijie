package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.AuthenticationDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.certificationstrategy.service.AuthenticationService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalLockDTO;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 终端上报锁定信息
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/27
 *
 * @author yxq
 */
@DispatcherImplemetion(ShineAction.SEND_TERMINAL_LOCK_STATE)
public class GetTerminalLockStateSPIImpl implements CbbDispatcherHandlerSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(GetTerminalLockStateSPIImpl.class);

    /**
     * 锁的前缀
     */
    private static final String LOCK_PREFIX = "SYNC_TERMINAL_LOCK_STATE_";

    /**
     * 获取锁的等待时间
     */
    private static final int WAIT_TIME = 3;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "CbbDispatcherRequest can not be null");
        String terminalId = request.getTerminalId();
        Assert.hasText(terminalId, "terminalId can not be blank");
        String data = request.getData();
        LOGGER.info("终端[{}]上报锁定信息为：[{}]", terminalId, data);
        TerminalLockDTO terminalLockDTO = JSON.parseObject(data, TerminalLockDTO.class);
        if (terminalLockDTO == null || terminalLockDTO.getLock() == null) {
            try {
                shineMessageHandler.response(request, CommonMessageCode.CODE_ERR_OTHER);
            } catch (Exception e) {
                LOGGER.warn("返回终端" + terminalId + "信息失败，异常为：", e);
                return;
            }
        }
        handlerMessage(request, terminalId, terminalLockDTO);
    }

    private void handlerMessage(CbbDispatcherRequest request, String terminalId, TerminalLockDTO terminalLockDTO) {
        Assert.notNull(request, "request can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        Assert.notNull(terminalLockDTO, "terminalLockDTO can not be null");

        try {
            LockableExecutor.executeWithTryLock(LOCK_PREFIX + terminalId, () -> {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
                if (terminalDTO != null) {
                    boolean isLock = Boolean.TRUE.equals(terminalDTO.getLock());
                    // 如果服务器上未锁定，并且终端上报的信息为锁定状态
                    if (!isLock && terminalLockDTO.getLock()) {
                        LOGGER.info("终端[{}]在离线期间为锁定状态，并且在服务器中为非锁定状态", terminalId);
                        auditLogAPI.recordLog(BusinessKey.RCDC_LOCK_TERMINAL_MNG_PWD, terminalDTO.getUpperMacAddrOrTerminalId());
                    }
                    AuthenticationDTO authenticationDTO = new AuthenticationDTO();
                    authenticationDTO.setResourceId(terminalDTO.getRealTerminalId());
                    authenticationDTO.setType(CertificationTypeEnum.TERMINAL);
                    authenticationDTO.setLock(terminalLockDTO.getLock());
                    authenticationDTO.setPwdErrorTimes(terminalLockDTO.getPwdErrorTimes());
                    authenticationService.updateAuthentication(authenticationDTO);
                }
            }, WAIT_TIME);
            responseMessage(request, CommonMessageCode.SUCCESS);
        } catch (Exception e) {
            LOGGER.error("终端[" + request.getTerminalId() + "]上报错锁定状态失败", e);
            responseMessage(request, CommonMessageCode.CODE_ERR_OTHER);
        }
    }

    private void responseMessage(CbbDispatcherRequest request, Integer code) {
        try {
            shineMessageHandler.response(request, code);
        } catch (Exception e) {
            LOGGER.error(String.format("应答终端[{%s}]上报锁定状态失败，应答状态码：{%s}", request.getTerminalId(), CommonMessageCode.CODE_ERR_OTHER), e);
        }
    }
}
