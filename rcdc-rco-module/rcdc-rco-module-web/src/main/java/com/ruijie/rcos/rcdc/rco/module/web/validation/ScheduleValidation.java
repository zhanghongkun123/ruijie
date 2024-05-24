package com.ruijie.rcos.rcdc.rco.module.web.validation;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.schedule.ScheduleDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.ScheduleTypeCodeConstants;
import com.ruijie.rcos.rcdc.rco.module.web.PublicBusinessKey;
import com.ruijie.rcos.sk.webmvc.api.vo.GenericIdLabelEntry;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rco.module.def.api.request.schedule.RcoScheduleTaskRequest;
import com.ruijie.rcos.rcdc.rco.module.def.timedtasks.validation.CycleScheduleTaskValidatorHandler;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.Objects;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月27日
 *
 * @author xgx validation
 */
@Service
public class ScheduleValidation {
    /**
     * 创建定时任务参数校验
     * 
     * @param scheduleTaskRequest 请求参数
     * @throws BusinessException 业务异常
     * @throws AnnotationValidationException 参数校验异常
     */
    public void scheduleTaskValidate(RcoScheduleTaskRequest scheduleTaskRequest) throws BusinessException, AnnotationValidationException {
        Assert.notNull(scheduleTaskRequest, "请求不能为空");
        try {
            CycleScheduleTaskValidatorHandler.validateScheduleTask(scheduleTaskRequest);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(PublicBusinessKey.RCDC_TIME_PATTERN_ILLEGAL, e);
        }
        // 桌面备份校验参数
        if (StringUtils.equals(scheduleTaskRequest.getScheduleTypeCode(), ScheduleTypeCodeConstants.CLOUD_DESK_CREATE_BACKUP_TYPR_CODE)) {
            ScheduleDataDTO<IdLabelEntry, GenericIdLabelEntry<String>> taskRequestData = scheduleTaskRequest.getData();
            if (Objects.isNull(taskRequestData.getPlatformId()) || Objects.isNull(taskRequestData.getExtStorageId())) {
                throw new BusinessException(BusinessKey.RCDC_RCO_CREATE_DESKTOP_BACKUP_SCHEDULED_TASK_PARAM_ERROR);
            }
        }
    }
}
