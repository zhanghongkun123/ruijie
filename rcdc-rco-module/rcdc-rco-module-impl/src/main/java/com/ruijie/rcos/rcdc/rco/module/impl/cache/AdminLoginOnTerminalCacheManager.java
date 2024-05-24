package com.ruijie.rcos.rcdc.rco.module.impl.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Description: 管理员在终端登录，会话缓存管理类
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/18 21:50
 *
 * @author zhangyichi
 */
@Service
public class AdminLoginOnTerminalCacheManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginOnTerminalCacheManager.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UserTerminalMgmtAPI userTerminalMgmtAPI;


    /**
     * 缓存过期时间（分）1周时间 =60*24*7 =10080 +10
     * 在清理缓存的定时任务失效时，避免缓存长期存在
     * 过期时间略大于定时任务中的时间，避免冲突
     */
    private static final Long CACHE_AUTO_EXPIRE_TIME_MINUTE = 60 * 24 * 7L + 10L;

    private static final Cache<UUID, AdminLoginOnTerminalCache> ADMIN_LOGIN_SESSION_CACHE =
            CacheBuilder.newBuilder().expireAfterAccess(CACHE_AUTO_EXPIRE_TIME_MINUTE, TimeUnit.MINUTES).build();

    /**
     * 创建新的会话缓存
     * @param adminDTO 管理员信息
     * @param terminalId 终端ID
     * @return 会话ID
     */
    public synchronized UUID add(IacAdminDTO adminDTO, String terminalId) {
        Assert.notNull(adminDTO, "adminDTO cannot be null!");
        Assert.notNull(adminDTO.getId(), "adminId cannot be null!");
        Assert.hasText(adminDTO.getUserName(), "adminName cannot be blank!");
        Assert.hasText(terminalId, "terminalId cannot be blank!");

        Map.Entry<UUID, AdminLoginOnTerminalCache> sessionCacheEntry = getSessionEntryByTerminalId(terminalId);
        if (sessionCacheEntry != null) {
            // 终端上已存在会话，先删除原有会话
            AdminLoginOnTerminalCache cache = sessionCacheEntry.getValue();
            try {
                TerminalDTO terminalDTO = userTerminalMgmtAPI.getTerminalById(cache.getTerminalId());
                String macAddr = Optional.ofNullable(terminalDTO.getMacAddr()).orElse(terminalDTO.getWirelessMacAddr());
                macAddr = StringUtils.isEmpty(macAddr) ? macAddr : macAddr.toUpperCase();
                LOGGER.info("管理员[{}]已在终端[{}]登录", cache.getAdminName(), macAddr);
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_OVERWRITE_LOG_KEY,
                        cache.getAdminName(), macAddr);
            } catch (BusinessException e) {
                LOGGER.info("记录管理员[{}]已在终端[{}]登录异常:", cache.getAdminName(), cache.getTerminalId(), e);
            }
            ADMIN_LOGIN_SESSION_CACHE.invalidate(sessionCacheEntry.getKey());
        }
        AdminLoginOnTerminalCache cache = new AdminLoginOnTerminalCache(adminDTO, terminalId);
        UUID sessionId = UUID.randomUUID();
        ADMIN_LOGIN_SESSION_CACHE.put(sessionId, cache);
        return sessionId;
    }

    /**
     * 获取所有会话信息
     * @return 所有会话信息
     */
    public Map<UUID, AdminLoginOnTerminalCache> getAll() {
        return ADMIN_LOGIN_SESSION_CACHE.asMap();
    }

    /**
     * 获取会话信息（若存在）
     * @param sessionId 会话ID
     * @return 会话信息（不存在返回null）
     */
    public AdminLoginOnTerminalCache getIfPresent(UUID sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null!");

        AdminLoginOnTerminalCache cache = ADMIN_LOGIN_SESSION_CACHE.getIfPresent(sessionId);
        if (cache != null) {
            // 存在则刷新时间戳
            cache.updateTimeStamp();
        }
        return cache;
    }

    /**
     * 使列目标会话缓存失效
     * @param sessionId 会话ID
     */
    public synchronized void invalidate(UUID sessionId) {
        Assert.notNull(sessionId, "sessionId cannot be null!");

        ADMIN_LOGIN_SESSION_CACHE.invalidate(sessionId);
    }

    /**
     * 使列表中的所有会话缓存失效
     * @param sessionIdList 会话ID列表
     */
    public synchronized void invalidateAllInList(List<UUID> sessionIdList) {
        Assert.notNull(sessionIdList, "esssionIdList cannot be null!");

        ADMIN_LOGIN_SESSION_CACHE.invalidateAll(sessionIdList);
    }

    /**
     * 根据终端ID查找会话记录
     * @param terminalId 终端ID
     * @return 会话记录缓存（不存在返回null）
     */
    private Map.Entry<UUID, AdminLoginOnTerminalCache> getSessionEntryByTerminalId(String terminalId) {
        Set<Map.Entry<UUID, AdminLoginOnTerminalCache>> entrySet = ADMIN_LOGIN_SESSION_CACHE.asMap().entrySet();
        for (Map.Entry<UUID, AdminLoginOnTerminalCache> entry : entrySet) {
            AdminLoginOnTerminalCache cache = entry.getValue();
            if (terminalId.equals(cache.getTerminalId())) {
                return entry;
            }
        }
        // 目标终端不存在会话记录，返回null
        return null;
    }
}
