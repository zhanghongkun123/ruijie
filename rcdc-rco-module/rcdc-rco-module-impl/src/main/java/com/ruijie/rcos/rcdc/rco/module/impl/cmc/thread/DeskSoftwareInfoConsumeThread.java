package com.ruijie.rcos.rcdc.rco.module.impl.cmc.thread;

import com.alibaba.fastjson.JSONArray;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto.CmcMessagePerformUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto.CmcMessageQueueUtil;
import com.ruijie.rcos.rcdc.rco.module.impl.cmc.service.DeskSoftwareInfoConsumeService;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.DesksoftOperateDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.GuestToolNormalMsgDTO;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Description:  GuestTool 上报软件信息消费线程
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@Service
public class DeskSoftwareInfoConsumeThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskSoftwareInfoConsumeThread.class);

    @Autowired
    private DeskSoftwareInfoConsumeService deskSoftwareInfoConsumeService;

    @Autowired
    private CmcMessageQueueUtil cmcMessageQueueUtil;

    @Override
    public void run() {

        LOGGER.info("初始化 GuestTool 上报软件信息处理线程成功，name：{}", Thread.currentThread().getName());

        //noinspection InfiniteLoopStatement
        while (true) {
            try {
                Object message = cmcMessageQueueUtil.poll();
                if (message == null) {
                    sleep(500);
                    continue;
                }

                GuestToolNormalMsgDTO guestToolMessage = (GuestToolNormalMsgDTO) message;
                List<DesksoftOperateDTO> desksoftOperateDTOList = JSONArray.parseArray(guestToolMessage.getContent(), DesksoftOperateDTO.class);
                if (desksoftOperateDTOList == null) {
                    continue;
                }

                if (LOGGER.isInfoEnabled()) {
                    LOGGER.info("CMC 消费数量：{}", desksoftOperateDTOList.size());
                }

                desksoftOperateDTOList = desksoftOperateDTOList.stream().filter(Objects::nonNull).collect(Collectors.toList());

                // 业务处理
                deskSoftwareInfoConsumeService.doRun(desksoftOperateDTOList);
                CmcMessagePerformUtil.receiveSuccessCount.incrementAndGet();

            } catch (Exception ex) {
                CmcMessagePerformUtil.receiveFailCount.incrementAndGet();
                LOGGER.error("消息队列消费异常", ex);
            }
        }
    }


    /**
     * 使线程睡眠, 单位为毫秒.
     *
     * @param durationMillis 睡眠时间
     */
    @SuppressWarnings("SameParameterValue")
    private static void sleep(long durationMillis) {
        try {
            Thread.sleep(durationMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
