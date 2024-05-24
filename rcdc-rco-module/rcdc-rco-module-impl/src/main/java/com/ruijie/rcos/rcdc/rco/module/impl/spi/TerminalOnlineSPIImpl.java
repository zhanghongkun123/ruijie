package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType.IDV;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserStateEnum;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.ShineAction;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UwsDockingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserTerminalDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.TerminalOnlineTimeRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalOnlineTimeRecordService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DesktopStateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbShineTerminalBasicInfo;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalEventNoticeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.SpiCustomThreadPoolConfig;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time:
 *
 * @author artom
 */
@DispatcherImplemetion(BusinessAction.ONLINE)
@SpiCustomThreadPoolConfig(threadPoolName = "online_spi_thread_pool")
public class TerminalOnlineSPIImpl implements CbbTerminalEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalOnlineSPIImpl.class);

    @Autowired
    private TerminalService terminalService;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Autowired
    private UwsDockingAPI uwsDockingAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI;

    @Autowired
    private UserMgmtAPI userMgmtAPI;

    @Autowired
    private UserInfoAPI userInfoAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private TerminalOnlineTimeRecordService onlineTimeRecordService;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public void notify(CbbNoticeRequest request) {
        Assert.notNull(request, "request can not be null");
        CbbShineTerminalBasicInfo terminalBasicInfo = request.getTerminalBasicInfo();
        Assert.notNull(terminalBasicInfo, "terminalBasicInfo id can not be null");

        terminalService.saveTerminal(terminalBasicInfo);

        // 处理终端上线后对应的IDV云桌面状态
        handleIDVDesktopState(terminalBasicInfo);

        // 通知终端所绑定的用户状态
        handleNotifyUserInfo(terminalBasicInfo);

        //处理终端上线后对应的终端在线时长
        handleTerminalTotalOnlineTime(request);

        // 终端上线，把终端所有连接中会话状态改为未连接
        userLoginRecordService.compensateTerminalConnectingRecord(terminalBasicInfo.getTerminalId());
    }


    /**
     * 修改IDV对应桌面状态
     *
     * @param terminalBasicInfo CbbShineTerminalBasicInfo
     */
    private void handleIDVDesktopState(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo.getTerminalId(), "Param [terminalId] must not be null");

        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(terminalBasicInfo.getTerminalId());
        if (entity == null) {
            LOGGER.error("未找到与终端[id:{}]的绑定关系", terminalBasicInfo.getTerminalId());
            return;
        }
        try {
            ViewTerminalEntity viewTerminalEntity = terminalService.getViewByTerminalId(entity.getTerminalId());
            // userid为空认为是 IDV终端, 需要在UserTerminalEntity增加终端类型后修改  并且是VOI终端
            if ((CbbTerminalPlatformEnums.IDV == viewTerminalEntity.getPlatform() || CbbTerminalPlatformEnums.VOI == viewTerminalEntity.getPlatform())
                    && entity.getBindDeskId() != null) {
                // 判断云桌面是否存在  由于CBB 没有提供VOI单独接口 沿用IDV接口查询
                cbbIDVDeskMgmtAPI.getDeskIDV(entity.getBindDeskId());
                CbbCloudDeskState deskStateByShine = getDeskStateByShine(entity);
                // 由于CBB 没有提供VOI单独接口 沿用IDV接口更新
                cbbIDVDeskMgmtAPI.updateIDVDeskStateByDeskId(entity.getBindDeskId(), deskStateByShine);

                // 部分状态更新需要通知到uws
                uwsNotifyDesktopStateUpdate(entity, deskStateByShine);
                uwsDockingAPI.notifyTerminalStateUpdate(entity.getBindDeskId(), entity.getTerminalId(), CbbTerminalStateEnums.ONLINE);
            }
        } catch (Exception e) {
            LOGGER.error(String.format("终端[%s]上线, 获取云桌面[%s]出现异常", terminalBasicInfo.getTerminalId(), entity.getBindDeskId()), e);
        }
    }

    private void uwsNotifyDesktopStateUpdate(UserTerminalEntity entity, CbbCloudDeskState deskStateByShine) {
        if (CbbCloudDeskState.RUNNING == deskStateByShine || CbbCloudDeskState.CLOSE == deskStateByShine) {
            LOGGER.info("uws更新桌面通知，桌面id:[{}], 状态：[{}]", entity.getBindDeskId(), deskStateByShine.name());
            uwsDockingAPI.notifyDesktopStateUpdate(entity.getBindDeskId(), deskStateByShine);
        }
    }

    private CbbCloudDeskState getDeskStateByShine(UserTerminalEntity entity) throws Exception {
        CbbShineMessageRequest request = CbbShineMessageRequest.create(ShineAction.RCDC_REQUEST_IDV_DESKTOP_STATE, entity.getTerminalId());
        request.setContent(new JSONObject());
        CbbShineMessageResponse cbbShineMessageResponse = messageHandlerAPI.syncRequest(request);

        LOGGER.info("从Shine查询到云桌面[id:{}]状态信息为[{}]", entity.getBindDeskId(), JSON.toJSONString(cbbShineMessageResponse));
        if (cbbShineMessageResponse.getCode() == CommonMessageCode.FAIL_CODE) {
            LOGGER.info("请求虚机状态异常，消息体为：{}", JSON.toJSONString(cbbShineMessageResponse));
            return CbbCloudDeskState.CLOSE;
        }

        DesktopStateDTO stateDTO = JSON.parseObject(cbbShineMessageResponse.getContent().toString(), DesktopStateDTO.class);
        // shine上报的桌面id和cdc的桌面id不一致，桌面改为离线状态
        if (!Objects.isNull(entity.getBindDeskId()) && !entity.getBindDeskId().equals(stateDTO.getId())) {
            LOGGER.error("shine返回的桌面id[{}]和cdc中查询的桌面id[{}]不一致，桌面状态置为离线状态[{}]",
                    stateDTO.getId(),
                    entity.getBindDeskId(),
                    CbbCloudDeskState.OFF_LINE);

            return CbbCloudDeskState.OFF_LINE;
        }
        return CbbCloudDeskState.valueOf(stateDTO.getState());
    }

    private void handleNotifyUserInfo(CbbShineTerminalBasicInfo terminalBasicInfo) {
        Assert.notNull(terminalBasicInfo.getTerminalId(), "Param [terminalId] must not be null");
        UserTerminalEntity entity = userTerminalDAO.findFirstByTerminalId(terminalBasicInfo.getTerminalId());
        if (entity == null) {
            LOGGER.error("未找到与终端[id:{}]的绑定关系", terminalBasicInfo.getTerminalId());
            return;
        }

        if (entity.getUserId() == null) {
            LOGGER.warn("终端[id:{}]未绑定用户", terminalBasicInfo.getTerminalId());
            return;
        }

        try {
            IacUserDetailDTO cbbUserDetailDTO = cbbUserAPI.getUserDetail(entity.getUserId());

            if (cbbUserDetailDTO == null) {
                LOGGER.warn("未找到与终端[id:{}]的绑定的用户[id:{}]", terminalBasicInfo.getTerminalId(), entity.getUserId());
                return;
            }
            if (cbbUserDetailDTO.getUserState() == IacUserStateEnum.ENABLE) {
                return;
            }
            LOGGER.info("禁用用户主动推送用户状态，用户id为[{}]", cbbUserDetailDTO.getId());
            userMgmtAPI.syncUserInfoToTerminal(cbbUserDetailDTO);
            LOGGER.info("禁用用户并退出终端会话，用户id为[{}]", cbbUserDetailDTO.getId());
            userInfoAPI.userLogout(cbbUserDetailDTO.getId());
            LOGGER.info("禁用用户关闭桌面，用户id为[{}]", cbbUserDetailDTO.getId());
            notifyCloseDisableUserVm(entity);
        } catch (BusinessException e) {
            LOGGER.error("禁用用户[{}]失败，失败原因: [{}]", entity.getUserId(), e.getI18nMessage(), e);
        }
    }

    /**
     * 关闭终端对应的虚机
     *
     * @param userTerminalEntity 用户信息
     * @throws BusinessException 业务异常
     */
    private void notifyCloseDisableUserVm(UserTerminalEntity userTerminalEntity) throws BusinessException {
        Assert.notNull(userTerminalEntity, "userTerminalEntity not allow null");
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(userTerminalEntity.getBindDeskId());
        if (cloudDesktopDetailDTO == null) {
            LOGGER.info("终端id[{}]未运行云桌面", userTerminalEntity.getTerminalId());
            return;
        }

        try {
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
            if (deskType == IDV) {
                CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                shutdownDeskIDVDTO.setId(cloudDesktopDetailDTO.getId());
                shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
            }
        } catch (BusinessException e) {
            LOGGER.error("通知终端id[{}]关闭云桌面失败，失败原因: [{}]", userTerminalEntity.getTerminalId(), e.getI18nMessage(), e);
        }
    }

    /**
     * 终端上线更新在线时长
     *
     * @param request 终端信息
     */
    private void handleTerminalTotalOnlineTime(CbbNoticeRequest request) {
        String terminalId = null;
        try {
            CbbShineTerminalBasicInfo terminalBasicInfo = request.getTerminalBasicInfo();
            Assert.notNull(terminalBasicInfo, "terminalBasicInfo id can not be null");
            CbbTerminalPlatformEnums platform = terminalBasicInfo.getPlatform();
            Assert.notNull(platform, "platform id can not be null");
            if (CbbTerminalPlatformEnums.APP == platform || CbbTerminalPlatformEnums.PC == platform ||
                    CbbTerminalPlatformEnums.RCA == platform || CbbTerminalPlatformEnums.CLOUD_DOCK == platform) {
                LOGGER.warn("终端类型为[{}],不计入统计", platform);
                return;
            }
            //利旧PC部署成TCI的也不统计
            if (CbbTerminalPlatformEnums.VOI == platform) {
                if (terminalBasicInfo.getTerminalWorkSupportModeArr() != null && terminalBasicInfo.getTerminalWorkSupportModeArr().length == 1) {
                    return;
                }
            }
            terminalId = terminalBasicInfo.getTerminalId();
            TerminalOnlineTimeRecordDTO dto = new TerminalOnlineTimeRecordDTO();
            dto.setTerminalId(terminalId);
            dto.setMacAddr(terminalBasicInfo.getMacAddr());
            dto.setPlatform(platform.name());
            onlineTimeRecordService.doUpdateByOnline(dto);
        } catch (Exception e) {
            LOGGER.error("终端[{}]上线,更新终端的在线时长错误", terminalId, e);
        }
    }
}

