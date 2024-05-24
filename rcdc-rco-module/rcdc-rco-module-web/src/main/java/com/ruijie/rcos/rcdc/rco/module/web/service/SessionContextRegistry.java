package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年03月17日
 *
 * @author xiejian
 */
@Service
public class SessionContextRegistry {

    /** logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionContextRegistry.class);

    private Map<UUID, SessionContext> sessionContextMap = new ConcurrentHashMap<>();

    /**
     * 登录时保存session信息
     * @param sessionContext session信息
     */
    public void addSessionContext(SessionContext sessionContext) {
        Assert.notNull(sessionContext, "sessionContext is not null");

        sessionContextMap.put(sessionContext.getUserId(), sessionContext);
    }

    /**
     * 退出时移除session信息
     * @param sessionUserId 用户ID
     */
    public void removeSessionContext(UUID sessionUserId) {
        Assert.notNull(sessionUserId, "sessionUserId is not null");

        sessionContextMap.remove(sessionUserId);
    }

    /**
     * 踢出某个管理员
     * @param sessionUserId 用户ID
     * @return
     */
    public void logoutSessionContext(UUID sessionUserId) {
        Assert.notNull(sessionUserId, "sessionUserId is not null");

        SessionContext sessionContext = sessionContextMap.get(sessionUserId);

        if (sessionContext == null) {
            LOGGER.error("logout error!,[{}] sessionContext is not exist!", sessionUserId);
            return;
        }

        if (sessionContext.isLogin()) {
            sessionContext.logout();
            sessionContextMap.remove(sessionUserId);
        } else {
            LOGGER.warn("sessionUserId [{}] is not login!", sessionUserId);
        }
    }
}
