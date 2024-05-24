package com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.init;

import com.ruijie.rcos.rcdc.rco.module.impl.useruseinfo.service.UserLoginRecordService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/27 17:23
 *
 * @author zdc
 */
@Service
public class UserUseInfoCompensateInit implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserUseInfoCompensateInit.class);

    @Autowired
    private UserLoginRecordService userLoginRecordService;

    @Override
    public void safeInit() {

        LOGGER.info("服务器重启，执行用户信息报表数据补偿任务");
        try {
            userLoginRecordService.compensateFishedRecord();
        } catch (Exception e) {
            LOGGER.error("用户信息报表数据补偿任务失败，失败原因：", e);
        }
    }
}
