package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoTrialLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesksoftUseConfigNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.util.Assert;

/**
 * Description: 在系统升级时 判断是否需要进行VOI 的临时授权 如果用户没有加入体验计划 则关闭CMC 配置
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/24
 *
 * @author xiaojiaxin
 */
@DispatcherImplemetion("SystemUpAutoVOINotifySPIImpl")
public class SystemUpAutoVOINotifySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUpAutoVOINotifySPIImpl.class);

    @Autowired
    private AutoTrialLicenseAPI autoTrialLicenseAPI;

    @Autowired
    private DesksoftUseConfigNotifyAPI desksoftUseConfigNotifyAPI;

    @Autowired
    private ConfigurationWizardAPI configurationWizardAPI;

    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {

        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

    @Override
    public Boolean afterUnderMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) {

        // 不需处理，直接响应成功
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

        LOGGER.info("进行VOI 临时授权的升级处理 ");
        //进行VOI 临时授权的升级处理
        autoTrialLicenseAPI.systemUpVoiAutoTrialLicense();
        // 用户体验计划如果原先是关闭的 则需要关闭CMC 的开关
        GetConfigurationWizardResponse configurationWizard = configurationWizardAPI.getConfigurationWizardResponse();
        // 如果走过配置向导的升级场景show 为false 进入这个方法定是升级场景
        // 如果用户没有加入体验计划 则关闭CMC 配置 不需要通知桌面 避免长时间等待
        if (!configurationWizard.getShow() && !configurationWizard.getIsJoinUserExperiencePlan()) {
            desksoftUseConfigNotifyAPI.updateUserConfigNotNotifyDesk(Boolean.FALSE.toString());
        }
        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }

}
