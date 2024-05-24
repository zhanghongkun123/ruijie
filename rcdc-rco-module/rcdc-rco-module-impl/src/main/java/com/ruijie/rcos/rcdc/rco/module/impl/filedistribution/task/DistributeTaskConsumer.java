package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeParameterDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeSubTaskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeTaskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeParameterEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeSubTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeParameterService;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/02/18 17:04
 *
 * @author coderLee23
 */
@Service
public class DistributeTaskConsumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskConsumer.class);

    @Autowired
    private DistributeTaskQueueService distributeTaskQueueService;

    @Autowired
    private DistributeTaskFactory distributeTaskFactory;

    @Autowired
    protected DistributeSubTaskDAO distributeSubTaskDAO;

    @Autowired
    private DistributeParameterService distributeParameterService;

    @Autowired
    private DistributeTaskDAO distributeTaskDAO;

    @Autowired
    private DistributeParameterDAO distributeParameterDAO;


    @Override
    public void run() {
        // 强制终端线程，比如spring优雅退出
        while (!Thread.interrupted()) {
            UUID subTaskId = distributeTaskQueueService.take();
            LOGGER.info("文件分发执行任务id{}", subTaskId);

            if (Objects.isNull(subTaskId)) {
                LOGGER.warn("分子发任务id为空{}，忽略处理", subTaskId);
                continue;
            }

            synchronized (subTaskId) {
                LOGGER.info("文件分发执行任务id{}开始...", subTaskId);
                // 提取到外面是避免数据库容器异常情况下恢复，还能继续维持线程正常执行。不让线程退出。
                DistributeTaskProcessor distributeTaskProcessor = null;
                DistributeSubTaskDTO distributeSubTaskDTO = null;
                DistributeParameterDTO distributeParameterDTO = null;
                try {
                    Optional<DistributeSubTaskEntity> distributeSubTaskEntityOptional = distributeSubTaskDAO.findById(subTaskId);
                    if (!distributeSubTaskEntityOptional.isPresent()) {
                        LOGGER.warn(" 子任务 {} 已经不存在！", subTaskId);
                        continue;
                    }

                    DistributeSubTaskEntity distributeSubTaskEntity = distributeSubTaskEntityOptional.get();
                    UUID parentTaskId = distributeSubTaskEntity.getParentTaskId();

                    Optional<DistributeTaskEntity> distributeTaskEntityOptional = distributeTaskDAO.findById(parentTaskId);
                    if (!distributeTaskEntityOptional.isPresent()) {
                        LOGGER.warn("子任务 {} 主任务 {} 已经不存在！", subTaskId, parentTaskId);
                        continue;
                    }

                    DistributeTaskEntity distributeTaskEntity = distributeTaskEntityOptional.get();

                    UUID parameterId = distributeTaskEntity.getParameterId();
                    Optional<DistributeParameterEntity> distributeParameterEntityOptional = distributeParameterDAO.findById(parameterId);
                    if (!distributeParameterEntityOptional.isPresent()) {
                        LOGGER.warn("子任务 {} 主任务 {} 的 DistributeParameter {} 已经不存在！", subTaskId, parentTaskId, parameterId);
                        continue;
                    }

                    distributeTaskProcessor = distributeTaskFactory.buildDistributeTaskProcessor(distributeSubTaskEntity.getTargetType());

                    distributeSubTaskDTO = new DistributeSubTaskDTO();
                    BeanUtils.copyProperties(distributeSubTaskEntity, distributeSubTaskDTO);

                    DistributeParameterEntity distributeParameterEntity = distributeParameterEntityOptional.get();
                    distributeParameterDTO = JSON.parseObject(distributeParameterEntity.getParameter(), DistributeParameterDTO.class);
                    distributeParameterDTO.setId(distributeParameterEntity.getId());

                    LOGGER.info("文件分发执行任务id{}调用具体执行任务开启...", subTaskId);
                    distributeTaskProcessor.execute(distributeSubTaskDTO, distributeParameterDTO);
                } catch (Exception e) {
                    LOGGER.error("文件分发子任务[{}]执行失败", subTaskId, e);
                    if (Objects.nonNull(distributeTaskProcessor) && Objects.nonNull(distributeSubTaskDTO)
                            && Objects.nonNull(distributeParameterDTO)) {
                        distributeTaskProcessor.errorProcess(distributeSubTaskDTO, distributeParameterDTO, e);
                    }
                }

            }

        }
    }

}
