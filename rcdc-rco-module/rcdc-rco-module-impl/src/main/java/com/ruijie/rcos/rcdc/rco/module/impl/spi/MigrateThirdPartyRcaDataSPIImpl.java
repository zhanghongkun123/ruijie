package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.rca.module.def.spi.MigrateThirdPartyRcaDataSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description: 旧版本升级到企金2.0,云应用策略需要迁移到新表后,再补偿管理员数据权限
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/12
 *
 * @author WuShengQiang
 */
public class MigrateThirdPartyRcaDataSPIImpl implements MigrateThirdPartyRcaDataSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(MigrateThirdPartyRcaDataSPIImpl.class);

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Override
    public void dataMigrationFinished() {
        try {
            LOGGER.info("云应用数据迁移完成,开始补偿云应用及外设策略数据权限");
            adminDataPermissionAPI.initRcaStrategyAdminDataPermission();
        } catch (Exception e) {
            LOGGER.error("补偿云应用及外设策略数据权限异常", e);
        }
    }
}
