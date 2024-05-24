package com.ruijie.rcos.rcdc.rco.module.impl.init;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserMgmtAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;


/**
 * Description: 用户账号失效任务继续执行
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-08-08
 *
 * @author liuwang1
 */
@Service
public class AccountInvalidTaskInit implements SafetySingletonInitializer {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInvalidTaskInit.class);

    @Autowired
    private IacUserMgmtAPI cbbUserAPI;

    private static final String BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN = "batch_account_invalid_init_thread_main";

    @Override
    public void safeInit() {
        ThreadExecutors.execute(BATCH_CREATE_VDI_DESKTOP_INIT_THREAD_MAIN, () -> {

            try {
                cbbUserAPI.accountInvalidInit();
            } catch (BusinessException e) {
                LOGGER.error("accountInvalidInit fail",e);
            }

        });

    }

}
