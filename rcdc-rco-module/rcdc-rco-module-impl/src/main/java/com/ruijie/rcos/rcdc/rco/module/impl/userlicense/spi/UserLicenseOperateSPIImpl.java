package com.ruijie.rcos.rcdc.rco.module.impl.userlicense.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.userlicense.UserSessionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.UserSessionOperateSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlicense.UserLicenseBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 申请用户授权操作SPI实现
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public class UserLicenseOperateSPIImpl implements UserSessionOperateSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseOperateSPIImpl.class);

    @Autowired
    private UserLicenseAPI userLicenseAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public UUID getUserSessionId(UserSessionDTO userSessionDTO) throws BusinessException {
        Assert.notNull(userSessionDTO, "userSessionDTO cannot be null");
        String terminalId = userSessionDTO.getTerminalId();
        try {
            TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(terminalId);
            UUID userId = terminalDTO.getUserId();
            userSessionDTO.setUserId(userId);
            if (CbbTerminalPlatformEnums.VDI == terminalDTO.getPlatform()) {
                userSessionDTO.setTerminalType(TerminalTypeEnum.VDI);
            } else if (CbbTerminalPlatformEnums.APP == terminalDTO.getPlatform()) {
                userSessionDTO.setTerminalType(TerminalTypeEnum.APP);
            } else {
                throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_TYPE_EXCEPTION,
                        terminalDTO.getPlatform().name());
            }
            userLoginRecordService.handleDeskSessionRecordStartTime(userSessionDTO);
        } catch (BusinessException e) {
            LOGGER.error("查询终端类型[{}]ID[{}]详细信息出错，userSessionDTO：{}，异常堆栈：{}", terminalId, userSessionDTO.getTerminalType().name(),
                    JSONObject.toJSONString(userSessionDTO), e);
            throw new BusinessException(UserLicenseBusinessKey.RCDC_RCO_USER_LICENSE_USER_BIND_TERMINAL_NOT_FIND, e);
        }
        return userLicenseAPI.createUserSessionAndLicense(userSessionDTO);
    }
}

