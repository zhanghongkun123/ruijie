package com.ruijie.rcos.rcdc.rco.module.web.ctrl.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskAPI;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskTypeAPI;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskDTO;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskTypeDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.backup.module.def.constant.ScheduleConstants;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.ExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.request.QueryExternalStorageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.EvaluationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ScheduleAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.RcoScheduleTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoCreateScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoEditScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.def.constants.UserConstants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.batchtask.BatchDeleteScheduleTaskHandle;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.dto.ListScheduleTaskTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request.CreateScheduleTaskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request.EditScheduleTaskWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request.SwitchScheduleTaskStateWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.request.TaskNameCheckDuplicationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.task.vo.TaskNameDuplicationVO;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.validation.ScheduleValidation;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.QuartzTaskState;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DuplicateRequest;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Description: 任务调度类
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年11月9日
 *
 * @author hhx
 */
@Controller
@EnableCustomValidate(validateClass = ScheduleValidation.class)
@RequestMapping("rco/task/schedule")
public class ScheduleCtrl {

    protected static final Logger LOGGER = LoggerFactory.getLogger(ScheduleCtrl.class);

    @Autowired
    private ScheduleTaskTypeAPI scheduleTaskTypeAPI;

    @Autowired
    private ScheduleAPI scheduleAPI;

    @Autowired
    private ScheduleTaskAPI scheduleTaskAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private EvaluationAPI evaluationAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;
    
    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;
    
    @Autowired
    private ExternalStorageMgmtAPI externalStorageMgmtAPI;

    /**
     * 创建定时器
     *
     * @param createScheduleTaskWebRequest 请求参数
     * @return 响应
     * @throws AnnotationValidationException 注解校验异常
     * @throws BusinessException             业务异常
     */
    @RequestMapping(value = "/create")
    @EnableCustomValidate(validateMethod = "scheduleTaskValidate")
    public DefaultWebResponse createScheduleTask(CreateScheduleTaskWebRequest createScheduleTaskWebRequest)
            throws AnnotationValidationException, BusinessException {
        Assert.notNull(createScheduleTaskWebRequest, "请求参数不能为空");
        RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest = new RcoCreateScheduleTaskRequest();
        BeanUtils.copyProperties(createScheduleTaskWebRequest, rcoCreateScheduleTaskRequest);

        try {
            scheduleAPI.createScheduleTask(rcoCreateScheduleTaskRequest);
            LOGGER.debug("创建定时器成功");
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_CREATE_SCHEDULE_TASK_SUCCESS, rcoCreateScheduleTaskRequest.getTaskName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("创建定时器失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_CREATE_SCHEDULE_TASK_FAIL, e, rcoCreateScheduleTaskRequest.getTaskName(),
                    e.getI18nMessage());
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_CREATE_SCHEDULE_TASK_FAIL, e,
                    rcoCreateScheduleTaskRequest.getTaskName(), e.getI18nMessage());
        }
    }

    /**
     * 删除定时任务
     *
     * @param idArrWebRequest  请求参数
     * @param batchTaskBuilder 批量任务建造者
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/delete")
    public DefaultWebResponse deleteSchedule(IdArrWebRequest idArrWebRequest, BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("删除定时器请求开始：{}", JSONObject.toJSONString(idArrWebRequest));
        }
        Assert.notNull(idArrWebRequest, "请求参数不能为空");
        UUID[] idArr = idArrWebRequest.getIdArr();
        boolean isBatch = idArr.length > 1;
        if (isBatch) {
            return batchDeleteScheduleTask(idArrWebRequest, batchTaskBuilder);
        } else {
            return deleteOneScheduleTask(idArr[0]);
        }
    }


    /**
     * 根据id查询定时器信息
     *
     * @param idWebRequest 请求参数
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/detail")
    public DefaultWebResponse queryScheduleTask(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "请求参数不能为空");

        RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> rcoScheduleTaskDTO = scheduleAPI.queryScheduleTask(idWebRequest.getId());
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data = rcoScheduleTaskDTO.getData();
        
        // 桌面备份填充云平台信息
        if (StringUtils.equals(rcoScheduleTaskDTO.getScheduleTypeCode(), ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE)) {
            try {
                UUID platformId = data.getPlatformId();
                CloudPlatformDTO cloudPlatformDTO = Objects.isNull(platformId) ? cloudPlatformManageAPI.getDefaultCloudPlatform() :
                        cloudPlatformManageAPI.getInfoById(platformId);
                data.setPlatformId(cloudPlatformDTO.getId());
                data.setPlatformName(cloudPlatformDTO.getName());
            } catch (BusinessException e) {
                LOGGER.error("查询云平台信息失败", e);
            }
        }

        if (data.getExtStorageId() != null && data.getPlatformId() != null) {

            try {
                QueryExternalStorageRequest storageRequest = new QueryExternalStorageRequest();
                storageRequest.setPlatformId(data.getPlatformId());
                storageRequest.setExternalStorageId(data.getExtStorageId());
                data.setExternalStorageName(externalStorageMgmtAPI.findExternalStorageInfoById(storageRequest).getName());
            } catch (BusinessException e) {
                LOGGER.error("查询外置存储信息失败:[{}]", e.getI18nMessage());
            }

        }

        return DefaultWebResponse.Builder.success(rcoScheduleTaskDTO);
    }

    /**
     * 编辑定时任务
     *
     * @param editScheduleTaskWebRequest 请求参数
     * @return 响应
     * @throws AnnotationValidationException 注解校验异常
     * @throws BusinessException             业务异常
     */
    @RequestMapping(value = "/edit")
    @EnableCustomValidate(validateMethod = "scheduleTaskValidate")
    public DefaultWebResponse editSchedule(EditScheduleTaskWebRequest editScheduleTaskWebRequest)
            throws AnnotationValidationException, BusinessException {
        Assert.notNull(editScheduleTaskWebRequest, "请求参数不能为空");

        RcoEditScheduleTaskRequest rcoEditScheduleTaskRequest = new RcoEditScheduleTaskRequest();
        BeanUtils.copyProperties(editScheduleTaskWebRequest, rcoEditScheduleTaskRequest);

        try {
            scheduleAPI.editScheduleTask(rcoEditScheduleTaskRequest);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_EDIT_SCHEDULE_TASK_SUCCESS, rcoEditScheduleTaskRequest.getTaskName());
            LOGGER.debug("编辑定时器请求结束");
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("编辑定时器失败", e);
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_EDIT_SCHEDULE_TASK_FAIL, e, rcoEditScheduleTaskRequest.getTaskName(),
                    e.getI18nMessage());
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_EDIT_SCHEDULE_TASK_FAIL, e, rcoEditScheduleTaskRequest.getTaskName(),
                    e.getI18nMessage());
        }
    }

    /**
     * 切换定时任务的状态
     *
     * @param switchScheduleTaskStateWebRequest 请求参数
     * @return 响应
     * @throws BusinessException             业务异常
     * @throws AnnotationValidationException 注解校验异常
     */
    @RequestMapping(value = "/switch")
    public DefaultWebResponse controlSwitch(SwitchScheduleTaskStateWebRequest switchScheduleTaskStateWebRequest) throws BusinessException,
            AnnotationValidationException {
        Assert.notNull(switchScheduleTaskStateWebRequest, "请求参数不能为空");
        RcoEditScheduleTaskRequest rcoEditScheduleTaskRequest = new RcoEditScheduleTaskRequest();
        // 查询一次，然后拼装进行更新
        RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> rcoScheduleTaskDTO =
                scheduleAPI.queryScheduleTask(switchScheduleTaskStateWebRequest.getId());
        BeanUtils.copyProperties(rcoScheduleTaskDTO, rcoEditScheduleTaskRequest);
        BeanUtils.copyProperties(switchScheduleTaskStateWebRequest, rcoEditScheduleTaskRequest);

        try {
            scheduleAPI.editScheduleTask(rcoEditScheduleTaskRequest);
            String businessKey = rcoEditScheduleTaskRequest.getQuartzTaskState() == QuartzTaskState.START
                    ? SysmanagerBusinessKey.BASE_SYS_MANAGE_START_SCHEDULE_TASK_SUCCESS
                    : SysmanagerBusinessKey.BASE_SYS_MANAGE_CLOSE_SCHEDULE_TASK_SUCCESS;
            auditLogAPI.recordLog(businessKey, rcoEditScheduleTaskRequest.getTaskName());
            LOGGER.debug("控制定时任务开关请求结束");
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("控制定时任务开关请求失败", e);
            String businessKey = rcoEditScheduleTaskRequest.getQuartzTaskState() == QuartzTaskState.START
                    ? SysmanagerBusinessKey.BASE_SYS_MANAGE_START_SCHEDULE_TASK_FAIL
                    : SysmanagerBusinessKey.BASE_SYS_MANAGE_CLOSE_SCHEDULE_TASK_FAIL;
            auditLogAPI.recordLog(businessKey, e, rcoEditScheduleTaskRequest.getTaskName(), e.getI18nMessage());
            throw new BusinessException(businessKey, e, rcoEditScheduleTaskRequest.getTaskName(), e.getI18nMessage());
        }
    }

    /**
     * 获取定时任务列表
     *
     * @param pageWebRequest 请求参数
     * @return 定时任务列表
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse listSchedule(PageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "请求参数不能为空");
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取定时器列表请求开始：{}", JSONObject.toJSONString(pageWebRequest));
        }

        DefaultPageRequest defaultPageRequest = new DefaultPageRequest();
        BeanUtils.copyProperties(pageWebRequest, defaultPageRequest);

        DefaultPageResponse defaultPageResponse = scheduleAPI.queryScheduleTaskByPage(defaultPageRequest);

        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

    /**
     * 获取定时任务类型列表
     *
     * @param defaultWebRequest web请求
     * @return 任务类型列表
     * @throws BusinessException 业务异常
     */
    @ApiVersions(value = {@ApiVersion(value = Version.V1_0_0, descriptions = {"获取定时任务类型"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"关闭评测功能时，不允许创建云桌面备份类型"})})
    @RequestMapping(value = "/type/list")
    public CommonWebResponse listTaskType(DefaultWebRequest defaultWebRequest) throws BusinessException {
        Assert.notNull(defaultWebRequest, "请求参数不能为空");
        LOGGER.debug("获取定时器类型列表开始");
        ScheduleTaskTypeDTO[] scheduleTaskTypeDTOArr = scheduleTaskTypeAPI.listQuery();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取定时器类型列表结束:{}", JSON.toJSONString(scheduleTaskTypeDTOArr));
        }

        // 定时任务不可以配置服务器定时备份, 第三方用户同步
        scheduleTaskTypeDTOArr = Arrays.asList(scheduleTaskTypeDTOArr).stream().filter(item ->
                        !(item.getId().equals(ScheduleConstants.RCDC_BACKUP))
                                && !item.getId().equals(UserConstants.THIRD_PARTY_USER_SYNC_CODE))
                .toArray(ScheduleTaskTypeDTO[]::new);

        if (!serverModelAPI.isVdiModel()) {
            scheduleTaskTypeDTOArr = Arrays.asList(scheduleTaskTypeDTOArr).stream()
                    .filter(item -> !(item.getId().equals(ScheduleTypeCodeConstants.CLOUD_DESK_START_SCHEDULE_TYPE_CODE)
                            || item.getId().equals(ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_SNAPSHOT_TYPR_CODE)
                            || item.getId().equals(ScheduleTypeCodeConstants.DESKTOP_POOL_START_TYPR_CODE)
                            || item.getId().equals(ScheduleTypeCodeConstants.DISK_CREATE_SNAPSHOT_TYPE_CODE)))
                    .toArray(ScheduleTaskTypeDTO[]::new);
        }

        // 如果没有开启评测功能，则不可创建配置云桌面备份
        if (Boolean.FALSE.equals(evaluationAPI.getEvaluationStrategy())) {
            scheduleTaskTypeDTOArr = Arrays.stream(scheduleTaskTypeDTOArr)
                    .filter(item -> !ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE.equals(item.getId()))
                    .toArray(ScheduleTaskTypeDTO[]::new);
        }

        // 将定时任务类型名称数组进行排序
        ScheduleTaskTypeDTO[] sortedDTOArr = scheduleAPI.sortScheduleTaskTypeList(scheduleTaskTypeDTOArr);
        return CommonWebResponse.success(new ListScheduleTaskTypeDTO(sortedDTOArr));
    }

    /**
     * 定时任务名称重复校验
     *
     * @param request 请求对象
     * @return 是否重复
     */
    @RequestMapping(value = "/checkDuplication")
    public DefaultWebResponse checkDuplication(TaskNameCheckDuplicationWebRequest request) {
        Assert.notNull(request, "请求参数不能为空");
        DuplicateRequest duplicateRequest = new DuplicateRequest();
        TaskNameDuplicationVO taskNameDuplicationVO = new TaskNameDuplicationVO();
        if (StringUtils.equals(ScheduleConstants.DEFAULT_BACKUP_SCHEDULE_NAME, request.getTaskName())) {
            LOGGER.info("[{}]任务名称已被服务器占用，不允许使用", request.getTaskName());
            taskNameDuplicationVO.setHasDuplication(true);
            return DefaultWebResponse.Builder.success(taskNameDuplicationVO);
        }

        duplicateRequest.setId(request.getId());
        duplicateRequest.setName(request.getTaskName());
        Boolean hasDuplication = scheduleTaskAPI.checkTaskNameDuplication(duplicateRequest);
        taskNameDuplicationVO.setHasDuplication(hasDuplication);
        return DefaultWebResponse.Builder.success(taskNameDuplicationVO);
    }

    private DefaultWebResponse batchDeleteScheduleTask(IdArrWebRequest idArrWebRequest, BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        String itemName = LocaleI18nResolver.resolve(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_ITEM_NAME);
        Iterator<BatchTaskItem> defaultBatchTaskItemList = Stream.of(idArrWebRequest.getIdArr()) //
                .map(id -> (BatchTaskItem) DefaultBatchTaskItem.builder() //
                        .itemId(id) //
                        .itemName(itemName) //
                        .build()) //
                .iterator();

        BatchTaskHandler<BatchTaskItem> batchDeleteTaskHandle =
                new BatchDeleteScheduleTaskHandle(defaultBatchTaskItemList, auditLogAPI, scheduleTaskAPI);
        BatchTaskSubmitResult batchTaskSubmitResult =
                batchTaskBuilder.setTaskName(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_SCHEDULE_TASK_NAME)
                        .setTaskDesc(SysmanagerBusinessKey.BASE_SYS_MANAGE_BATCH_DELETE_SCHEDULE_TASK_DESC) //
                        .registerHandler(batchDeleteTaskHandle) //
                        .start();
        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private DefaultWebResponse deleteOneScheduleTask(UUID id) throws BusinessException {
        ScheduleTaskDTO scheduleTaskDTO = null;
        try {
            scheduleTaskDTO = scheduleTaskAPI.queryById(id);

            scheduleTaskAPI.delete(id);

            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_DO_SUCCESS, scheduleTaskDTO.getTaskName());
            return DefaultWebResponse.Builder.success(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_SUCCESS, StringUtils.EMPTY);
        } catch (BusinessException e) {
            LOGGER.error("删除日志配置失败", e);
            String name = scheduleTaskDTO == null ? id.toString() : scheduleTaskDTO.getTaskName();
            auditLogAPI.recordLog(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_DO_FAIL, e, name, e.getI18nMessage());
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_DELETE_SCHEDULE_TASK_DO_FAIL, e, name, e.getI18nMessage());
        }
    }

}


