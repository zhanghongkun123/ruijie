package com.ruijie.rcos.rcdc.rco.module.impl.hotupdate.spi;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.ruijie.rcos.base.sysmanage.module.def.api.SambaServiceAPI;
import com.ruijie.rcos.base.sysmanage.module.def.dto.SambaConfigDTO;
import com.ruijie.rcos.base.sysmanage.module.def.enums.SambaMountState;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseUpgradeNotifySPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbInnerDriverUpdateMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.hotupdate.constants.HotUpdateConstant;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;


/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/27 14:04
 * fixme BaseUpgradeNotifySPI需要base组件迁移完T版本后，更新版本号支持该SPI
 *
 * @author coderLee23
 */
@DispatcherImplemetion("innerDriverUpgradeNotifySPIImpl")
public class InnerDriverUpgradeNotifySPIImpl implements BaseUpgradeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(InnerDriverUpgradeNotifySPIImpl.class);

    private static final Integer RETRY_COUNT = 60;

    private static final String SAMBA_SHARE_NAME = "iso";

    private static final String TASK_NAME = "hot_upgrade_inner_driver";

    private static final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    @Autowired
    private CbbInnerDriverUpdateMgmtAPI cbbInnerDriverUpdateMgmtAPI;

    @Autowired
    private SambaServiceAPI sambaServiceAPI;



    /**
     *
     * @param dispatchKey dispatchKey
     * @param upgradeDTO upgradeDTO
     * @return Boolean
     * @throws BusinessException BusinessException
     */
    //    @Override
    public Boolean postUpgrade(String dispatchKey, BaseUpgradeDTO upgradeDTO) throws BusinessException {
        Assert.notNull(upgradeDTO, "upgradeDTO must not be null");
        if (upgradeDTO.getType() != BaseUpgradeDTO.UpgradeType.ONLINE) {
            LOGGER.info("当前为冷补丁升级，无需执行");
            return true;
        }

        if (!Files.exists(Paths.get(HotUpdateConstant.INNER_DRIVER_UPDATE_FLAG))) {
            LOGGER.info("当前为热补丁升级，无更新内置驱动包，不执行内置驱动初始化操作");
            return true;
        }

        return true;
    }


    private boolean waitSambaMount(String sambaShareName) {
        int retryCount = RETRY_COUNT;
        while (retryCount-- > 0) {
            SambaConfigDTO sambaConfigDTO;
            try {
                sambaConfigDTO = sambaServiceAPI.getSambaConfig(sambaShareName);
                if (sambaConfigDTO.getState() == SambaMountState.MOUNTED) {
                    return true;
                }
                LOGGER.info("内置驱动，当前samba未挂载，等待3s重试");
            } catch (BusinessException e) {
                LOGGER.info("samba获取配置失败", e);
            }

            waitSeconds();
        }
        return false;
    }

    private void waitSeconds() {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
