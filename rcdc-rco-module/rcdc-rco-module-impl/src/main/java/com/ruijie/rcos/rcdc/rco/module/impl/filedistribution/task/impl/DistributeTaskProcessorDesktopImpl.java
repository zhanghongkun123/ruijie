package com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.impl;

import static com.ruijie.rcos.rcdc.rco.module.impl.Constants.FTP_DIR;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGuestToolMessageAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGuesttoolMessageDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.SeedMakeStatusEnum;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.guesttool.GuesttoolMessageContent;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterSeedDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.RcdcGuestToolCmdKey;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.DesktopDistributeTaskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.DistributeTaskCancelGtMessageDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.dto.DistributeTaskGtMessageDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;

/**
 * Description: 云桌面类型文件分发任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/22 14:56
 *
 * @author zhangyichi
 */
@Service
public class DistributeTaskProcessorDesktopImpl extends AbstractDistributeTaskProcessor {

    private final static int SUCCESS = 0;

    //vmsg通道消息最长接收65535个字节
    private final static long MESSAGE_TO_GT_MAX_LENGTH = 60000;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbGuestToolMessageAPI cbbGuestToolMessageAPI;

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributeTaskProcessorDesktopImpl.class);

    @Override
    public FileDistributionTargetType fileDistributionTargetType() {
        return FileDistributionTargetType.DESKTOP;
    }

    @Override
    public void preProcess(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        // 任务信息基础检查
        if (!preProcessBasicCheck(subTaskDTO)) {
            return;
        }

        List<DistributeParameterDataDTO> dataList = parameterDTO.getDataList();
        List<UUID> appIdList = dataList.stream().map(DistributeParameterDataDTO::getId).collect(Collectors.toList());

        Boolean isExistMakeFail =
                cbbDeskSoftMgmtAPI.existsByIdInAndSeedMakeStatusIn(appIdList, Collections.singletonList(SeedMakeStatusEnum.MAKE_FAIL));
        if (Boolean.TRUE.equals(isExistMakeFail)) {
            LOGGER.warn("子任务[{}]中，存在做种失败的文件", subTaskDTO.getId());
            subTaskService.changeSubTaskToFail(subTaskDTO,
                    LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_HAS_MAKE_SEED_FAIL));
            return;
        }

        Boolean isExistNotMakeSuccess = cbbDeskSoftMgmtAPI.existsByIdInAndSeedMakeStatusIn(appIdList,
                Stream.of(SeedMakeStatusEnum.ENABLE_MAKE, SeedMakeStatusEnum.MAKING).collect(Collectors.toList()));
        // 等待种子文件制作完成
        if (Boolean.TRUE.equals(isExistNotMakeSuccess)) {
            LOGGER.warn("等待种子文件制作完成，子任务id[{}]，自动暂存", subTaskDTO.getId());
            subTaskService.changeStashStatus(subTaskDTO, FileDistributionStashTaskStatus.STASHED);
            return;
        }

        // 检查对象云桌面状态
        UUID targetId = resolveTargetId(subTaskDTO);
        CloudDesktopDetailDTO targetDesktop = getCloudDesktopDetail(targetId);

        if (targetDesktop.getDesktopState().equals(CbbCloudDeskState.RECYCLE_BIN.name())) {
            LOGGER.warn("文件分发对象云桌面在回收站中");
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TARGET_DESKTOP_IS_DELETED);
        }

        if (!targetDesktop.getDesktopState().equals(CbbCloudDeskState.RUNNING.name())) {
            LOGGER.warn("文件分发对象云桌面处于[{}]状态，自动暂存", targetDesktop.getDesktopState());
            subTaskService.changeStashStatus(subTaskDTO, FileDistributionStashTaskStatus.STASHED);
            return;
        }

        // 变更子任务状态
        subTaskService.changeSubTaskToRunning(subTaskDTO);

    }

    private CloudDesktopDetailDTO getCloudDesktopDetail(UUID targetId) throws BusinessException {
        try {
            return userDesktopMgmtAPI.getDesktopDetailById(targetId);
        } catch (BusinessException be) {
            if (be.getKey().equals(BusinessKey.RCDC_USER_CLOUDDESKTOP_NOT_FOUNT_BY_ID)) {
                LOGGER.error("文件分发对象云桌面[{}]不存在", targetId, be);
                throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TARGET_DESKTOP_IS_DELETED, be);
            }
            throw be;
        }
    }

    @Override
    public void doDistribute(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        // 更新任务信息
        subTaskDTO = subTaskService.findById(subTaskDTO.getId());

        // 检查子任务状态
        if (subTaskDTO.getStatus() != FileDistributionTaskStatus.RUNNING) {
            LOGGER.warn("任务处于[{}]状态，不向桌面分发", subTaskDTO.getStatus().name());
            return;
        }

        try {
            // 向云桌面发送任务
            sendTaskToGt(subTaskDTO, parameterDTO);
        } catch (BusinessException e) {
            LOGGER.error("文件分发子任务[{}]，向GT发送消息失败", subTaskDTO.getId(), e);

            if (BusinessKey.RCDC_RCO_DESK_SOFT_MESSAGE_TO_GT_TOO_LONG.equals(e.getKey())) {
                subTaskService.changeSubTaskToFail(subTaskDTO, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESK_SOFT_MESSAGE_TO_GT_TOO_LONG));
                return;
            }


            if (BusinessKey.RCDC_RCO_DESK_SOFT_HAS_BE_DELETE.equals(e.getKey())) {
                subTaskService.changeSubTaskToFail(subTaskDTO, LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_DESK_SOFT_HAS_BE_DELETE));
                return;
            }

            subTaskService.changeRunningSubTaskToWaiting(subTaskDTO);
        }

    }

    private void sendTaskToGt(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID targetId = resolveTargetId(subTaskDTO);
        messageDTO.setDeskId(targetId);
        messageDTO.setCmdId(RcdcGuestToolCmdKey.RCDC_GT_CMD_ID_FILE_DISTRIBUTE_TASK);
        messageDTO.setPortId(RcdcGuestToolCmdKey.RCDC_GT_PORT_ID_FILE_DISTRIBUTE_TASK);
        messageDTO.setBody(JSON.toJSONString(buildTaskMessageContent(subTaskDTO, parameterDTO)));

        if (JSON.toJSONString(messageDTO).getBytes().length > MESSAGE_TO_GT_MAX_LENGTH) {
            throw new BusinessException(BusinessKey.RCDC_RCO_DESK_SOFT_MESSAGE_TO_GT_TOO_LONG);
        }

        cbbGuestToolMessageAPI.syncRequest(messageDTO);
    }

    private GuesttoolMessageContent buildTaskMessageContent(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO)
            throws BusinessException {

        DesktopDistributeTaskInfoDTO taskInfoDTO = new DesktopDistributeTaskInfoDTO();
        BeanCopyUtil.copy(parameterDTO, taskInfoDTO);

        List<DistributeParameterDataDTO> dataList = parameterDTO.getDataList();
        if (!CollectionUtils.isEmpty(dataList)) {
            List<UUID> appIdList = dataList.stream().map(DistributeParameterDataDTO::getId).collect(Collectors.toList());
            List<CbbDeskSoftDTO> cbbDeskSoftDTOList = cbbDeskSoftMgmtAPI.listByIdIn(appIdList);

            if (cbbDeskSoftDTOList.size() != dataList.size()) {
                throw new BusinessException(BusinessKey.RCDC_RCO_DESK_SOFT_HAS_BE_DELETE);
            }

            List<DistributeParameterSeedDataDTO> seedDataList = cbbDeskSoftDTOList.stream().map(cbbDeskSoftDTO -> {
                DistributeParameterSeedDataDTO distributeParameterSeedDataDTO = new DistributeParameterSeedDataDTO();
                BeanUtils.copyProperties(cbbDeskSoftDTO, distributeParameterSeedDataDTO);
                distributeParameterSeedDataDTO.setTorrentPath(cbbDeskSoftDTO.getTorrentPath().substring(FTP_DIR.length()));
                return distributeParameterSeedDataDTO;
            }).collect(Collectors.toList());
            taskInfoDTO.setDataList(seedDataList);
        }

        taskInfoDTO.setTaskId(subTaskDTO.getId());
        DistributeTaskGtMessageDTO messageDTO = new DistributeTaskGtMessageDTO();
        messageDTO.setTaskInfo(taskInfoDTO);
        messageDTO.setFtpConfig(getFtpConfig());

        GuesttoolMessageContent content = new GuesttoolMessageContent();
        content.setCode(SUCCESS);
        content.setContent(messageDTO);
        return content;
    }


    @Override
    public void doCancel(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) throws BusinessException {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        sendCancelToGt(subTaskDTO);
    }

    private void sendCancelToGt(DistributeSubTaskDTO subTaskDTO) throws BusinessException {
        CbbGuesttoolMessageDTO messageDTO = new CbbGuesttoolMessageDTO();
        UUID targetId = resolveTargetId(subTaskDTO);
        messageDTO.setDeskId(targetId);
        messageDTO.setCmdId(RcdcGuestToolCmdKey.RCDC_GT_CMD_ID_FILE_DISTRIBUTE_TASK_CANCEL);
        messageDTO.setPortId(RcdcGuestToolCmdKey.RCDC_GT_PORT_ID_FILE_DISTRIBUTE_TASK);
        messageDTO.setBody(JSON.toJSONString(buildTaskCancelMessageContent(subTaskDTO)));
        cbbGuestToolMessageAPI.asyncRequest(messageDTO);
    }

    private GuesttoolMessageContent buildTaskCancelMessageContent(DistributeSubTaskDTO subTaskDTO) {
        DistributeTaskCancelGtMessageDTO messageDTO = new DistributeTaskCancelGtMessageDTO();
        messageDTO.setTaskId(subTaskDTO.getId());

        GuesttoolMessageContent content = new GuesttoolMessageContent();
        content.setCode(SUCCESS);
        content.setContent(messageDTO);
        return content;
    }

    private UUID resolveTargetId(DistributeSubTaskDTO subTaskDTO) {
        return UUID.fromString(subTaskDTO.getTargetId());
    }

}
