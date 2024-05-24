package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLicenseAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.enums.ClearSessionReasonTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.userlicense.service.UserLicenseService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户并发授权API实现
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月29日
 *
 * @author lihengjing
 */
public class UserLicenseAPIImpl implements UserLicenseAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseAPIImpl.class);

    @Autowired
    private UserLicenseService userLicenseService;

    @Override
    public UUID createUserSessionAndLicense(UserSessionDTO userSessionDTO) throws BusinessException {
        Assert.notNull(userSessionDTO, "userSessionDTO can not be null");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        UUID sessionId = userLicenseService.createUserSessionAndLicense(userSessionDTO);
        stopWatch.stop();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("用户[{}]通过终端类型[{}:{}]申请资源类型[{}:{}]用户并发授权, 操作耗时：[{}] ms", userSessionDTO.getUserId(), userSessionDTO.getResourceType(),
                    userSessionDTO.getTerminalType(), userSessionDTO.getTerminalId(), userSessionDTO.getResourceId(), stopWatch.getTotalTimeMillis());
        }
        return sessionId;
    }

    @Override
    public void updateUserSessionAndLicense(UUID userId, String terminalId, TerminalTypeEnum terminalType, List<UserSessionDTO> sessionInfoList)
            throws BusinessException {
        Assert.notNull(userId, "userId can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(sessionInfoList, "sessionInfoList can not be null");

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        userLicenseService.updateUserSessionAndLicense(userId, terminalId, terminalType, sessionInfoList);
        stopWatch.stop();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("用户[{}]通过终端类型[{}:{}]更新会话详情[{}]更新用户并发授权, 操作耗时：[{}] ms", userId, terminalType, terminalId,
                    JSONObject.toJSONString(sessionInfoList), stopWatch.getTotalTimeMillis());
        }
    }

    @Override
    public void clearUserSessionByTerminalId(TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        userLicenseService.clearUserSessionAndLicenseByTerminal(ClearSessionReasonTypeEnum.TERMINAL_CLEAR, terminalType, terminalId);
    }

    @Override
    public List<UUID> findSessionUserIdListByTerminalId(TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        return userLicenseService.findSessionUserIdListByTerminalId(terminalType, terminalId);
    }

}
