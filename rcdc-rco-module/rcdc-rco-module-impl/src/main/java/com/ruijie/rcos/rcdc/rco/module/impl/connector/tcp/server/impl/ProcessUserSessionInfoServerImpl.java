package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.common.dto.Result;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.request.UserSessionInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server.ProcessUserSessionInfoServer;
import com.ruijie.rcos.rcdc.rco.module.impl.monitor.dashboard.service.DesktopSessionLogService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.CollectionUitls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月29日
 *
 * @author lihengjing
 */
public class ProcessUserSessionInfoServerImpl implements ProcessUserSessionInfoServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessUserSessionInfoServerImpl.class);

    @Autowired
    private UserLicenseAPI userLicenseAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private DesktopSessionLogService desktopSessionLogService;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public Result updateUserSessionInfo(String terminalId, UserSessionInfoRequest request) throws BusinessException {
        Assert.notNull(terminalId, "terminalId is null");
        Assert.notNull(request, "request is null");

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("更新客户端ID[{}]在线会话详情，原始报文: {}", terminalId, JSONObject.toJSONString(request));
        }

        validateUserSessionInfoRequest(request);

        Result response = new Result();
        response.setCode(CommonMessageCode.SUCCESS);

        TerminalDTO terminalDTO;
        try {
            terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
        } catch (BusinessException e) {
            LOGGER.error("更新客户端ID[{}]在线会话详情，原始报文: {}获取终端信息失败，e={}", terminalId, JSONObject.toJSONString(request), e);
            throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_NOT_FIND, e);
        }

        UUID userId = terminalDTO.getUserId();
        TerminalTypeEnum terminalType;

        if (CbbTerminalPlatformEnums.VDI == terminalDTO.getPlatform()) {
            terminalType = TerminalTypeEnum.VDI;
        } else if (CbbTerminalPlatformEnums.APP == terminalDTO.getPlatform()) {
            terminalType = TerminalTypeEnum.APP;
        } else {
            BusinessException e = new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_TYPE_EXCEPTION,
                    terminalDTO.getPlatform().name());
            LOGGER.error("更新客户端ID[{}]在线会话详情，原始报文: {}获取终端信息失败，e={}", terminalId, JSONObject.toJSONString(request), e);
            throw e;
        }

        if (CollectionUitls.isEmpty(request.getSessionInfoList())) {
            LOGGER.info("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情为空直接清空该终端会话详情", userId, terminalType, terminalId);
            userLicenseAPI.clearUserSessionByTerminalId(terminalType, terminalId);

            // 多会话和第三方桌面连接日志要在这里处理
            handleDeskSessionInfo(userId, terminalId, terminalType, new ArrayList<>());
        } else {
            if (Objects.isNull(userId)) {
                BusinessException e = new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_USER_NULL);
                LOGGER.error("更新客户端ID[{}]在线会话详情，原始报文: {}获取终端信息绑定的用户信息为空，e={}", terminalId, JSONObject.toJSONString(request), e);
                throw e;
            }
            List<UserSessionDTO> sessionInfoList = buildSessionInfoList(request, userId, terminalId, terminalType);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情, 处理后会话报文: {}", userId, terminalType, terminalId,
                        JSONObject.toJSONString(sessionInfoList));
            }

            // 多会话和第三方桌面连接日志要在这里处理
            handleDeskSessionInfo(userId, terminalId, terminalType, sessionInfoList);

            try {
                userLicenseAPI.updateUserSessionAndLicense(userId, terminalId, terminalType, sessionInfoList);
            } catch (BusinessException e) {
                LOGGER.error("用户ID[{}]更新客户端类型[{}]ID[{}]在线会话详情失败，处理后会话报文: {}，e={}", userId, terminalType, terminalId,
                        JSONObject.toJSONString(sessionInfoList), e);
                throw e;
            }
        }

        return response;
    }

    private void validateUserSessionInfoRequest(UserSessionInfoRequest request) throws BusinessException {
        if (request.getCount() != 0) {
            if (CollectionUitls.isEmpty(request.getSessionInfoList()) || request.getSessionInfoList().size() != request.getCount()) {
                throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_UPDATE_CURRENT_SESSION_INFO_PARAMETER_ILLEGAL);
            }
        }
    }

    private List<UserSessionDTO> buildSessionInfoList(UserSessionInfoRequest request, UUID userId, String terminalId, TerminalTypeEnum terminalType) {
        List<UserSessionDTO> sessionInfoList = request.getSessionInfoList();
        Assert.notEmpty(sessionInfoList, "sessionInfoList is empty");
        for (UserSessionDTO userSessionDTO : sessionInfoList) {
            userSessionDTO.setTerminalId(terminalId);
            userSessionDTO.setTerminalType(terminalType);
            userSessionDTO.setUserId(userId);
        }
        return sessionInfoList;
    }

    private void handleDeskSessionInfo(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList) {
        if (terminalType == TerminalTypeEnum.WEB_CLIENT) {
            // 网页客户端不支持HEST
            return;
        }
        desktopSessionLogService.handleDeskSessionLog(userId, terminalId, sessionInfoList);
        userLoginRecordService.handleDeskSessionRecordEndTime(userId, terminalId, sessionInfoList);
    }
}
