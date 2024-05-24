package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbResponseShineMessage;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/14 09:27
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AdminLogoutHandlerSPIImplTest {

    @Tested
    private AdminLogoutHandlerSPIImpl adminLogoutHandlerSPI;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private CbbTranspondMessageHandlerAPI messageHandlerAPI;

    @Injectable
    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    @Injectable
    private BaseAuditLogAPI auditLogAPI;


    /**
     * 管理员登出，会话已失效
     */
    @Test
    public void testDispatchSessionNotExist() {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminSessionId\":\"d4f58080-4006-4818-9d12-36420caa7b43\"}");

        new Expectations() {
            {
                cacheManager.getIfPresent((UUID) any);
                result = null;
            }
        };

        adminLogoutHandlerSPI.dispatch(request);

        new Verifications() {
            {
                cacheManager.invalidate((UUID) any);
                times = 0;

                CbbResponseShineMessage shineMessage;
                messageHandlerAPI.response(shineMessage = withCapture());
                Assert.assertEquals(0, shineMessage.getCode().intValue());
            }
        };
    }

    /**
     * 管理员登出，异常
     */
    @Test
    public void testDispatchException() {
        CbbDispatcherRequest request = new CbbDispatcherRequest();
        request.setData("{\"adminSessionId\":\"d4f58080-4006-4818-9d12-36420caa7b43\"}");
        UUID adminSessionId = UUID.fromString("d4f58080-4006-4818-9d12-36420caa7b43");
        AdminLoginOnTerminalCache sessionCache = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(sessionCache, "adminId", adminSessionId);

        new Expectations() {
            {
                cacheManager.getIfPresent((UUID) any);
                result = new Exception("xxx");
            }
        };

        adminLogoutHandlerSPI.dispatch(request);

        new Verifications() {
            {
                cacheManager.invalidate((UUID) any);
                times = 0;

                CbbResponseShineMessage shineMessage;
                messageHandlerAPI.response(shineMessage = withCapture());
                Assert.assertEquals(-1, shineMessage.getCode().intValue());
            }
        };
    }
}