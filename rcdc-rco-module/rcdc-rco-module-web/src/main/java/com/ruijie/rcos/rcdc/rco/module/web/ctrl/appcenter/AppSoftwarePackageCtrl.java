package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTaskAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.request.CbbAppRelativeDesktopRequest;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageVersionDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageVersionListDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbStartConnectVmDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.ConnectVmStateDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.ValidateAppSoftwarePackageNameDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.AppSoftwarePackageVersionListRequestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppSoftwarePackageVersionState;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbDeskInfoForAppCenterPageRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDiskInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbQueryVmVncURLResultDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.rco.module.def.api.EstClientMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RemoteAssistInfoOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppDiskAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.softwarecontrol.dto.CbbAppRelativeDeskInfo;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.UnbindUamAppPrePublishVersionRelateDeskTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.AbortAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.CloneAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.CreateAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.PublishAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.RollbackAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.handler.UpdateAppSoftwarePackageHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.AppCreateBatchTaskSubmitResult;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.CanUpdateAppResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.task.ext.module.def.api.CbbBatchTaskProgressAPI;
import com.ruijie.rcos.rcdc.task.ext.module.def.dto.StateMachineAloneProgressDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.connectkit.api.data.base.PageResponse;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.Match;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.pagekit.api.match.ExactMatch;
import com.ruijie.rcos.sk.pagekit.api.match.FuzzyMatch;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


/**
 * Description: 软件应用包管理
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author chenl
 */
@Api(tags = "软件应用包管理")
@Controller
@RequestMapping("/rco/app/disk")
public class AppSoftwarePackageCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppSoftwarePackageCtrl.class);

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private RemoteAssistInfoOperateAPI remoteAssistInfoOperateAPI;

    @Autowired
    private EstClientMgmtAPI estClientMgmtAPI;

    @Autowired
    private CbbBatchTaskProgressAPI cbbBatchTaskProgressAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private UamAppDiskAPI uamAppDiskAPI;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private CbbUamAppTestTaskAPI cbbUamAppTestTaskAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    /**
     * 创建应用软件包
     *
     * @param createAppSoftwarePackageRequest 创建应用软件包页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "创建应用软件包")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"创建应用软件包"})})
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> createAppSoftwarePackage(CreateAppSoftwarePackageRequest createAppSoftwarePackageRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(createAppSoftwarePackageRequest, "createAppSoftwarePackageRequest is null");
        Assert.notNull(builder, "builder is null");
        Assert.notNull(sessionContext, "sessionContext is null");


        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_CREATE_TASK_NAME));

        UUID appSoftwarePackageId = UUID.randomUUID();
        CreateAppSoftwarePackageHandler createAppSoftwarePackageHandler = new CreateAppSoftwarePackageHandler(batchTaskItem, auditLogAPI,
                cbbAppSoftwarePackageMgmtAPI, createAppSoftwarePackageRequest, appSoftwarePackageId);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_CREATE_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_CREATE_DESC, createAppSoftwarePackageRequest.getName())
                .registerHandler(createAppSoftwarePackageHandler).start();
        // 保存权限数据
        generalPermissionHelper.savePermission(sessionContext, appSoftwarePackageId, AdminDataPermissionType.UAM_APP);

        AppCreateBatchTaskSubmitResult appCreateBatchTaskSubmitResult =
                new AppCreateBatchTaskSubmitResult(result.getTaskId(), result.getTaskName(), result.getTaskDesc(), result.getTaskStatus());
        appCreateBatchTaskSubmitResult.setAppSoftwarePackageId(appSoftwarePackageId);

        return CommonWebResponse.success(appCreateBatchTaskSubmitResult);
    }


    /**
     * 编辑应用软件包基本信息
     *
     * @param editAppSoftwarePackageRequest 编辑应用软件包基本信息页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation(value = "编辑应用软件包基本信息")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑应用软件包基本信息"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> editAppSoftwarePackage(EditAppSoftwarePackageRequest editAppSoftwarePackageRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(editAppSoftwarePackageRequest, "editAppSoftwarePackageRequest is null");
        Assert.notNull(builder, "builder is null");
        Assert.notNull(sessionContext, "sessionContext is null");
        AppSoftwarePackageDTO newAppSoftwarePackageDTO = editAppSoftwarePackageRequest.convertToDTO();

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, editAppSoftwarePackageRequest.getId(), AdminDataPermissionType.UAM_APP);
        try {
            cbbAppSoftwarePackageMgmtAPI.editBaseAppSoftwarePackage(newAppSoftwarePackageDTO);
        } catch (BusinessException e) {
            LOGGER.error("编辑应用软件包[" + newAppSoftwarePackageDTO.getName() + "]出错", e);
            auditLogAPI.recordLog(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_BASE_EDIT_FAIL_LOG, newAppSoftwarePackageDTO.getName(),
                    e.getI18nMessage());
            throw e;
        }
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(editAppSoftwarePackageRequest.getId());
        return CommonWebResponse.success(AppCenterBusinessKey.RCDC_APP_CENTER_MODULE_OPERATE_SUCCESS, appSoftwarePackageDTO);


    }


    /**
     * 校验应用软件包名称是否存在
     *
     * @param webRequest web请求参数
     * @return 校验结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "checkDuplication", method = RequestMethod.POST)
    public CommonWebResponse checkAppSoftwarePackageNameDuplication(ValidateAppSoftwarePackageNameWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        ValidateAppSoftwarePackageNameDTO validateAppSoftwarePackageNameDTO = new ValidateAppSoftwarePackageNameDTO();
        BeanUtils.copyProperties(webRequest, validateAppSoftwarePackageNameDTO);
        Boolean hasDuplication = cbbAppSoftwarePackageMgmtAPI.checkAppSoftwarePackageNameDuplication(validateAppSoftwarePackageNameDTO);
        CheckDuplicationWebResponse webResponse = new CheckDuplicationWebResponse();
        webResponse.setHasDuplication(hasDuplication);
        return CommonWebResponse.success(webResponse);
    }


    /**
     * 查询应用软件包详情
     *
     * @param webRequest web请求参数
     * @return 校验结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = {"getInfo", "detail"}, method = RequestMethod.POST)
    public CommonWebResponse<AppSoftwarePackageDTO> getAppSoftwarePackageInfo(GetAppSoftwarePackageRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(webRequest.getId());
        return CommonWebResponse.success(appSoftwarePackageDTO);
    }


    /**
     * 更新应用软件包
     *
     * @param webRequest 页面请求参数
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return 校验结果
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonWebResponse<?> updateAppSoftwarePackage(UpdateAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(webRequest.getId());

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getId(), AdminDataPermissionType.UAM_APP);

        // 判断是否有其他管理员正在编辑
        if (AppStatusEnum.EDITING == appSoftwarePackageDTO.getState()) {
            cbbAppSoftwarePackageMgmtAPI.verifyEditingAppUser(webRequest.getId(), sessionContext.getUserId());
        }

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_UPDATE_TASK_NAME));

        UpdateAppSoftwarePackageHandler updateAppSoftwarePackageHandler =
                new UpdateAppSoftwarePackageHandler(batchTaskItem, auditLogAPI, cbbAppSoftwarePackageMgmtAPI, appSoftwarePackageDTO);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_UPDATE_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_UPDATE_DESC, appSoftwarePackageDTO.getName())
                .setUniqueId(appSoftwarePackageDTO.getId()).registerHandler(updateAppSoftwarePackageHandler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 查询完成制作进度
     *
     * @param webRequest 页面请求参数
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "finish/progress", method = RequestMethod.POST)
    public CommonWebResponse<?> finishAppSoftwarePackageProgress(FinishAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        StateMachineAloneProgressDTO machineAloneProgressDTO = cbbBatchTaskProgressAPI.getStateMachineAloneProgressBySmId(webRequest.getId());
        return CommonWebResponse.success(machineAloneProgressDTO);
    }


    /**
     * 放弃更新应用软件包
     *
     * @param webRequest 页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "abort", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> abortAppSoftwarePackage(AbortAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(webRequest.getId());
        // 校验是否有放弃该记录权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getId(), AdminDataPermissionType.UAM_APP);

        UUID id = webRequest.getId();
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ABORT_TASK_NAME));

        AbortAppSoftwarePackageHandler abortAppSoftwarePackageHandler =
                new AbortAppSoftwarePackageHandler(batchTaskItem, auditLogAPI, cbbAppSoftwarePackageMgmtAPI, appSoftwarePackageDTO);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ABORT_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ABORT_DESC, appSoftwarePackageDTO.getName()).setUniqueId(id)
                .registerHandler(abortAppSoftwarePackageHandler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 复制应用软件包
     *
     * @param webRequest 页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "clone", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> cloneAppSoftwarePackage(CloneAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");

        UUID id = webRequest.getId();
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_COPY_TASK_NAME));

        CloneAppSoftwarePackageHandler cloneAppSoftwarePackageHandler =
                new CloneAppSoftwarePackageHandler(batchTaskItem, auditLogAPI, cbbAppSoftwarePackageMgmtAPI, webRequest);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_COPY_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_COPY_DESC, webRequest.getName()).setUniqueId(id)
                .registerHandler(cloneAppSoftwarePackageHandler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 发布应用软件包
     *
     * @param webRequest 页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "publish", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> publishAppSoftwarePackage(PublishAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(webRequest.getId());

        // 校验是否有发布该记录权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getId(), AdminDataPermissionType.UAM_APP);

        UUID id = webRequest.getId();
        CbbDeskInfoForAppCenterPageRequest request = new CbbDeskInfoForAppCenterPageRequest();
        request.setDeskStateList(Arrays.asList(CbbCloudDeskState.RUNNING));
        request.setDiskList(obtainRestorePointIdList(id));
        request.setLimit(1);
        PageResponse<CbbDeskDiskInfoDTO> cbbDeskDiskInfoDTOPageResponse = cbbDeskMgmtAPI.pageAttachAppDiskDesktopList(request);
        if (cbbDeskDiskInfoDTOPageResponse.getTotal() > 0) {
            // 抛出异常，存在开机状态的桌面，不允许执行发布。
            throw new BusinessException(BusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_PUBLISH_SUPPORT_ERROR);
        }

        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_PUBLISH_TASK_NAME));

        PublishAppSoftwarePackageHandler cloneAppSoftwarePackageHandler =
                new PublishAppSoftwarePackageHandler(batchTaskItem, auditLogAPI, cbbAppSoftwarePackageMgmtAPI, appSoftwarePackageDTO);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_PUBLISH_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_PUBLISH_DESC, appSoftwarePackageDTO.getName()).setUniqueId(id)
                .registerHandler(cloneAppSoftwarePackageHandler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 应用软件包版本回退
     *
     * @param webRequest 页面请求
     * @param builder 任务构造
     * @param sessionContext 会话上下文
     * @return WebResponse
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "rollback", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<?> rollbackAppSoftwarePackage(RollbackAppSoftwarePackageRequest webRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "request must not be null");
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(webRequest.getId());

        // 校验是否有回退该记录权限
        generalPermissionHelper.checkPermission(sessionContext, webRequest.getId(), AdminDataPermissionType.UAM_APP);

        UUID id = webRequest.getId();
        final BatchTaskItem batchTaskItem = new DefaultBatchTaskItem(UUID.randomUUID(),
                LocaleI18nResolver.resolve(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ROLLBACK_TASK_NAME));

        RollbackAppSoftwarePackageHandler rollbackAppSoftwarePackageHandler =
                new RollbackAppSoftwarePackageHandler(batchTaskItem, auditLogAPI, cbbAppSoftwarePackageMgmtAPI, appSoftwarePackageDTO);
        BatchTaskSubmitResult result = builder.setTaskName(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ROLLBACK_TASK_NAME)
                .setTaskDesc(AppCenterBusinessKey.RCDC_APP_CENTER_APP_SOFTWARE_PACKAGE_ROLLBACK_DESC, appSoftwarePackageDTO.getName()).setUniqueId(id)
                .registerHandler(rollbackAppSoftwarePackageHandler).start();
        return CommonWebResponse.success(result);
    }


    /**
     * 查询应用软件包关联的云桌面列表
     *
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/deskInfo/list", method = RequestMethod.POST)
    @ApiOperation("查询应用软件包关联的云桌面列表")
    public CommonWebResponse<DefaultPageResponse<CloudDesktopDTO>> list(PageQueryRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");
        Match[] matchArr = request.getMatchArr();
        String appSoftwarePackageIdStr = null;
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals("appSoftwarePackageId", ((ExactMatch) match).getFieldName())) {
                    appSoftwarePackageIdStr = String.valueOf(((ExactMatch) match).getValueArr()[0]);
                }
            }
        }
        if (appSoftwarePackageIdStr == null) {
            // 提示参数异常
            throw new BusinessException(AppCenterBusinessKey.RCDC_APP_CENTER_REQUEST_PARAM_SOFTWARE_PACKAGE_ID_NOT_EXIST);
        }

        UUID appSoftwarePackageId = UUID.fromString(appSoftwarePackageIdStr);
        CbbDeskInfoForAppCenterPageRequest appCenterPageRequest = new CbbDeskInfoForAppCenterPageRequest();
        appCenterPageRequest.setPage(request.getPage());
        appCenterPageRequest.setLimit(request.getLimit());
        appCenterPageRequest.setDiskList(obtainRestorePointIdList(appSoftwarePackageId));

        PageResponse<CbbDeskDiskInfoDTO> cbbDeskDiskInfoDTOPageResponse = cbbDeskMgmtAPI.pageAttachAppDiskDesktopList(appCenterPageRequest);
        DefaultPageResponse defaultPageResponse = new DefaultPageResponse();
        defaultPageResponse.setTotal(cbbDeskDiskInfoDTOPageResponse.getTotal());
        if (ArrayUtils.isNotEmpty(cbbDeskDiskInfoDTOPageResponse.getItems())) {
            List<UUID> deskIdList =
                    Arrays.stream(cbbDeskDiskInfoDTOPageResponse.getItems()).map(CbbDeskDiskInfoDTO::getDeskId).collect(Collectors.toList());
            List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.listDesktopByDesktopIds(deskIdList);
            defaultPageResponse.setItemArr(cloudDesktopDTOList.toArray(new CloudDesktopDTO[0]));
        } else {
            defaultPageResponse.setItemArr(new CloudDesktopDTO[0]);
        }
        return CommonWebResponse.success(defaultPageResponse);
    }

    /**
     * 启动应用软件包连接
     *
     * @param request 请求参数，应用软件包id
     * @param sessionContext 会话
     * @return web response
     * @throws BusinessException ex
     */
    @ApiOperation("启动应用软件包连接")
    @RequestMapping(value = "/startConnectTempVm")
    public DefaultWebResponse startConnectTempVm(IdWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null!");
        Assert.notNull(sessionContext, "sessionContext must not be null!");

        CbbStartConnectVmDTO connectVmDTO = new CbbStartConnectVmDTO();
        connectVmDTO.setAdminId(sessionContext.getUserId());
        connectVmDTO.setAppId(request.getId());
        connectVmDTO.setAdminName(sessionContext.getUserName());

        // 校验EstClient数量
        checkEstClientNum(request.getId());

        cbbAppSoftwarePackageMgmtAPI.startConnectTempVm(connectVmDTO);
        return DefaultWebResponse.Builder.success();
    }


    /**
     * 查询编辑中的应用软件包状态
     *
     * @param webRequest 请求参数
     * @param sessionContext sessionContext
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询编辑中的应用软件包状态")
    @RequestMapping(value = "queryState")
    public DefaultWebResponse queryImageTemplateEditState(IdWebRequest webRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        Assert.notNull(sessionContext, "sessionContext is not null");
        final ConnectVmStateDTO response = cbbAppSoftwarePackageMgmtAPI.queryTempVmState(webRequest.getId());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 查询VNC地址
     *
     * @param webRequest 请求查询
     * @return 查询结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("查询VNC地址")
    @RequestMapping(value = "queryVncCondition")
    public DefaultWebResponse queryVmVncURL(IdWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");
        try {
            final CbbQueryVmVncURLResultDTO response = cbbAppSoftwarePackageMgmtAPI.queryVncConnect(webRequest.getId());
            return DefaultWebResponse.Builder.success(response);
        } catch (BusinessException e) {
            LOGGER.error("查询vnc状态出错{}", e);
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_QUERY_VNC_CONDITION_FAIL_LOG, e, e.getI18nMessage());
        }

    }


    /**
     * 查询应用软件包关联的软件列表
     *
     * @param request 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/software/list", method = RequestMethod.POST)
    @ApiOperation("查询应用软件包关联的软件列表")
    public CommonWebResponse<DefaultPageResponse<AppSoftwarePackageVersionListDTO>> softwareList(PageQueryRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null.");
        Match[] matchArr = request.getMatchArr();
        String appSoftwarePackageIdStr = null;
        String softwareName = null;
        if (ArrayUtils.isNotEmpty(matchArr)) {
            for (Match match : matchArr) {
                if (match.getType() == Match.Type.EXACT && StringUtils.equals("appSoftwarePackageId", ((ExactMatch) match).getFieldName())) {
                    appSoftwarePackageIdStr = String.valueOf(((ExactMatch) match).getValueArr()[0]);
                } else if (match.getType() == Match.Type.FUZZY
                        && StringUtils.equals(Constants.SOFT_NAME, ((FuzzyMatch) match).getFieldNameArr()[0])) {
                    softwareName = String.valueOf(((FuzzyMatch) match).getValue());
                }
            }
        }
        if (appSoftwarePackageIdStr == null) {
            // 提示参数异常
            throw new BusinessException(AppCenterBusinessKey.RCDC_APP_CENTER_REQUEST_PARAM_SOFTWARE_PACKAGE_ID_NOT_EXIST);
        }

        AppSoftwarePackageVersionListRequestDTO requestDTO = new AppSoftwarePackageVersionListRequestDTO();
        requestDTO.setAppSoftwarePackageId(UUID.fromString(appSoftwarePackageIdStr));
        requestDTO.setSearchKeyword(softwareName);

        Pageable pageable;
        if (request.getSortArr() != null && request.getSortArr().length > NumberUtils.INTEGER_ZERO) {
            pageable = PageRequest.of(request.getPage(), request.getLimit(), Sort.Direction.valueOf(request.getSortArr()[0].getDirection().name()),
                    request.getSortArr()[0].getFieldName(), Constants.SOFT_NAME);
        } else {
            pageable = PageRequest.of(request.getPage(), request.getLimit(), Sort.Direction.ASC, Constants.SOFT_NAME);
        }

        DefaultPageResponse<AppSoftwarePackageVersionListDTO> packageVersionListDTODefaultPageResponse =
                cbbAppSoftwarePackageMgmtAPI.pageAppSoftwareVersionList(requestDTO, pageable);
        return CommonWebResponse.success(packageVersionListDTODefaultPageResponse);
    }

    /**
     * 查询应用软件包关联的版本列表
     *
     * @param webRequest 页面请求参数
     * @param sessionContext session信息
     * @return DataResult
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/version/list", method = RequestMethod.POST)
    @ApiOperation("查询应用软件包关联的版本列表")
    public CommonWebResponse<DefaultPageResponse<AppSoftwarePackageVersionDTO>> versionList(IdWebRequest webRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(webRequest, "request is null.");
        Assert.notNull(sessionContext, "sessionContext is null");
        UUID appSoftwarePackageId = webRequest.getId();
        List<AppSoftwarePackageVersionDTO> versionDTOList =
                cbbAppSoftwarePackageMgmtAPI.findByAppSoftwarePackageIdAndState(appSoftwarePackageId, null);

        DefaultPageResponse<AppSoftwarePackageVersionDTO> defaultPageResponse = new DefaultPageResponse<>();
        defaultPageResponse.setItemArr(versionDTOList.toArray(new AppSoftwarePackageVersionDTO[0]));
        defaultPageResponse.setTotal(versionDTOList.size());
        return CommonWebResponse.success(defaultPageResponse);
    }

    /**
     * 查看应用是否允许编辑
     *
     * @param request 请求对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/canUpdateApp")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @ApiOperation("查询应用是否允许编辑")
    public CommonWebResponse<CanUpdateAppResponse> canUpdateApp(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        boolean canUpdateApp = uamAppDiskAPI.canUpdateAppDisk(request.getId());
        return CommonWebResponse.success(new CanUpdateAppResponse(canUpdateApp));
    }

    /**
     * 设置测试为暂停状态且下发通知
     *
     * @param request 请求对象
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/pauseTest")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @ApiOperation("设置测试为暂停状态且下发通知")
    public CommonWebResponse<CanUpdateAppResponse> pauseTest(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");

        uamAppDiskAPI.pauseTest(request.getId());
        return CommonWebResponse.success();
    }


    /**
     * 查询应用磁盘待发布版本关联桌面列表
     *
     * @param request 查询请求参数
     * @return CommonWebResponse<PageQueryResponse < CbbAppRelativeDeskInfo>>响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/relateDesktop/list")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @ApiOperation("查询应用磁盘待发布版本关联桌面列表")
    public CommonWebResponse<PageQueryResponse<CbbAppRelativeDeskInfo>> listAppPrePublishVersionRelativeVDIDesktop(PageQueryRequest request)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");

        PageQueryResponse<CbbAppRelativeDeskInfo> cbbAppRelativeDeskInfoPageQueryResponse = uamAppDiskAPI.pageQueryRelativeDeskInfo(request);

        return CommonWebResponse.success(cbbAppRelativeDeskInfoPageQueryResponse);
    }


    /**
     * 关闭当前VDI应用磁盘关联运行中桌面
     *
     * @param request 业务请求
     * @param builder BatchTaskBuilder
     * @param sessionContext SessionContext
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "stopRelateDesktop")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @ApiOperation("关闭当前VDI应用磁盘关联运行中桌面")
    CommonWebResponse<BatchTaskSubmitResult> stopRelateDesktop(IdWebRequest request, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request can not be null");

        generalPermissionHelper.checkPermission(sessionContext, request.getId(), AdminDataPermissionType.UAM_APP);

        CbbUamAppDTO uamApp = cbbAppStoreMgmtAPI.getUamApp(request.getId());
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(request.getId());
        if (appSoftwarePackageDTO.getAppSoftwarePackageType() != CbbImageType.VDI) {
            return CommonWebResponse.success();
        }

        List<CbbAppRelativeDeskInfo> appPrePublishVersionRelativeDeskInfoList = getAppPrePublishVersionRelativeDeskInfoList(request.getId());
        if (ObjectUtils.isEmpty(appPrePublishVersionRelativeDeskInfoList)) {
            LOGGER.info("应用磁盘[{}]不存在待发布版本，或待发布版本未关联桌面", request.getId());
            return CommonWebResponse.success();
        }

        List<DefaultBatchTaskItem> batchTaskItemList = appPrePublishVersionRelativeDeskInfoList.stream()
                .map(item -> DefaultBatchTaskItem.builder().itemId(item.getDesktopId()).itemName(item.getDesktopName()).build())
                .collect(Collectors.toList());
        UnbindUamAppPrePublishVersionRelateDeskTaskHandler unbindUamAppPrePublishVersionRelateDeskTaskHandler =
                applicationContext.getBean(UnbindUamAppPrePublishVersionRelateDeskTaskHandler.class, batchTaskItemList, uamApp.getAppName());
        BatchTaskSubmitResult result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_TASK_NAME) //
                .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_TASK_DESC) //
                .enableParallel().registerHandler(unbindUamAppPrePublishVersionRelateDeskTaskHandler) //
                .start();

        return CommonWebResponse.success(result);
    }

    /**
     * 桌面是否关联进行中的测试
     *
     * @param request 业务请求
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "isTesting")
    @ApiVersions({@ApiVersion(value = Version.V3_2_0)})
    @ApiOperation("桌面是否关联进行中的测试")
    public CommonWebResponse<Boolean> isAppInTesting(IdWebRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        Boolean existsUsedApplication = cbbUamAppTestAPI.existsUsedApplication(request.getId());
        return CommonWebResponse.success(existsUsedApplication);
    }

    private List<CbbAppRelativeDeskInfo> getAppPrePublishVersionRelativeDeskInfoList(UUID appId) throws BusinessException {
        CbbAppRelativeDesktopRequest cbbAppRelativeDesktopRequest = new CbbAppRelativeDesktopRequest();
        cbbAppRelativeDesktopRequest.setAppId(appId);
        cbbAppRelativeDesktopRequest.setDeskType(CbbCloudDeskType.VDI);
        cbbAppRelativeDesktopRequest.setVersionState(AppSoftwarePackageVersionState.UNAVAILABLE);

        List<CbbAppRelativeDeskInfo> cbbAppRelativeDeskInfoList = uamAppDiskAPI.listAppRelativeDeskInfo(cbbAppRelativeDesktopRequest);
        return cbbAppRelativeDeskInfoList;
    }


    /**
     * 判断 EST Client 是否满足数量
     *
     * @throws BusinessException 未找到部署模式/超出限制大小
     */
    private void checkEstClientNum(UUID imageTemplateId) throws BusinessException {

        if (!cbbAppSoftwarePackageMgmtAPI.isVncEditingAppInfo(imageTemplateId)) {
            long vncCount = cbbImageTemplateMgmtAPI.getVncEditingImageNum() + remoteAssistInfoOperateAPI.remoteAssistNum()
                    + cbbAppSoftwarePackageMgmtAPI.getVncEditingAppNum();
            int vncLimit = estClientMgmtAPI.estClientLimit();
            if (vncCount + 1 > vncLimit) {
                throw new BusinessException(AppCenterBusinessKey.RCDC_APP_CENTER_REQUEST_PARAM_SOFTWARE_PACKAGE_EDIT_VNX_LIMIT);
            }
        }
    }


    private List<UUID> obtainRestorePointIdList(UUID appSoftwarePackageId) {

        List<AppSoftwarePackageVersionDTO> versionDTOList =
                cbbAppSoftwarePackageMgmtAPI.findByAppSoftwarePackageIdAndState(appSoftwarePackageId, null);
        List<UUID> diskIdList = versionDTOList.stream().filter(versionDTO -> versionDTO.getState() == AppSoftwarePackageVersionState.AVAILABLE)
                .map(AppSoftwarePackageVersionDTO::getDiskId).collect(Collectors.toList());
        return diskIdList;
    }
}
