package com.ruijie.rcos.rcdc.rco.module.impl.cmc.dto;

import com.ruijie.rcos.rcdc.rco.module.impl.cmc.contant.CmcConstants;
import com.ruijie.rcos.sk.base.config.ConfigFacadeHolder;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Description:  GuestTool 上报软件信息内部队列
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.11.05
 *
 * @author LinHJ
 */
@Service
public class CmcMessageQueueUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmcMessageQueueUtil.class);

    private static ArrayBlockingQueue<Object> queue = null;

    private static volatile boolean inited = false;

    /**
     * 初始化
     */
    @PostConstruct
    private void initMethod() {
        init();
    }

    /**
     * 初始化消息队列
     */
    private void init() {
        if (!inited) {
            synchronized (this) {
                if (!inited) {
                    int size = Optional.ofNullable(ConfigFacadeHolder.getFacade().read("rcdc.cmc.queue.num"))
                            .map(Integer::parseInt).orElse(CmcConstants.DEFAULT_QUEUE_NUM);
                    queue = new ArrayBlockingQueue<>(size);
                    inited = true;
                }
            }
        }
    }

    /**
     * 消费队列
     *
     * @return 消费成功的对象
     */
    public Object poll() {
        try {
            return queue.poll();
        } catch (Exception ex) {
            LOGGER.error("消息队列消费异常", ex);
            // 消费异常不影响业务
            return null;
        }
    }

    /**
     * 写入队列
     *
     * @param msg 需要写入的对象
     */
    public void offer(@Nullable Object msg) {
        try {
            CmcMessagePerformUtil.dealCount.incrementAndGet();
            queue.offer(msg);
        } catch (Exception ex) {
            LOGGER.error("消息队列添加异常", ex);
        }
    }

    public int getQueueSize() {
        return queue.size();
    }
}
