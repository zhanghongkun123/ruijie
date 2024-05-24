package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.dao.DistributeSubTaskDAO;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.DistributeSubTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.DistributeTaskCancelStrategy;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.strategy.DistributeTaskCancelStrategyFactory;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 15:56
 *
 * @author zhangyichi
 */
@Service
public class DistributeSubTaskServiceImpl implements DistributeSubTaskService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeSubTaskServiceImpl.class);

    @Autowired
    private DistributeSubTaskDAO distributeSubTaskDAO;

    @Autowired
    private DistributeTaskService taskService;

    @Autowired
    private DistributeParameterService parameterService;

    @Autowired
    private DistributeTaskCancelStrategyFactory distributeTaskCancelStrategyFactory;


    @Override
    public UUID createTask(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        subTaskDTO.setId(UUID.randomUUID());
        DistributeSubTaskEntity subTaskEntity = new DistributeSubTaskEntity();
        BeanCopyUtil.copy(subTaskDTO, subTaskEntity);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("新建文件分发子任务[{}]", JSON.toJSONString(subTaskEntity));
        }
        distributeSubTaskDAO.save(subTaskEntity);

        return subTaskEntity.getId();
    }

    @Override
    public List<DistributeSubTaskDTO> findByParentId(UUID parentId) {
        Assert.notNull(parentId, "parentId cannot be null!");

        List<DistributeSubTaskEntity> entityList = distributeSubTaskDAO.findByParentTaskId(parentId);
        return convertToDtoList(entityList);
    }

    @Override
    public DistributeSubTaskDTO findById(UUID subTaskId) throws BusinessException {
        Assert.notNull(subTaskId, "subTaskId cannot be null!");

        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskId);
        DistributeSubTaskEntity taskEntity = optional.orElseThrow(() -> {
            LOGGER.error("文件分发子任务[{}]不存在或已删除", subTaskId);
            return new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_NOT_EXIST);
        });
        DistributeSubTaskDTO subTaskDTO = new DistributeSubTaskDTO();
        BeanCopyUtil.copy(taskEntity, subTaskDTO);
        return subTaskDTO;
    }

    @Override
    public List<DistributeSubTaskDTO> findByTargetId(String targetId) {
        Assert.hasText(targetId, "targetId cannot be blank!");

        List<DistributeSubTaskEntity> entityList = distributeSubTaskDAO.findByTargetId(targetId);
        return convertToDtoList(entityList);
    }

    @Override
    public List<DistributeSubTaskDTO> findByStashStatus(FileDistributionStashTaskStatus stashStatus) {
        Assert.notNull(stashStatus, "stashStatus cannot be null!");

        List<DistributeSubTaskEntity> entityList = distributeSubTaskDAO.findByStashStatus(stashStatus);
        return convertToDtoList(entityList);
    }

    private List<DistributeSubTaskDTO> convertToDtoList(List<DistributeSubTaskEntity> entityList) {
        return entityList.stream().map(entity -> {
            DistributeSubTaskDTO subTaskDTO = new DistributeSubTaskDTO();
            BeanCopyUtil.copy(entity, subTaskDTO);
            return subTaskDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteById(UUID subTaskId) {
        Assert.notNull(subTaskId, "subTaskId cannot be null!");

        distributeSubTaskDAO.deleteById(subTaskId);
    }

    @Override
    public void changeSubTaskToCancel(DistributeSubTaskDTO subTaskDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将文件分发子任务[{}]变更为取消状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略取消操作", subTaskDTO.getId());
            return;
        }
        DistributeSubTaskEntity subTaskEntity = optional.get();
        FileDistributionTaskStatus status = subTaskEntity.getStatus();
        switch (status) {
            case WAITING:
                // 等待中的直接取消
                subTaskEntity.setStatus(FileDistributionTaskStatus.CANCELED);
                subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.NONE);
                distributeSubTaskDAO.save(subTaskEntity);
                break;
            case RUNNING:
                DistributeTaskDTO taskDTO = taskService.findTaskById(subTaskDTO.getParentTaskId());
                if (ObjectUtils.isEmpty(taskDTO)) {
                    LOGGER.warn("子任务[{}]的父任务[{}]不存在", subTaskDTO.getId(), subTaskDTO.getParentTaskId());
                    break;
                }
                DistributeParameterDTO parameterDTO = parameterService.findById(taskDTO.getParameterId());
                if (ObjectUtils.isEmpty(parameterDTO)) {
                    LOGGER.warn("子任务[{}]的父任务[{}]分发参数不存在", subTaskDTO.getId(), subTaskDTO.getParentTaskId());
                    break;
                }

                DistributeTaskCancelStrategy distributeTaskCancelStrategy =
                        distributeTaskCancelStrategyFactory.getDistributeTaskCancelStrategy(subTaskDTO.getTargetType());
                distributeTaskCancelStrategy.doCancel(subTaskDTO, parameterDTO);
                subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.CANCELING);
                distributeSubTaskDAO.save(subTaskEntity);
                break;
            default:
                LOGGER.debug("文件分发子任务[{}]处于[{}]状态，忽略取消操作", subTaskDTO.getId(), status.name());
        }

    }

    @Override
    public void changeSubTaskToRunning(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将文件分发子任务[{}]变更为执行状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略执行操作", subTaskDTO.getId());
            return;
        }

        DistributeSubTaskEntity subTaskEntity = optional.get();
        FileDistributionTaskStatus status = subTaskEntity.getStatus();
        List<FileDistributionTaskStatus> allowedStateList = Lists.newArrayList(FileDistributionTaskStatus.WAITING);
        if (!allowedStateList.contains(status)) {
            LOGGER.debug("文件分发子任务[{}]处于[{}]状态，忽略执行操作", subTaskDTO.getId(), status.name());
            return;
        }
        subTaskEntity.setStatus(FileDistributionTaskStatus.RUNNING);
        subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.RUNNING);
        subTaskEntity.setStartTime(new Date());
        subTaskEntity.setUpdateTime(new Date());
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public void changeSubTaskToFail(DistributeSubTaskDTO subTaskDTO, @Nullable String failMessage) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将文件分发子任务[{}]变更为失败状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略失败操作", subTaskDTO.getId());
            return;
        }

        DistributeSubTaskEntity subTaskEntity = optional.get();
        FileDistributionTaskStatus status = subTaskEntity.getStatus();
        List<FileDistributionTaskStatus> allowedStateList =
                Lists.newArrayList(FileDistributionTaskStatus.RUNNING, FileDistributionTaskStatus.WAITING, FileDistributionTaskStatus.CANCELED);
        if (!allowedStateList.contains(status)) {
            LOGGER.debug("文件分发子任务[{}]处于[{}]状态，忽略失败操作", subTaskDTO.getId(), status.name());
            return;
        }
        subTaskEntity.setStatus(FileDistributionTaskStatus.FAIL);
        subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.NONE);
        subTaskEntity.setMessage(StringUtils.isNotBlank(failMessage) ? failMessage : StringUtils.EMPTY);
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public void changeSubTaskToSuccess(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将文件分发子任务[{}]变更为成功状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略成功操作", subTaskDTO.getId());
            return;
        }

        DistributeSubTaskEntity subTaskEntity = optional.get();
        FileDistributionTaskStatus status = subTaskEntity.getStatus();
        List<FileDistributionTaskStatus> allowedStateList =
                Lists.newArrayList(FileDistributionTaskStatus.RUNNING, FileDistributionTaskStatus.CANCELED);
        if (!allowedStateList.contains(status)) {
            LOGGER.debug("文件分发子任务[{}]处于[{}]状态，忽略成功操作", subTaskDTO.getId(), status.name());
            return;
        }
        subTaskEntity.setStatus(FileDistributionTaskStatus.SUCCESS);
        subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.NONE);
        subTaskEntity.setMessage(StringUtils.EMPTY);
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public void changeSubTaskToWaiting(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将文件分发子任务[{}]变更为等待状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略等待操作", subTaskDTO.getId());
            return;
        }
        DistributeSubTaskEntity subTaskEntity = optional.get();
        FileDistributionTaskStatus status = subTaskEntity.getStatus();
        List<FileDistributionTaskStatus> allowedStateList = Lists.newArrayList(FileDistributionTaskStatus.FAIL, FileDistributionTaskStatus.CANCELED);
        if (!allowedStateList.contains(status)) {
            LOGGER.debug("文件分发子任务[{}]处于[{}]状态，忽略等待操作", subTaskDTO.getId(), status.name());
            return;
        }
        subTaskEntity.setStatus(FileDistributionTaskStatus.WAITING);
        subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.NONE);
        subTaskEntity.setMessage(StringUtils.EMPTY);
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public void changeRunningSubTaskToWaiting(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("将运行中的文件分发子任务[{}]变更为等待状态", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略等待操作", subTaskDTO.getId());
            return;
        }
        DistributeSubTaskEntity subTaskEntity = optional.get();
        subTaskEntity.setStatus(FileDistributionTaskStatus.WAITING);
        subTaskEntity.setStashStatus(FileDistributionStashTaskStatus.STASHED);
        subTaskEntity.setMessage(StringUtils.EMPTY);
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public void updateSubTaskTime(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        LOGGER.info("更新文件分发子任务[{}]", subTaskDTO.getId());
        Optional<DistributeSubTaskEntity> optional = distributeSubTaskDAO.findById(subTaskDTO.getId());
        if (!optional.isPresent()) {
            LOGGER.warn("文件分发子任务[{}]不存在，忽略更新操作", subTaskDTO.getId());
            return;
        }
        DistributeSubTaskEntity subTaskEntity = optional.get();
        subTaskEntity.setUpdateTime(new Date());
        distributeSubTaskDAO.save(subTaskEntity);
    }

    @Override
    public List<DistributeSubTaskDTO> findExecutableTaskList() {
        List<DistributeSubTaskEntity> executableTaskList = distributeSubTaskDAO.findExecutableTaskList();
        return executableTaskList.stream().map(distributeSubTaskEntity -> {
            DistributeSubTaskDTO distributeSubTaskDTO = new DistributeSubTaskDTO();
            BeanUtils.copyProperties(distributeSubTaskEntity, distributeSubTaskDTO);
            return distributeSubTaskDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public void deleteByTargetId(UUID targetId) {
        Assert.notNull(targetId, "targetId cannot be null!");
        LOGGER.info("云桌面被删除！桌面id：{}的文件分发子任务级联删除", targetId);
        distributeSubTaskDAO.deleteByTargetId(targetId.toString());
    }

    @Override
    public void changeStashStatus(DistributeSubTaskDTO subTaskDTO, FileDistributionStashTaskStatus stashStatus) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(stashStatus, "stashStatus cannot be null!");
        LOGGER.info("修改任务[{}]状态为：[{}]", subTaskDTO.getId(), stashStatus);
        distributeSubTaskDAO.updateStashStatusById(subTaskDTO.getId(), stashStatus);
    }

}
