package com.ruijie.rcos.rcdc.rco.module.impl.sm.image.sync.slave.publish;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbPublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.common.sm.AbstractTaskExecuteProcessor;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.image.PublishSyncingImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateTaskHandle;
import com.ruijie.rcos.sk.base.sm2.exception.StateTaskExcludeExecuteException;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_EXIST_ENABLE_MULTIPLE_IMAGE_TEMPLATE_IN_PUBLISHING;

/**
 * Description: 镜像发布Processor，等待其他镜像完成
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/7/3
 *
 * @author zhiweiHong
 */
@Service
public class PublishImageWithWaitProcessor extends AbstractTaskExecuteProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublishImageWithWaitProcessor.class);

    @Autowired
    private CbbImageTemplateMgmtAPI imageTemplateMgmtAPI;

    @Autowired
    private PublishStageContextResolver resolver;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private RcoGlobalParameterAPI globalParameterAPI;

    private static final String TASK_KEY = "publish_image";

    private static final Long DEFAULT_RETRY_TIME = TimeUnit.SECONDS.toMillis(10);

    private static final String PUBLISH_IMAGE_MAX_TIME = "publish_image_max_time";

    @Override
    protected StateTaskHandle.ProcessResult innerDoProcess(StateTaskHandle.StateProcessContext context, UUID taskId) throws BusinessException {


        PublishSyncingImageTemplateDTO dto = resolver.resolveDTO(context);

        LOGGER.info("发布镜像模版[{}]", JSON.toJSONString(dto));

        CbbPublishSyncingImageTemplateDTO request = new CbbPublishSyncingImageTemplateDTO();
        request.setTaskId(taskId);

        CbbImageTemplateSnapshotDTO cbbImageTemplateSnapshotDTO = new CbbImageTemplateSnapshotDTO();
        BeanUtils.copyProperties(dto.getPublishSnapshot(), cbbImageTemplateSnapshotDTO);
        cbbImageTemplateSnapshotDTO.setId(UUID.randomUUID());

        String imageVersionName = dto.getPublishSnapshot().getName();
        cbbImageTemplateSnapshotDTO.setName(imageVersionName);
        request.setPublishSnapshot(cbbImageTemplateSnapshotDTO);
        request.setImageTemplateId(dto.getImageTemplateId());
        request.setTaskId(taskId);

        imageTemplateMgmtAPI.publishSyncingImageSync(request);

        return StateTaskHandle.ProcessResult.next();
    }


    @Override
    protected StateTaskHandle.ProcessResult postProcess(StateTaskHandle.StateProcessContext context) throws Exception {


        PublishSyncingImageTemplateDTO dto = resolver.resolveDTO(context);
        LOGGER.info("发布镜像成功[{}]", dto.getImageTemplateId());
        CbbImageTemplateDetailDTO imageTemplateDetail = imageTemplateMgmtAPI.getImageTemplateDetail(dto.getImageTemplateId());
        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_SLAVE_PUBLISH_SUCCESS, imageTemplateDetail.getImageName());


        UnifiedManageDataRequest request = new UnifiedManageDataRequest();
        request.setUnifiedManageDataId(dto.getPublishSnapshot().getUnifiedManageDataId());
        request.setRelatedType(UnifiedManageFunctionKeyEnum.IMAGE_SNAPSHOT);
        request.setRelatedId(imageTemplateDetail.getLastRecoveryPointId());
        LOGGER.info("保存镜像[{}]统一管理id为：[{}]", dto.getImageTemplateId(), JSON.toJSONString(request));
        unifiedManageDataService.saveUnifiedManage(request);

        return StateTaskHandle.ProcessResult.next();
    }


    @Override
    protected StateTaskHandle.ProcessResult exceptionPostProcess(TaskExecuteResult taskFlowDTO, StateTaskHandle.StateProcessContext context)
            throws Exception {
        PublishSyncingImageTemplateDTO dto = resolver.resolveDTO(context);
        String exceptionKey = taskFlowDTO.getTaskErrCode();
        if (RCDC_CLOUDDESKTOP_EXIST_ENABLE_MULTIPLE_IMAGE_TEMPLATE_IN_PUBLISHING.equals(exceptionKey)) {
            int rertyCount = 0;

            Long maxRetryCount = getMaxRetryCont();
            while (rertyCount < maxRetryCount) {
                LOGGER.warn("发布镜像模版[{}], 存在版本镜像处于发布中，等待后重试！", dto.getImageTemplateId());
                try {
                    super.retryDoProcess(context);
                    return StateTaskHandle.ProcessResult.retry(getDefaultRetryTime());
                } catch (BusinessException ex) {
                    if (!RCDC_CLOUDDESKTOP_EXIST_ENABLE_MULTIPLE_IMAGE_TEMPLATE_IN_PUBLISHING.equals(ex.getKey())) {
                        throw ex;
                    }
                    LOGGER.warn("发布镜像模版[{}]异常，线程等待后重试！", dto.getImageTemplateId());
                    Thread.sleep(DEFAULT_RETRY_TIME);
                }
                rertyCount++;
            }

            CbbImageTemplateDTO cbbImageTemplateDTO = imageTemplateMgmtAPI.getCbbImageTemplateDTO(dto.getImageTemplateId());
            throw new BusinessException(BusinessKey.RCDC_RCO_PUBLISHING_OVER_MAX_RETRY_COUNT, cbbImageTemplateDTO.getImageTemplateName());
        }

        return super.exceptionPostProcess(taskFlowDTO, context);
    }

    @Override
    public StateTaskHandle.ProcessResult doProcessExceptionTranslator(StateTaskHandle.StateProcessContext context, Exception ex) throws Exception {
        Assert.notNull(context, "context can not be null");
        Assert.notNull(ex, "ex can not be null");

        PublishSyncingImageTemplateDTO dto = resolver.resolveDTO(context);
        if (ex instanceof BusinessException) {
            BusinessException businessException = (BusinessException) ex;

            if (RCDC_CLOUDDESKTOP_EXIST_ENABLE_MULTIPLE_IMAGE_TEMPLATE_IN_PUBLISHING.equals(businessException.getKey())) {
                Long maxRetryCount = getMaxRetryCont();
                if (context.getRetryCount() < maxRetryCount) {
                    LOGGER.warn("发布镜像模版[{}], 存在版本镜像处于发布中，等待[{}]后重试！", dto.getImageTemplateId(), DEFAULT_RETRY_TIME);
                    return StateTaskHandle.ProcessResult.retry(DEFAULT_RETRY_TIME);
                }


                CbbImageTemplateDTO cbbImageTemplateDTO = imageTemplateMgmtAPI.getCbbImageTemplateDTO(dto.getImageTemplateId());

                throw new BusinessException(BusinessKey.RCDC_RCO_PUBLISHING_OVER_MAX_RETRY_COUNT, ex, cbbImageTemplateDTO.getImageTemplateName());
            }
        }

        if (ex instanceof StateTaskExcludeExecuteException) {
            LOGGER.warn("发布镜像模版[{}], 存在版本镜像处于发布中，等待[{}]后重试！", dto.getImageTemplateId(), DEFAULT_RETRY_TIME);
            return StateTaskHandle.ProcessResult.retry(DEFAULT_RETRY_TIME);
        }


        throw ex;

    }

    private Long getMaxRetryCont() {
        FindParameterResponse parameter = globalParameterAPI.findParameter(new FindParameterRequest(PUBLISH_IMAGE_MAX_TIME));
        return Long.parseLong(parameter.getValue()) / DEFAULT_RETRY_TIME;
    }

    @Override
    protected String getTaskKey() {
        return TASK_KEY;
    }
}
