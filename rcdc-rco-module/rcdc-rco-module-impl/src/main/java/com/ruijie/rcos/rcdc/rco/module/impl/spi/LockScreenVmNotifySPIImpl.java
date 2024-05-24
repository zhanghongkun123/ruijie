package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDispatcherDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbVmLockScreenNotifySPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.struct.LockScreenRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/8/10
 *
 * @author hs
 */
public class LockScreenVmNotifySPIImpl implements CbbVmLockScreenNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockScreenVmNotifySPIImpl.class);

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Override
    public void notifyLockScreen(CbbDispatcherDTO dispatcherDTO) {
        Assert.notNull(dispatcherDTO, "cbbLockScreenDTO must not be null");

        // 锁屏用户退出登录，移除用户登录Session
        String terminalId = dispatcherDTO.getTerminalId();
        LOGGER.info("终端[{}]锁屏", terminalId);
        LockScreenRequest lockReq = JSON.parseObject(dispatcherDTO.getData(), LockScreenRequest.class);

        try {
            UserDesktopEntity desktop = queryCloudDesktopService.checkAndFindById(lockReq.getId());
            CbbTerminalBasicInfoDTO terminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            if (terminalBasicInfo.getTerminalPlatform() != CbbTerminalPlatformEnums.APP) {
                userLoginSession.removeLoginUser(terminalId);
            }
            // 记录云桌面日志
            recordUserLockScreenOperateLog(desktop, terminalBasicInfo.getIp());
            // 处理云桌面操作缓存
            handleDeskOperateCache(terminalId, desktop.getCbbDesktopId());
        } catch (BusinessException e) {
            LOGGER.error("dispatcherDTO:" + dispatcherDTO, e);
        }
    }

    private void recordUserLockScreenOperateLog(UserDesktopEntity desktop, String sourceIp) throws BusinessException {
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(desktop.getUserId());
        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(desktop.getCbbDesktopId());
        req.setDesktopName(desktop.getDesktopName());
        req.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
        req.setOperatorId(userDetail.getId());
        req.setOperatorName(userDetail.getUserName());
        req.setSourceIp(sourceIp);
        req.setEventName(DesktopOpEvent.LOGIN_OUT);
        req.setDetail(
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_LOGOUT, userDetail.getUserName(), desktop.getDesktopName()));
        req.setTerminalId(desktop.getTerminalId());
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private void handleDeskOperateCache(String terminalId, UUID cbbDesktopId) {
        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(cbbDesktopId);
        if (desktopRequestDTO == null) {
            LOGGER.info("shine上报锁屏消息，桌面操作缓存信息为空，桌面id[{}]，终端id[{}]", cbbDesktopId, terminalId);
            return;
        }
        // 终端抢占桌面登录，被抢占的终端（shine）也会向RCDC发送锁屏消息，只有非抢占登录桌面的锁屏消息才需要更新isDesktopRunInTerminal为false
        CbbDispatcherRequest cbbDispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
        if (StringUtils.equals(terminalId, cbbDispatcherRequest.getTerminalId())) {
            desktopRequestDTO.setDesktopRunInTerminal(false);
            desktopOperateRequestCache.addCache(cbbDesktopId, desktopRequestDTO);
        }
    }
}
