package com.ruijie.rcos.rcdc.rco.module.impl.init;

import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskConsumer;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskProducer;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Description: 文件分发任务重建初始化器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/24 09:17
 *
 * @author zhangyichi
 */
@Service
public class FileDistributionTaskRebuildInitializer implements SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDistributionTaskRebuildInitializer.class);

    @Autowired
    private DistributeSubTaskService subTaskService;

    @Autowired
    private FileDistributionTaskManageAPI fileDistributionTaskManageAPI;

    /**
     * 默认采用cpu核心数*2
     */
    private static final Integer DEFAULT_THREAD_NUM = Runtime.getRuntime().availableProcessors() * 2;

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(DEFAULT_THREAD_NUM);

    @Autowired
    private DistributeTaskConsumer distributeTaskConsumer;

    @Autowired
    private DistributeTaskProducer distributeTaskProducer;

    @Override
    public void safeInit() {
        // 重建所有运行中状态的任务
        int runningSubTask = rebuildSubTask(FileDistributionStashTaskStatus.RUNNING);
        if (runningSubTask == 0) {
            LOGGER.info("未查询到运行中的文件分发任务");
        }

        // 将所有已入队但未执行的任务重新入队
        int queuedSubTask = rebuildSubTask(FileDistributionStashTaskStatus.QUEUED);
        if (queuedSubTask == 0) {
            LOGGER.info("未查询到已入队的文件分发任务");
        }

        // 启动生产者,只需要一个生成者处理任务即可
        EXECUTOR_SERVICE.execute(distributeTaskProducer);
        // 启动消费者
        for (int i = 0; i < DEFAULT_THREAD_NUM - 1; i++) {
            EXECUTOR_SERVICE.execute(distributeTaskConsumer);
        }

    }

    private int rebuildSubTask(FileDistributionStashTaskStatus stashStatus) {
        List<DistributeSubTaskDTO> subTaskList = subTaskService.findByStashStatus(stashStatus);
        subTaskList.forEach(fileDistributionTaskManageAPI::rebuildSubTask);
        return subTaskList.size();
    }
}
