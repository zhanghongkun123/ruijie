package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeTaskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeTaskService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:07
 *
 * @author zhangyichi
 */
@Service
public class DistributeTaskServiceImpl implements DistributeTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskServiceImpl.class);

    @Autowired
    private DistributeTaskDAO distributeTaskDAO;

    @Override
    public UUID createTask(DistributeTaskDTO taskDTO) {
        Assert.notNull(taskDTO, "taskDTO cannot be null!");

        taskDTO.setId(UUID.randomUUID());
        DistributeTaskEntity taskEntity = new DistributeTaskEntity();
        BeanCopyUtil.copy(taskDTO, taskEntity);
        LOGGER.debug("新建文件分发父任务[{}]", JSON.toJSONString(taskEntity));
        distributeTaskDAO.save(taskEntity);

        return taskEntity.getId();
    }

    @Override
    public DistributeTaskDTO findTaskById(UUID taskId) throws BusinessException {
        Assert.notNull(taskId, "taskId cannot be null!");

        Optional<DistributeTaskEntity> optional = distributeTaskDAO.findById(taskId);
        DistributeTaskEntity taskEntity = optional.orElseThrow(() -> {
            LOGGER.error("文件分发父任务[{}]不存在或已删除", taskId);
            return new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_NOT_EXIST);
        });
        DistributeTaskDTO taskDTO = new DistributeTaskDTO();
        BeanCopyUtil.copy(taskEntity, taskDTO);
        return taskDTO;
    }

    @Override
    public List<DistributeTaskDTO> findTaskByName(String name) {
        Assert.hasText(name, "name cannot be blank!");

        List<DistributeTaskEntity> taskEntityList = distributeTaskDAO.findByTaskName(name);
        if (CollectionUtils.isEmpty(taskEntityList)) {
            return Lists.newArrayList();
        }
        return taskEntityList.stream().map(entity -> {
            DistributeTaskDTO dto = new DistributeTaskDTO();
            BeanCopyUtil.copy(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DistributeTaskDTO> findTaskByParameterId(UUID parameterId) {
        Assert.notNull(parameterId, "parameterId cannot be null!");

        List<DistributeTaskEntity> taskEntityList = distributeTaskDAO.findByParameterId(parameterId);
        if (taskEntityList == null) {
            // 为空则返回空列表
            return Lists.newArrayList();
        }
        return taskEntityList.stream().map(entity -> {
            DistributeTaskDTO dto = new DistributeTaskDTO();
            BeanCopyUtil.copy(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID taskId) {
        Assert.notNull(taskId, "taskId cannot be nulL!");

        distributeTaskDAO.deleteById(taskId);
    }
}
