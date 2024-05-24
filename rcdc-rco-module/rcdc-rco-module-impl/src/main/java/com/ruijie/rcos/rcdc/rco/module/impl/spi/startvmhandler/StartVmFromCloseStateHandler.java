package com.ruijie.rcos.rcdc.rco.module.impl.spi.startvmhandler;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.VmState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.StartVmDispatcherDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.exception.ShineUserNotLoginException;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 启动云桌面（当前状态处于关闭状态）
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @author Jarman
 */
@Service
public class StartVmFromCloseStateHandler extends AbstractMessageHandlerTemplate<StartVmDispatcherDTO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartVmFromCloseStateHandler.class);

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private CbbDesktopOpLogMgmtAPI cbbDesktopOpLogMgmtAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private HostUserService hostUserService;

    @Override
    protected boolean isNeedHandleMessage(StartVmDispatcherDTO request) {
        if (request.getVmState() != null) {
            return request.getVmState() == VmState.STOPPED;
        }
        return CbbCloudDeskState.CLOSE == request.getDeskState() || CbbCloudDeskState.OFF_LINE == request.getDeskState();
    }

    @Override
    protected void processMessage(StartVmDispatcherDTO request) throws Exception {
        String terminalId = request.getDispatcherRequest().getTerminalId();
        UUID desktopId = request.getDesktopId();
        LOGGER.info("当前桌面状态处于[{}]状态，terminalId={},desktopId={}", request.getDeskState(), terminalId, desktopId);
        UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(request.getDesktopId());
        UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
        if (userInfo == null || StringUtils.isEmpty(userInfo.getUserName())) {
            throw new ShineUserNotLoginException(BusinessKey.RCDC_RCO_DESK_START_FAIL_REASON_USER_NOT_LOGIN, desktopId.toString());
        }

        final DesktopRequestDTO dto = new DesktopRequestDTO(request.getDispatcherRequest(), userInfo.getUserName());
        desktopOperateRequestCache.addCache(request.getDesktopId(), dto);
        try {
            CloudDesktopStartRequest startRequest = new CloudDesktopStartRequest(desktopId);
            startRequest.setVmState(request.getVmState());
            startRequest.setDeskBackupState(request.getDeskBackupState());
            cloudDesktopOperateService.startDesktop(startRequest);
            // startDesktop 为阻塞调用，为了避免并发问题，这里等5秒
            dto.getLatch().await(5, TimeUnit.SECONDS);
        } catch (BusinessException e) {
            logDeskStartFailEvent(userDesktopEntity, terminalId, e);
            throw e;
        }

        try {
            // 启动云桌面成功, 日志记录云桌面启动成功事件
            logDeskStartSucEvent(userDesktopEntity, terminalId);
        } catch (BusinessException e) {
            LOGGER.error("云桌面启动成功, 日志记录时出现异常！", e);
        }
    }

    private void logDeskStartSucEvent(UserDesktopEntity desktop, String terminalId) throws BusinessException {

        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(desktop.getCbbDesktopId());
        IacUserDetailDTO userDetail;
        UUID userId;
        if (deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
            userId = userInfo.getUserId();
        } else {
            userId = desktop.getUserId();
        }
        userDetail = cbbUserAPI.getUserDetail(userId);
        String logDetail =
                LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_START_SUC, userDetail.getUserName(), desktop.getDesktopName());
        DesktopOpLogDTO req = createCommonRequest(desktop, userDetail, terminalId, logDetail);
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private void logDeskStartFailEvent(UserDesktopEntity desktop, String terminalId, BusinessException businessException) throws BusinessException {
        UUID userId;
        CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(desktop.getCbbDesktopId());
        if (deskDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE) {
            UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
            userId = userInfo.getUserId();
        } else {
            userId = desktop.getUserId();
        }
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(userId);
        String logDetail = LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_START_FAIL, userDetail.getUserName(),
                desktop.getDesktopName(), businessException.getI18nMessage());
        DesktopOpLogDTO req = createCommonRequest(desktop, userDetail, terminalId, logDetail);
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    private DesktopOpLogDTO createCommonRequest(UserDesktopEntity desktop, IacUserDetailDTO userDetail, String terminalId, String logDetail)
            throws BusinessException {
        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(desktop.getCbbDesktopId());
        req.setDesktopName(desktop.getDesktopName());
        req.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
        req.setOperatorId(userDetail.getId());
        req.setOperatorName(userDetail.getUserName());
        req.setEventName(DesktopOpEvent.START);
        CbbTerminalBasicInfoDTO cbbTerminalBasicInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        req.setSourceIp(cbbTerminalBasicInfo.getIp());
        req.setDetail(logDetail);
        req.setTerminalName(cbbTerminalBasicInfo.getTerminalName());
        req.setTerminalId(desktop.getTerminalId());
        return req;
    }

}
