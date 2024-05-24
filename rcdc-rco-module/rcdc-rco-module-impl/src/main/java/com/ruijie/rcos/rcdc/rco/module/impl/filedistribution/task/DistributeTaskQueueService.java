package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/18 17:11
 *
 * @author coderLee23
 */
@Service
public class DistributeTaskQueueService {


    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskQueueService.class);

    /**
     * 任务队列(线程安全) 指定队列的大小1000创建有界队列
     */
    private static final BlockingQueue<UUID> DISTRIBUTE_TASK_BLOCKING_QUEUE = new LinkedBlockingQueue<>(1000);


    /**
     * 添加文件分发任务
     * add(anObject)：
     * 把anObject添加到BlockingQueue里，添加成功返回true，如果BlockingQueue空间已满则抛出异常。
     * offer(anObject)：
     * 表示如果可能的话，将anObject加到BlockingQueue里，即如果BlockingQueue可以容纳，则返回true，否则返回false。
     * put(anObject)：
     * 把anObject加到BlockingQueue里，如果BlockingQueue没有空间，则调用此方法的线程被阻断直到BlockingQueue里有空间再继续。
     * 
     * @param subTaskId 子任务id
     *
     */
    public void put(UUID subTaskId) {
        Assert.notNull(subTaskId, "subTaskId must not be null");
        try {
            // 去重处理，避免重复执行[同时为了避免，uuid1 被消费中，又传进来继续消费，因此，在做种时在用synchronized(uuid)处理]
            if (DISTRIBUTE_TASK_BLOCKING_QUEUE.contains(subTaskId)) {
                LOGGER.warn("队列中已经存在该任务{},忽略", subTaskId);
                return;
            }

            LOGGER.info("添加文件分发任务id{}", subTaskId);
            // put方法是自带锁的阻塞唤醒方法，不需要我们写锁，通知和唤醒
            DISTRIBUTE_TASK_BLOCKING_QUEUE.put(subTaskId);
        } catch (InterruptedException e) {
            LOGGER.error("中断异常", e);
            // 通过调用Thread.currentThread().interrupt()，你可以设置线程的中断标志（即把中断标志设为true），因此更高级别的中断处理程序会注意到它并且可以正确处理它。
            Thread.currentThread().interrupt();
        }
    }


    /**
     * 取出一个分发子任务id
     * poll(time)：
     * 获取并移除此队列的头，若不能立即取出，则可以等time参数规定的时间，取不到时返回null。
     * take()：
     * 获取BlockingQueue里排在首位的对象，若BlockingQueue为空，阻断进入等待状态直到BlockingQueue有新的对象被加入为止。
     * clear()：
     * 从队列彻底移除所有元素。
     * remove()方法直接删除队头的元素
     * peek()方法直接取出队头的元素，并不删除
     *
     * @return subTaskId 发子任务id
     */
    public UUID take() {
        try {
            // 自带锁和通知唤醒方法
            UUID subTaskId = DISTRIBUTE_TASK_BLOCKING_QUEUE.take();
            LOGGER.info("取出一个分发子任务id{}", subTaskId);
            return subTaskId;
        } catch (InterruptedException e) {
            LOGGER.error("中断异常", e);
            Thread.currentThread().interrupt();
        }
        //
        return null;
    }



}
