package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.UserDesktopDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.UserDesktopEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.UserDesktopService;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalConfigChangeSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.compress.utils.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Description: CbbTerminalConfigChangeSPIImpl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年10月20日
 *
 * @author clone
 */
public class CbbTerminalConfigChangeSPIImpl implements CbbTerminalConfigChangeSPI {
    private static final Logger LOGGER = LoggerFactory.getLogger(CbbTerminalConfigChangeSPIImpl.class);

    @Autowired
    private UserDesktopDAO userDesktopDAO;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    /**
     * 判断是否有桌面在运行
     * @param terminalId 终端id
     * @return boolean
     */
    @Override
    public Boolean checkDesktopIsRunning(String terminalId) {
        Assert.notNull(terminalId, "terminalId can not be null");

        try {
            UserDesktopEntity userEntity = userDesktopDAO.findUserDesktopEntityByTerminalId(terminalId);
            if (Objects.isNull(userEntity)) {
                return false;
            }
            CbbDeskDTO cbbDesk = cbbDeskMgmtAPI.getDeskById(userEntity.getCbbDesktopId());
            if (Objects.isNull(cbbDesk)) {
                return false;
            }
            return cbbDesk.getDeskState() == CbbCloudDeskState.RUNNING;
        } catch (BusinessException e) {
            LOGGER.error("检查桌面是否允许运行失败", e);
            return false;
        }
    }

    /**
     * 判断是否是mini服务器
     * @return boolean
     */
    @Override
    public Boolean checkIsMiniServer() {
        return serverModelAPI.isMiniModel();
    }

    /**
     * 判断是否是idv服务器
     * @return boolean
     */
    @Override
    public Boolean checkIsIdvServer() {
        return serverModelAPI.isIdvModel();
    }
}
