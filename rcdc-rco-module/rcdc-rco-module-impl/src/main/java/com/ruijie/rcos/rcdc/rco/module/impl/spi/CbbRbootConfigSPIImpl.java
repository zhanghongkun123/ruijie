package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.CbbTerminalPlatformEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.rboot.dto.CbbRbootConfigDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbRbootConfigSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Description: 获取Rboot开关配置
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/3 17:35
 *
 * @author yanlin
 */
public class CbbRbootConfigSPIImpl implements CbbRbootConfigSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbRbootConfigSPIImpl.class);

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Override
    public CbbRbootConfigDTO getTerminalRbootConfig(String terminalId) throws BusinessException {
        Assert.notNull(terminalId, "terminalId cannot be null");

        // 判断是TCI公共终端，则直接返回True
        CbbRbootConfigDTO configDTO = new CbbRbootConfigDTO();
        if (isTciPublicTerminal(terminalId)) {
            LOGGER.info("设置默认开启TCI公共终端[{}]的rboot启动开关", terminalId);
            configDTO.setEnableRboot(Boolean.TRUE);
        } else {
            configDTO.setEnableRboot(Boolean.FALSE);
        }
        return configDTO;
    }

    private boolean isTciPublicTerminal(String terminalId) throws BusinessException {
        CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
        if (terminalBasicInfoDTO.getTerminalPlatform() == CbbTerminalPlatformEnums.VOI) {
            CloudDesktopDTO desktopDTO = userDesktopMgmtAPI.findByTerminalId(terminalId);
            return Objects.equals(desktopDTO.getIdvTerminalMode(), IdvTerminalModeEnums.PUBLIC.name());
        }
        return false;
    }
}
