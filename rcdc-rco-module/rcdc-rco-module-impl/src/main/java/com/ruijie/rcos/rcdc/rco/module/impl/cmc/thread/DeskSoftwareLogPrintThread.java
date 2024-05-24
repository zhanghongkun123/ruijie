package com.ruijie.rcos.rcdc.rco.module.impl.cmc.thread;

import com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant.CmcConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto.CmcMessagePerformUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto.CmcMessageQueueUtil;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Description:  GuestTool 上报软件信息日志打印线程
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@Service
public class DeskSoftwareLogPrintThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSoftwareLogPrintThread.class);

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 记录当天时间
    private static String day;

    // 每次轮训处理次数
    private static long recvSuccCntBySec = 0;

    // 每次轮训处理次数
    private static long recvFailCntBySec = 0;

    // 每次轮训处理次数
    private static long dealCntBySec = 0;

    static {
        day = DATE_FORMAT.format(LocalDateTime.now());
    }

    @Autowired
    private CmcMessageQueueUtil cmcMessageQueueUtil;

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {

        LOGGER.info("初始化 GuestTool 上报软件信息日志线程成功，name：{}", Thread.currentThread().getName());
        while (true) {
            reset();
            double dealCntByPerSec = (double) (CmcMessagePerformUtil.dealCount.get() - dealCntBySec) * 1000
                    / CmcConstants.LOG_PRINT_TIMES;
            double recvSuccCntByPerSec = (double) (CmcMessagePerformUtil.receiveSuccessCount.get() - recvSuccCntBySec) * 1000
                    / CmcConstants.LOG_PRINT_TIMES;
            double recvFailCntByPerSec = (double) (CmcMessagePerformUtil.receiveFailCount.get() - recvFailCntBySec) * 1000
                    / CmcConstants.LOG_PRINT_TIMES;

            dealCntBySec = CmcMessagePerformUtil.dealCount.get();
            recvSuccCntBySec = CmcMessagePerformUtil.receiveSuccessCount.get();
            recvFailCntBySec = CmcMessagePerformUtil.receiveFailCount.get();

            LOGGER.info("GuestTool 上报软件信息处理量 {}/d，成功量 {}/d，失败量 {}/d，队列量 {}", CmcMessagePerformUtil.dealCount.get(),
                    CmcMessagePerformUtil.receiveSuccessCount.get(), CmcMessagePerformUtil.receiveFailCount.get(),
                    cmcMessageQueueUtil.getQueueSize());
            LOGGER.info("GuestTool 上报软件信息处理量 {}/s，成功量 {}/s，失败量 {}/s", dealCntByPerSec, recvSuccCntByPerSec, recvFailCntByPerSec);
            try {
                //noinspection BusyWait
                Thread.sleep(CmcConstants.LOG_PRINT_TIMES);
            } catch (InterruptedException ex) {
                LOGGER.error("日志打印失败", ex);
            }
        }
    }

    private static void reset() {

        // 业务唯一赋值，防止跨天情况发生
        LocalDateTime currDate = LocalDateTime.now();
        if (!day.equals(DATE_FORMAT.format(currDate))) {

            if (CmcMessagePerformUtil.dealCount.get() !=
                    (CmcMessagePerformUtil.receiveSuccessCount.get() + CmcMessagePerformUtil.receiveFailCount.get())) {
                LOGGER.warn("存在超出当前线程处理能力的并发请求，需要核对");
            }

            CmcMessagePerformUtil.receiveSuccessCount.set(0);
            CmcMessagePerformUtil.receiveFailCount.set(0);
            CmcMessagePerformUtil.dealCount.set(0);
            day = DATE_FORMAT.format(currDate);
        }
    }
}
