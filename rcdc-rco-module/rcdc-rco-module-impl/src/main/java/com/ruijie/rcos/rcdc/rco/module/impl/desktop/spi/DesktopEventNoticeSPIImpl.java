package com.ruijie.rcos.rcdc.rco.module.impl.desktop.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbOneAgentTcpSendAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.VmXuanYuanEventNotifyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.monitor.dashboard.enums.DesktopSessionLogState;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DesktopSessionLogDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktop.constant.DesktopVmEventConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.dto.DesktopOnlineLogDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoploginlog.service.DesktopOnlineLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DesktopSessionLogEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.guesttool.GuestToolCmdId;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import com.ruijie.rcos.rcdc.hciadapter.module.def.spi.VmXuanYuanEventSPI;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Description: VM内部事件通知SPI
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月20日
 *
 * @author 林科
 */
public class DesktopEventNoticeSPIImpl implements VmXuanYuanEventSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopEventNoticeSPIImpl.class);

    private static final String LOCK_KEY = "DESKTOP_EVENT_LOCK_";

    private static final Set<String> LOGOUT_EVENT_SET = new HashSet<>();

    private static final Set<String> LOGIN_EVENT_SET = new HashSet<>();

    private static final Set<String> SYSTEM_CLOSE_EVENT_SET = new HashSet<>();

    static {
        SYSTEM_CLOSE_EVENT_SET.add(DesktopVmEventConstants.SHUTDOWN);
        SYSTEM_CLOSE_EVENT_SET.add(DesktopVmEventConstants.DESTROY);
        SYSTEM_CLOSE_EVENT_SET.add(DesktopVmEventConstants.CRASH);
        SYSTEM_CLOSE_EVENT_SET.add(DesktopVmEventConstants.SUSPEND_DISK);

        LOGOUT_EVENT_SET.add(DesktopVmEventConstants.SHUTDOWN);
        LOGOUT_EVENT_SET.add(DesktopVmEventConstants.DESTROY);
        LOGOUT_EVENT_SET.add(DesktopVmEventConstants.CRASH);
        LOGOUT_EVENT_SET.add(DesktopVmEventConstants.SUSPEND_DISK);
        LOGOUT_EVENT_SET.add(DesktopVmEventConstants.EST_LOGOUT);

        LOGIN_EVENT_SET.add(DesktopVmEventConstants.EST_LOGIN);
    }

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DesktopOnlineLogService desktopOnlineLogService;

    @Autowired
    private UserDesktopService userDesktopService;

    @Autowired
    private DesktopSessionLogDAO desktopSessionLogDAO;

    @Autowired
    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private UserDiskMgmtAPI userDiskMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Autowired
    private CbbOneAgentTcpSendAPI cbbOneAgentTcpSendAPI;


    @Override
    public void notifyEvent(VmXuanYuanEventNotifyDTO request) {
        Assert.notNull(request, "request can not be null");

        String eventType = request.getState();
        LOGGER.info("记录VM云桌面通知事件[{}]，消息：{}", eventType, request.getUuid());
        CloudDesktopDetailDTO desktopDetailDTO = null;
        UUID deskId = UUID.fromString(request.getUuid());
        try {
            desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        } catch (BusinessException e) {
            LOGGER.error("获取桌面[{}]详情异常", request.getUuid(), e);
            return;
        }
        dealSessionLog(eventType, desktopDetailDTO, request);
        dealDesktopConnectClosedTime(eventType, desktopDetailDTO, request);
        try {
            saveDesktopOnlineLog(deskId, request);
        } catch (BusinessException e) {
            LOGGER.error("保存桌面[{}]在线日志异常", deskId, e);
        }
        attachDesktopDisk(eventType, deskId);
    }

    private void dealDesktopConnectClosedTime(String eventType, CloudDesktopDetailDTO desktopDetailDTO, VmXuanYuanEventNotifyDTO eventNoticeDTO) {
        try {
            LockableExecutor.executeWithTryLock(LOCK_KEY + desktopDetailDTO.getId().toString(), () -> {
                if (LOGIN_EVENT_SET.contains(eventType)) {
                    cbbOneAgentTcpSendAPI.notifyOaEstConnectStatus(desktopDetailDTO.getId(), DesktopVmEventConstants.EST_LOGIN);
                    userDesktopService.clearConnectClosedTime(desktopDetailDTO.getId());
                    return;
                }
                if (LOGOUT_EVENT_SET.contains(eventType)) {
                    // 池桌面处理
                    dealPoolDesktopDisconnectEvent(desktopDetailDTO, eventNoticeDTO);
                }

                // 处理音频异常问题
                if (DesktopVmEventConstants.AUDIO_NULL_EXCEPTION.equals(eventType)) {
                    dealDesktopAudioNullException(desktopDetailDTO);
                }

            }, 1);
        } catch (Exception e) {
            LOGGER.error("处理云桌面[{}]事件[{}]，记录断连时间异常", desktopDetailDTO.getId(), eventType, e);
        }
    }

    private void dealDesktopAudioNullException(CloudDesktopDetailDTO desktopDetailDTO) {
        if (!Objects.equals(CbbCloudDeskState.RUNNING.name(), desktopDetailDTO.getDesktopState())) {
            LOGGER.info("桌面[{}]状态为[{}]，非运行中，不处理音频异常问题", desktopDetailDTO.getId(),
                    desktopDetailDTO.getDesktopState());
            return;
        }

        CbbGuesttoolMessageDTO dto = new CbbGuesttoolMessageDTO();
        dto.setBody("");
        dto.setPortId(GuestToolCmdId.REPAIR_AUDIO_EXCEPTION_PORT_ID);
        dto.setCmdId(Integer.valueOf(GuestToolCmdId.REPAIR_AUDIO_EXCEPTION_CMD_ID));
        try {
            dto.setDeskId(desktopDetailDTO.getId());
            cbbGuestToolMessageAPI.asyncRequest(dto);
            LOGGER.info("修复处理音频异常消息发送成功，桌面id: [{}]", desktopDetailDTO.getId());
        } catch (BusinessException e) {
            LOGGER.error("通知云桌面[{}]修复音频消息发送失败", e, desktopDetailDTO.getId());
        }
    }

    private void dealSessionLog(String eventType, CloudDesktopDetailDTO desktopDetailDTO, VmXuanYuanEventNotifyDTO eventNoticeDTO) {
        if (LOGIN_EVENT_SET.contains(eventType)) {
            // 记录桌面会话信息
            recordDeskSessionLog(desktopDetailDTO, eventNoticeDTO.getConnectionId());
            userLoginRecordService.saveConnectVmSuccessTime(desktopDetailDTO.getId().toString(), eventNoticeDTO.getConnectionId());
            return;
        }
        if (LOGOUT_EVENT_SET.contains(eventType)) {
            // 记录会话结束信息
            recordLogoutLog(desktopDetailDTO, eventNoticeDTO.getConnectionId());
            userLoginRecordService.saveLogoutTime(desktopDetailDTO.getId().toString(), eventNoticeDTO.getConnectionId());
        }
    }

    private void dealPoolDesktopDisconnectEvent(CloudDesktopDetailDTO desktopDetail, VmXuanYuanEventNotifyDTO noticeDTO) {
        UUID desktopId = desktopDetail.getId();
        // 通过查询qemu信息判断是否要标记断连时间
        if (!Objects.equals(CbbCloudDeskState.RUNNING.name(), desktopDetail.getDesktopState())) {
            // 已关机
            LOGGER.info("桌面[{}]状态为[{}]，非运行中，不处理断连时间相关逻辑", desktopId, desktopDetail.getDesktopState());
            return;
        }

        try {
            if (desktopAPI.isAnyConnectedChannel(desktopId)) {
                LOGGER.info("事件[{}],桌面[{}]存在未断开的est连接不标记断连时间", noticeDTO.getState(), desktopId);
                return;
            }
            cbbOneAgentTcpSendAPI.notifyOaEstConnectStatus(desktopId, DesktopVmEventConstants.EST_LOGOUT);
            setDesktopConnectClosedTime(desktopId, noticeDTO);
        } catch (BusinessException e) {
            LOGGER.error("桌面[{}]查询qemu信息异常", desktopId, e);
            if (SYSTEM_CLOSE_EVENT_SET.contains(noticeDTO.getState())) {
                setDesktopConnectClosedTime(desktopId, noticeDTO);
            }
        } catch (Exception e) {
            LOGGER.error("桌面[{}]查询qemu信息异常", desktopId, e);
        }
    }

    private void setDesktopConnectClosedTime(UUID desktopId, VmXuanYuanEventNotifyDTO noticeDTO) {
        LOGGER.info("事件[{}],已无est连接桌面[{}]标记断连时间", noticeDTO.getState(), desktopId);
        userDesktopService.setConnectClosedTime(desktopId, noticeDTO.getTimestamp());
    }

    private void recordLogoutLog(CloudDesktopDetailDTO desktopDetailDTO, Long connectionId) {
        // 如果没有会话id，则证明是虚机内关机等事件，结束这个桌面的所有会话
        Date currentTime = new Date();
        if (connectionId == null) {
            List<DesktopSessionLogEntity> entityList = desktopSessionLogDAO.findByDesktopIdAndState(desktopDetailDTO.getId(),
                    DesktopSessionLogState.CONNECTING);
            entityList = entityList.stream().filter(item -> Objects.nonNull(item.getConnectionId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(entityList)) {
                return;
            }
            entityList.forEach(entity -> {
                entity.setLogoutTime(currentTime);
                entity.setState(DesktopSessionLogState.FINISHED);
            });
            desktopSessionLogDAO.saveAll(entityList);
            return;
        }

        // 如果有会话id，就使用会话id+桌面id结束对应会话
        DesktopSessionLogEntity entity = desktopSessionLogDAO.findFirstByDesktopIdAndConnectionIdAndState(desktopDetailDTO.getId(),
                connectionId, DesktopSessionLogState.CONNECTING);
        if (entity == null) {
            LOGGER.debug("桌面[{}]不存在需要登陆后没有退出的情况，无需记录退出时间", desktopDetailDTO.getId());
            return;
        }

        entity.setLogoutTime(currentTime);
        entity.setState(DesktopSessionLogState.FINISHED);
        desktopSessionLogDAO.save(entity);
    }

    private void recordDeskSessionLog(CloudDesktopDetailDTO desktopDetailDTO, Long connectionId) {
        UUID desktopPoolId = desktopDetailDTO.getDesktopPoolId();

        if (desktopPoolId == null || desktopDetailDTO.getSessionType() == CbbDesktopSessionType.MULTIPLE
                || Objects.equals(desktopDetailDTO.getDeskType(), CbbCloudDeskType.THIRD.name())) {
            LOGGER.debug("多会话桌面、第三方桌面或者非池桌面无需记录会话记录，桌面[{}]", desktopDetailDTO.getId());
            return;
        }

        DesktopSessionLogEntity entity = desktopSessionLogDAO.findFirstByDesktopIdAndConnectionIdAndState(desktopPoolId, connectionId,
                DesktopSessionLogState.CONNECTING);
        Date currentTime = new Date();
        // 如果原来就已经存在登录桌面后没有退出的情况，结束原来的会话
        if (entity != null) {
            entity.setLogoutTime(currentTime);
            entity.setState(DesktopSessionLogState.FINISHED);
            desktopSessionLogDAO.save(entity);
        }

        // 插入新的会话记录
        entity = new DesktopSessionLogEntity();
        entity.setCreateTime(currentTime);
        entity.setConnectionId(connectionId);
        entity.setDesktopId(desktopDetailDTO.getId());
        entity.setLoginTime(currentTime);
        entity.setDesktopPoolType(DesktopPoolType.valueOf(desktopDetailDTO.getDesktopPoolType()));
        entity.setDesktopName(desktopDetailDTO.getDesktopName());
        entity.setUserGroupName(desktopDetailDTO.getUserGroupName());
        entity.setUserGroupId(desktopDetailDTO.getUserGroupId());
        entity.setUserId(desktopDetailDTO.getUserId());
        entity.setUserName(desktopDetailDTO.getUserName());
        entity.setRelatedId(desktopPoolId);
        String desktopPoolName = cbbDesktopPoolMgmtAPI.getDesktopPoolName(desktopPoolId);
        entity.setDesktopPoolName(desktopPoolName);
        entity.setCbbDesktopPoolType(CbbDesktopPoolType.valueOf(desktopDetailDTO.getDesktopCategory()));
        entity.setDesktopSessionType(desktopDetailDTO.getSessionType());
        entity.setState(DesktopSessionLogState.CONNECTING);

        desktopSessionLogDAO.save(entity);
    }

    private CbbGuesttoolMessageDTO buildDefaultResult(CbbGuesttoolMessageDTO request) {
        CbbGuesttoolMessageDTO cbbGuesttoolMessageDTO = new CbbGuesttoolMessageDTO();
        cbbGuesttoolMessageDTO.setCmdId(request.getCmdId());
        cbbGuesttoolMessageDTO.setPortId(request.getPortId());
        return cbbGuesttoolMessageDTO;
    }

    private void saveDesktopOnlineLog(UUID deskId, VmXuanYuanEventNotifyDTO eventNoticeDTO) throws BusinessException {
        CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        if (desktopDetailDTO == null) {
            LOGGER.error("桌面[{}]不存在", deskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_EVENT_NOTICE_DESKTOP_NOT_EXIST, String.valueOf(deskId));
        }

        if (Objects.isNull(desktopDetailDTO.getUserId())) {
            LOGGER.error("桌面[{}]不存在关联的用户信息", deskId);
            return;
        }

        DesktopOnlineLogDTO desktopOnlineLogDTO = new DesktopOnlineLogDTO();
        desktopOnlineLogDTO.setDesktopId(desktopDetailDTO.getId());
        desktopOnlineLogDTO.setDesktopName(desktopDetailDTO.getDesktopName());
        desktopOnlineLogDTO.setUserId(desktopDetailDTO.getUserId());
        desktopOnlineLogDTO.setUserName(desktopDetailDTO.getUserName());
        desktopOnlineLogDTO.setDesktopIp(desktopDetailDTO.getDesktopIp());
        desktopOnlineLogDTO.setOperationType(eventNoticeDTO.getState());
        desktopOnlineLogDTO.setOperationTime(eventNoticeDTO.getTimestamp());
        desktopOnlineLogService.saveDesktopOnlineLog(desktopOnlineLogDTO);
    }

    private void attachDesktopDisk(String eventType, UUID deskId) {
        if (!Objects.equals(DesktopVmEventConstants.EST_LOGIN, eventType)) {
            return;
        }
        try {
            LOGGER.info("桌面[{}]EST_LOGIN事件挂载磁盘", deskId);
            userDiskMgmtAPI.attachDesktopDisk(deskId);
        } catch (Exception e) {
            LOGGER.error("桌面[{}]EST_LOGIN事件挂载磁盘失败", deskId, e);
        }
    }
}

