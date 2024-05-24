package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: 在系统升级时 进行5.4 绑定镜像
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/08/28
 *
 * @author linrenjian
 */
@DispatcherImplemetion("SystemUpAutoBindImageNotifySPIImpl")
public class SystemUpAutoBindImageNotifySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemUpAutoBindImageNotifySPIImpl.class);

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private GlobalParameterService globalParameterService;

    /**
     * rco_globalParameter表的key
     */
    private static final String SYSTEM_UP_AUTO_BIND_IMAGE = "system_up_auto_bind_image";

    /**
     * globalParameter表的value
     */
    private static final String CAN_BIND_IMAGE_VALUE = "true";


    /**
     * globalParameter表的value
     */
    private static final String NOT_BIND_IMAGE_VALUE = "false";

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

        LOGGER.info(" 在系统升级时 进行5.4 绑定镜像 ");
        // 查询是否能进行绑定镜像操作
        String value = globalParameterService.findParameter(SYSTEM_UP_AUTO_BIND_IMAGE);
        // 由历史版本系统升级到5.4时候 由于旧系统没有镜像数据权限，新版对于所有管理员都赋值镜像权限
        LOGGER.info("接收到系统升级后所有管理员都赋值镜像权限赋值请求，当前是否允许赋值为[{}]", value);
        if (Objects.equals(CAN_BIND_IMAGE_VALUE, value)) {
            // 当第一次升级上来初始化数据
            adminDataPermissionAPI.initializeAdminDataPermission();
            // 更新global表，变为不需要绑定的状态
            globalParameterService.updateParameter(SYSTEM_UP_AUTO_BIND_IMAGE, NOT_BIND_IMAGE_VALUE);
        }

        return Boolean.TRUE;
    }

}
