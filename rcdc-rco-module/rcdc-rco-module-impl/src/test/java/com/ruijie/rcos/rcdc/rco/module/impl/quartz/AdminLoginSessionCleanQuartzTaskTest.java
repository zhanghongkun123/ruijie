package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Maps;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.ViewTerminalDAO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/3 10:48
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AdminLoginSessionCleanQuartzTaskTest {

    @Tested
    private AdminLoginSessionCleanQuartzTask quartzTask;

    @Injectable
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Injectable
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Injectable
    private BaseAuditLogAPI auditLogAPI;

    @Injectable
    private ViewTerminalDAO viewTerminalDAO;

    @Injectable
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private String normalTerminalId = "normal";

    private String expiredOnlineTerminalId = "expiredOnline";

    private String expiredOfflineTerminalId = "expiredOffline";

    /**
     * 清理缓存，正常流程
     * @throws BusinessException 异常
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testCleanExpiredSession() throws BusinessException, IOException, InterruptedException {
        UUID normalCacheId = UUID.randomUUID();
        AdminLoginOnTerminalCache normalCache = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(normalCache, "terminalId", normalTerminalId);
        Deencapsulation.setField(normalCache, "timestamp", LocalDateTime.now());

        UUID expiredCacheId1 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache1 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache1, "terminalId", expiredOnlineTerminalId);
        Deencapsulation.setField(expiredCache1, "timestamp", LocalDateTime.now().minusHours(2L));

        UUID expiredCacheId2 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache2 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache2, "terminalId", expiredOfflineTerminalId);
        Deencapsulation.setField(expiredCache2, "timestamp", LocalDateTime.now().minusHours(2L));

        Map<UUID, AdminLoginOnTerminalCache> cacheMap = Maps.newHashMap();
        cacheMap.put(normalCacheId, normalCache);
        cacheMap.put(expiredCacheId1, expiredCache1);
        cacheMap.put(expiredCacheId2, expiredCache2);

        new Expectations() {
            {
                cacheManager.getAll();
                result = cacheMap;
            }
        };

        quartzTask.run();

        new Verifications() {
            {
                List<UUID> idList;
                cacheManager.invalidateAllInList(idList = withCapture());
                Assert.assertEquals(2, idList.size());
                cbbTranspondMessageHandlerAPI.request((CbbShineMessageRequest) any);
                times = 2;
            }
        };
    }

    /**
     * 清理缓存，无会话过期
     * @throws BusinessException 异常
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testCleanExpiredSessionNoSessionExpired() throws BusinessException, IOException, InterruptedException {
        UUID normalCacheId = UUID.randomUUID();
        AdminLoginOnTerminalCache normalCache = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(normalCache, "terminalId", normalTerminalId);
        Deencapsulation.setField(normalCache, "timestamp", LocalDateTime.now());

        UUID expiredCacheId1 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache1 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache1, "terminalId", expiredOnlineTerminalId);
        Deencapsulation.setField(expiredCache1, "timestamp", LocalDateTime.now());

        UUID expiredCacheId2 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache2 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache2, "terminalId", expiredOfflineTerminalId);
        Deencapsulation.setField(expiredCache2, "timestamp", LocalDateTime.now());

        Map<UUID, AdminLoginOnTerminalCache> cacheMap = Maps.newHashMap();
        cacheMap.put(normalCacheId, normalCache);
        cacheMap.put(expiredCacheId1, expiredCache1);
        cacheMap.put(expiredCacheId2, expiredCache2);

        new Expectations() {
            {
                cacheManager.getAll();
                result = cacheMap;
            }
        };

        quartzTask.run();

        new Verifications() {
            {
                cacheManager.invalidateAllInList((List<UUID>) any);
                times = 0;
                cbbTranspondMessageHandlerAPI.request((CbbShineMessageRequest) any);
                times = 0;
            }
        };
    }

    /**
     * 清理缓存，通知终端异常
     * @throws BusinessException 异常
     * @throws IOException 异常
     * @throws InterruptedException 异常
     */
    @Test
    public void testCleanExpiredSessionException() throws BusinessException, IOException, InterruptedException {
        UUID normalCacheId = UUID.randomUUID();
        AdminLoginOnTerminalCache normalCache = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(normalCache, "terminalId", normalTerminalId);
        Deencapsulation.setField(normalCache, "timestamp", LocalDateTime.now());

        UUID expiredCacheId1 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache1 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache1, "terminalId", expiredOnlineTerminalId);
        Deencapsulation.setField(expiredCache1, "timestamp", LocalDateTime.now().minusHours(2L));

        UUID expiredCacheId2 = UUID.randomUUID();
        AdminLoginOnTerminalCache expiredCache2 = new AdminLoginOnTerminalCache();
        Deencapsulation.setField(expiredCache2, "terminalId", expiredOfflineTerminalId);
        Deencapsulation.setField(expiredCache2, "timestamp", LocalDateTime.now().minusHours(2L));

        Map<UUID, AdminLoginOnTerminalCache> cacheMap = Maps.newHashMap();
        cacheMap.put(normalCacheId, normalCache);
        cacheMap.put(expiredCacheId1, expiredCache1);
        cacheMap.put(expiredCacheId2, expiredCache2);

        new Expectations() {
            {
                cacheManager.getAll();
                result = cacheMap;
                cbbTranspondMessageHandlerAPI.request((CbbShineMessageRequest) any);
                result = new Exception("xxx");
            }
        };

        quartzTask.run();

        new Verifications() {
            {
                List<UUID> idList;
                cacheManager.invalidateAllInList(idList = withCapture());
                Assert.assertEquals(2, idList.size());
                cbbTranspondMessageHandlerAPI.request((CbbShineMessageRequest) any);
                times = 2;
            }
        };
    }

    /**
     * 初始化异常
     * @throws ParseException 异常
     */
    @Test
    public void testSafeInit() throws ParseException {
        new Expectations(ThreadExecutors.class) {
            {
                ThreadExecutors.scheduleWithCron(anyString, (Runnable) any, anyString);
                result = new ParseException("", 0);
            }
        };
        try {
            quartzTask.safeInit();
            Assert.fail();
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "定时任务[" + quartzTask.getClass() + "]cron表达式[* 0/5 * * * ? ]解析异常");
        }

        new Verifications() {
            {
                ThreadExecutors.scheduleWithCron(anyString, (Runnable) any, anyString);
                times = 1;
            }
        };
    }
}