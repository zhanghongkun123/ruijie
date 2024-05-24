package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbPowerPlanEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.MigrateAutoSleepStrategyServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 从低版本升级到6.0R3，将全局配置中的休眠时间迁移到VDI桌面策略中
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/18 19:57
 *
 * @author yxq
 */
@DispatcherImplemetion("MigrateAutoSleepConfig2StrategySPIImpl")
public class MigrateAutoSleepConfig2StrategySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateAutoSleepConfig2StrategySPIImpl.class);

    /**
     * 5.3R1S2T1中休眠策略在全局表中的key
     */
    private static final String GLOBAL_POWER_PLAN = "global_power_plan";

    /**
     * 5.3R1S2T1休眠策略JSON配置里面的KEY
     */
    private static final String POWER_PLAN_TYPE_KEY = "powerPlan";

    private static final String POWER_PLAN_TIME_KEY = "time";

    /**
     * 新版本支持自动关机的最小时间
     */
    private static final Integer POWER_PLAN_MIN_SHUTDOWN_TIME = 15;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private GlobalParameterAPI skGlobalParameterAPI;

    @Autowired
    private MigrateAutoSleepStrategyServiceTx migrateAutoSleepStrategyServiceTx;

    @Override
    public Boolean beforeEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String s, BaseUpgradeDTO baseUpgradeDTO) {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterMaintenanceEnd(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        if (baseUpgradeDTO.getType() == BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为热补丁升级，无需执行");
            return Boolean.TRUE;
        }

        String hasMigrateAutoSleepConfig = globalParameterService.findParameter(Constants.HAS_MIGRATE_KEY);
        LOGGER.info("全局表查询是否需要迁移自动休眠的值为[{}]", hasMigrateAutoSleepConfig);
        // 如果已经有值，并且为true，则不需要迁移
        if (Boolean.parseBoolean(hasMigrateAutoSleepConfig)) {
            LOGGER.info("不需要进行迁移操作");
            return Boolean.TRUE;
        }

        String powerPlanValue = null;
        try {
            powerPlanValue = skGlobalParameterAPI.findParameter(GLOBAL_POWER_PLAN);
        } catch (Exception e) {
            LOGGER.info("sk全局表不存在电源计划策略，证明不是从5.3R1S2T1升级");
        }

        // 如果存在对应值，证明该版本从5.3R1S2T1升级上来，则以这个策略为准
        if (StringUtils.isNotEmpty(powerPlanValue)) {
            LOGGER.info("从5.3R1S2T1迁移休眠策略");
            // 获取配置
            JSONObject jsonObject = JSON.parseObject(powerPlanValue);
            CbbPowerPlanEnum powerPlan = CbbPowerPlanEnum.valueOf(jsonObject.getString(POWER_PLAN_TYPE_KEY));
            Integer powerPlanTime = jsonObject.getInteger(POWER_PLAN_TIME_KEY);
            // 低版本支持5、10分钟关机，但是新的版本不支持，所以低版本为5、10分钟关机的时候，升级上来默认设置为15分钟关机
            if (powerPlan == CbbPowerPlanEnum.SHUTDOWN && powerPlanTime > 0 && powerPlanTime <= POWER_PLAN_MIN_SHUTDOWN_TIME) {
                powerPlanTime = POWER_PLAN_MIN_SHUTDOWN_TIME;
            }

            // 迁移
            migrateAutoSleepStrategyServiceTx.migrateAutoSleepStrategy(powerPlan, powerPlanTime);
        } else {
            LOGGER.info("从通用版本迁移休眠策略");
            // 不存在则则证明为通用版本升级上来，原本只支持设置休眠，只需要查询休眠的时间
            Integer autoSleepTime = cbbGlobalStrategyMgmtAPI.getAutoSleepStrategy();
            // 迁移
            migrateAutoSleepStrategyServiceTx.migrateAutoSleepStrategy(CbbPowerPlanEnum.SLEEP, autoSleepTime);
        }

        return Boolean.TRUE;
    }
}
