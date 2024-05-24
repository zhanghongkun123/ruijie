package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.impl;


import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcaSupportAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.RcacRestClient;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.CancelDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.connector.rest.request.RunDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.AppClientDistributeTaskInfoDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;

/**
 * Description: 应用客户端类型文件分发任务处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/22 15:00
 *
 * @author zhangyichi
 */
@Service
public class DistributeTaskProcessorAppClientImpl extends AbstractDistributeTaskProcessor {

    private static final String APP_CLIENT_ONLINE_STATE = "online";

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Autowired
    private RcacRestClient rcacRestClient;

    @Override
    public FileDistributionTargetType fileDistributionTargetType() {
        return FileDistributionTargetType.APP_CLIENT;
    }

    @Override
    public void preProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        // 任务信息基础检查
        if (!preProcessBasicCheck(subTaskDTO)) {
            return;
        }

        // 检查对象应用客户端在线状态
        int targetId = resolveTargetId(subTaskDTO);
        AppTerminalDTO targetAppClient = rcaSupportAPI.getAppTerminalDetail(targetId);
        if (!targetAppClient.getState().equals(APP_CLIENT_ONLINE_STATE)) {
            LOGGER.error("文件分发对象云应用客户端处于[{}]状态，自动暂存", targetAppClient.getState());
            subTaskService.changeStashStatus(subTaskDTO, FileDistributionStashTaskStatus.STASHED);
            return;
        }

        // 变更子任务状态
        subTaskService.changeSubTaskToRunning(subTaskDTO);
    }

    @Override
    public void doDistribute(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        // 更新任务信息
        subTaskDTO = subTaskService.findById(subTaskDTO.getId());

        // 检查子任务状态
        if (subTaskDTO.getStatus() != FileDistributionTaskStatus.RUNNING) {
            LOGGER.info("任务处于[{}]状态，不向应用客户端分发", subTaskDTO.getStatus().name());
            return;
        }
        if (subTaskDTO.getStashStatus() == FileDistributionStashTaskStatus.REBUILDING) {
            LOGGER.info("任务重建中，不重复分发");
            return;
        }

        AppClientDistributeTaskInfoDTO taskInfoDTO = new AppClientDistributeTaskInfoDTO();
        BeanCopyUtil.copy(parameterDTO, taskInfoDTO);
        taskInfoDTO.setTaskId(subTaskDTO.getId());
        taskInfoDTO.setRcaClientId(resolveTargetId(subTaskDTO));

        List<DistributeParameterDataDTO> dataList = parameterDTO.getDataList();
        if (!CollectionUtils.isEmpty(dataList)) {
            List<UUID> appIdList = dataList.stream().map(DistributeParameterDataDTO::getId).collect(Collectors.toList());
            List<CbbDeskSoftDTO> cbbDeskSoftDTOList = cbbDeskSoftMgmtAPI.listByIdIn(appIdList);
            List<DistributeParameterSeedDataDTO> seedDataList = cbbDeskSoftDTOList.stream().map(cbbDeskSoftDTO -> {
                DistributeParameterSeedDataDTO distributeParameterSeedDataDTO = new DistributeParameterSeedDataDTO();
                BeanUtils.copyProperties(cbbDeskSoftDTO, distributeParameterSeedDataDTO);
                return distributeParameterSeedDataDTO;
            }).collect(Collectors.toList());
            taskInfoDTO.setDataList(seedDataList);
        }

        RunDistributeTaskRequest request = new RunDistributeTaskRequest();
        request.setTaskArr(new AppClientDistributeTaskInfoDTO[] {taskInfoDTO});
        request.setFtpConfig(getFtpConfig());

        rcacRestClient.runDistributeTask(request);
    }

    @Override
    public void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        CancelDistributeTaskRequest request = new CancelDistributeTaskRequest();
        request.setRcaClientId(resolveTargetId(subTaskDTO));
        request.setTaskId(subTaskDTO.getId());
        rcacRestClient.cancelDistributeTask(request);
    }

    private Integer resolveTargetId(DistributeSubTaskDTO subTaskDTO) {
        return Integer.valueOf(subTaskDTO.getTargetId());
    }


}
