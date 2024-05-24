package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.junit.SkyEngineRunner;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/3 09:49
 *
 * @author zhangyichi
 */
@RunWith(SkyEngineRunner.class)
public class AdminLoginOnTerminalCacheManagerTest {

    @Mocked
    private BaseAuditLogAPI auditLogAPI;

    @Injectable
    private UserTerminalMgmtAPI userTerminalMgmtAPI;


    /**
     * 加入缓存
     */
    @Test
    public void testAdd() throws BusinessException {
        UUID adminId = UUID.randomUUID();
        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setId(adminId);
        adminDTO.setUserName("admin");
        String terminalId = "terminalId";

        // 新加入缓存
        AdminLoginOnTerminalCacheManager cacheManager = new AdminLoginOnTerminalCacheManager();
        Deencapsulation.setField(cacheManager, "auditLogAPI", auditLogAPI);
        Deencapsulation.setField(cacheManager, "userTerminalMgmtAPI", userTerminalMgmtAPI);
        UUID sessionId1 = cacheManager.add(adminDTO, terminalId);
        AdminLoginOnTerminalCache cache = cacheManager.getIfPresent(sessionId1);
        Assert.assertNotNull(cache);
        Assert.assertEquals("terminalId", cache.getTerminalId());

        // 同终端重复加入缓存
        AdminLoginOnTerminalCache cache1 = cacheManager.getIfPresent(sessionId1);
        String adminName1 = cache1.getAdminName();
        adminDTO.setUserName("admin2");
        TerminalDTO terminalDTO = new TerminalDTO();
        terminalDTO.setId(terminalId);
        terminalDTO.setMacAddr("macaddr");
        new Expectations() {
            {
                userTerminalMgmtAPI.getTerminalById(terminalId);
                result = terminalDTO;
            }
        };
        UUID sessionId2 = cacheManager.add(adminDTO, terminalId);
        Assert.assertNotEquals(sessionId1, sessionId2);
        AdminLoginOnTerminalCache cache2 = cacheManager.getIfPresent(sessionId2);
        Assert.assertNotEquals(adminName1, cache2.getAdminName());

        // 不同终端重复加入
        UUID sessionId3 = cacheManager.add(adminDTO, "terminalId2");
        AdminLoginOnTerminalCache cache3 = cacheManager.getIfPresent(sessionId3);
        Assert.assertNotNull(cache3);
        Assert.assertEquals("terminalId2", cache3.getTerminalId());
    }

    /**
     * 使列表中的缓存失效
     */
    @Test
    public void testInvalidateAllInList() {
        UUID adminId = UUID.randomUUID();
        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setId(adminId);
        adminDTO.setUserName("admin");

        AdminLoginOnTerminalCacheManager cacheManager = new AdminLoginOnTerminalCacheManager();
        Deencapsulation.setField(cacheManager, "auditLogAPI", auditLogAPI);
        UUID session1 = cacheManager.add(adminDTO, "expired1");
        UUID session2 = cacheManager.add(adminDTO, "expired2");

        Assert.assertNotNull(cacheManager.getIfPresent(session1));
        Assert.assertNotNull(cacheManager.getIfPresent(session2));

        List<UUID> idList = Lists.newArrayList(session1, session2);
        cacheManager.invalidateAllInList(idList);

        Set<UUID> uuidSet = cacheManager.getAll().keySet();
        Assert.assertFalse(uuidSet.contains(session1));
        Assert.assertFalse(uuidSet.contains(session2));
    }

    /**
     * 使单一缓会话失效
     */
    @Test
    public void testInvalidate() {
        UUID adminId = UUID.randomUUID();
        IacAdminDTO adminDTO = new IacAdminDTO();
        adminDTO.setId(adminId);
        adminDTO.setUserName("admin");

        AdminLoginOnTerminalCacheManager cacheManager = new AdminLoginOnTerminalCacheManager();
        Deencapsulation.setField(cacheManager, "auditLogAPI", auditLogAPI);
        UUID session1 = cacheManager.add(adminDTO, "expired1");
        UUID session2 = cacheManager.add(adminDTO, "expired2");

        cacheManager.invalidate(session1);

        Assert.assertNull(cacheManager.getIfPresent(session1));
        Assert.assertNotNull(cacheManager.getIfPresent(session2));
    }
}