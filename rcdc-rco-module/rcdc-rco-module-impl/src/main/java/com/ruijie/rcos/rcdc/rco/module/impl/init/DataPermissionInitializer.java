package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Description: 数据权限相关初始话
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/8/28
 *
 * @author linke
 */
@Service
public class DataPermissionInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataPermissionInitializer.class);

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Override
    public void safeInit() {
        // 初始化云桌面策略数据权限
        initDeskStrategyDataPermission();
    }

    private void initDeskStrategyDataPermission() {
        FindParameterRequest request = new FindParameterRequest(Constants.NEED_COMPENSATE_STRATEGY_DATA_PERMISSION);
        FindParameterResponse response = rcoGlobalParameterAPI.findParameter(request);
        if (Objects.equals(Boolean.TRUE.toString(), response.getValue())) {
            LOGGER.info("开始Ent1.1升级后初始化云桌面策略数据权限");
            try {
                adminDataPermissionAPI.initDeskStrategyAdminDataPermission();
                UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest(Constants.NEED_COMPENSATE_STRATEGY_DATA_PERMISSION,
                        Boolean.FALSE.toString());
                rcoGlobalParameterAPI.updateParameter(updateParameterRequest);
            } catch (Exception e) {
                LOGGER.error("初始化云桌面策略数据权限异常", e);
            }
            return;
        }
        LOGGER.info("数据库查询的值为[{}]，无需进行云桌面策略数据权限补偿", response.getValue());
    }
}
