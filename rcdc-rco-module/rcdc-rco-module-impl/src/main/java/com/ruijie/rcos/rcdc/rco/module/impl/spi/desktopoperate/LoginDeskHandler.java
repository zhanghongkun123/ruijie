package com.ruijie.rcos.rcdc.rco.module.impl.spi.desktopoperate;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskOperateNotifyRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WebclientNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.RepeatStartVmWebclientNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopOperateRequestCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.DesktopRequestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.HostUserEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.CloudDesktopOperateService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.HostUserService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.QueryCloudDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserInfo;
import com.ruijie.rcos.rcdc.rco.module.impl.session.UserLoginSession;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.RepeatStartVmTerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler.AbstractMessageHandlerTemplate;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * Description: 登录桌面事件处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/29
 *
 * @author Jarman
 */
@Service
public class LoginDeskHandler extends AbstractMessageHandlerTemplate<CbbDeskOperateNotifyRequest> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginDeskHandler.class);

    @Autowired
    private DesktopOperateRequestCache desktopOperateRequestCache;

    @Autowired
    private UserLoginSession userLoginSession;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private QueryCloudDesktopService queryCloudDesktopService;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private CloudDesktopOperateService cloudDesktopOperateService;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private StartDesktopSPIHelper startDesktopSPIHelper;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private WebclientNotifyAPI webclientNotifyAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private HostUserService hostUserService;

    private final Interner<UUID> desktopIdInterner = Interners.newWeakInterner();

    @Override
    protected boolean isNeedHandleMessage(CbbDeskOperateNotifyRequest request) {
        return CbbCloudDeskOperateType.RUNNING_DESK == request.getOperateType();
    }

    @Override
    protected void processMessage(CbbDeskOperateNotifyRequest request) throws Exception {
        LOGGER.info("桌面操作：{{}}，桌面id:{}", request.getOperateType().name(), request.getDeskId());
        boolean isSuccess = startDesktopSPIHelper.responseDeskConnectionInfoToShine(request);
        if (!isSuccess) {
            // 获取桌面连接失败
            return;
        }

        DesktopRequestDTO desktopRequestDTO = desktopOperateRequestCache.getCache(request.getDeskId());
        UUID desktopId = request.getDeskId();

        synchronized (desktopIdInterner.intern(desktopId)) {
            try {
                CbbDispatcherRequest dispatcherRequest = desktopRequestDTO.getCbbDispatcherRequest();
                String terminalId = dispatcherRequest.getTerminalId();

                UserInfo userInfo = userLoginSession.getLoginUserInfo(terminalId);
                UserDesktopEntity userDesktopEntity = queryCloudDesktopService.checkAndFindById(desktopId);
                CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(desktopId);
                RepeatStartVmTerminalDTO repeatStartVmTerminalDTO = getRepeatStartVmTerminalDTO(userInfo, userDesktopEntity);
                if (deskDTO.getSessionType() == CbbDesktopSessionType.SINGLE) {
                    String loginTerminalId = userDesktopEntity.getTerminalId();
                    // 单会话判断云桌面原来是否在其他终端运行，是则发送桌面被抢占消息
                    if (loginTerminalId != null &&
                            (buildTerminalDesktopIsRobbed(desktopId, terminalId,
                                    userInfo, userDesktopEntity, repeatStartVmTerminalDTO, loginTerminalId))) {
                        return;
                    }
                } else {
                    //多会话判断云桌面+用户原来是否在其他终端运行，是则发送桌面被抢占消息
                    HostUserEntity hostUserEntity = hostUserService.findByDeskIdAndUserId(desktopId, userInfo.getUserId());
                    if (hostUserEntity != null && hostUserEntity.getTerminalId() != null && !hostUserEntity.getTerminalId().equals(terminalId) &&
                            (buildTerminalDesktopIsRobbed(desktopId, terminalId,
                                    userInfo, userDesktopEntity, repeatStartVmTerminalDTO, hostUserEntity.getTerminalId()))) {
                        return;
                    }
                }
                // 通知网页版客户端 桌面登录
                notifyTerminalDesktopIsRobbed(repeatStartVmTerminalDTO);
                // 更新缓存信息，桌面运行在终端上
                desktopRequestDTO.setDesktopRunInTerminal(true);
                desktopOperateRequestCache.addCache(desktopId, desktopRequestDTO);
                // 更新绑定的终端
                cloudDesktopOperateService.bindDesktopTerminal(desktopId, terminalId);

                CbbTerminalBasicInfoDTO currentTerminalInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
                userDesktopEntity.setUserId(userInfo.getUserId());
                recordDeskLoginEvent(userDesktopEntity, currentTerminalInfo.getIp(), terminalId);
                // 完成桌面与终端的绑定后，删除用户使用信息内桌面绑定终端的缓存
                userLoginRecordService.deleteTerminalCacheByDeskId(desktopId.toString());
                LOGGER.info("Send start vm response finish. vm :" + userDesktopEntity.getDesktopName());
            } finally {
                final CountDownLatch latch = desktopRequestDTO.getLatch();
                latch.countDown();
            }
        }

        // 通知 UWS 云桌面
        uwsDockingAPI.notifyDesktopStateUpdate(request.getDeskId(), CbbCloudDeskState.RUNNING);
    }

    @SuppressWarnings(value = {"checkstyle:ParameterNumber"})
    private boolean buildTerminalDesktopIsRobbed(UUID desktopId, String terminalId, UserInfo userInfo, UserDesktopEntity userDesktopEntity,
                                                 RepeatStartVmTerminalDTO repeatStartVmTerminalDTO, String loginTerminalId) {
        LOGGER.info("抢占登录桌面,terminalId={},desktopId={}", terminalId, desktopId);
        CbbTerminalBasicInfoDTO currentTerminalInfo;
        try {
            currentTerminalInfo = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("获取当前登录云桌面终端信息失败，terminalId：" + userDesktopEntity.getTerminalId(), e);
            return true;
        }
        repeatStartVmTerminalDTO.setIp(currentTerminalInfo.getIp());
        repeatStartVmTerminalDTO.setMacAddr(currentTerminalInfo.getMacAddr());
        repeatStartVmTerminalDTO.setTerminalName(currentTerminalInfo.getTerminalName());
        notifyTerminalDesktopIsRobbed(terminalId, loginTerminalId, userInfo.getUserName(), repeatStartVmTerminalDTO);
        return false;
    }

    private void notifyTerminalDesktopIsRobbed(RepeatStartVmTerminalDTO repeatStartVmTerminalDTO) {
        RepeatStartVmWebclientNotifyDTO repeatStartVmWebclientNotifyDTO = new RepeatStartVmWebclientNotifyDTO();
        BeanUtils.copyProperties(repeatStartVmTerminalDTO, repeatStartVmWebclientNotifyDTO);
        webclientNotifyAPI.notifyTerminalDesktopIsRobbed(true, repeatStartVmWebclientNotifyDTO);
    }

    private RepeatStartVmTerminalDTO getRepeatStartVmTerminalDTO(UserInfo userInfo, UserDesktopEntity userDesktopEntity) {
        RepeatStartVmTerminalDTO repeatStartVmTerminalDTO = new RepeatStartVmTerminalDTO();
        repeatStartVmTerminalDTO.setDesktopId(userDesktopEntity.getCbbDesktopId());
        repeatStartVmTerminalDTO.setUserName(userInfo.getUserName());
        repeatStartVmTerminalDTO.setDesktopName(userDesktopEntity.getDesktopName());
        repeatStartVmTerminalDTO.setStartVmTime(Instant.now().toEpochMilli());
        return repeatStartVmTerminalDTO;
    }

    private void recordDeskLoginEvent(UserDesktopEntity desktop, String sourceIp, String terminalId) throws BusinessException {
        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(desktop.getUserId());
        DesktopOpLogDTO req = new DesktopOpLogDTO();
        req.setDesktopId(desktop.getId());
        req.setDesktopName(desktop.getDesktopName());
        req.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
        req.setOperatorId(userDetail.getId());
        req.setOperatorName(userDetail.getUserName());
        req.setSourceIp(sourceIp);
        req.setEventName(DesktopOpEvent.LOGIN);
        req.setDetail(LocaleI18nResolver.resolve(BusinessKey.RCDC_USER_DESKTOP_OPLOG_USER_LOGIN, userDetail.getUserName(), desktop.getDesktopName()));
        req.setTerminalId(terminalId);
        desktopOpLogMgmtAPI.saveOperateLog(req);
    }

    /**
     * 通知已运行云桌面的终端云桌面被抢占
     */
    private void notifyTerminalDesktopIsRobbed(String currentTerminalId, String lastLoginTerminalId, String userName,
            RepeatStartVmTerminalDTO repeatStartVmTerminalDTO) {
        if (currentTerminalId.equals(lastLoginTerminalId)) {
            LOGGER.info("[{}]登录与最近登录一致，不发送抢占通知，登录用户=[{}]", lastLoginTerminalId, userName);
            return;
        }
        LOGGER.info("发送抢占消息给终端[{}]，登录用户={}", lastLoginTerminalId, userName);
        try {
            shineMessageHandler.requestContent(lastLoginTerminalId, ShineAction.REPEAT_START_VM_IN_DIFFERENT_PLACE, repeatStartVmTerminalDTO);
        } catch (Exception e) {
            LOGGER.error("发送抢占消息给终端[" + lastLoginTerminalId + "]，登录用户=" + userName + "，失败", e);
        }
    }
}
