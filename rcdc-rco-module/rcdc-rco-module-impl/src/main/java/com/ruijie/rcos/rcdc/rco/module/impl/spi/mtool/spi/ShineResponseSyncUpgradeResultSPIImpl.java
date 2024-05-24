package com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineResponseSyncUpgradeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mtool.dto.SyncUpgradeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.dto.MtoolAction;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.12
 *
 * @author linhj
 */
@DispatcherImplemetion(MtoolAction.SYNC_OLD_VERSION_UPGRADE_RESULT)
public class ShineResponseSyncUpgradeResultSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineResponseSyncUpgradeResultSPIImpl.class);

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    // 锁前缀
    private static final String LOCK_PRE = "ShineResponseSyncUpgradeResultSPIImpl_";

    private static final int LOCK_TIMES = 15;

    @Override
    public void dispatch(CbbDispatcherRequest request) {

        Assert.notNull(request, "request must not null");
        Assert.notNull(request.getData(), "request.data must not null");
        Assert.notNull(request.getTerminalId(), "request.terminalId must not null");

        LOGGER.info("Shine 上报旧平台终端 {} 升级结果：{}", request.getTerminalId(), request.getData());
        ShineResponseSyncUpgradeDTO shineResponseSyncUpgradeDTO = JSON.parseObject(request.getData(), ShineResponseSyncUpgradeDTO.class);

        SyncUpgradeDTO syncUpgradeDTO = new SyncUpgradeDTO();
        syncUpgradeDTO.setStatus(SyncUpgradeDTO.SyncUpgradeStatus.STATUS_DEAL);
        syncUpgradeDTO.setShineCode(shineResponseSyncUpgradeDTO.getCode());
        syncUpgradeDTO.setUpdateDate(new Date());

        LockableExecutor.LockableRunnable runnable = () -> {
            // 查询是否已有纪录
            SystemBusinessMappingDTO systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT, request.getTerminalId());
            if (systemBusinessMapping == null) {
                systemBusinessMapping = new SystemBusinessMappingDTO();
                systemBusinessMapping.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
                systemBusinessMapping.setBusinessType(SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT);
                systemBusinessMapping.setSrcId(request.getTerminalId());
            }
            systemBusinessMapping.setContext(JSON.toJSONString(syncUpgradeDTO));
            systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMapping);
        };

        try {
            // 锁定实现
            LockableExecutor.executeWithTryLock(LOCK_PRE + request.getTerminalId(), runnable, LOCK_TIMES);
        } catch (BusinessException ex) {
            LOGGER.error("旧平台升级终端纪录状态错误", ex);
        }
    }
}
