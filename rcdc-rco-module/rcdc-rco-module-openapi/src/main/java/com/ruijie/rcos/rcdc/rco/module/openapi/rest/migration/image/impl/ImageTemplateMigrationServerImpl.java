package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.base.task.module.def.enums.TaskResult;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbNetworkMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.image.CbbImageCdromDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskNetworkDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageTemplateDriverDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbVswitchDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.MatchEqual;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCheckImageNameDuplicationDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbConfigVmForEditImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbCreateImageTemplateByOsFileDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateStartDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbPublishImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbNetworkStrategyMode;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbVswitchState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateGuestToolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.CmsUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ImageTemplateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ServerModelAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SystemBusinessMappingAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.TerminalDriverConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.AllowCreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.SystemBusinessMappingDTO;
import com.ruijie.rcos.rcdc.rco.module.def.migration.dto.SunnyStatusDTO;
import com.ruijie.rcos.rcdc.rco.module.def.terminaldriver.response.TerminalDriverConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.def.upgrade.dto.SyncUpgradeConsts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.MtoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.RestErrorCode;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.request.PageServerRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.Consts;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.ImageTemplateMigrationServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.CreateImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.CreateImageTemplateDriverInstallRecordDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.EditImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImageMigrationTaskIdDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.ImageTemplateAdvancedConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.QueryImageOperateTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.dto.SunnyInstallResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.image.enums.ImageOperateType;
import com.ruijie.rcos.rcdc.rco.module.openapi.service.RestErrorCodeMapping;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.StateMachineConfigAndBatchTaskItemDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.lockable.LockableExecutor;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineFactory;
import com.ruijie.rcos.sk.base.sm2.StateMachineMgmtAgent;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.kernel.request.DefaultPageQueryRequest;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskExecuteResult;
import com.ruijie.rcos.sk.taskkit.flow.api.TaskFlowRouter;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年04月07日
 *
 * @author xgx
 */
public class ImageTemplateMigrationServerImpl implements ImageTemplateMigrationServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageTemplateMigrationServerImpl.class);

    private static final String IMAGE_TEMPLATE_NOT_EXIST = "23250236";

    private static final String NETWORK_STRATEGY_PRE = "迁移网络策略_";

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    private static final Integer DEFAULT_CURRENT_PAGE = 0;

    private static final String CREATE_IMAGE_LOCK = "create_image_lock";

    private static final String SAVE_IMAGE_DRIVER_LOCK = "save_image_driver_lock";

    private static final Integer INTERVAL = 3;

    private static final Integer RETRY_COUNT = 5;

    @Autowired
    private CbbNetworkMgmtAPI cbbNetworkMgmtAPI;

    @Autowired
    private SystemBusinessMappingAPI systemBusinessMappingAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CmsUpgradeAPI cmsUpgradeAPI;

    @Autowired
    private ImageTemplateAPI imageTemplateAPI;

    @Autowired
    private TaskFlowRouter taskFlowRouter;

    @Autowired
    private TerminalDriverConfigAPI terminalDriverConfigAPI;


    @Autowired
    private StateMachineFactory stateMachineFactory;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    private static final String CREATE_IMAGE_STATE_MACHINE_NAME = "qcow2创建镜像模板";

    private static final String IMAGE_TEMPLATE_NOT_EXIST_ERROR_KEY = "23250270";

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;


    @Override
    public void createImageTemplate(final CreateImageTemplateDTO createImageTemplateDTO) throws BusinessException {
        Assert.notNull(createImageTemplateDTO, "createImageTemplateDTO can not be null");
        try {
            LockableExecutor.executeWithTryLock(CREATE_IMAGE_LOCK, () -> {
                UUID imageTemplateId;
                UUID osIsoFileId = getOsIsoFieldId(createImageTemplateDTO);
                SystemBusinessMappingDTO systemBusinessMappingDTO =
                        getSystemBusinessMappingDTO(createImageTemplateDTO.getId(), SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE);
                if (null == systemBusinessMappingDTO) {
                    imageTemplateId = UUID.randomUUID();
                    saveImageTemplateBusinessMapping(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, createImageTemplateDTO.getId(),
                            String.valueOf(imageTemplateId));

                } else {
                    LOGGER.info("镜像模版[{}]映射关系已存在", createImageTemplateDTO.getId());
                    imageTemplateId = UUID.fromString(systemBusinessMappingDTO.getDestId());
                    if (cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageTemplateId)) {
                        LOGGER.info("镜像模版[{}]已存在", createImageTemplateDTO.getId());
                        return;
                    }
                }

                CbbCheckImageNameDuplicationDTO cbbCheckImageNameDuplicationDTO = new CbbCheckImageNameDuplicationDTO();
                cbbCheckImageNameDuplicationDTO.setImageName(createImageTemplateDTO.getImageName());
                cbbCheckImageNameDuplicationDTO.setId(imageTemplateId);
                if (cbbImageTemplateMgmtAPI.checkImageNameDuplication(cbbCheckImageNameDuplicationDTO)) {
                    // 迁移场景，导入镜像默认未开启多版本
                    throw new BusinessException(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NAME_EXISTS, createImageTemplateDTO.getImageName());
                }
                CloudPlatformDTO defaultCloudPlatform = cloudPlatformManageAPI.getDefaultCloudPlatform();
                AllowCreateImageTemplateDTO allowCreateImageTemplateDTO = imageTemplateAPI.checkIsAllowCreate(null, defaultCloudPlatform.getId());
                if (!allowCreateImageTemplateDTO.getEnableCreate()) {
                    // 其他
                    throw new BusinessException(RestErrorCode.RCDC_CODE_IMAGE_BACK_OR_CREATE);
                }

                CbbCreateImageTemplateByOsFileDTO cbbCreateImageTemplateByOsFileDTO =
                        buildCbbCreateImageTemplateByOsFileDTO(createImageTemplateDTO, osIsoFileId, imageTemplateId);

                updateTaskId(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, createImageTemplateDTO.getId(), (taskIdDTO) -> { //
                    taskIdDTO.setCreateTaskId(cbbCreateImageTemplateByOsFileDTO.getTaskId());
                });

                LOGGER.info("开启创建镜像模版[{}][{}]", imageTemplateId, JSON.toJSONString(cbbCreateImageTemplateByOsFileDTO));
                cbbImageTemplateMgmtAPI.createImageTemplateByOsFile(cbbCreateImageTemplateByOsFileDTO);
                auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_CREATE_SUCCESS_LOG, createImageTemplateDTO.getImageName());

            }, Consts.LOCK_WAIT);
        } catch (Throwable e) {
            BusinessException businessException = RestErrorCodeMapping.convert2BusinessException(e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_CREATE_FAIL_LOG, (Exception) e, createImageTemplateDTO.getImageName(),
                    businessException.getI18nMessage());
            throw businessException;
        }
    }



    @Override
    public void createDriveInstallRecord(CreateImageTemplateDriverInstallRecordDTO createImageTemplateDriverInstallRecordDTO)
            throws BusinessException {
        Assert.notNull(createImageTemplateDriverInstallRecordDTO, "createImageTemplateDriverInstallRecordDTO can not be null");

        String driveType = createImageTemplateDriverInstallRecordDTO.getSupportCpu().getDriveType();
        String productId = createImageTemplateDriverInstallRecordDTO.getSupportCpu().getProductId();
        String imageTemplateName = null;
        try {

            String srcId = createImageTemplateDriverInstallRecordDTO.getId() + driveType;
            UUID imageTemplateId = getImageTemplateId(createImageTemplateDriverInstallRecordDTO.getId());

            CbbGetImageTemplateInfoDTO imageTemplateInfo = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
            imageTemplateName = imageTemplateInfo.getImageName();

            LockableExecutor.executeWithTryLock(SAVE_IMAGE_DRIVER_LOCK, () -> {
                CbbImageTemplateDriverDTO[] imageTemplateDriverInfoArr = Optional
                        .ofNullable(cbbImageTemplateMgmtAPI.findImageTemplateDriverInfos(imageTemplateId)).orElse(new CbbImageTemplateDriverDTO[0]);
                Optional<CbbImageTemplateDriverDTO> imageTemplateDriverDTOOptional =
                        Stream.of(imageTemplateDriverInfoArr).filter(item -> Objects.equals(item.getDriverType(), driveType)).findFirst();
                UUID driverId;
                if (imageTemplateDriverDTOOptional.isPresent()) {
                    CbbImageTemplateDriverDTO cbbImageTemplateDriverDTO = imageTemplateDriverDTOOptional.get();
                    driverId = cbbImageTemplateDriverDTO.getId();
                } else {
                    CbbCreateImageTemplateDriverDTO cbbCreateImageTemplateDriverDTO = new CbbCreateImageTemplateDriverDTO();
                    cbbCreateImageTemplateDriverDTO.setImageId(imageTemplateId);
                    cbbCreateImageTemplateDriverDTO.setDriverType(driveType);
                    cbbCreateImageTemplateDriverDTO.setPublished(false);
                    driverId = cbbImageTemplateMgmtAPI.insertImageTemplateInstallDriver(cbbCreateImageTemplateDriverDTO);
                }
                saveImageTemplateBusinessMapping(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE_DRIVER, srcId, driverId.toString());
            }, Consts.LOCK_WAIT);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_INSTALL_DRIVER_SUCCESS_LOG, imageTemplateName, driveType);
        } catch (Throwable e) {
            BusinessException businessException = RestErrorCodeMapping.convert2BusinessException(e);
            imageTemplateName = imageTemplateName == null ? createImageTemplateDriverInstallRecordDTO.getId() : imageTemplateName;
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_INSTALL_DRIVER_FAIL_LOG, (Exception) e, imageTemplateName, driveType,
                    businessException.getI18nMessage());
            throw businessException;
        }

    }

    @Override
    public PageQueryResponse<CbbImageTemplateDTO> pageQuery(PageServerRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        try {
            DefaultPageQueryRequest pageQueryRequest = new DefaultPageQueryRequest();
            pageQueryRequest.setLimit(Optional.ofNullable(request.getLimit()).orElse(DEFAULT_PAGE_SIZE));
            pageQueryRequest.setPage(Optional.ofNullable(request.getPage()).orElse(DEFAULT_CURRENT_PAGE));

            return cbbImageTemplateMgmtAPI.pageQuery(pageQueryRequest);
        } catch (Throwable ex) {
            throw RestErrorCodeMapping.convert2BusinessException(ex);
        }
    }

    @Override
    public CbbGetImageTemplateInfoDTO getDetail(String id) throws BusinessException {
        Assert.notNull(id, "id can not be null");

        SystemBusinessMappingDTO systemBusinessMappingDTO = getSystemBusinessMappingDTO(id, SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE);
        if (ObjectUtils.isEmpty(systemBusinessMappingDTO)) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_IMAGE_NOT_EXIST, id);
        }

        UUID imageTemplateId = UUID.fromString(systemBusinessMappingDTO.getDestId());

        try {
            return cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
        } catch (BusinessException ex) {
            if (Objects.equals(IMAGE_TEMPLATE_NOT_EXIST_ERROR_KEY, ex.getKey())) {
                // 由于镜像数据在状态机内部入库，可能出现调用完创建接口查询时无法获取到镜像信息的情况，此处等待15秒，若15秒仍然没有创建成功则异常提示
                if (isNeedWaitForImageTemplateInsert2Db(imageTemplateId) && waitForImageTemplateInsert2Db(imageTemplateId)) {
                    return cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
                }
            }
            throw RestErrorCodeMapping.convert2BusinessException(ex);
        } catch (Throwable ex) {
            throw RestErrorCodeMapping.convert2BusinessException(ex);
        }

    }

    private boolean isNeedWaitForImageTemplateInsert2Db(UUID imageTemplateId) {
        StateMachineMgmtAgent[] agentArr =
                Optional.ofNullable(stateMachineFactory.findByResourceId(imageTemplateId.toString())).orElse(new StateMachineMgmtAgent[0]);
        Optional<StateMachineMgmtAgent> stateMachineMgmtAgentOptional =
                Stream.of(agentArr).filter(item -> CREATE_IMAGE_STATE_MACHINE_NAME.equals(item.getName())).findFirst();
        return stateMachineMgmtAgentOptional.isPresent();
    }

    private boolean waitForImageTemplateInsert2Db(UUID imageTemplateId) {
        for (int i = 0; i < RETRY_COUNT; i++) {
            LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(INTERVAL));
            if (cbbImageTemplateMgmtAPI.checkImageTemplateExist(imageTemplateId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void editImageTemplate(String id, EditImageTemplateDTO editImageTemplateDTO) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        Assert.notNull(editImageTemplateDTO, "editImageTemplateDTO can not be null");
        CbbGetImageTemplateInfoDTO cbbImageTemplateDetailDTO = null;
        try {
            UUID imageTemplateId = getImageTemplateId(id);
            cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
            if (cbbImageTemplateDetailDTO.getGuestToolState() == ImageTemplateGuestToolState.GUEST_VERSION_SUCCESS
                    && cbbImageTemplateDetailDTO.getHasInstallScsiController()) {
                LOGGER.info("镜像[{}]编辑成功，无需再次编辑", id);
                return;
            }
            if (cbbImageTemplateDetailDTO.getImageState() == ImageTemplateState.EDITING) {
                LOGGER.info("镜像[{}]编辑中，无需再次编辑", id);
                return;
            }

            if (serverModelAPI.isIdvModel() || serverModelAPI.isMiniModel()) {
                imageTemplateAPI.checkHasImageRunning(imageTemplateId);
            }

            // 若当前系统不存在dhcp网络策略则创建
            UUID networkId = createDhcpNetworkStrategyIfNotExist();
            CbbConfigVmForEditImageTemplateDTO configVmForEditImageTemplateDTO =
                    buildConfigVmForEditImageTemplateArg(editImageTemplateDTO, imageTemplateId, networkId);
            cbbImageTemplateMgmtAPI.configVmForEditImageTemplate(configVmForEditImageTemplateDTO);
            LOGGER.info("修改镜像[{}]虚机配置成功", id);

            List<CbbImageCdromDTO> cdromList = cmsUpgradeAPI.getCmIso(imageTemplateId, cbbImageTemplateDetailDTO.getCbbOsType());
            CbbImageTemplateStartDTO imageTemplateStartDTO = buildStartImageTemplateArg(editImageTemplateDTO, imageTemplateId, cdromList);
            updateTaskId(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, id, (taskIdDTO) -> { //
                taskIdDTO.setEditTaskId(imageTemplateStartDTO.getTaskId());
            });

            cbbImageTemplateMgmtAPI.createAndRunVmForEditImageTemplate(imageTemplateStartDTO);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_EDIT_SUCCESS_LOG, cbbImageTemplateDetailDTO.getImageName());
            LOGGER.info("启动镜像[{}]编辑成功", id);
        } catch (Throwable e) {
            String image = cbbImageTemplateDetailDTO == null ? id : cbbImageTemplateDetailDTO.getImageName();
            BusinessException businessException = RestErrorCodeMapping.convert2BusinessException(e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_EDIT_FAIL_LOG, (Exception) e, image,
                    businessException.getI18nMessage());
            throw businessException;
        }
    }

    private CbbConfigVmForEditImageTemplateDTO buildConfigVmForEditImageTemplateArg(EditImageTemplateDTO editImageTemplateDTO, UUID imageTemplateId,
            UUID networkId) {
        ImageTemplateAdvancedConfigDTO advancedConfig = editImageTemplateDTO.getAdvancedConfig();
        CbbConfigVmForEditImageTemplateDTO configVmForEditImageTemplateDTO = new CbbConfigVmForEditImageTemplateDTO();
        configVmForEditImageTemplateDTO.setEnableNested(advancedConfig.getEnableNested());
        configVmForEditImageTemplateDTO.setCpuCoreCount(advancedConfig.getCpu());
        configVmForEditImageTemplateDTO.setDeskNetworkId(networkId);
        configVmForEditImageTemplateDTO.setDiskSize(advancedConfig.getSystemDisk());
        configVmForEditImageTemplateDTO.setImageTemplateId(imageTemplateId);
        configVmForEditImageTemplateDTO.setMemorySize(advancedConfig.getMemory());
        return configVmForEditImageTemplateDTO;
    }

    private CbbImageTemplateStartDTO buildStartImageTemplateArg(EditImageTemplateDTO editImageTemplateDTO, UUID imageTemplateId,
            List<CbbImageCdromDTO> cdromList) {
        CbbImageTemplateStartDTO imageTemplateStartDTO = new CbbImageTemplateStartDTO();
        imageTemplateStartDTO.setImageId(imageTemplateId);
        imageTemplateStartDTO.setCdromList(cdromList);
        imageTemplateStartDTO.setShouldAsync(editImageTemplateDTO.getShouldAsync());
        imageTemplateStartDTO.setTaskId(Optional.ofNullable(editImageTemplateDTO.getTaskId()).orElse(UUID.randomUUID()));
        imageTemplateStartDTO.setSystemDiskCtrlType(editImageTemplateDTO.getSystemDiskCtrlType());
        return imageTemplateStartDTO;
    }


    @Override
    public void publishImageTemplate(String id, StateMachineConfigAndBatchTaskItemDTO taskDTO) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        Assert.notNull(taskDTO, "taskDTO can not be null");
        CbbGetImageTemplateInfoDTO cbbImageTemplateDetailDTO = null;
        try {
            UUID imageTemplateId = getImageTemplateId(id);

            cbbImageTemplateDetailDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageTemplateId);
            if (cbbImageTemplateDetailDTO.getImageState() == ImageTemplateState.PUBLISHING) {
                LOGGER.info("镜像[{}]发布中，无需再次发布", id);
                return;
            }
            if (cbbImageTemplateDetailDTO.getLastRecoveryPointId() != null) {
                LOGGER.info("镜像[{}]已发布，无需再次发布", id);
                return;
            }

            CbbPublishImageTemplateDTO request = new CbbPublishImageTemplateDTO();
            request.setTaskId(Optional.ofNullable(taskDTO.getTaskId()).orElse(UUID.randomUUID()));
            request.setShouldAsync(taskDTO.getShouldAsync());
            request.setImageTemplateId(imageTemplateId);
            updateTaskId(SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, id, (taskIdDTO) -> taskIdDTO.setPublishTaskId(request.getTaskId()));

            cbbImageTemplateMgmtAPI.publishImageTemplate(request);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_PUBLISH_SUCCESS_LOG, cbbImageTemplateDetailDTO.getImageName());

        } catch (Throwable e) {
            String image = cbbImageTemplateDetailDTO == null ? id : cbbImageTemplateDetailDTO.getImageName();
            BusinessException businessException = RestErrorCodeMapping.convert2BusinessException(e);
            auditLogAPI.recordLog(MtoolBusinessKey.RCDC_OPENAPI_IMAGE_TEMPLATE_PUBLISH_FAIL_LOG, (Exception) e, image,
                    businessException.getI18nMessage());
            throw businessException;
        }
    }

    @Override
    public TaskExecuteResult getTask(String id, QueryImageOperateTaskDTO taskDTO) throws BusinessException {
        Assert.notNull(id, "id can not be null");
        Assert.notNull(taskDTO, "taskDTO can not be null");

        try {
            UUID taskId = getTaskId(id, taskDTO.getImageOperateType());
            TaskExecuteResult taskExecuteResult = taskFlowRouter.findTaskExecuteResult(taskId);
            if (taskExecuteResult.getTaskResult() == TaskResult.FAILURE) {
                throw new BusinessException(taskExecuteResult.getTaskErrCode(), taskExecuteResult.getTaskErrMsgArgArr());
            }
            return taskExecuteResult;
        } catch (Throwable e) {
            throw RestErrorCodeMapping.convert2BusinessException(e, RestErrorCode.RCDC_CODE_NORMAL_BUSINESS_EXCEPTION);
        }
    }

    /**
     * 查询镜像的sunny安装状态
     *
     * @param oldImageId 镜像id
     * @return 任务结果
     * @throws BusinessException 业务异常
     */
    @Override
    public SunnyInstallResponse getSunnyStatus(String oldImageId) throws BusinessException {
        Assert.notNull(oldImageId, "oldImageId can not be null");
        String imageId = getImageId(oldImageId);
        if (imageId != null) {
            SystemBusinessMappingDTO systemBusinessMappingDTO =
                    systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                    SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_SUNNY_STATUS, imageId);

            if (systemBusinessMappingDTO == null) {
                return new SunnyInstallResponse(RestErrorCode.RCDC_CODE_SUNNY_INSTALL_INFO_NOT_FOUND_ERROR);
            }
            SunnyStatusDTO sunnyStatusDTO = JSONObject.parseObject(systemBusinessMappingDTO.getContext(), SunnyStatusDTO.class);
            // 判断是sunny是否安装成功
            if (sunnyStatusDTO.getNeedInstallSunny() && BooleanUtils.isFalse(sunnyStatusDTO.getSunnyInstall())) {
                return new SunnyInstallResponse(RestErrorCode.RCDC_CODE_SUNNY_INSTALL_ERROR);
            }
        } else {
            return new SunnyInstallResponse(RestErrorCode.RCDC_CODE_IMAGE_TEMPLATE_NOT_FOUND);
        }
        return new SunnyInstallResponse();
    }


    /**
     * 判断镜像是否迁移成功，
     *
     * @param oldImageId 旧平台镜像ID
     * @return UUID 镜像ID
     */
    private String getImageId(String oldImageId) {

        SystemBusinessMappingDTO imageMapping =
                systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL,
                        SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE, oldImageId);
        return Optional.ofNullable(imageMapping).orElse(new SystemBusinessMappingDTO()).getDestId();
    }

    private UUID getImageTemplateId(String id) throws BusinessException {
        SystemBusinessMappingDTO systemBusinessMappingDTO = getSystemBusinessMappingDTO(id, SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE);
        if (ObjectUtils.isEmpty(systemBusinessMappingDTO)) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_IMAGE_NOT_EXIST, id);
        }
        return UUID.fromString(systemBusinessMappingDTO.getDestId());
    }

    private UUID getTaskId(String id, ImageOperateType type) throws BusinessException {
        SystemBusinessMappingDTO systemBusinessMappingDTO = getSystemBusinessMappingDTO(id, SyncUpgradeConsts.BUSINESS_TYPE_IMAGE_TEMPLATE);
        if (ObjectUtils.isEmpty(systemBusinessMappingDTO)) {
            throw new BusinessException(RestErrorCode.RCDC_CODE_IMAGE_NOT_EXIST, id);
        }
        ImageMigrationTaskIdDTO imageMigrationTaskIdDTO = JSON.parseObject(systemBusinessMappingDTO.getContext(), ImageMigrationTaskIdDTO.class);

        switch (type) {
            case CREATE:
                return imageMigrationTaskIdDTO.getCreateTaskId();
            case EDIT:
                return imageMigrationTaskIdDTO.getEditTaskId();
            case PUBLISH:
                return imageMigrationTaskIdDTO.getPublishTaskId();
            default:
                throw new RuntimeException("不支持的类型");
        }

    }


    private CbbCreateImageTemplateByOsFileDTO buildCbbCreateImageTemplateByOsFileDTO(CreateImageTemplateDTO createImageTemplateDTO, UUID osIsoFileId,
            UUID newImageTemplateId) {
        CbbCreateImageTemplateByOsFileDTO cbbCreateImageTemplateByOsFileDTO = new CbbCreateImageTemplateByOsFileDTO();
        cbbCreateImageTemplateByOsFileDTO.setNewImageTemplateId(newImageTemplateId);
        cbbCreateImageTemplateByOsFileDTO.setNewImageTemplateName(createImageTemplateDTO.getImageName());
        cbbCreateImageTemplateByOsFileDTO.setOsIsoFileId(osIsoFileId);
        cbbCreateImageTemplateByOsFileDTO.setCbbOsType(createImageTemplateDTO.getImageSystemType());
        cbbCreateImageTemplateByOsFileDTO.setNote(createImageTemplateDTO.getNote());
        cbbCreateImageTemplateByOsFileDTO.setCbbImageType(createImageTemplateDTO.getCbbImageType());
        cbbCreateImageTemplateByOsFileDTO.setPartType(createImageTemplateDTO.getPartType());

        UUID taskId = Optional.ofNullable(createImageTemplateDTO.getTaskId()).orElse(UUID.randomUUID());
        cbbCreateImageTemplateByOsFileDTO.setTaskId(taskId);
        cbbCreateImageTemplateByOsFileDTO.setShouldAsync(createImageTemplateDTO.getShouldAsync());

        return cbbCreateImageTemplateByOsFileDTO;
    }

    private UUID getOsIsoFieldId(CreateImageTemplateDTO createImageTemplateDTO) throws BusinessException {
        SystemBusinessMappingDTO osFileBusinessMappingDTO =
                getSystemBusinessMappingDTO(createImageTemplateDTO.getIsoImageFileName(), SyncUpgradeConsts.BUSINESS_TYPE_IMAGE);
        osFileBusinessMappingDTO =
                Optional.ofNullable(osFileBusinessMappingDTO).orElseThrow(() -> new BusinessException(RestErrorCode.RCDC_CODE_FILE_NOT_EXISTS));
        UUID osIsoFileId = UUID.fromString(osFileBusinessMappingDTO.getDestId());
        return osIsoFileId;
    }


    private UUID createDhcpNetworkStrategyIfNotExist() throws BusinessException {
        PageSearchRequest pageSearchRequest = new PageSearchRequest();
        MatchEqual matchEqual = new MatchEqual("networkMode", new Object[] {CbbNetworkStrategyMode.DHCP});
        pageSearchRequest.setMatchEqualArr(new MatchEqual[] {matchEqual});
        DefaultPageResponse<CbbDeskNetworkDetailDTO> pageResponse = cbbNetworkMgmtAPI.pageQuery(pageSearchRequest);
        if (ObjectUtils.isEmpty(pageResponse.getItemArr())) {
            CbbDeskNetworkDTO cbbDeskNetworkDTO = new CbbDeskNetworkDTO();
            UUID networkId = UUID.randomUUID();
            cbbDeskNetworkDTO.setId(networkId);

            cbbDeskNetworkDTO.setDeskNetworkName(NETWORK_STRATEGY_PRE + systemBusinessMappingAPI.obtainMappingSequenceVal());
            cbbDeskNetworkDTO.setNetworkMode(CbbNetworkStrategyMode.DHCP);
            CbbVswitchDTO cbbVswitchDTO = getCbbVswitchDTO();
            cbbDeskNetworkDTO.setVswitchId(cbbVswitchDTO.getId());
            cbbNetworkMgmtAPI.createDeskNetwork(cbbDeskNetworkDTO);
            LOGGER.info("创建网络策略[{}]成功", networkId);
            return networkId;
        }
        CbbDeskNetworkDetailDTO cbbDeskNetworkDetailDTO = pageResponse.getItemArr()[0];
        return cbbDeskNetworkDetailDTO.getId();
    }

    private void saveImageTemplateBusinessMapping(String businessType, String srcId, String destId) {
        SystemBusinessMappingDTO sourceSystemBusinessMapping = getSystemBusinessMappingDTO(srcId, businessType);
        if (ObjectUtils.isEmpty(sourceSystemBusinessMapping)) {
            SystemBusinessMappingDTO systemBusinessMappingDTO = new SystemBusinessMappingDTO();
            systemBusinessMappingDTO.setSystemType(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL);
            systemBusinessMappingDTO.setBusinessType(businessType);
            systemBusinessMappingDTO.setSrcId(srcId);
            systemBusinessMappingDTO.setDestId(destId);
            systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMappingDTO);
            return;
        }
        if (!Objects.equals(destId, sourceSystemBusinessMapping.getDestId())) {
            LOGGER.warn("对象[{}]目标ID由{}变更成{}", srcId, sourceSystemBusinessMapping.getDestId(), destId);
            sourceSystemBusinessMapping.setDestId(destId);
            systemBusinessMappingAPI.saveSystemBusinessMapping(sourceSystemBusinessMapping);
        }

    }

    private void updateTaskId(String businessType, String srcId, Consumer<ImageMigrationTaskIdDTO> taskIdSetter) {
        SystemBusinessMappingDTO systemBusinessMapping = getSystemBusinessMappingDTO(srcId, businessType);
        ImageMigrationTaskIdDTO imageMigrationTaskIdDTO = JSON.parseObject(systemBusinessMapping.getContext(), ImageMigrationTaskIdDTO.class);
        imageMigrationTaskIdDTO = Optional.ofNullable(imageMigrationTaskIdDTO).orElse(new ImageMigrationTaskIdDTO());
        taskIdSetter.accept(imageMigrationTaskIdDTO);
        systemBusinessMapping.setContext(JSON.toJSONString(imageMigrationTaskIdDTO));
        systemBusinessMappingAPI.saveSystemBusinessMapping(systemBusinessMapping);

    }

    private CbbVswitchDTO getCbbVswitchDTO() throws BusinessException {
        List<CbbVswitchDTO> cbbVswitchList = Optional.ofNullable(cbbNetworkMgmtAPI.listVswitch()).orElse(new ArrayList<>());
        Optional<CbbVswitchDTO> vswitchOptional = cbbVswitchList.stream().filter(item -> item.getState() == CbbVswitchState.AVAILABLE).findFirst();
        CbbVswitchDTO cbbVswitchDTO = vswitchOptional.orElseThrow(() -> new BusinessException(RestErrorCode.RCDC_CODE_VSWITCH_NOT_EXIST));
        return cbbVswitchDTO;
    }


    private SystemBusinessMappingDTO getSystemBusinessMappingDTO(String srcId, String businessType) {
        return systemBusinessMappingAPI.findSystemBusinessMapping(SyncUpgradeConsts.SYSTEM_TYPE_MTOOL, businessType, srcId);
    }

}
