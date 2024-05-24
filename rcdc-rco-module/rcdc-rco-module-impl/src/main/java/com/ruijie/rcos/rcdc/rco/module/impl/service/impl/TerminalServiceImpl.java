package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import java.io.IOException;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.log.module.def.api.BaseSystemLogMgmtAPI;
import com.ruijie.rcos.gss.log.module.def.api.request.systemlog.BaseCreateSystemLogRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacUserTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.DeleteIDVDesktopOperateLogTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloperate.exception.TerminalOperateSuccessBusinessException;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloplog.enums.ClientOperateLogType;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.*;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.*;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.FullSystemDiskErrorEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.TerminalChangeServerIpResponseCode;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.TerminalInitResponseCode;
import com.ruijie.rcos.rcdc.rco.module.impl.service.TerminalService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.TerminalSettingDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.TerminalServiceTx;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalStateEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.CbbTerminalWorkModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 终端操作
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月20日
 *
 * @author nt
 */
@Service
public class TerminalServiceImpl implements TerminalService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TerminalServiceImpl.class);

    @Autowired
    private ViewTerminalDAO viewTerminalDAO;

    @Autowired
    private UserTerminalDAO userTerminalDAO;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private TerminalServiceTx terminalServiceTx;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private ViewDesktopDetailDAO viewDesktopDetailDAO;

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    @Autowired
    private UserService userService;

    @Autowired
    private BaseSystemLogMgmtAPI baseSystemLogMgmtAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private UamAppTestAPI uamAppTestAPI;

    @Autowired
    private TerminalWorkModeMappingDAO terminalWorkModeMappingDAO;

    @Autowired
    private CbbTerminalConfigAPI cbbTerminalConfigAPI;

    @Autowired
    private UserNotifyAPI userNotifyAPI;

    @Autowired
    private WatermarkMessageAPI watermarkMessageAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private ClientOpLogAPI clientOpLogAPI;

    private static final Object LOCK = new Object();

    private static final String LOCK_PRE = "TERMINAL_SAVE_";

    private static final Object LOCK_OBJ = new Object();

    private static final int LOCK_TIMES = 15;

    private static final String PRODUCT_PREFIX = "RG-";

    public static final String OLD_PREFIX = "R";

    public static final String NEW_PREFIX = "C";

    private static final String[] TERMINAL_DEFAULT_WORK_MODE = new String[]{CbbTerminalPlatformEnums.VOI.name(), CbbTerminalPlatformEnums.VDI.name()};

    /**
     * O 前缀
     */
    public static final String O_PREFIX = "O";

    /**
     * 智慧教室OPS 前缀
     */
    public static final String RG_OPS_PREFIX = "RG-OPS";

    /**
     * 智慧教室OPS 前缀长度限制
     */
    public static final int RG_OPS_LENGTH = 6;

    public static final String PLATFORM_DELIMITER = ",";

    @Override
    public ViewTerminalEntity getViewByTerminalId(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminal id can not be null");

        ViewTerminalEntity terminal = viewTerminalDAO.findFirstByTerminalId(terminalId);
        if (terminal == null) {
            LOGGER.error("terminal not exist, terminal id[{}]", terminalId);
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_TERMINAL_NOT_EXIST, terminalId.toUpperCase());
        }
        return terminal;
    }

    @Override
    public void setLoginUserOnTerminal(String terminalId, UUID userId) throws BusinessException {
        Assert.hasText(terminalId, "terminal id can not be null");
        Assert.notNull(userId, "userId id can not be null");

        UserTerminalEntity terminal = getAndCheckByTerminalId(terminalId);
        terminal.setUserId(userId);
        terminal.setHasLogin(true);
        userTerminalDAO.save(terminal);
        clientOpLogAPI.saveUserOperateLog(terminalId, userId, ClientOperateLogType.ONE_CLIENT_LOG_LOGIN_SUCCESS);
    }

    @Override
    public void saveTerminal(CbbShineTerminalBasicInfo terminalBasicInfo) {

        Assert.notNull(terminalBasicInfo, "terminal id can not be null");
        String terminalId = terminalBasicInfo.getTerminalId();
        Assert.hasText(terminalId, "terminal id can not be null");

        try {
            LockableExecutor.executeWithTryLock(LOCK_PRE + terminalId, () -> doSaveTerminal(terminalId, terminalBasicInfo), LOCK_TIMES);
        } catch (BusinessException ex) {
            LOGGER.error(String.format("轻量级锁定失败，继续尝试使用重量级锁，对应 terminlId：%s", terminalId), ex);
            synchronized (LOCK_OBJ) {
                doSaveTerminal(terminalId, terminalBasicInfo);
            }
        }
    }

    private void doSaveTerminal(String terminalId, CbbShineTerminalBasicInfo terminalBasicInfo) {
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        if (userTerminalEntity == null) {
            LOGGER.info("新接入终端[{}]，保存关联关系", terminalId);
            userTerminalEntity = new UserTerminalEntity();
            userTerminalEntity.setTerminalPlatform(terminalBasicInfo.getPlatform());
            userTerminalEntity.setCreateTime(new Date());
            userTerminalEntity.setTerminalId(terminalId);
            setTerminalMode(userTerminalEntity, terminalBasicInfo);
            userTerminalDAO.save(userTerminalEntity);
        }
    }

    private void setTerminalMode(UserTerminalEntity userTerminalEntity, CbbShineTerminalBasicInfo terminalBasicInfo) {
        if (StringUtils.isNotEmpty(terminalBasicInfo.getIdvTerminalMode())) {
            userTerminalEntity.setTerminalMode(IdvTerminalModeEnums.valueOf(terminalBasicInfo.getIdvTerminalMode().toUpperCase()));
        } else {
            userTerminalEntity.setTerminalMode(IdvTerminalModeEnums.UNKNOWN);
        }
    }


    @Override
    public void initialize(String terminalId, Boolean retainImage) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be empty");
        Assert.notNull(retainImage, "retainImage can not be empty");
        // 终端必须存在
        UserTerminalEntity userTerminalEntity = getAndCheckByTerminalId(terminalId);
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        CbbTerminalStateEnums state = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId).getState();
        if (state == CbbTerminalStateEnums.OFFLINE) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_OFFLINE_NOT_ALLOW_INIT, terminalAddr);
        }
        if (state == CbbTerminalStateEnums.UPGRADING) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_UPGRADING_NOT_ALLOW_INIT, terminalAddr);
        }
        // 向idv终端发送终端初始化请求
        sendInitializeRequestToIdv(userTerminalEntity, retainImage, terminalAddr);
    }

    private void sendInitializeRequestToIdv(UserTerminalEntity userTerminalEntity, Boolean retainImage,
                                            String terminalMacAddr) throws BusinessException {
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(Constants.TERMINAL_INIT, userTerminalEntity.getTerminalId());
        LOGGER.info("向shine发送的action： " + Constants.TERMINAL_INIT);
        TerminalInitRequest terminalInitRequest = new TerminalInitRequest(retainImage);
        messageRequest.setContent(terminalInitRequest);
        try {
            CbbShineMessageResponse response = cbbTranspondMessageHandlerAPI.syncRequest(messageRequest);
            LOGGER.info("shine传回的状态码：" + response.getCode());
            // 终端云桌面正在运行，不可初始化
            if (response.getCode() == TerminalInitResponseCode.DESKTOP_ON_RUNNING.getCode()) {
                throw new TerminalOperateSuccessBusinessException(BusinessKey.RCDC_USER_TERMINAL_CLOUD_DESKTOP_ON_RUNNING, terminalMacAddr);
            }

            // 终端上正在编辑或提取镜像
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_LOCAL_EDIT_IMAGE.getCode()) {
                throw new TerminalOperateSuccessBusinessException(BusinessKey.RCDC_USER_TERMINAL_LOCAL_EDIT_IMAGE, terminalMacAddr);
            }

            // 通知shine前端失败，不可初始化
            if (response.getCode() == TerminalInitResponseCode.NOTIFY_SHINE_WEB_FAIL.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOTIFY_SHINE_WEB_FAIL, terminalMacAddr);
            }
            // 终端正在清空数据盘，不能同时初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_CLEAR_DATA_DISK.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_CLEARING_DATA_DISK, terminalMacAddr);
            }
            // 终端正在初始化，不能再次初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_INIT.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_INIT, terminalMacAddr);
            }
            // 终端正在初始化，不能再次初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_RESTORE_DESKTOP.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_RESTORE_DESKTOP, terminalMacAddr);
            }
            //终端正在变更镜像
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_IMAGE_REPLACEMENT.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_IMAGE_REPLACEMENT, terminalMacAddr);
            }
            // 使用4.x迁移过来的镜像的终端正在初始化，不能再次初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_RESTORE_DESKTOP_FOR_4X25X.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_RESTORE_DESKTOP, terminalMacAddr);
            }
            // 终端正在升级中，不能再次初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_UPGRADE.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_UPGRADE, terminalMacAddr);
            }
            // 终端正在检查和修复镜像，不能再次初始化
            if (response.getCode() == TerminalInitResponseCode.TERMINAL_ON_REPAIR_IMAGE.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_ON_REPAIR_IMAGE, terminalMacAddr);
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                LOGGER.error("向终端[{}]下发初始化请求超时", userTerminalEntity.getTerminalId(), e);
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_REQUEST_TERMINAL_TIME_OUT, e, terminalMacAddr);
            }
        }
    }

    @Override
    public void delete(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminal id can not be null");
        // 终端必须存在
        UserTerminalEntity terminal = getAndCheckByTerminalId(terminalId);
        // 未离线终端不允许删除
        CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (CbbTerminalStateEnums.OFFLINE != response.getState()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_ONLINE_NOT_ALLOW_DELETE, response.getTerminalName());
        }

        // 根据终端平台类型进行删除
        switch (response.getTerminalPlatform()) {
            case IDV:
                deleteIdv(terminal);
                break;
            case VOI:
                deleteIdv(terminal);
                break;
            case VDI:
                deleteVdi(terminal);
                break;
            default:
                deleteTerminal(terminal.getTerminalId());
        }
    }

    private void deleteIdv(UserTerminalEntity terminal) throws BusinessException {
        LOGGER.info("进行终端删除，终端模式是：" + terminal.getTerminalMode());
        // 终端接入未进行配置向导，直接下线终端模式为空，赋予未知类型进行删除
        if (terminal.getTerminalMode() == null) {
            terminal.setTerminalMode(IdvTerminalModeEnums.UNKNOWN);
        }
        // 根据terminal_mode进行判断是进行公有终端删除还是私有终端删除
        switch (terminal.getTerminalMode()) {
            case PUBLIC:
                deletePublicIdv(terminal);
                break;
            case PERSONAL:
                deletePersonalIdv(terminal);
                break;
            case UNKNOWN:
                deleteUnknownIdv(terminal);
                break;
            default:
                deleteTerminal(terminal.getTerminalId());
        }

    }

    private void deletePublicIdv(UserTerminalEntity terminal) throws BusinessException {
        DesktopOpLogDTO desktopOpLogRequest =
                desktopOpLogMgmtAPI.buildDeleteDesktopOpLogRequest(terminal.getBindDeskId(), DeleteIDVDesktopOperateLogTypeEnums.DELETE_TERMINAL);
        // 调用cbb组件删除终端记录
        deleteTerminal(terminal.getTerminalId());
        // 删除公用终端的配置信息
        userTerminalDAO.deleteById(terminal.getId());
        // 调用云桌面cbb组件删除终端关联的云桌面
        cbbIDVDeskMgmtAPI.deleteDeskIDV(terminal.getBindDeskId());
        desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
    }

    private void deletePersonalIdv(UserTerminalEntity terminal) throws BusinessException {
        // 在终端删除前调用，准备审计日志所需信息
        DesktopOpLogDTO desktopOpLogRequest =
                desktopOpLogMgmtAPI.buildDeleteDesktopOpLogRequest(terminal.getBindDeskId(), DeleteIDVDesktopOperateLogTypeEnums.DELETE_TERMINAL);
        // 调用终端cbb组件删除终端记录
        deleteTerminal(terminal.getTerminalId());
        // 解除用户和终端、云桌面和终端的绑定信息
        terminalServiceTx.deleteById(terminal);
        // 调用云桌面cbb组件删除终端关联的云桌面
        cbbIDVDeskMgmtAPI.deleteDeskIDV(terminal.getBindDeskId());
        desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
    }

    private void deleteUnknownIdv(UserTerminalEntity terminal) throws BusinessException {
        // 调用cbb组件删除终端记录
        deleteTerminal(terminal.getTerminalId());
        // 删除公用终端的配置信息
        userTerminalDAO.deleteById(terminal.getId());
    }

    private void deleteVdi(UserTerminalEntity terminal) throws BusinessException {
        // 调用cbb组件删除终端
        deleteTerminal(terminal.getTerminalId());
        // 解除与终端的绑定
        LOGGER.warn("deleteDeskCompletely rco terminal record, terminal id[{}]", terminal.getTerminalId());
        terminalServiceTx.deleteById(terminal);
    }

    private void deleteTerminal(String terminalId) throws BusinessException {
        // 调用cbb组件删除终端
        LOGGER.warn("invoke cbb terminal api to deleteDeskCompletely terminal, terminal id[{}]", terminalId);
        cbbTerminalOperatorAPI.delete(terminalId);
    }

    @Override
    public void syncTerminalVisitorSetting(VisitorSettingDTO request) throws BusinessException {
        Assert.notNull(request, "IdvUpdateVisitorSetting request can not be null");

        UserTerminalEntity terminalEntity = getAndCheckByTerminalId(request.getTerminalId());
        if (isVisitorSettingNotChange(request, terminalEntity)) {
            // 设置未变化，不发送消息
            return;
        }

        // 通知终端进行访客登录设置
        sendVisitorSettingToOnlineIdv(request);
    }

    private boolean isVisitorSettingNotChange(VisitorSettingDTO visitorSettingDTO, UserTerminalEntity userTerminalEntity) {
        if (userTerminalEntity.getEnableVisitorLogin() != visitorSettingDTO.getEnableVisitorLogin()) {
            return false;
        }

        return userTerminalEntity.getEnableAutoLogin() == visitorSettingDTO.getEnableAutoLogin();
    }

    private void sendVisitorSettingToOnlineIdv(VisitorSettingDTO request) throws BusinessException {
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(Constants.UPDATE_VISITOR_SETTING, request.getTerminalId());

        String terminalAddr = request.getTerminalId();
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(request.getTerminalId());
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", request.getTerminalId());
        }

        CbbShineMessageResponse response;
        try {
            response = cbbTranspondMessageHandlerAPI.syncRequest(messageRequest);
            // 终端访客登录设置失败
            if (response.getCode() == -1) {
                //不区分IDV|VOI的业务KEY  不需要改造 终端[{0}]设置访客登录失败
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_IDV_VISITOR_SETTING_FAIL, terminalAddr);
            }
        } catch (Exception e) {
            if (e instanceof BusinessException) {
                LOGGER.error("向终端[{}]发送更新访客登录设置请求失败", request.getTerminalId(), e);
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_OPERATE_MSG_SEND_FAIL, e, terminalAddr);
            }
            LOGGER.error("向终端[{}]发送更新访客登录设置请求失败", request.getTerminalId(), e);
            throw new IllegalStateException("向终端[" + request.getTerminalId() + "]发送更新访客登录设置请求失败", e);
        }

    }


    @Override
    public void editTerminalSetting(TerminalSettingDTO terminalSettingDTO) throws BusinessException {
        Assert.notNull(terminalSettingDTO, "terminalSettingDTO can not be null");

        UserTerminalEntity terminalEntity = getAndCheckByTerminalId(terminalSettingDTO.getTerminalId());
        terminalEntity.setBindUserId(terminalSettingDTO.getUserId());
        terminalEntity.setBindUserName(terminalSettingDTO.getUserName());
        terminalEntity.setBindUserTime(IdvTerminalModeEnums.PERSONAL == terminalSettingDTO.getIdvTerminalMode() ? new Date() : null);
        terminalEntity.setBindDeskId(terminalSettingDTO.getDeskId());
        terminalEntity.setTerminalMode(terminalSettingDTO.getIdvTerminalMode());
        terminalEntity.setEnableAutoLogin(terminalSettingDTO.getEnableAutoLogin());
        terminalEntity.setEnableVisitorLogin(terminalSettingDTO.getEnableVisitorLogin());

        userTerminalDAO.save(terminalEntity);
    }

    @Override
    public void noticeIdvAbortLocalEditImageTemplate(String terminalId, UUID imageId) {
        Assert.hasText(terminalId, "terminalId must not be null or empty");
        Assert.notNull(imageId, "imageId can not be null");
        try {
            CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);

            if (cbbTerminalBasicInfoDTO.getTerminalPlatform() != CbbTerminalPlatformEnums.IDV
                    && cbbTerminalBasicInfoDTO.getTerminalPlatform() != CbbTerminalPlatformEnums.VOI) {
                LOGGER.info("非IDV|VOI终端,无需检测是否在终端本地编辑镜像，terminalId[{}]", terminalId);
                return;
            }

            if (cbbTerminalBasicInfoDTO.getState() != CbbTerminalStateEnums.ONLINE) {
                LOGGER.info("当前终端不在线无需通知，terminalId[{}]", terminalId);
                return;
            }
            //与shine 确认 编辑IDV|VOI 不需要区分IDV|VOI  接口不变
            CbbShineMessageRequest cbbShineMessageRequest = CbbShineMessageRequest.create(Constants.IDV_ABORT_LOCAL_EDIT_IMAGE_TEMPLATE, terminalId);
            cbbShineMessageRequest.setContent(new IdvAbortLocalEditImageTemplateRequest(imageId));
            cbbTranspondMessageHandlerAPI.asyncRequest(cbbShineMessageRequest, new CbbTerminalCallback() {
                @Override
                public void success(String terminalId, CbbShineMessageResponse msg) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    Assert.notNull(msg, "msg cannot be null!");
                    LOGGER.info("通知终端放弃编辑成功，terminalId[{}]，信息[{}]", terminalId, msg.toString());
                }

                @Override
                public void timeout(String terminalId) {
                    Assert.notNull(terminalId, "terminalId cannot be null!");
                    LOGGER.error("通知终端放弃编辑超时，terminalId[{}]", terminalId);
                }
            });
        } catch (BusinessException e) {
            // 通知失败还是成功，对于RCDC来说可以不关注。
            LOGGER.error("通知IDV|VOI终端放弃编辑失败！", e);
        }
    }

    @Override
    public void checkIdvExistsLocalEditImageTemplate(String terminalId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must not be null or empty");
        String terminalMacAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO cbbTerminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalMacAddr = cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId();

            if (cbbTerminalBasicInfoDTO.getTerminalPlatform() != CbbTerminalPlatformEnums.IDV
                    && cbbTerminalBasicInfoDTO.getTerminalPlatform() != CbbTerminalPlatformEnums.VOI) {
                LOGGER.info("非IDV|VOI终端,无需检测是否在终端本地编辑镜像，terminalId[{}]", terminalId);
                return;
            }

            if (cbbTerminalBasicInfoDTO.getState() != CbbTerminalStateEnums.ONLINE) {
                LOGGER.error("终端镜像编辑，检测到终端状态[{}]不是在线的状态", cbbTerminalBasicInfoDTO.getState().name());
                throw new BusinessException(BusinessKey.RCDC_RCO_TERMINAL_NOT_ONLINE, terminalId);
            }
            //与shine 确认 编辑IDV|VOI 不需要区分IDV|VOI  接口不变
            CbbShineMessageRequest cbbShineMessageRequest =
                    CbbShineMessageRequest.create(Constants.CHECK_IDV_EXISTS_LOCAL_EDIT_IMAGE_TEMPLATE, terminalId);
            cbbShineMessageRequest.setContent(new Object());
            CbbShineMessageResponse<Boolean> cbbShineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(cbbShineMessageRequest);
            LOGGER.info("终端反馈是否存在编辑镜像结果：{}", JSON.toJSONString(cbbShineMessageRequest));

            if (cbbShineMessageResponse.getContent()) {
                LOGGER.info("终端反馈是否存在编辑镜像结果：{}", JSON.toJSONString(cbbShineMessageRequest));
                throw new BusinessException(BusinessKey.IDV_LOCAL_EDITING_IMAGE_TEMPLATE, cbbTerminalBasicInfoDTO.getUpperMacAddrOrTerminalId());
            }
        } catch (IOException | RuntimeException e) {
            LOGGER.error("向终端[{" + terminalId + "}]下发信息[{" + Constants.CHECK_IDV_EXISTS_LOCAL_EDIT_IMAGE_TEMPLATE + "}]超时", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REQUEST_TERMINAL_TIME_OUT, e, terminalMacAddr);
        } catch (InterruptedException e) {
            LOGGER.error("向终端[{}]下发信息[{}]被外部中断", terminalId, Constants.CHECK_IDV_EXISTS_LOCAL_EDIT_IMAGE_TEMPLATE, e);
            throw new IllegalStateException("下发终端失败", e);
        }
    }

    @Override
    public void wakeUpTerminal(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId can not be null");
        cbbTerminalOperatorAPI.wakeUpTerminal(terminalId);
    }

    @Override
    public void configTerminalFullSystemDisk(TerminalDTO terminalDTO, ShineConfigFullSystemDiskDTO request)
            throws BusinessException {
        Assert.notNull(terminalDTO, "terminalDTO must not be null");
        Assert.notNull(request, "request must not be null");

        CbbTerminalBasicInfoDTO basicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalDTO.getId());
        // 校验是否可以配置
        checkCanOpenFullSystemDisk(basicInfoDTO,terminalDTO, request.getEnableFullSystemDisk());

        // 下发通知
        notifyTerminalConfigFullSystemDisk(basicInfoDTO,terminalDTO.getId(), request);

        // 修改云桌面配置表信息
        userDesktopDAO.updateEnableFullSystemDiskByDeskId(terminalDTO.getBindDeskId(), request.getEnableFullSystemDisk());
    }

    @Override
    public void bindUser(String terminalId, UUID bindUserId) throws BusinessException {
        Assert.hasText(terminalId, "terminalId must not be null");
        Assert.notNull(bindUserId, "bindUserId must not be null");
        UserTerminalEntity userTerminalEntity = userTerminalDAO.findFirstByTerminalId(terminalId);
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        if (userTerminalEntity == null) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_TERMINAL_NOT_EXIST, terminalAddr.toUpperCase());
        }
        if (IdvTerminalModeEnums.PERSONAL != userTerminalEntity.getTerminalMode()) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOT_PERSONAL_NOT_BIND_USER, terminalAddr.toUpperCase());
        }

        IacUserDetailDTO userDetail = cbbUserAPI.getUserDetail(bindUserId);
        if (IacUserTypeEnum.VISITOR == userDetail.getUserType()) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_NOT_BIND_VISITOR_USER, terminalAddr.toUpperCase());
        }

        if (bindUserId.equals(userTerminalEntity.getBindUserId())) {
            LOGGER.info("终端:[{}]已经绑定用户:[{}]，无需更新,直接返回成功", terminalId, bindUserId);
            return;
        }
        String oldUserName = userTerminalEntity.getBindUserName();

        // 检查终端绑定的桌面是否处于测试中
        if (userTerminalEntity.getBindDeskId() != null) {
            uamAppTestAPI.checkTestingDesk(userTerminalEntity.getBindDeskId());
        }

        // 修改桌面绑定的用户信息
        terminalServiceTx.bindUser(userTerminalEntity, userDetail);

        // 当桌面在线时，发送新用户的账号密码到gt, 并更新水印信息
        CbbDeskDTO cbbDeskDTO = cbbIDVDeskMgmtAPI.getDeskIDV(userTerminalEntity.getBindDeskId());
        if (CbbCloudDeskState.RUNNING == cbbDeskDTO.getDeskState()) {
            userNotifyAPI.notifyUserPwdToGtByDesktopId(userTerminalEntity.getBindDeskId(),
                    userDetail.getUserName(), userDetail.getPassword());


            CloudDesktopDetailDTO desktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(userTerminalEntity.getBindDeskId());
            watermarkMessageAPI.sendToDesktopList(Lists.newArrayList(desktopDetailDTO), null);
        }
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_TERMINAL_BIND_USER_SUCCESS_LOG, terminalId, oldUserName, userTerminalEntity.getBindUserName());

        // 通知shine用户变更
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(Constants.TERMINAL_USER_CHANGE, terminalId);
        BindUserRequest bindUserRequest = new BindUserRequest();
        bindUserRequest.setOldUserName(oldUserName);
        bindUserRequest.setUserName(userTerminalEntity.getBindUserName());
        messageRequest.setContent(bindUserRequest);
        try {
            CbbShineMessageResponse cbbShineMessageResponse = cbbTranspondMessageHandlerAPI.syncRequest(messageRequest);
        } catch (Exception e) {
            // shine会补偿处理,这里无需抛出异常
            LOGGER.error("RCDC用户变更成功,通知终端消息出现异常:", e);
        }
    }

    @Override
    public List<ViewTerminalEntity> listByTerminalSn(String sn) {
        Assert.hasText(sn, "sn can not be empty");
        return viewTerminalDAO.findBySerialNumber(sn);
    }

    @Override
    public void initialTerminalCleanData(UserTerminalEntity userTerminalEntity) {
        Assert.notNull(userTerminalEntity, "userTerminalEntity cant be null");

        // 获取终端mac
        String terminalId = userTerminalEntity.getTerminalId();
        // 记录终端初始化成功
        LOGGER.info("终端终端初始化成功,terminalId = [{}]", terminalId);
        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }
        // 记录系统日志
        baseSystemLogMgmtAPI.createSystemLog(new BaseCreateSystemLogRequest(BusinessKey.RCDC_USER_TERMINAL_INIT_SUCCESS, terminalAddr));
        IdvTerminalModeEnums terminalMode = userTerminalEntity.getTerminalMode();
        switch (terminalMode) {
            case PERSONAL:
                // 解除用户、云桌面和终端的绑定信息,删除对应idv云桌面
                deletePersonalDesktop(userTerminalEntity);
                // 将终端移动至未分组
                recoverTerminalGroupToDefault(terminalId);
                break;
            case PUBLIC:
                // 调用云桌面cbb组件删除idv云桌面
                deletePublicDesktop(userTerminalEntity);
                // 将终端移动至未分组
                recoverTerminalGroupToDefault(terminalId);
                break;
            default:
                LOGGER.info("终端[{}]处于初始化模式，不需要初始化操作", terminalId);
        }
    }

    private void deletePersonalDesktop(UserTerminalEntity userTerminalEntity) {
        // 获取桌面id
        UUID desktopId = userTerminalEntity.getBindDeskId();
        // 获取终端mac
        String terminalId = userTerminalEntity.getTerminalId();
        try {
            DesktopOpLogDTO desktopOpLogRequest =
                    desktopOpLogMgmtAPI.buildDeleteDesktopOpLogRequest(desktopId, DeleteIDVDesktopOperateLogTypeEnums.INIT_TERMINAL);
            userService.deleteDesktop(desktopId, CbbCloudDeskType.IDV);
            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        } catch (BusinessException e) {
            LOGGER.error("终端[{0}]删除云桌面失败，失败原因：[{1}]", terminalId, e.getI18nMessage());
            throw new IllegalStateException("终端[" + terminalId + "]删除云桌面失败", e);
        }
    }

    private void deletePublicDesktop(UserTerminalEntity userTerminalEntity) {
        // 获取桌面id
        UUID desktopId = userTerminalEntity.getBindDeskId();
        // 获取终端mac
        String terminalId = userTerminalEntity.getTerminalId();
        try {
            DesktopOpLogDTO desktopOpLogRequest =
                    desktopOpLogMgmtAPI.buildDeleteDesktopOpLogRequest(desktopId, DeleteIDVDesktopOperateLogTypeEnums.INIT_TERMINAL);
            cbbIDVDeskMgmtAPI.deleteDeskIDV(desktopId);
            userTerminalEntity.setBindDeskId(null);
            userTerminalEntity.setTerminalMode(IdvTerminalModeEnums.UNKNOWN);
            userTerminalDAO.save(userTerminalEntity);
            desktopOpLogMgmtAPI.saveOperateLog(desktopOpLogRequest);
        } catch (BusinessException e) {
            LOGGER.error("终端[{0}]删除云桌面失败，失败原因：[{1}]", terminalId, e.getI18nMessage());
            throw new IllegalStateException("终端[" + terminalId + "]删除云桌面失败", e);
        }
    }

    @Override
    public void changeTerminalServerIp(String terminalId, String serverIp) throws BusinessException {
        Assert.hasText(terminalId, "terminalId can not be empty");
        Assert.hasText(serverIp, "serverIp can not be empty");
        // 终端必须存在
        UserTerminalEntity userTerminalEntity = getAndCheckByTerminalId(terminalId);
        CbbTerminalStateEnums state = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId).getState();
        if (state == CbbTerminalStateEnums.OFFLINE) {
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_OFFLINE_NOT_ALLOW_CHANGE_SERVER_IP, terminalId);
        }
        // 向idv终端发送终端初始化请求
        sendChangeServerIpRequestToIdv(userTerminalEntity, serverIp);
    }

    @Override
    public CbbTerminalBizConfigDTO getTerminalSupportMode(TerminalWorkModeConfigDTO terminalWorkModeConfigDTO) {
        Assert.notNull(terminalWorkModeConfigDTO, "terminalWorkModeConfigDTO cant be null");

        if (terminalWorkModeConfigDTO.getTerminalWorkSupportModeArr() == null) {
            LOGGER.info("终端能力列表为空，此为未升级终端[{}]连接...", terminalWorkModeConfigDTO.getTerminalId());
            CbbTerminalBizConfigDTO configDTO = new CbbTerminalBizConfigDTO();
            configDTO.setTerminalWorkModeArr(new CbbTerminalWorkModeEnums[] {convertPlatformToWorkMode(terminalWorkModeConfigDTO.getPlatform())});
            configDTO.setTerminalPlatform(terminalWorkModeConfigDTO.getPlatform());
            configDTO.setAuthMode(terminalWorkModeConfigDTO.getPlatform());
            return configDTO;
        }
        return extractTerminalMode(terminalWorkModeConfigDTO);
       
    }

    private CbbTerminalBizConfigDTO extractTerminalMode(TerminalWorkModeConfigDTO terminalWorkModeConfigDTO) {
        // 软终端没有终端型号
        if (StringUtils.isNotBlank(terminalWorkModeConfigDTO.getProductType())) {
            LOGGER.info("上报的终端型号为[{}]", terminalWorkModeConfigDTO.getProductType());

            // 查找终端型号
            String productType = terminalWorkModeConfigDTO.getProductType();
            List<CbbTerminalConfigDTO> terminalConfigDTOList = cbbTerminalConfigAPI.findTerminalConfigByProductTypeLike("%" + productType);
            if (CollectionUtils.isNotEmpty(terminalConfigDTOList)) {
                //构造终端配置信息
                CbbTerminalConfigDTO terminalConfigDTO = terminalConfigDTOList.get(0);
                return buildConvertConfigDTO(terminalWorkModeConfigDTO,  terminalConfigDTO);
            }

            // 自研终端，且未在配置列表中, 返回VDI类型
            if (cbbTerminalConfigAPI.isRjTerminal(terminalWorkModeConfigDTO.getProductType())) {
                LOGGER.info("自研终端[{}]-[{}], 不在配置表中，默认使用VDI模式接入", terminalWorkModeConfigDTO.getTerminalId(),
                        terminalWorkModeConfigDTO.getProductType());
                return convertConfigDTO(CbbTerminalPlatformEnums.VDI, CbbTerminalPlatformEnums.VDI.toString(), CbbTerminalPlatformEnums.VDI);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("其它类型终端，默认使用TCI + VDI模式， VDI授权模式接入。");
        }
        // 都不符合，则按照终端上报的类型，否则为VOI,VDI
        return Optional.ofNullable(terminalWorkModeConfigDTO.getPlatform())
                .map(platformEnum -> convertConfigDTO(platformEnum, platformEnum.toString(), platformEnum))
                .orElse(convertConfigDTO(CbbTerminalPlatformEnums.VOI, getDefaultWorkMode() , CbbTerminalPlatformEnums.VOI));
    }

    /**
     * 构造终端配置信息 抽出为方法 逻辑不动
     *
     * @param terminalWorkModeConfigDTO 终端工作模式配置信息
     * @param terminalConfig 终端硬件配置
     * @return convertConfigDTO
     */
    private CbbTerminalBizConfigDTO buildConvertConfigDTO(TerminalWorkModeConfigDTO terminalWorkModeConfigDTO,
                                                          CbbTerminalConfigDTO terminalConfig) {
        LOGGER.info("终端型号[{}]支持模式为[{}]", terminalWorkModeConfigDTO.getProductType(), terminalConfig.getSupportWorkMode());
        // 单模式支持，以rcdc配置为准
        CbbTerminalPlatformEnums workMode = terminalWorkModeConfigDTO.getPlatform();
        if (!StringUtils.contains(terminalConfig.getSupportWorkMode(), PLATFORM_DELIMITER)) {
            workMode = CbbTerminalPlatformEnums.convert(terminalConfig.getSupportWorkMode());
        }
        return convertConfigDTO(workMode, terminalConfig.getSupportWorkMode(), CbbTerminalPlatformEnums.convert(terminalConfig.getAuthMode()));
    }

    /**
     * 根据支持的类型，返回对应的结果集
     * 默认返回终端传进来的镜像类型
     *
     * @param workingMode 支持的类型
     * @return CbbTerminalBizConfigDTO
     */
    private CbbTerminalBizConfigDTO convertConfigDTO(CbbTerminalPlatformEnums cbbTerminalPlatformEnums, String workingMode
            , CbbTerminalPlatformEnums authMode) {
        Assert.notNull(workingMode, "不支持的终端类型");

        CbbTerminalWorkModeEnums[] cbbTerminalWorkModeEnumsArr =
                Arrays.stream(StringUtils.split(workingMode, ',')).map(this::parseCbbTerminalWorkModeEnum).toArray(CbbTerminalWorkModeEnums[]::new);
        CbbTerminalBizConfigDTO configDTO = new CbbTerminalBizConfigDTO();
        configDTO.setTerminalWorkModeArr(cbbTerminalWorkModeEnumsArr);
        configDTO.setTerminalPlatform(Optional.ofNullable(cbbTerminalPlatformEnums).orElse(authMode));
        configDTO.setAuthMode(authMode);
        return configDTO;
    }

    private CbbTerminalWorkModeEnums convertPlatformToWorkMode(CbbTerminalPlatformEnums platform) {
        for (CbbTerminalWorkModeEnums mode : CbbTerminalWorkModeEnums.values()) {
            if (mode.name().equals(platform.name())) {
                return mode;
            }
        }

        throw new IllegalArgumentException("终端类型【" + platform + "】未定义，不支持该类型的终端");
    }

    /**
     * 根据枚举值，获取枚举具体类型
     *
     * @param strEnumName 字符串枚举值
     * @return
     */

    private CbbTerminalWorkModeEnums parseCbbTerminalWorkModeEnum(String strEnumName) {
        for (CbbTerminalWorkModeEnums enums : CbbTerminalWorkModeEnums.values()) {
            if (enums.name().equals(strEnumName)) {
                return enums;
            }
        }

        throw new IllegalArgumentException("终端类型【" + strEnumName + "】未定义，不支持该类型的终端");
    }
    
    private void sendChangeServerIpRequestToIdv(UserTerminalEntity userTerminalEntity, String serverIp) throws BusinessException {
        String terminalMac = userTerminalEntity.getTerminalId();
        CbbShineMessageRequest messageRequest = CbbShineMessageRequest.create(Constants.CHANGE_TERMINAL_SERVER_IP, terminalMac);
        LOGGER.info("向shine发送的action： " + Constants.CHANGE_TERMINAL_SERVER_IP);
        messageRequest.setContent(JSON.toJSONString(new IdvChangeServerIpDTO(serverIp)));
        try {
            CbbShineMessageResponse response = cbbTranspondMessageHandlerAPI.syncRequest(messageRequest);
            int resCode = response.getCode();
            LOGGER.info("shine传回的状态码：" + resCode);
            if (resCode == TerminalChangeServerIpResponseCode.WRITE_TERMINAL_SETTING_FAIL.getCode()) {
                throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_CHANGE_SERVER_IP_SETTING_FAIL, terminalMac);
            }
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("向终端[{}]下发修改云服务器地址请求失败", terminalMac, e);
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_CHANGE_SERVER_IP_FAIL, e, terminalMac);
        }
    }

    private void recoverTerminalGroupToDefault(String terminalId) {
        // 获取终端名称
        CbbTerminalBasicInfoDTO response = new CbbTerminalBasicInfoDTO();
        try {
            response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("获取终端[{0}]基本信息失败，失败原因[{1}]", terminalId, e.getI18nMessage());
            throw new IllegalStateException("获取终端[" + terminalId + "]基本信息失败", e);
        }
        String terminalName = response.getTerminalName();
        try {
            CbbModifyTerminalDTO cbbModifyTerminalRequest = new CbbModifyTerminalDTO();
            cbbModifyTerminalRequest.setCbbTerminalId(terminalId);
            cbbModifyTerminalRequest.setTerminalName(terminalName);
            cbbModifyTerminalRequest.setGroupId(cbbTerminalGroupMgmtAPI.DEFAULT_TERMINAL_GROUP_ID);
            cbbTerminalOperatorAPI.modifyTerminal(cbbModifyTerminalRequest);
        } catch (BusinessException e) {
            LOGGER.error("修改终端[{0}]分组信息失败，失败原因[{1}]", terminalId, e.getI18nMessage());
            throw new IllegalStateException("修改终端[" + terminalId + "]分组信息失败", e);
        }
    }

    private void notifyTerminalConfigFullSystemDisk( CbbTerminalBasicInfoDTO basicInfoDTO,String terminalId,
                                                     ShineConfigFullSystemDiskDTO request) throws BusinessException {

        //获取终端操作LOG 用于打印日志
        String  terminalIdentification = basicInfoDTO.getUpperMacAddrOrTerminalId();
        CbbShineMessageRequest<String> shineMessageRequest = CbbShineMessageRequest.create(ShineAction.SET_FULL_SYSTEM_DISK_CONFIG, terminalId);
        shineMessageRequest.setContent(JSON.toJSONString(request));

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }


        CbbShineMessageResponse<?> response;
        try {
            LOGGER.info("通知终端[{}]修改系统盘自动扩容请求为[{}]", terminalIdentification, JSON.toJSONString(request));
            response = cbbTranspondMessageHandlerAPI.syncRequest(shineMessageRequest);
        } catch (Exception e) {
            LOGGER.error("修改终端[" + terminalIdentification + "]网卡工作模式失败，失败原因：", e);
            throw new BusinessException(BusinessKey.RCDC_RCO_REQUEST_TERMINAL_TIME_OUT, e, terminalAddr);
        }

        // 如果shine返回失败
        if (response != null && response.getCode() != FullSystemDiskErrorEnum.SUCCESS.getCode()) {
            FullSystemDiskErrorEnum errorMessage = FullSystemDiskErrorEnum.getByCode(response.getCode());
            if (errorMessage == FullSystemDiskErrorEnum.UNKNOWN) {
                LOGGER.error("未找到预期错误码[{}],强制转成UNKNOWN. 请求信息为：[terminalId:{}, request:{}]", response.getCode(),
                        terminalIdentification, JSON.toJSONString(request));
            }

            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ERROR_MESSAGE_PREFIX + errorMessage.name().toLowerCase(),
                    terminalIdentification);
        }

    }

    private void checkCanOpenFullSystemDisk(CbbTerminalBasicInfoDTO basicInfoDTO, TerminalDTO terminalDTO, Boolean enableFullSystemDisk)
            throws BusinessException {
        //获取终端操作LOG 用于打印日志
        String  terminalIdentification = basicInfoDTO.getUpperMacAddrOrTerminalId();
        // 是否IDV/TCI模式
        if (!terminalDTO.isIDVModel() && !terminalDTO.isVOIModel()) {
            LOGGER.error("终端[{}]部署模式为[{}]，不允许开启系统盘自动扩容", terminalIdentification, terminalDTO.getPlatform());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ONLY_SUPPORT_IDV_OR_TCI);
        }

        // 是否在线
        if (!terminalDTO.isTerminalOnline()) {
            LOGGER.error("终端[{}]状态为[{}]，不允许开启系统盘自动扩容", terminalIdentification, terminalDTO.getTerminalState());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_MODIFY_NIC_WORK_MODE_ONLY_SUPPORT_ONLINE);
        }

        if (terminalDTO.getBindDeskId() == null) {
            LOGGER.error("终端[{}]不存在云桌面，无需开启系统盘自动扩容", terminalIdentification);
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_EXISTS_CLOUD_DESK, terminalIdentification);
        }

        // 只允许WIN7、WIN10, WIN11\tci uos kos进行配置
        if (!CbbOsType.isWin7UpOS(terminalDTO.getImageTemplateOsType())
                && !((CbbOsType.UOS_64 == terminalDTO.getImageTemplateOsType() || CbbOsType.KYLIN_64 == terminalDTO.getImageTemplateOsType())
                        && terminalDTO.isVOIModel())) {
            LOGGER.error("终端[{}]上桌面的系统类型为[{}]，不允许开启系统盘自动扩容", terminalIdentification, terminalDTO.getImageTemplateOsType());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_ONLY_SUPPORT_WIN7_AND_WIN10, terminalIdentification,
                    terminalDTO.getImageTemplateOsType().toString());
        }

        // 如果没开启系统盘自动扩容，则不需要后续的校验
        if (Boolean.FALSE.equals(enableFullSystemDisk)) {
            return;
        }

        // 系统盘未关闭
        TerminalCloudDesktopDTO cloudDesktopInfo = terminalDTO.getTerminalCloudDesktop();
        if (Boolean.TRUE.equals(cloudDesktopInfo.getAllowLocalDisk())) {
            ViewUserDesktopEntity userDeskInfo = viewDesktopDetailDAO.findByCbbDesktopId(cloudDesktopInfo.getDesktopId());
            LOGGER.error("终端[{}]上桌面[{}]存在本地盘，不允许开启系统盘自动扩容", terminalIdentification, cloudDesktopInfo.getDesktopId());
            throw new BusinessException(BusinessKey.RCDC_TERMINAL_CONFIG_FULL_SYSTEM_DISK_NOT_ALLOW_LOCAL_DISK, userDeskInfo.getStrategyName());
        }
    }

    private UserTerminalEntity getAndCheckByTerminalId(String terminalMac) throws BusinessException {
        UserTerminalEntity terminal = userTerminalDAO.findFirstByTerminalId(terminalMac);
        if (terminal == null) {
            LOGGER.error("terminal not exist, terminalMac[{}]", terminalMac);
            throw new BusinessException(BusinessKey.RCDC_USER_TERMINAL_TERMINAL_NOT_EXIST, terminalMac.toUpperCase());
        }
        return terminal;
    }

    private String getDefaultWorkMode() {
        return StringUtils.join(TERMINAL_DEFAULT_WORK_MODE, ",");
    }

}
