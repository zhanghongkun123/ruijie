package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.EnableFullSystemDiskAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.globalparameter.GlobalParameterModifiedEventContext;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 9:56
 *
 * @author yxq
 */
public class EnableFullSystemDiskAPIImpl implements EnableFullSystemDiskAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnableFullSystemDiskAPIImpl.class);

    @Autowired
    private GlobalParameterService globalParameterService;

    @Autowired
    private GlobalParameterModifiedEventContext globalParameterModifiedEventContext;

    @Override
    public Boolean getEnableFullSystemDisk() {
        return Boolean.parseBoolean(globalParameterService.findParameter(Constants.ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY));
    }

    @Override
    public void editEnableFullSystemDisk(Boolean enableFullSystemDisk) {
        Assert.notNull(enableFullSystemDisk, "enableFullSystemDisk must not be null");

        LOGGER.info("修改自动扩容全局配置为[{}]", enableFullSystemDisk);
        globalParameterService.updateParameter(Constants.ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY, enableFullSystemDisk.toString());
        //通知策略
        globalParameterModifiedEventContext.notifyEvent(Constants.ENABLE_FULL_SYSTEM_DISK_GLOBAL_STRATEGY);
    }
}
