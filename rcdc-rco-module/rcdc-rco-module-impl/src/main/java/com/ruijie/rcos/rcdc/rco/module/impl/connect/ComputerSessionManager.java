package com.ruijie.rcos.rcdc.rco.module.impl.connect;

import com.ruijie.rcos.rcdc.rco.module.def.utils.ComputerSessionUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.tcp.session.Session;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Description: Computer会话管理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/29
 *
 * @author zqj
 */
@Service
public class ComputerSessionManager {

    private static Logger LOGGER = LoggerFactory.getLogger(ComputerSessionManager.class);

    /**
     * key 为alias, value为Session
     */
    protected static final Map<String, Session> COMPUTER_SESSION_ALIAS_MAP = new ConcurrentHashMap<>();

    /**
     * key 为sessionId,value为alias
     */
    protected static final Map<String, String> COMPUTER_SESSIONID_ALIAS_MAPPING_MAP = new ConcurrentHashMap<>();

    private static final int BIND_SESSION_TIMEOUT_MINUTE = 5;

    /**
     * 将computer与对应的会话绑定
     * @param computerId computerId
     * @param session 会话
     * @throws BusinessException 业务异常
     */
    public void bindSession(UUID computerId, Session session) throws BusinessException {
        Assert.notNull(computerId, "request can not be null");
        Assert.notNull(session, "session can not be null");

        String alias = ComputerSessionUtil.getAliasByComputerId(computerId.toString());
        LockableExecutor.executeWithTryLock(alias, () -> {
            LOGGER.info("绑定会话，alias：{}，sessionId：{}", alias, session.getId());

            session.setSessionAlias(alias);
            COMPUTER_SESSION_ALIAS_MAP.put(alias, session);
            COMPUTER_SESSIONID_ALIAS_MAPPING_MAP.put(session.getId(), alias);
        }, BIND_SESSION_TIMEOUT_MINUTE);

    }

    /**
     * 移除会话
     * @param sessionId sessionId
     * @return 是否移除成功
     */
    public boolean removeSession(String sessionId) {
        Assert.hasText(sessionId, "sessionId can not be blank");

        String alias = COMPUTER_SESSIONID_ALIAS_MAPPING_MAP.remove(sessionId);
        if (StringUtils.isEmpty(alias)) {
            LOGGER.info("会话[{}]未曾绑定alias，无须移除", sessionId);
            return false;
        }

        Session currentSession = COMPUTER_SESSION_ALIAS_MAP.get(alias);

        if (currentSession == null) {
            LOGGER.info("终端[{}]已完成离线处理", alias);
            return false;
        }
        if (!sessionId.equals(currentSession.getId())) {
            LOGGER.info("computer[{}]已绑定其它session", alias);
            return false;
        }

        COMPUTER_SESSION_ALIAS_MAP.remove(alias);
        return true;
    }

    /**
     * 通过sessionId获取所属computer的虚机Id
     * @param sessionId sessionId
     * @return 虚机Id
     */
    public String getComputerIdBySessionId(String sessionId) {
        Assert.hasText(sessionId, "sessionId can not be blank");

        String alias = COMPUTER_SESSIONID_ALIAS_MAPPING_MAP.get(sessionId);

        return StringUtils.hasText(alias) ? ComputerSessionUtil.getComputerIdFromAlias(alias) : null;
    }


    /**
     * 通过alias获取所属computer的Session
     * @param alias alias
     * @return Session
     */
    public Session getSessionByAlias(String alias) {
        Assert.hasText(alias, "alias can not be blank");

        Session session = COMPUTER_SESSION_ALIAS_MAP.get(alias);

        return ObjectUtils.isEmpty(session) ?  null : session;
    }
}
