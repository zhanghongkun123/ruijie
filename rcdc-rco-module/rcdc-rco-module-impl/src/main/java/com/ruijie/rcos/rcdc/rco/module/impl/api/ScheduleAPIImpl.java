package com.ruijie.rcos.rcdc.rco.module.impl.api;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.common.query.DefaultConditionQueryRequestBuilder;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import org.apache.commons.lang3.ArrayUtils;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.ScheduleTaskTypeSortEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskAPI;
import com.ruijie.rcos.base.task.module.def.api.ScheduleTaskTypeAPI;
import com.ruijie.rcos.base.task.module.def.api.request.CreateScheduleTaskRequest;
import com.ruijie.rcos.base.task.module.def.api.request.EditScheduleTaskRequest;
import com.ruijie.rcos.base.task.module.def.api.request.ScheduleTaskPageRequest;
import com.ruijie.rcos.base.task.module.def.api.request.ScheduleTaskRequest;
import com.ruijie.rcos.base.task.module.def.dto.CronExpressionDTO;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskDTO;
import com.ruijie.rcos.base.task.module.def.dto.ScheduleTaskTypeDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.ScheduleAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.RcoScheduleTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoCreateScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoEditScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.dto.CronConvertDTO;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.CycleScheduleTaskValidatorHandler;
import com.ruijie.rcos.rcdc.rco.module.impl.conversion.data.DataConversionChain;
import com.ruijie.rcos.rcdc.rco.module.impl.timedtasks.handler.CronExpressionConvertHandler;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.quartz.TaskGroup;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;


/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年09月16日
 *
 * @author xgx
 */
public class ScheduleAPIImpl implements ScheduleAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleAPIImpl.class);

    private static final String ID = "id";

    private static final String POOL_MODEL = "poolModel";

    @Autowired
    DataConversionChain dataConversionChain;

    @Autowired
    private ScheduleTaskAPI scheduleTaskAPI;

    @Autowired
    private ScheduleTaskTypeAPI scheduleTaskTypeAPI;

    @Autowired
    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    @Override
    public RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> queryScheduleTask(UUID taskId) throws BusinessException {
        Assert.notNull(taskId, "taskId can not be null");

        ScheduleTaskDTO scheduleTaskDTO = scheduleTaskAPI.queryById(taskId);

        return getRcoScheduleTaskDTO(scheduleTaskDTO);
    }

    @Override
    public DefaultPageResponse<RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>>> queryScheduleTaskByPage(DefaultPageRequest pageRequest)
            throws BusinessException {
        Assert.notNull(pageRequest, "pageRequest can not be null");
        ScheduleTaskPageRequest scheduleTaskPageRequest = new ScheduleTaskPageRequest();
        scheduleTaskPageRequest.setGroup(TaskGroup.ADMIN_CONFIG);
        BeanUtils.copyProperties(pageRequest, scheduleTaskPageRequest);

        DefaultPageResponse<ScheduleTaskDTO> defaultPageResponse = scheduleTaskAPI.pageQuery(scheduleTaskPageRequest);
        ScheduleTaskDTO[] scheduleTaskDTOArr = defaultPageResponse.getItemArr();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取定时器列表，获取请求类型列表成功：{}", JSONObject.toJSONString(scheduleTaskDTOArr));
        }
        Map<String, String> id2NameMap = getScheduleTypeToScheduleTypeNameMap();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("获取定时器列表，类型id到名称映射成功：{}", JSONObject.toJSONString(id2NameMap));
        }

        ScheduleTaskDTO[] scheduleDTOArr = Optional.ofNullable(scheduleTaskDTOArr).orElse(new ScheduleTaskDTO[1]);
        int length = scheduleDTOArr.length;
        RcoScheduleTaskDTO[] rcoScheduleTaskDTOArr = new RcoScheduleTaskDTO[length];
        for (int i = 0; i < length; i++) {
            RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> rcoScheduleTaskDTO = getRcoScheduleTaskDTOWithoutData(scheduleDTOArr[i]);
            String scheduleTypeName = id2NameMap.get(scheduleDTOArr[i].getScheduleTypeCode());
            rcoScheduleTaskDTO.setScheduleTypeName(scheduleTypeName);
            rcoScheduleTaskDTOArr[i] = rcoScheduleTaskDTO;
        }
        return DefaultPageResponse.Builder.success(pageRequest.getLimit(), (int) defaultPageResponse.getTotal(), rcoScheduleTaskDTOArr);
    }

    private RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> getRcoScheduleTaskDTO(ScheduleTaskDTO scheduleTaskDTO)
            throws BusinessException {
        RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> rcoScheduleTaskDTO = getRcoScheduleTaskDTOWithoutData(scheduleTaskDTO);

        // 将json数据转成id-label数组
        String businessData = scheduleTaskDTO.getBusinessData();
        if (null != businessData) {
            Type type = new TypeReference<ScheduleDataDTO<UUID, String>>() { //
            }.getType();
            ScheduleDataDTO<UUID, String> scheduleDataDTO = JSON.parseObject(businessData, type);
            ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> toScheduleDataDTO = dataConversionChain.conversion(scheduleDataDTO);
            toScheduleDataDTO.setExtStorageId(scheduleDataDTO.getExtStorageId());
            toScheduleDataDTO.setBackupDuration(scheduleDataDTO.getBackupDuration());
            toScheduleDataDTO.setExternalStorageId(scheduleDataDTO.getExternalStorageId());
            toScheduleDataDTO.setExternalStorageName(scheduleDataDTO.getExternalStorageName());
            toScheduleDataDTO.setMaxBackupDuration(scheduleDataDTO.getMaxBackupDuration());
            toScheduleDataDTO.setMaxBackUp(scheduleDataDTO.getMaxBackUp());
            toScheduleDataDTO.setAllowCancel(scheduleDataDTO.getAllowCancel());
            toScheduleDataDTO.setAmount(scheduleDataDTO.getAmount());
            toScheduleDataDTO.setPlatformId(scheduleDataDTO.getPlatformId());
            rcoScheduleTaskDTO.setData(toScheduleDataDTO);
        }
        return rcoScheduleTaskDTO;
    }

    private RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> getRcoScheduleTaskDTOWithoutData(ScheduleTaskDTO scheduleTaskDTO)
            throws BusinessException {
        RcoScheduleTaskDTO<IdLabelEntry, GenericIdLabelEntry<String>> rcoScheduleTaskDTO = new RcoScheduleTaskDTO<>();
        CronConvertDTO cronConvertDTO = CronExpressionConvertHandler.parseCronExpression(scheduleTaskDTO.getCronExpression());
        BeanUtils.copyProperties(cronConvertDTO, rcoScheduleTaskDTO);
        BeanUtils.copyProperties(scheduleTaskDTO, rcoScheduleTaskDTO);
        return rcoScheduleTaskDTO;
    }

    @Override
    public void createScheduleTask(RcoCreateScheduleTaskRequest rcoCreateScheduleTaskRequest)
            throws BusinessException, AnnotationValidationException {
        Assert.notNull(rcoCreateScheduleTaskRequest, "rcoCreateScheduleTaskRequest can not be null");
        CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoCreateScheduleTaskRequest);
        validateScheduleTask(rcoCreateScheduleTaskRequest);
        CreateScheduleTaskRequest createScheduleTaskRequest = new CreateScheduleTaskRequest();
        wrapScheduleTaskRequest(rcoCreateScheduleTaskRequest, createScheduleTaskRequest);
        scheduleTaskAPI.add(createScheduleTaskRequest);
    }

    private void validateScheduleTask(RcoCreateScheduleTaskRequest scheduleTaskRequest) throws BusinessException {
        if (Objects.equals(scheduleTaskRequest.getScheduleTypeCode(), ScheduleTypeCodeConstants.DESKTOP_POOL_START_TYPR_CODE)) {
            // 动态池预启动，只支持配置动态池
            // 判断是否都是动态池
            if (checkDesktopPoolCount(scheduleTaskRequest.getData(), CbbDesktopPoolModel.DYNAMIC)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_NOT_DYNAMIC);
            }
        }
        if (ScheduleTypeCodeConstants.STATIC_POOL_SUPPORT_TASK_SET.contains(scheduleTaskRequest.getScheduleTypeCode())) {
            // 判断是否都是静态池
            if (checkDesktopPoolCount(scheduleTaskRequest.getData(), CbbDesktopPoolModel.STATIC)) {
                throw new BusinessException(BusinessKey.RCDC_RCO_PART_DESK_POOL_NOT_STATIC);
            }
        }
    }

    private boolean checkDesktopPoolCount(ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> data, CbbDesktopPoolModel poolModel)
            throws BusinessException {
        if (Objects.isNull(data)) {
            return false;
        }
        UUID[] poolIdArr = idLabelArrToIdArr(data.getDesktopPoolArr());
        if (ArrayUtils.isEmpty(poolIdArr)) {
            return false;
        }
        ConditionQueryRequestBuilder deskPoolBuilder = new DefaultConditionQueryRequestBuilder();
        deskPoolBuilder.in(ID, poolIdArr);
        deskPoolBuilder.eq(POOL_MODEL, poolModel);
        return desktopPoolMgmtAPI.countByConditions(deskPoolBuilder.build()) < poolIdArr.length;
    }

    @Override
    public void editScheduleTask(RcoEditScheduleTaskRequest rcoEditScheduleTaskRequest) throws BusinessException, AnnotationValidationException {
        Assert.notNull(rcoEditScheduleTaskRequest, "rcoEditScheduleTaskRequest can not be null");
        CycleScheduleTaskValidatorHandler.validateScheduleTask(rcoEditScheduleTaskRequest);
        EditScheduleTaskRequest editScheduleTaskRequest = new EditScheduleTaskRequest();
        wrapScheduleTaskRequest(rcoEditScheduleTaskRequest, editScheduleTaskRequest);
        scheduleTaskAPI.update(editScheduleTaskRequest);
    }

    @Override
    public ScheduleTaskTypeDTO[] sortScheduleTaskTypeList(ScheduleTaskTypeDTO[] scheduleTaskTypeDTOArr) {
        Assert.notEmpty(scheduleTaskTypeDTOArr, "scheduleTaskTypeDTOArr must not be empty");

        ScheduleTaskTypeDTO[] sortedDTOArr = new ScheduleTaskTypeDTO[ScheduleTaskTypeSortEnums.values().length];
        Arrays.stream(scheduleTaskTypeDTOArr).forEach(scheduleTaskTypeDTO -> {
            Integer sortIndex = ScheduleTaskTypeSortEnums.getSortByScheduleTaskName(scheduleTaskTypeDTO.getLabel());
            sortedDTOArr[sortIndex] = scheduleTaskTypeDTO;
        });

        // 过滤空值（IDV服务器和VDI服务器的定时任务类型数量不一样）
        ScheduleTaskTypeDTO[] sortedDTOFilterNullArr = new ScheduleTaskTypeDTO[scheduleTaskTypeDTOArr.length];
        int index = 0;
        for (ScheduleTaskTypeDTO scheduleTaskTypeDTO : sortedDTOArr) {
            if (Objects.isNull(scheduleTaskTypeDTO)) {
                continue;
            }
            sortedDTOFilterNullArr[index] = scheduleTaskTypeDTO;
            index++;
        }
        return sortedDTOFilterNullArr;
    }

    private Map<String, String> getScheduleTypeToScheduleTypeNameMap() {
        ScheduleTaskTypeDTO[] scheduleTaskTypeDTOArr = scheduleTaskTypeAPI.listQuery();
        if (ObjectUtils.isEmpty(scheduleTaskTypeDTOArr)) {
            return new HashMap<>();
        }
        return Arrays.stream(scheduleTaskTypeDTOArr).collect(Collectors.toMap(dto -> dto.getId(), dto -> dto.getLabel()));
    }

    private void wrapScheduleTaskRequest(RcoScheduleTaskRequest rcoScheduleTaskWebRequest, ScheduleTaskRequest scheduleTaskRequest)
            throws BusinessException {
        BeanUtils.copyProperties(rcoScheduleTaskWebRequest, scheduleTaskRequest);
        CronExpressionDTO cronExpressionDTO = new CronExpressionDTO();
        BeanUtils.copyProperties(rcoScheduleTaskWebRequest, cronExpressionDTO);
        String cronExpression = CronExpressionConvertHandler.generateExpression(rcoScheduleTaskWebRequest);
        scheduleTaskRequest.setCronExpression(cronExpression);

        // 将id-label数组转成json
        ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> scheduleDataDTO = rcoScheduleTaskWebRequest.getData();
        if (null != scheduleDataDTO) {
            ScheduleDataDTO<UUID, String> toScheduleDataDTO = new ScheduleDataDTO<>();
            toScheduleDataDTO.setDeskArr(idLabelArrToIdArr(scheduleDataDTO.getDeskArr()));
            toScheduleDataDTO.setDesktopPoolArr(idLabelArrToIdArr(scheduleDataDTO.getDesktopPoolArr()));
            toScheduleDataDTO.setTerminalGroupArr(idLabelArrToIdArr(scheduleDataDTO.getTerminalGroupArr()));
            toScheduleDataDTO.setUserGroupArr(idLabelArrToIdArr(scheduleDataDTO.getUserGroupArr()));
            toScheduleDataDTO.setUserArr(idLabelArrToIdArr(scheduleDataDTO.getUserArr()));
            toScheduleDataDTO.setTerminalArr(idLabelArrToIdArr(scheduleDataDTO.getTerminalArr()));
            toScheduleDataDTO.setExtStorageId(scheduleDataDTO.getExtStorageId());
            toScheduleDataDTO.setBackupDuration(scheduleDataDTO.getBackupDuration());
            toScheduleDataDTO.setExternalStorageId(scheduleDataDTO.getExternalStorageId());
            toScheduleDataDTO.setExternalStorageName(scheduleDataDTO.getExternalStorageName());
            toScheduleDataDTO.setMaxBackupDuration(scheduleDataDTO.getMaxBackupDuration());
            toScheduleDataDTO.setMaxBackUp(scheduleDataDTO.getMaxBackUp());
            toScheduleDataDTO.setAllowCancel(scheduleDataDTO.getAllowCancel());
            toScheduleDataDTO.setAmount(scheduleDataDTO.getAmount());
            toScheduleDataDTO.setDiskArr(idLabelArrToIdArr(scheduleDataDTO.getDiskArr()));
            toScheduleDataDTO.setDiskPoolArr(idLabelArrToIdArr(scheduleDataDTO.getDiskPoolArr()));
            toScheduleDataDTO.setPlatformId(scheduleDataDTO.getPlatformId());
            toScheduleDataDTO.setPlatformName(scheduleDataDTO.getPlatformName());
            scheduleTaskRequest.setBusinessData(JSON.toJSONString(toScheduleDataDTO));
        }
    }

    private UUID[] idLabelArrToIdArr(IdLabelEntry[] idLabelEntryArr) {
        if (null == idLabelEntryArr) {
            //
            return new UUID[0];
        }
        return Arrays.stream(idLabelEntryArr).map(item -> item.getId()).toArray(UUID[]::new);
    }

    private String[] idLabelArrToIdArr(GenericIdLabelEntry<String>[] idLabelStringDTOArr) {
        if (null == idLabelStringDTOArr) {
            //
            return new String[0];
        }
        return Arrays.stream(idLabelStringDTOArr).map(item -> item.getId()).toArray(String[]::new);
    }
}
