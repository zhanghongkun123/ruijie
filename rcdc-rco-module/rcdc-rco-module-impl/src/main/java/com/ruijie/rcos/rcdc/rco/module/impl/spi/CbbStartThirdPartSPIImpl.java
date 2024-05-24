package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbStartThirdPartyDeskSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.api.ComputerBusinessAPIImpl;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: 开启第三方桌面
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/25
 *
 * @author zqj
 */
public class CbbStartThirdPartSPIImpl implements CbbStartThirdPartyDeskSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComputerBusinessAPIImpl.class);

    @Autowired
    private ComputerBusinessAPI computerBusinessAPI;

    @Override
    public Boolean wakeComputer(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId can not be null");
        try {
            computerBusinessAPI.wakeUpDesk(deskId);
        } catch (Exception ex) {
            LOGGER.error("唤醒桌面[{}]异常", deskId, ex);
        }

        return Boolean.TRUE;
    }
}
