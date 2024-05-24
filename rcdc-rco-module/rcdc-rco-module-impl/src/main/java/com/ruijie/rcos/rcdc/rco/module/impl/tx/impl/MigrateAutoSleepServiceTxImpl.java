package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbPowerPlanEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.RcoGlobalParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.MigrateAutoSleepStrategyServiceTx;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * Description: 迁移自动休眠全局配置到VDI桌面策略TX实现类
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/24 10:16
 *
 * @author yxq
 */
@Service
public class MigrateAutoSleepServiceTxImpl implements MigrateAutoSleepStrategyServiceTx {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateAutoSleepServiceTxImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI deskStrategyMgmtAPI;

    @Override
    public void migrateAutoSleepStrategy(CbbPowerPlanEnum powerPlan, Integer powerPlanTime) {
        Assert.notNull(powerPlanTime, "autoSleepTime must not be null");
        Assert.notNull(powerPlan, "powerPlan must not be null");

        LOGGER.info("旧版本休眠计划配置的策略为[{}]，时间为[{}]", powerPlan, powerPlanTime);
        // 修改
        deskStrategyMgmtAPI.updateAllVDIStrategyPowerPlan(powerPlan, powerPlanTime);

        // 修改全局表的配置
        RcoGlobalParameterEntity rcoGlobalParameterEntity = new RcoGlobalParameterEntity();
        Date createTime = new Date();
        rcoGlobalParameterEntity.setParamKey(Constants.HAS_MIGRATE_KEY);
        rcoGlobalParameterEntity.setParamValue(Boolean.TRUE.toString());
        rcoGlobalParameterEntity.setDefaultValue(Boolean.FALSE.toString());
        rcoGlobalParameterEntity.setCreateTime(createTime);
        rcoGlobalParameterEntity.setUpdateTime(createTime);
        LOGGER.info("生成的是否迁移自动休眠策略的信息为[{}]", JSON.toJSONString(rcoGlobalParameterEntity));
        globalParameterService.saveParameter(rcoGlobalParameterEntity);
    }
}
