package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.sysmanage.module.def.dto.BaseUpgradeDTO;
import com.ruijie.rcos.base.sysmanage.module.def.spi.BaseMaintenanceModeNotifySPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.callback.CbbTerminalCallback;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.Map;
import java.util.UUID;

/**
 * Description: 维护模式通知
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/04/24
 *
 * @author xiaojiaxin
 */
@DispatcherImplemetion("BaseMaintenanceModeNotifySPIImpl")
public class BaseMaintenanceModeNotifySPIImpl implements BaseMaintenanceModeNotifySPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseMaintenanceModeNotifySPIImpl.class);

    @Autowired
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Autowired
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;


    @Override
    public Boolean beforeEnteringMaintenance(String dispatchKey, BaseUpgradeDTO baseUpgradeDTO) throws BusinessException {
        Assert.hasText(dispatchKey, "dispatchKey must not be null or empty");
        Assert.notNull(baseUpgradeDTO, "baseUpgradeDTO must not be null");
        Map<UUID, AdminLoginOnTerminalCache> cacheMap = cacheManager.getAll();

        for (Map.Entry<UUID, AdminLoginOnTerminalCache> map : cacheMap.entrySet()) {
            String terminalId = map.getValue().getTerminalId();
            CbbShineMessageRequest request = CbbShineMessageRequest.create(Constants.ADMIN_LOGOUT, terminalId);
            request.setContent(new JSONObject());
            try {
                messageHandlerAPI.asyncRequest(request, new ExitShineManagerCbbTerminalCallbackImpl());
            } catch (Exception e) {
                LOGGER.error("通知终端[" + terminalId + "]管理员退出登录失败.", e);
            }

        }
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

        // 不需处理，直接响应成功
        return Boolean.TRUE;
    }


    /**
     * 异步消息回调实现类
     */
    private class ExitShineManagerCbbTerminalCallbackImpl implements CbbTerminalCallback {
        @Override
        public void success(String terminalId, CbbShineMessageResponse msg) {
            Assert.notNull(terminalId, "terminalId cannot be null!");
            Assert.notNull(msg, "msg cannot be null!");

            LOGGER.info("通知终端退出管理员登陆成功，terminalId[{}]，信息[{}]", terminalId, msg.toString());
        }

        @Override
        public void timeout(String terminalId) {
            Assert.notNull(terminalId, "terminalId cannot be null!");
            LOGGER.error("通知终端退出管理员登陆超时，terminalId[{}]", terminalId);
        }
    }
}
