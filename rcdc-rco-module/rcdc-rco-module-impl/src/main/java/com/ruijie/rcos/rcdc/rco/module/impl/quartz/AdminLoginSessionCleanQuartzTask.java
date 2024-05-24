package com.ruijie.rcos.rcdc.rco.module.impl.quartz;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.api.CbbTranspondMessageHandlerAPI;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbShineMessageRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.Constants;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCache;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.AdminLoginOnTerminalCacheManager;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 管理员在终端登录，清理过期会话
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/28 17:55
 *
 * @author zhangyichi
 */
@Service
public class AdminLoginSessionCleanQuartzTask implements SafetySingletonInitializer, Runnable  {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminLoginSessionCleanQuartzTask.class);

    @Autowired
    private AdminLoginOnTerminalCacheManager cacheManager;

    @Autowired
    private CbbTranspondMessageHandlerAPI cbbTranspondMessageHandlerAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;


    /**
     * 会话过期时间（分） 1周时间 =60*24*7 =10080
     */
    private static final Integer SESSION_EXPIRE_TIME = 60 * 24 * 7;

    @Override
    public void safeInit() {
        String cronExpression = "* 0/5 * * * ? ";
        try {
            ThreadExecutors.scheduleWithCron(this.getClass().getSimpleName(), this, cronExpression);
        } catch (ParseException e) {
            throw new RuntimeException("定时任务[" + this.getClass() + "]cron表达式[" + cronExpression + "]解析异常", e);
        }
    }

    @Override
    public void run() {
        cleanExpiredSession();
    }

    private void cleanExpiredSession() {
        List<Map.Entry<UUID, AdminLoginOnTerminalCache>> expiredSessionList = getExpiredSessionList();

        if (!CollectionUtils.isEmpty(expiredSessionList)) {
            // 使会话缓存失效
            List<UUID> sessionIdList = expiredSessionList.stream().map(Map.Entry::getKey).collect(Collectors.toList());
            cacheManager.invalidateAllInList(sessionIdList);

            // 通知对应终端
            List<String> terminalIdList = expiredSessionList.stream().
                    map(entry -> entry.getValue().getTerminalId()).collect(Collectors.toList());
            informTerminal(terminalIdList);
        }
    }

    private List<Map.Entry<UUID, AdminLoginOnTerminalCache>> getExpiredSessionList() {
        List<Map.Entry<UUID, AdminLoginOnTerminalCache>> expiredSessionList = Lists.newArrayList();

        Map<UUID, AdminLoginOnTerminalCache> cacheMap = cacheManager.getAll();
        Set<Map.Entry<UUID, AdminLoginOnTerminalCache>> cacheEntrySet = cacheMap.entrySet();
        for (Map.Entry<UUID, AdminLoginOnTerminalCache> cacheEntry : cacheEntrySet) {
            AdminLoginOnTerminalCache cache = cacheEntry.getValue();
            if (LocalDateTime.now().minusMinutes(SESSION_EXPIRE_TIME).isAfter(cache.getTimestamp())) {
                String terminalMac = cache.getTerminalId();
                try {
                    terminalMac = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(cache.getTerminalId()).getUpperMacAddrOrTerminalId();
                } catch (BusinessException e) {
                    LOGGER.error("查询终端信息异常:", e);
                }
                LOGGER.info("管理员[{}]在终端[{}]登录会话已过期", cache.getAdminName(), terminalMac);
                auditLogAPI.recordLog(BusinessKey.RCDC_RCO_ADMIN_LOGIN_ON_TERMINAL_TIME_OUT_LOG_KEY,
                        cache.getAdminName(), terminalMac);
                expiredSessionList.add(cacheEntry);
            }
        }
        return expiredSessionList;
    }

    private void informTerminal(List<String> expiredTerminalIdList) {
        for (String terminalId : expiredTerminalIdList) {
            try {
                CbbShineMessageRequest request  = CbbShineMessageRequest.create(Constants.ADMIN_SESSION_TIME_OUT, terminalId);
                cbbTranspondMessageHandlerAPI.request(request);
            } catch (Exception e) {
                LOGGER.error("send admin login session time out message to terminal failed, terminalId[" + terminalId + "]", e);
            }
        }
    }
}
