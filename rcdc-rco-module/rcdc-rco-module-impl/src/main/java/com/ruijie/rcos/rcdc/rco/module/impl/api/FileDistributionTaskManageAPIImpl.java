package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.SeedMakeStatusEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.FileDistributionTaskManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskGetGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TaskSearchGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeParameterDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeSubTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskParameterDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.filedistribution.DistributeTaskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionStashTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTargetType;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.FileDistributionTaskStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.filedistribution.CreateDistributeTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.TerminalGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopCountDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UserGroupDesktopRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.dto.DistributeTaskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.distribution.request.SearchDistributeTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewDistributeTaskEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewTerminalGroupDesktopRelatedEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.entity.ViewUserGroupDesktopRelatedEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeSubTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.service.DistributeTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.filedistribution.task.DistributeTaskFactory;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewDistributeTaskService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewTerminalGroupDesktopRelatedService;
import com.ruijie.rcos.rcdc.rco.module.impl.service.ViewUserGroupDesktopRelatedService;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutor;
import com.ruijie.rcos.sk.base.concurrent.ThreadExecutors;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.BeanCopyUtil;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.IdRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/17 14:36
 *
 * @author zhangyichi
 */
public class FileDistributionTaskManageAPIImpl implements FileDistributionTaskManageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDistributionTaskManageAPIImpl.class);

    private static final String SUB_TASK_TARGET_TYPE_KEY = "targetType";

    private static final String SUB_TASK_CREATE_TIME_KEY = "createTime";

    /**
     * 最大并发执行子任务数
     */
    private static final int MAX_THREAD_NUM = 30;

    /**
     * 任务等待队列长度
     */
    private static final int TASK_QUEUE_SIZE = 1000;

    private static final List<SeedMakeStatusEnum> NEED_SEED_MAKE_STATUS_LIST =
            Arrays.asList(SeedMakeStatusEnum.NOT_MAKE, SeedMakeStatusEnum.MAKE_FAIL);

    private static final ThreadExecutor EXECUTOR = ThreadExecutors.newBuilder("文件分发任务线程池") //
            .maxThreadNum(MAX_THREAD_NUM) //
            .queueSize(TASK_QUEUE_SIZE) //
            .addStartupLineNumberToThreadName() //
            .build();

    @Autowired
    private DistributeParameterService parameterService;

    @Autowired
    private DistributeTaskService taskService;

    @Autowired
    private DistributeSubTaskService subTaskService;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private DistributeTaskFactory distributeTaskFactory;

    @Autowired
    private CbbDeskSoftMgmtAPI cbbDeskSoftMgmtAPI;

    @Autowired
    private ViewTerminalGroupDesktopRelatedService viewTerminalGroupDesktopRelatedService;

    @Autowired
    private ViewUserGroupDesktopRelatedService viewUserGroupDesktopRelatedService;

    @Autowired
    private ViewDistributeTaskService viewDistributeTaskService;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;


    @Override
    public synchronized UUID createTask(CreateDistributeTaskRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        // 检查任务名
        List<DistributeTaskDTO> taskDTOList = taskService.findTaskByName(request.getTaskName());
        if (!CollectionUtils.isEmpty(taskDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_NAME_REPEATED);
        }

        DistributeParameterDTO parameter = request.getParameter();
        enableMakeSeed(parameter.getDataList());
        // 创建分发参数记录
        UUID parameterId = parameterService.createNewParameter(parameter);

        // 创建父任务记录，并关联参数
        DistributeTaskDTO parentTaskDTO = new DistributeTaskDTO();
        parentTaskDTO.setTaskName(request.getTaskName());
        parentTaskDTO.setParameterId(parameterId);
        parentTaskDTO.setCreateTime(new Date());
        UUID parentTaskId = taskService.createTask(parentTaskDTO);

        // 创建子任务记录
        createSubTaskForDesktop(request, parentTaskId);
        createSubTaskForAppClient(request, parentTaskId);

        return parentTaskId;
    }

    private void createSubTaskForAppClient(CreateDistributeTaskRequest request, UUID parentTaskId) {
        List<Integer> appClientIdList = request.getCloudAppIdList();
        if (appClientIdList == null || CollectionUtils.isEmpty(appClientIdList)) {
            return;
        }
        for (Integer appClientId : appClientIdList) {
            DistributeSubTaskDTO subTaskDTO = new DistributeSubTaskDTO();
            subTaskDTO.setParentTaskId(parentTaskId);
            subTaskDTO.setTargetType(FileDistributionTargetType.APP_CLIENT);
            subTaskDTO.setTargetId(appClientId.toString());
            subTaskDTO.setCreateTime(new Date());
            subTaskService.createTask(subTaskDTO);
        }
    }

    private void createSubTaskForDesktop(CreateDistributeTaskRequest request, UUID parentTaskId) {
        List<UUID> desktopIdList = request.getDesktopIdList();
        if (desktopIdList == null || CollectionUtils.isEmpty(desktopIdList)) {
            return;
        }
        for (UUID desktopId : desktopIdList) {
            DistributeSubTaskDTO subTaskDTO = new DistributeSubTaskDTO();
            subTaskDTO.setParentTaskId(parentTaskId);
            subTaskDTO.setTargetType(FileDistributionTargetType.DESKTOP);
            subTaskDTO.setTargetId(desktopId.toString());
            subTaskDTO.setCreateTime(new Date());
            subTaskService.createTask(subTaskDTO);
        }
    }

    @Override
    public List<DistributeTaskDTO> findByTaskName(String taskName) {
        Assert.hasText(taskName, "taskName cannot be blank!");

        return taskService.findTaskByName(taskName);
    }

    @Override
    public DistributeTaskParameterDTO getBasicInfo(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DistributeTaskDTO taskDTO = taskService.findTaskById(request.getId());
        DistributeParameterDTO parameterDTO = parameterService.findById(taskDTO.getParameterId());
        DistributeTaskParameterDTO taskParameterDTO = new DistributeTaskParameterDTO();
        BeanCopyUtil.copy(taskDTO, taskParameterDTO);
        taskParameterDTO.setParameter(parameterDTO);
        return taskParameterDTO;
    }

    @Override
    public List<DistributeTaskDTO> findTaskByParameterContent(String content) {
        Assert.hasText(content, "content cannot be null!");

        List<UUID> parameterIdList = parameterService.findByParameterContent(content);
        List<DistributeTaskDTO> taskDTOList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(parameterIdList)) {
            return taskDTOList;
        }
        parameterIdList.forEach(parameterId -> {
            List<DistributeTaskDTO> taskList = taskService.findTaskByParameterId(parameterId);
            taskDTOList.addAll(taskList);
        });
        return taskDTOList;
    }

    @Override
    public PageQueryResponse<DistributeSubTaskDTO> pageQuerySubTask(PageQueryRequest request, FileDistributionTargetType targetType)
            throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(targetType, "targetType cannot be null!");

        PageQueryRequest queryRequest = pageQueryBuilderFactory.newRequestBuilder(request).desc(SUB_TASK_CREATE_TIME_KEY)
                .eq(SUB_TASK_TARGET_TYPE_KEY, targetType.name()).build();
        return subTaskService.pageQuery(queryRequest);
    }

    @Override
    public DistributeSubTaskDTO getSubTaskInfo(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        return subTaskService.findById(request.getId());
    }

    @Override
    public Long countSubTaskWithState(UUID taskId, List<FileDistributionTaskStatus> statusList) {
        Assert.notNull(taskId, "taskId cannot be null!");
        Assert.notNull(statusList, "statusList cannot be null!");

        List<DistributeSubTaskDTO> subTaskList = subTaskService.findByParentId(taskId);
        return subTaskList.stream().filter(subTaskDTO -> statusList.contains(subTaskDTO.getStatus())).count();
    }

    @Override
    public void runTask(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        // 检查父任务并获取参数
        DistributeTaskParameterDTO taskParameterDTO = getBasicInfo(request);

        // 获取父任务下所有子任务
        List<DistributeSubTaskDTO> subTaskDTOList = subTaskService.findByParentId(taskParameterDTO.getId());

        // 执行每一个子任务
        if (CollectionUtils.isEmpty(subTaskDTOList)) {
            return;
        }
        subTaskDTOList.forEach(subTaskDTO -> runSubTask(subTaskDTO, taskParameterDTO.getParameter()));
    }

    @Override
    public void cancelTask(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DistributeTaskDTO taskDTO = taskService.findTaskById(request.getId());
        List<DistributeSubTaskDTO> subTaskList = subTaskService.findByParentId(taskDTO.getId());
        if (CollectionUtils.isEmpty(subTaskList)) {
            LOGGER.debug("文件分发任务[{}]为空，忽略取消操作", taskDTO.getTaskName());
            return;
        }

        subTaskList.stream().filter(subTaskDTO -> subTaskDTO.getStashStatus() != FileDistributionStashTaskStatus.CANCELING).forEach(subTaskDTO -> {
            try {
                subTaskService.changeSubTaskToCancel(subTaskDTO);
            } catch (BusinessException e) {
                LOGGER.error("子任务[{}]取消失败", e);
            }
        });
    }

    @Override
    public void processSubTaskResult(DistributeTaskResultDTO request) {
        Assert.notNull(request, "request cannot be null!");

        try {
            LOGGER.debug("处理文件分发子任务[{}]执行结果[{}]", request.getTaskId(), request.getResult().name());
            DistributeSubTaskDTO subTaskDTO = subTaskService.findById(request.getTaskId());
            DistributeTaskResultDTO.TaskResult result = request.getResult();
            switch (result) {
                case SUCCESS:
                    subTaskService.changeSubTaskToSuccess(subTaskDTO);
                    break;
                case FAIL:
                    subTaskService.changeSubTaskToFail(subTaskDTO, request.getMessage());
                    break;
                case RUNNING:
                    subTaskService.updateSubTaskTime(subTaskDTO);
                    break;
                default:
                    LOGGER.error("未知的任务执行结果[{}]", result.name());
                    throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_REPORT_RESULT_UNKNOWN);
            }
        } catch (Exception e) {
            LOGGER.error("处理文件分发子任务[{}]结果异常", request.getTaskId(), e);
        }
    }

    @Override
    public void deleteTask(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        UUID parentTaskId = request.getId();
        // 检查子任务状态
        Boolean hasRunningTask = countSubTaskWithState(parentTaskId, Collections.singletonList(FileDistributionTaskStatus.RUNNING)) > 0;
        if (hasRunningTask) {
            LOGGER.error("任务[{}]存在运行中的子任务，不可删除", parentTaskId);
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_TASK_DELETE_FAIL_SUBTASK_RUNNING);
        }

        // 删除子任务
        List<DistributeSubTaskDTO> subTaskList = subTaskService.findByParentId(parentTaskId);
        subTaskList.forEach(subTaskDTO -> subTaskService.deleteById(subTaskDTO.getId()));

        // 检查参数引用情况
        DistributeTaskDTO taskDTO = taskService.findTaskById(parentTaskId);
        UUID parameterId = taskDTO.getParameterId();
        List<DistributeTaskDTO> parameterRelatedTaskList = taskService.findTaskByParameterId(parameterId);
        if (parameterRelatedTaskList.size() > 1) {
            LOGGER.debug("参数[{}]存在另外的任务引用记录，不删除参数", parameterId);
        } else {
            // 删除参数
            parameterService.deleteParameter(parameterId);
        }

        // 删除父任务
        taskService.deleteById(parentTaskId);
    }

    @Override
    public void cancelSubTask(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        UUID subTaskId = request.getId();
        DistributeSubTaskDTO subTaskDTO = subTaskService.findById(subTaskId);
        if (subTaskDTO.getStatus() == FileDistributionTaskStatus.CANCELED) {
            LOGGER.error("文件分发子任务[{}]已取消", subTaskDTO.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_SUB_TASK_HAS_CANCELED);
        }
        FileDistributionStashTaskStatus stashStatus = subTaskDTO.getStashStatus();
        if (stashStatus == FileDistributionStashTaskStatus.CANCELING || stashStatus == FileDistributionStashTaskStatus.PRE_CANCEL) {
            LOGGER.error("文件分发子任务[{}]正在取消中", subTaskDTO.getId());
            throw new BusinessException(BusinessKey.RCDC_RCO_FILE_DISTRIBUTION_SUB_TASK_IS_CANCELING);
        }
        subTaskService.changeSubTaskToCancel(subTaskDTO);
    }

    @Override
    public void retrySubTask(IdRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");

        DistributeSubTaskDTO subTaskDTO = subTaskService.findById(request.getId());
        // 重试前须将目标任务状态复位
        subTaskService.changeSubTaskToWaiting(subTaskDTO);
        // 更新任务信息
        subTaskDTO = subTaskService.findById(request.getId());
        DistributeTaskParameterDTO taskParameterDTO = getBasicInfo(new IdRequest(subTaskDTO.getParentTaskId()));

        enableMakeSeed(taskParameterDTO.getParameter().getDataList());

        runSubTask(subTaskDTO, taskParameterDTO.getParameter());
    }


    private void enableMakeSeed(List<DistributeParameterDataDTO> dataList) {
        // 任务中存在需要下载的app列表，则需要做种处理
        if (!CollectionUtils.isEmpty(dataList)) {
            List<UUID> appIdList = dataList.stream().map(DistributeParameterDataDTO::getId).collect(Collectors.toList());
            // 将未启动的和上次做种失败的也进行重试
            cbbDeskSoftMgmtAPI.updateDeskSoft(appIdList, NEED_SEED_MAKE_STATUS_LIST, SeedMakeStatusEnum.ENABLE_MAKE);
        }
    }


    private void runSubTask(DistributeSubTaskDTO subTaskDTO, DistributeParameterDTO parameterDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");
        Assert.notNull(parameterDTO, "parameterDTO cannot be null!");

        FileDistributionTaskStatus status = subTaskDTO.getStatus();
        FileDistributionStashTaskStatus stashStatus = subTaskDTO.getStashStatus();
        if (status != FileDistributionTaskStatus.WAITING || stashStatus == FileDistributionStashTaskStatus.QUEUED) {
            // 非等待中，或已在线程池的任务不重复执行
            return;
        }

        // 修改子任务的暂存状态
        subTaskService.changeStashStatus(subTaskDTO, FileDistributionStashTaskStatus.STASHED);

    }

    @Override
    public void rebuildSubTask(DistributeSubTaskDTO subTaskDTO) {
        Assert.notNull(subTaskDTO, "subTaskDTO cannot be null!");

        if (subTaskDTO.getStashStatus() != FileDistributionStashTaskStatus.QUEUED
                && subTaskDTO.getStashStatus() != FileDistributionStashTaskStatus.RUNNING) {
            LOGGER.debug("文件分发子任务[{}]暂存状态为[{}]，无需重建", subTaskDTO.getId(), subTaskDTO.getStashStatus());
            return;
        }

        LOGGER.info("开始重建文件分发子任务[{}]", subTaskDTO.getId());
        // 此处重置状态即可
        subTaskService.changeRunningSubTaskToWaiting(subTaskDTO);

    }

    @Override
    public DefaultPageResponse<TerminalGroupDesktopRelatedDTO> pageTerminalGroupDesktopRelated(
            TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO, Pageable pageable) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewTerminalGroupDesktopRelatedEntity> viewTerminalGroupDesktopRelatedList =
                viewTerminalGroupDesktopRelatedService.pageTerminalGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO, pageable);

        Page<TerminalGroupDesktopRelatedDTO> page = viewTerminalGroupDesktopRelatedList.map(viewTerminalGroupDesktopRelatedEntity -> {
            TerminalGroupDesktopRelatedDTO terminalGroupDesktopRelatedDTO = new TerminalGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(viewTerminalGroupDesktopRelatedEntity, terminalGroupDesktopRelatedDTO);
            // 根据云桌面id查询云桌面类型、会话类型
            convertDeskType(viewTerminalGroupDesktopRelatedEntity, terminalGroupDesktopRelatedDTO);
            return terminalGroupDesktopRelatedDTO;
        });

        DefaultPageResponse<TerminalGroupDesktopRelatedDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(page.stream().toArray(TerminalGroupDesktopRelatedDTO[]::new));
        defaultPageResponse.setTotal(page.getTotalElements());
        return defaultPageResponse;
    }

    @Override
    public List<TerminalGroupDesktopCountDTO> listTerminalGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO) {
        Assert.notNull(taskGetGroupDesktopCountDTO, "taskGetGroupDesktopCountDTO must not be null");
        return viewTerminalGroupDesktopRelatedService.listTerminalGroupDesktopCount(taskGetGroupDesktopCountDTO);
    }

    @Override
    public List<TerminalGroupDesktopRelatedDTO> listTerminalGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");
        List<ViewTerminalGroupDesktopRelatedEntity> viewTerminalGroupDesktopRelatedList =
                viewTerminalGroupDesktopRelatedService.listTerminalGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO);

        return viewTerminalGroupDesktopRelatedList.stream().map(entity -> {
            TerminalGroupDesktopRelatedDTO terminalGroupDesktopRelatedDTO = new TerminalGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(entity, terminalGroupDesktopRelatedDTO);
            // 根据云桌面id查询云桌面类型
            convertDeskType(entity, terminalGroupDesktopRelatedDTO);
            return terminalGroupDesktopRelatedDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<UserGroupDesktopRelatedDTO> pageUserGroupDesktopRelated(
            TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO, Pageable pageable) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");

        Page<ViewUserGroupDesktopRelatedEntity> viewUserGroupDesktopRelatedList =
                viewUserGroupDesktopRelatedService.pageUserGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO, pageable);

        Page<UserGroupDesktopRelatedDTO> page = viewUserGroupDesktopRelatedList.map(viewUserGroupDesktopRelatedEntity -> {
            UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO = new UserGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(viewUserGroupDesktopRelatedEntity, userGroupDesktopRelatedDTO);
            // 根据云桌面id查询云桌面类型、会话类型
            convertDeskType(viewUserGroupDesktopRelatedEntity, userGroupDesktopRelatedDTO);
            return userGroupDesktopRelatedDTO;
        });

        DefaultPageResponse<UserGroupDesktopRelatedDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(page.stream().toArray(UserGroupDesktopRelatedDTO[]::new));
        defaultPageResponse.setTotal(page.getTotalElements());
        return defaultPageResponse;
    }

    private void convertDeskType(ViewTerminalGroupDesktopRelatedEntity entity,
                                 TerminalGroupDesktopRelatedDTO dto) {
        UUID desktopId = dto.getCloudDesktopId();
        CloudDesktopDetailDTO desktopDetail;
        try {
            desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
            dto.setSessionType(desktopDetail.getSessionType());
            dto.setDeskType(desktopDetail.getDeskType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面信息失败，桌面id为[{}]", desktopId, e);
            dto.setDeskType(entity.getCbbImageType().name());
        }
    }

    private void convertDeskType(ViewUserGroupDesktopRelatedEntity viewUserGroupDesktopRelatedEntity,
                                 UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO) {
        UUID desktopId = userGroupDesktopRelatedDTO.getCloudDesktopId();
        CloudDesktopDetailDTO desktopDetail;
        try {
            desktopDetail = userDesktopMgmtAPI.getDesktopDetailById(desktopId);
            userGroupDesktopRelatedDTO.setSessionType(desktopDetail.getSessionType());
            userGroupDesktopRelatedDTO.setDeskType(desktopDetail.getDeskType());
        } catch (BusinessException e) {
            LOGGER.error("获取桌面信息失败，桌面id为[{}]", desktopId, e);
            userGroupDesktopRelatedDTO.setDeskType(viewUserGroupDesktopRelatedEntity.getCbbImageType().name());
        }
    }

    @Override
    public List<UserGroupDesktopCountDTO> listUserGroupDesktopCount(TaskGetGroupDesktopCountDTO taskGetGroupDesktopCountDTO) {
        Assert.notNull(taskGetGroupDesktopCountDTO, "taskGetGroupDesktopCountDTO must not be null");
        return viewUserGroupDesktopRelatedService.listUserGroupDesktopCount(taskGetGroupDesktopCountDTO);
    }

    @Override
    public List<UserGroupDesktopRelatedDTO> listUserGroupDesktopRelated(TaskSearchGroupDesktopRelatedDTO taskSearchGroupDesktopRelatedDTO) {
        Assert.notNull(taskSearchGroupDesktopRelatedDTO, "taskSearchGroupDesktopRelatedDTO must not be null");
        List<ViewUserGroupDesktopRelatedEntity> viewUserGroupDesktopRelatedList =
                viewUserGroupDesktopRelatedService.listUserGroupDesktopRelated(taskSearchGroupDesktopRelatedDTO);

        return viewUserGroupDesktopRelatedList.stream().map(entity -> {
            UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO = new UserGroupDesktopRelatedDTO();
            BeanUtils.copyProperties(entity, userGroupDesktopRelatedDTO);
            return userGroupDesktopRelatedDTO;
        }).collect(Collectors.toList());
    }

    @Override
    public DefaultPageResponse<DistributeTaskDetailDTO> pageDistributeTask(SearchDistributeTaskDTO searchDistributeTaskDTO, Pageable pageable) {
        Assert.notNull(searchDistributeTaskDTO, "searchDistributeTaskDTO must not be null");
        Assert.notNull(pageable, "pageable must not be null");
        Page<ViewDistributeTaskEntity> viewDistributeTaskPage = viewDistributeTaskService.pageDistributeTask(searchDistributeTaskDTO, pageable);

        Page<DistributeTaskDetailDTO> page = viewDistributeTaskPage.map(entity -> {
            DistributeTaskDetailDTO distributeTaskDetailDTO = new DistributeTaskDetailDTO();
            BeanUtils.copyProperties(entity, distributeTaskDetailDTO);
            return distributeTaskDetailDTO;
        });

        DefaultPageResponse<DistributeTaskDetailDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(page.stream().toArray(DistributeTaskDetailDTO[]::new));
        defaultPageResponse.setTotal(page.getTotalElements());
        return defaultPageResponse;
    }

}
