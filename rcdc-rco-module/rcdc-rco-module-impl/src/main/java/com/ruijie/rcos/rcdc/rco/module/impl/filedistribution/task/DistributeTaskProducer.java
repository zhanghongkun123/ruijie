package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/19 1:17
 *
 * @author coderLee23
 */
@Service
public class DistributeTaskProducer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskProducer.class);


    @Autowired
    private DistributeTaskQueueService distributeTaskQueueService;

    @Autowired
    private DistributeSubTaskService distributeSubTaskService;

    @Override
    public void run() {
        LOGGER.info("文件分发生产者启动！");
        // 用于优雅关闭springboot业务
        while (!Thread.interrupted()) {
            try {
                // 识别方式 targetId【云桌面】，云桌面运行中。相同云桌面同一时间只能执行一个任务。 可能存在多个父任务，必须优先执行最早的未完成的任务，
                // 如果该任务已经在队列或者线程中，则不在将该任务放入到队列中。
                List<DistributeSubTaskDTO> distributeSubTaskList = distributeSubTaskService.findExecutableTaskList();

                // 根据桌面进行分组，因为当前要求一个桌面只能一个任务在运行。
                Map<String, List<DistributeSubTaskDTO>> distributeSubTaskMap =
                        distributeSubTaskList.stream().collect(Collectors.groupingBy(DistributeSubTaskDTO::getTargetId, Collectors.toList()));
                for (List<DistributeSubTaskDTO> distributeSubTaskDTOList : distributeSubTaskMap.values()) {
                    List<DistributeSubTaskDTO> subTaskSortedList = distributeSubTaskDTOList.stream()
                            .sorted(Comparator.comparing(DistributeSubTaskDTO::getCreateTime)).collect(Collectors.toList());
                    DistributeSubTaskDTO distributeSubTaskDTO = subTaskSortedList.get(0);
                    distributeSubTaskService.changeStashStatus(distributeSubTaskDTO, FileDistributionStashTaskStatus.QUEUED);
                    distributeTaskQueueService.put(distributeSubTaskDTO.getId());
                }

            } catch (Exception e) {
                // 防止数据库异常重启等出现查询异常问题
                LOGGER.error("忽略异常，避免生产者失效！", e);
            }

            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
