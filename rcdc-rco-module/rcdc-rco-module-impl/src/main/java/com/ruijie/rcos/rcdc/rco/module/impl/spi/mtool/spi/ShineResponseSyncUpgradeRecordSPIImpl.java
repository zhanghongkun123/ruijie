package com.ruijie.rcos.rcdc.rco.module.impl.spi.mtool.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.mtool.dto.SyncUpgradeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.impl.message.computer.BusinessAction;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.CbbTerminalEventNoticeSPI;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbNoticeRequest;
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
@DispatcherImplemetion(BusinessAction.ONLINE)
public class ShineResponseSyncUpgradeRecordSPIImpl implements CbbTerminalEventNoticeSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShineResponseSyncUpgradeRecordSPIImpl.class);

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    private static final String LOCK_PRE = "ShineResponseSyncUpgradeRecordSPIImpl_";

    private static final int LOCK_TIMES = 15;

    @Override
    public void notify(CbbNoticeRequest request) {

        Assert.notNull(request, "request can not be null");
        Assert.notNull(request.getTerminalBasicInfo(), "terminalBaseInfo can not be null");

        SystemBusinessMappingDTO systemBusinessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL, request.getTerminalBasicInfo().getTerminalId());
        if (systemBusinessMapping == null) {
            return;
        }

        LOGGER.info("终端 [{}] 属于旧平台升级终端，需要记录状态", request.getTerminalBasicInfo().getTerminalId());

        try {
            // 线程实现
            LockableExecutor.LockableRunnable runnable = () -> {
                SystemBusinessMappingDTO businessMapping = systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                        SyncUpgradeConsts.BUSINESS_TYPE_TERMINAL_STAT, request.getTerminalBasicInfo().getTerminalId());
                if (businessMapping == null) {
                    return;
                }
                SyncUpgradeDTO syncUpgradeDTO = JSON.parseObject(businessMapping.getContext(), SyncUpgradeDTO.class);
                if (syncUpgradeDTO.getStatus() == SyncUpgradeDTO.SyncUpgradeStatus.STATUS_IMPORT && syncUpgradeDTO.getShineCode() == 0) {
                    syncUpgradeDTO.setStatus(SyncUpgradeDTO.SyncUpgradeStatus.STATUS_INIT);
                    syncUpgradeDTO.setShineCode(0);
                    syncUpgradeDTO.setUpdateDate(new Date());

                    businessMapping.setContext(JSON.toJSONString(syncUpgradeDTO));
                    systemBusinessMappingAPI.saveSystemBusinessMapping(businessMapping);
                    LOGGER.info("4.x 升级 5.x 终端 [{}] 开始接入完成", request.getTerminalBasicInfo().getTerminalId());
                } else {
                    LOGGER.info("4.x 升级 5.x 终端 [{}] 状态不符合或已上线", request.getTerminalBasicInfo().getTerminalId());
                }
            };

            // 锁定实现
            LockableExecutor.executeWithTryLock(LOCK_PRE + request.getTerminalBasicInfo().getTerminalId(), runnable, LOCK_TIMES);

        } catch (BusinessException ex) {
            LOGGER.error("旧平台升级终端纪录状态错误", ex);
        }
    }
}
