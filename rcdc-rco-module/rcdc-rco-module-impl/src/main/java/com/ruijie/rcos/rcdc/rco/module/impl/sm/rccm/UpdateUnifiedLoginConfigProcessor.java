package com.ruijie.rcos.rcdc.rco.module.impl.sm.rccm;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.impl.dto.RccmServerConfigSmRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageSmService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.ProcessResult;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle.StateProcessContext;

/**
 * Description: 更新RCenter统一登录配置
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月11日
 *
 * @author jarman
 */
@Service
public class UpdateUnifiedLoginConfigProcessor implements StateTaskHandle.StateProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateUnifiedLoginConfigProcessor.class);

    /** 最大重试时间60分钟 */
    private static final int MAX_RETRY_MILLS = 60 * 60 * 1000;

    @Autowired
    private RccmManageSmService rccmManageSmService;


    @Override
    public ProcessResult doProcess(StateProcessContext stateProcessContext) throws Exception {
        Assert.notNull(stateProcessContext, "stateProcessContext is not null");
        RccmServerConfigSmRequest configSmRequest =
                stateProcessContext.get(UpdateUnifiedLoginContextKey.UPDATE_UNIFIED_LOGIN_REQUEST, RccmServerConfigSmRequest.class);

        Boolean hasUnifiedLogin = configSmRequest.getHasUnifiedLogin();
        Boolean enableAssistAuth = configSmRequest.getEnableAssistAuth();

        rccmManageSmService.updateUnifiedLogin(hasUnifiedLogin, enableAssistAuth);

        return ProcessResult.next();
    }

    @Override
    public ProcessResult doProcessExceptionTranslator(StateProcessContext context, Exception ex) throws Exception {
        Assert.notNull(context, "context is not null");
        Assert.notNull(ex, "ex is not null");
        LOGGER.error("更新RCenter统一登录配置失败，重试", ex);
        if (context.getRetryTotalMills() > MAX_RETRY_MILLS) {
            LOGGER.error("更新RCenter统一登录配置超过最大重试时间：{}", MAX_RETRY_MILLS);
            throw ex;
        }

        return StateTaskHandle.ProcessResult.retry(TimeUnit.SECONDS.toMillis(5));
    }
}
