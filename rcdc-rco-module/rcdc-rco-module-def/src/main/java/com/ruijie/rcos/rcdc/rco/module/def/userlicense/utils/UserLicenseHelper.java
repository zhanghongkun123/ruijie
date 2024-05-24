package com.ruijie.rcos.rcdc.rco.module.def.userlicense.utils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.userlicense.TerminalTypeEnum;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 用户并发授权工具类
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月6日
 *
 * @author lihengjing
 */
public class UserLicenseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLicenseHelper.class);

    public static final Map<UUID, ReentrantLock> USER_LOCK_MAP = new ConcurrentHashMap<>();

    private static final Map<String, ReentrantLock> TERMINAL_LOCK_MAP = new ConcurrentHashMap<String, ReentrantLock>();

    private static final Map<String, Long> TERMINAL_REPORT_TIME_MAP = new ConcurrentHashMap<String, Long>();

    private static final long TERMINAL_REPORT_TIMEOUT_SECONDS = 3 * 60L;

    private static final long TERMINAL_REPORT_TIMEOUT_DELAY_SECONDS = 5L;

    /**
     * 更新终端上报时间
     * 
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     */
    public static void updateTerminalReportTime(TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        String terminalKey = getTerminalKey(terminalType, terminalId);
        ReentrantLock lock = getTerminalLockAlways(terminalKey);
        lock.lock();
        try {
            // 进行需要同步的操作
            TERMINAL_REPORT_TIME_MAP.put(terminalKey, System.currentTimeMillis());
        } finally {
            lock.unlock(); // 确保在 finally 块中释放锁
        }
    }

    /**
     * 终端上报时间是否超时
     * 
     * @param terminalType 终端类型
     * @param terminalId 终端ID
     * @return 是否超时
     */
    public static boolean isTerminalReportTimeout(TerminalTypeEnum terminalType, String terminalId) {
        Assert.notNull(terminalType, "terminalType can not be null");
        Assert.notNull(terminalId, "terminalId can not be null");
        String terminalKey = getTerminalKey(terminalType, terminalId);
        ReentrantLock lock = getTerminalLockAlways(terminalKey);
        lock.lock();
        // 如果终端正在更新时间，则等待更新完成后进行超时计算
        try {
            // 进行需要同步的操作
            long lastReportTime = TERMINAL_REPORT_TIME_MAP.getOrDefault(terminalKey, 0L);
            return System.currentTimeMillis() - lastReportTime > TimeUnit.SECONDS
                    .toMillis(TERMINAL_REPORT_TIMEOUT_SECONDS + TERMINAL_REPORT_TIMEOUT_DELAY_SECONDS);
        } finally {
            lock.unlock(); // 确保在 finally 块中释放锁
        }
    }

    private static ReentrantLock getTerminalLockAlways(String terminalKey) {
        return TERMINAL_LOCK_MAP.computeIfAbsent(terminalKey, k -> new ReentrantLock());
    }

    private static String getTerminalKey(TerminalTypeEnum terminalType, String terminalId) {
        return terminalType.name() + "-" + terminalId;
    }

    /**
     * 获取一个锁，如果不存在，就生成一个
     *
     * @param userId 用户ID
     * @return ReentrantLock
     */
    public static ReentrantLock getUserLockAlways(UUID userId) {
        Assert.notNull(userId, "userId cannot be null");

        return USER_LOCK_MAP.computeIfAbsent(userId, k -> {
            LOGGER.info("用户[{}]设置锁成功", userId);
            return new ReentrantLock();
        });
    }
}
