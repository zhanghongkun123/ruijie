package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserGroupDetailDTO;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdminMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacUserGroupMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.*;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.*;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DataSourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DeliveryStatusEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RequestSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.aaa.AaaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.CheckAppNameDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.UamDeliveryAppVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util.SortUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.validation.AppDeliveryValidation;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalGroupMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalGroupDetailDTO;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdArrWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.*;
import java.util.stream.Stream;

;

/**
 * Description: 应用交付
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/03 13:53
 *
 * @author coderLee23
 */
@Api(tags = "应用中心-应用交付")
@Controller
@RequestMapping("/rco/appCenter/appDelivery")
@EnableCustomValidate(validateClass = AppDeliveryValidation.class)
public class AppDeliveryController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeliveryController.class);

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private IacUserGroupMgmtAPI cbbUserGroupAPI;

    @Autowired
    private CbbTerminalGroupMgmtAPI cbbTerminalGroupMgmtAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private IacAdminMgmtAPI baseAdminMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    /**
     * 应用交付组列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用交付组列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamDeliveryGroupDTO>> pageUamDeliveryGroup(GetDeliveryGroupPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDeliveryGroupDTO searchDeliveryGroupDTO = new SearchDeliveryGroupDTO();
        BeanUtils.copyProperties(request, searchDeliveryGroupDTO);
        searchDeliveryGroupDTO.setName(request.getSearchKeyword());

        UUID appId = request.getAppId();
        if (Objects.nonNull(appId)) {
            // 如果传入的应用
            CbbUamAppDTO uamApp = cbbAppStoreMgmtAPI.getUamApp(appId);
            AppDeliveryTypeEnum appDeliveryType =
                    uamApp.getAppType() == AppTypeEnum.APP_SOFTWARE_PACKAGE ? AppDeliveryTypeEnum.APP_DISK : AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE;
            searchDeliveryGroupDTO.setAppDeliveryType(appDeliveryType);
        }

        generalPermissionHelper.setPermissionParam(sessionContext, searchDeliveryGroupDTO);
        // 分页中处理数据权限
        DefaultPageResponse<UamDeliveryGroupDTO> uamAppPageResponse = appDeliveryMgmtAPI.pageUamDeliveryGroup(searchDeliveryGroupDTO, pageable);
        return CommonWebResponse.success(uamAppPageResponse);
    }


    /**
     * 获取交付详情
     *
     * @param idWebRequest 交付组id
     * @param sessionContext 上下文
     * @return CommonWebResponse<CbbUamDeliveryGroupDetailDTO> 获取交付组详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取交付组详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    public CommonWebResponse<UamDeliveryGroupDTO> getUamDeliveryGroupDetail(IdWebRequest idWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, idWebRequest.getId(), AdminDataPermissionType.DELIVERY_GROUP);
        UamDeliveryGroupDTO uamDeliveryGroup = appDeliveryMgmtAPI.getUamDeliveryGroup(idWebRequest.getId());
        return CommonWebResponse.success(uamDeliveryGroup);
    }


    /**
     * 检测交付组名称是否重复
     *
     * @param checkDeliveryGroupNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检测应用名称是否重复")
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckAppNameDuplicationResponse> checkNameDuplication(
            CheckDeliveryGroupNameDuplicationRequest checkDeliveryGroupNameDuplicationRequest) {
        Assert.notNull(checkDeliveryGroupNameDuplicationRequest, "checkDeliveryGroupNameDuplicationRequest must not be null");
        CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO = new CheckDeliveryGroupNameDuplicationDTO();
        BeanUtils.copyProperties(checkDeliveryGroupNameDuplicationRequest, checkDeliveryGroupNameDuplicationDTO);
        Boolean hasDuplication = cbbAppDeliveryMgmtAPI.checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);
        return CommonWebResponse.success(new CheckAppNameDuplicationResponse(hasDuplication));
    }

    /**
     * 创建应用磁盘交付组
     *
     * @param createAppDiskDeliveryGroupRequest 创建应用磁盘交付组
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建应用磁盘交付组")
    @RequestMapping(value = "/appDisk/create", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateCreateUamAppDiskDeliveryGroup")
    @EnableAuthority
    public DefaultWebResponse createAppDiskDeliveryGroup(CreateAppDiskDeliveryGroupRequest createAppDiskDeliveryGroupRequest,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(createAppDiskDeliveryGroupRequest, "createDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        String deliveryGroupName = createAppDiskDeliveryGroupRequest.getDeliveryGroupName();
        try {
            // 校验是否第三方桌面，不支持推送
            checkThirdPartyDesktop(createAppDiskDeliveryGroupRequest, deliveryGroupName);
            // 检验推送对象是否包含多会话桌面池桌面，不支持推送
            checkSessionTypeDesktop(createAppDiskDeliveryGroupRequest.getCloudDesktopIdList(), deliveryGroupName);
            CbbCreateDeliveryGroupDTO cbbCreateDeliveryGroupDTO = new CbbCreateDeliveryGroupDTO();
            BeanUtils.copyProperties(createAppDiskDeliveryGroupRequest, cbbCreateDeliveryGroupDTO);
            cbbCreateDeliveryGroupDTO.setAppDeliveryType(AppDeliveryTypeEnum.APP_DISK);
            UUID deliveryGroupId = cbbAppDeliveryMgmtAPI.createUamDeliveryGroup(cbbCreateDeliveryGroupDTO);
            // 保存权限数据
            generalPermissionHelper.savePermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_SUCCESS_LOG, deliveryGroupName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {""}, deliveryGroupId);
        } catch (BusinessException e) {
            LOGGER.error("创建应用磁盘交付组！", e);
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_LOG, deliveryGroupName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_LOG, e, deliveryGroupName,
                    e.getI18nMessage());
        }
    }

    private void checkThirdPartyDesktop(CreateAppDiskDeliveryGroupRequest createAppDiskDeliveryGroupRequest, String deliveryGroupName)
            throws BusinessException {
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.listDesktopByDesktopIds(createAppDiskDeliveryGroupRequest.getCloudDesktopIdList());
        long count = desktopList.stream().filter(dto -> CbbCloudDeskType.THIRD.name().equals(dto.getDesktopType())).count();
        if (count > 0) {
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_CREATE_THIRD_PARTY_DELIVERY_GROUP_FAIL, deliveryGroupName);
        }
    }

    /**
     * 创建推送安装包交付组
     *
     * @param createDeliveryGroupRequest 创建推送安装包交付组
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建推送安装包交付组")
    @RequestMapping(value = "/pushInstallPackage/create", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateCreateUamPushInstallPackageDeliveryGroup")
    @EnableAuthority
    public DefaultWebResponse createPushInstallPackageDeliveryGroup(CreatePushInstallPackageAppDiskDeliveryGroupRequest createDeliveryGroupRequest,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(createDeliveryGroupRequest, "createDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        String deliveryGroupName = createDeliveryGroupRequest.getDeliveryGroupName();
        try {
            // 检验推送对象是否包含多会话桌面池桌面，不支持推送安装包
            checkSessionTypeDesktop(createDeliveryGroupRequest.getCloudDesktopIdList(), deliveryGroupName);
            CbbCreateDeliveryGroupDTO cbbCreateDeliveryGroupDTO = new CbbCreateDeliveryGroupDTO();
            BeanUtils.copyProperties(createDeliveryGroupRequest, cbbCreateDeliveryGroupDTO);
            cbbCreateDeliveryGroupDTO.setPushAppConfig(JSON.toJSONString(createDeliveryGroupRequest.getPushAppConfig()));
            cbbCreateDeliveryGroupDTO.setAppDeliveryType(AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE);

            UUID deliveryGroupId = cbbAppDeliveryMgmtAPI.createUamDeliveryGroup(cbbCreateDeliveryGroupDTO);
            // 保存权限数据
            generalPermissionHelper.savePermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_SUCCESS_LOG, deliveryGroupName);
            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {""}, deliveryGroupId);
        } catch (BusinessException e) {
            LOGGER.error("创建推送安装包交付组！", e);
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_LOG, deliveryGroupName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_LOG, e, deliveryGroupName,
                    e.getI18nMessage());
        }
    }




    /**
     * 批量删除交付组
     *
     * @param idArrWebRequest 删除idArr
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量删除交付组")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse deleteUamDeliveryGroup(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "idArrWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        // 批量添加任务
        final Iterator<DefaultBatchTaskItem> iterator = Arrays.stream(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UamDeliveryGroupBusinessKey.RCDC_DELIVERY_GROUP_DELETE_ITEM_NAME, new String[] {}).build()).iterator();

        DeleteUamDeliveryGroupBatchTaskHandler handler = new DeleteUamDeliveryGroupBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setSessionContext(sessionContext);

        BatchTaskSubmitResult result = addBatchTask(idArr, handler, builder);

        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult addBatchTask(UUID[] idArr, DeleteUamDeliveryGroupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单个应用
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(idArr[0]);
            String deliveryGroupName = cbbUamDeliveryGroupDetail.getDeliveryGroupName();
            handler.setBatchFlag(false);
            handler.setDeliveryGroupName(deliveryGroupName);
            result = builder.setTaskName(UamDeliveryGroupBusinessKey.RCDC_DELIVERY_GROUP_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryGroupBusinessKey.RCDC_DELIVERY_GROUP_DELETE_SINGLE_TASK_DESC, deliveryGroupName).registerHandler(handler)
                    .start();
        } else {
            result = builder.setTaskName(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_TASK_NAME)
                    .setTaskDesc(UamDeliveryGroupBusinessKey.RCDC_UAM_DELIVERY_GROUP_DELETE_TASK_DESC).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }


    /**
     * 编辑推送安装包交付组基础信息
     *
     * @param editPushInstallPackageDeliveryGroupRequest 编辑推送安装包交付组基本数据
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑推送安装包交付组基础信息")
    @RequestMapping(value = "/pushInstallPackage/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateEditUamPushInstallPackageDeliveryGroup")
    @EnableAuthority
    public DefaultWebResponse editUamPushInstallPackageDeliveryGroup(
            EditPushInstallPackageDeliveryGroupRequest editPushInstallPackageDeliveryGroupRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(editPushInstallPackageDeliveryGroupRequest, "editDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, editPushInstallPackageDeliveryGroupRequest.getId(),
                AdminDataPermissionType.DELIVERY_GROUP);

        String deliveryGroupName = editPushInstallPackageDeliveryGroupRequest.getDeliveryGroupName();
        try {
            CbbEditDeliveryGroupDTO cbEditDeliveryGroupDTO = new CbbEditDeliveryGroupDTO();
            BeanUtils.copyProperties(editPushInstallPackageDeliveryGroupRequest, cbEditDeliveryGroupDTO);
            cbEditDeliveryGroupDTO.setPushAppConfig(JSON.toJSONString(editPushInstallPackageDeliveryGroupRequest.getPushAppConfig()));

            cbbAppDeliveryMgmtAPI.editUamDeliveryGroup(cbEditDeliveryGroupDTO);
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_SUCCESS_LOG, deliveryGroupName);
        } catch (BusinessException e) {
            LOGGER.error("编辑推送安装包交付组基本数据失败！", e);
            if (CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_NOT_EXISTS_LOG, e.getI18nMessage());
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_NOT_EXISTS_LOG, e, e.getI18nMessage());
            }
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_FAIL_LOG, deliveryGroupName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_FAIL_LOG, e, deliveryGroupName, e.getI18nMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});

    }

    /**
     * 编辑应用磁盘交付组基础信息
     *
     * @param editAppDiskDeliveryGroupRequest 编辑应用磁盘交付组基本数据
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑应用磁盘交付组基础信息")
    @RequestMapping(value = "/appDisk/edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "validateEditUamAppDiskDeliveryGroup")
    @EnableAuthority
    public DefaultWebResponse editUamAppDiskDeliveryGroup(EditAppDiskDeliveryGroupRequest editAppDiskDeliveryGroupRequest,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(editAppDiskDeliveryGroupRequest, "editDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, editAppDiskDeliveryGroupRequest.getId(), AdminDataPermissionType.DELIVERY_GROUP);

        String deliveryGroupName = editAppDiskDeliveryGroupRequest.getDeliveryGroupName();
        try {
            CbbEditDeliveryGroupDTO cbEditDeliveryGroupDTO = new CbbEditDeliveryGroupDTO();
            BeanUtils.copyProperties(editAppDiskDeliveryGroupRequest, cbEditDeliveryGroupDTO);

            cbbAppDeliveryMgmtAPI.editUamDeliveryGroup(cbEditDeliveryGroupDTO);
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_SUCCESS_LOG, deliveryGroupName);
        } catch (BusinessException e) {
            LOGGER.error("编辑应用磁盘交付组基础信息失败！", e);
            if (CbbAppCenterBusinessKey.RCDC_UAM_DELIVERY_GROUP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_NOT_EXISTS_LOG, e.getI18nMessage());
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_NOT_EXISTS_LOG, e, e.getI18nMessage());
            }
            auditLogAPI.recordLog(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_FAIL_LOG, deliveryGroupName, e.getI18nMessage());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_EDIT_DELIVERY_GROUP_FAIL_LOG, e, deliveryGroupName, e.getI18nMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});

    }


    /**
     * 交付应用列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付应用列表")
    @RequestMapping(value = "/deliveryApp/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamDeliveryAppVO>> pageUamDeliveryApp(GetDeliveryAppPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(request.getDeliveryGroupId());
        AppDeliveryTypeEnum deliveryType = cbbUamDeliveryGroupDetail.getAppDeliveryType();

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, request.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.CREATE_TIME));
        orderList.add(new Sort.Order(Sort.Direction.ASC, DBConstants.ID));
        Sort sort = Sort.by(orderList);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        SearchDeliveryAppDTO cbbSearchDeliveryAppDTO = new SearchDeliveryAppDTO();
        BeanUtils.copyProperties(request, cbbSearchDeliveryAppDTO);
        cbbSearchDeliveryAppDTO.setAppName(request.getSearchKeyword());


        DefaultPageResponse<UamDeliveryAppDTO> uamDeliveryAppPageResponse = appDeliveryMgmtAPI.pageUamDeliveryApp(cbbSearchDeliveryAppDTO, pageable);

        DefaultPageResponse<UamDeliveryAppVO> defaultPageResponse = new DefaultPageResponse<>();
        // 推送安装包类型
        if (AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE == deliveryType) {
            BeanUtils.copyProperties(uamDeliveryAppPageResponse, defaultPageResponse);
        }
        // 应用磁盘类型
        if (AppDeliveryTypeEnum.APP_DISK == deliveryType) {
            UamDeliveryAppDTO[] uamDeliveryAppDTOArr = Optional.ofNullable(uamDeliveryAppPageResponse.getItemArr()).orElse(new UamDeliveryAppDTO[0]);
            int length = uamDeliveryAppDTOArr.length;
            UamDeliveryAppVO[] uamDeliveryAppVOArr = new UamDeliveryAppVO[length];
            // 当前只支持一个应用先这么处理
            for (int i = 0; i < length; i++) {
                UamDeliveryAppDTO uamDeliveryAppDTO = uamDeliveryAppDTOArr[i];
                UamDeliveryAppVO uamDeliveryAppVO = getUamDeliveryAppVO(uamDeliveryAppDTO);
                uamDeliveryAppVOArr[i] = uamDeliveryAppVO;
            }
            defaultPageResponse.setTotal(uamDeliveryAppVOArr.length);
            defaultPageResponse.setItemArr(uamDeliveryAppVOArr);
        }


        return CommonWebResponse.success(defaultPageResponse);
    }

    private UamDeliveryAppVO getUamDeliveryAppVO(UamDeliveryAppDTO uamDeliveryAppDTO) throws BusinessException {
        UamDeliveryAppVO uamDeliveryAppVO = new UamDeliveryAppVO();
        uamDeliveryAppVO.setId(uamDeliveryAppDTO.getId());
        uamDeliveryAppVO.setDeliveryGroupId(uamDeliveryAppDTO.getDeliveryGroupId());
        uamDeliveryAppVO.setAppId(uamDeliveryAppDTO.getAppId());
        uamDeliveryAppVO.setAppName(uamDeliveryAppDTO.getAppName());
        uamDeliveryAppVO.setAppType(uamDeliveryAppDTO.getAppType());
        uamDeliveryAppVO.setDeliveryStatus(uamDeliveryAppDTO.getDeliveryStatus());
        uamDeliveryAppVO.setCreateTime(uamDeliveryAppDTO.getCreateTime());
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(uamDeliveryAppDTO.getAppId());

        uamDeliveryAppVO.setPlatformId(appSoftwarePackageDTO.getPlatformId());
        uamDeliveryAppVO.setPlatformType(appSoftwarePackageDTO.getPlatformType());
        uamDeliveryAppVO.setPlatformName(appSoftwarePackageDTO.getPlatformName());
        uamDeliveryAppVO.setPlatformStatus(appSoftwarePackageDTO.getPlatformStatus());
        return uamDeliveryAppVO;
    }


    /**
     * 添加交付应用-点击弹应用磁盘列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("添加交付应用-点击弹应用磁盘列表")
    @RequestMapping(value = "/deliveryApp/selectAppDisk/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamAppDiskDTO>> pageAppDisk(GetAppDiskPageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        UUID deliveryGroupId = request.getDeliveryGroupId();
        if (Objects.nonNull(deliveryGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        SearchAppDiskDTO searchAppDiskDTO = new SearchAppDiskDTO();
        BeanUtils.copyProperties(request, searchAppDiskDTO);
        searchAppDiskDTO.setAppName(request.getSearchKeyword());
        searchAppDiskDTO.setFilterGroupId(request.getDeliveryGroupId());
        searchAppDiskDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);
        searchAppDiskDTO.setRequestSource(RequestSourceEnum.DELIVERY_GROUP);

        generalPermissionHelper.setPermissionParam(sessionContext, searchAppDiskDTO);

        DefaultPageResponse<UamAppDiskDTO> uamAppPageResponse = appDeliveryMgmtAPI.pageAppDisk(searchAppDiskDTO, pageable);
        return CommonWebResponse.success(uamAppPageResponse);
    }


    /**
     * 添加交付应用-点击弹应用磁盘列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("添加交付应用-点击弹推送安装包列表")
    @RequestMapping(value = "/deliveryApp/selectPushInstallPackage/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamPushInstallPackageDTO>> pagePushInstallPackage(GetPushInstallPackagePageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有编辑该记录权限
        UUID deliveryGroupId = request.getDeliveryGroupId();
        if (Objects.nonNull(deliveryGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);
        SearchPushInstallPackageDTO searchPushInstallPackageDTO = new SearchPushInstallPackageDTO();
        searchPushInstallPackageDTO.setAppName(request.getSearchKeyword());
        searchPushInstallPackageDTO.setFilterGroupId(request.getDeliveryGroupId());
        searchPushInstallPackageDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);

        // 根据前端选择的操作系统类型筛选应用
        if (Objects.nonNull(request.getOsPlatform())) {
            searchPushInstallPackageDTO.setOsPlatform(request.getOsPlatform());
        }

        if (Objects.nonNull(deliveryGroupId)) {
            // 判定交付组是否存在
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            searchPushInstallPackageDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        }

        generalPermissionHelper.setPermissionParam(sessionContext, searchPushInstallPackageDTO);

        DefaultPageResponse<UamPushInstallPackageDTO> uamAppPageResponse =
                appDeliveryMgmtAPI.pagePushInstallPackage(searchPushInstallPackageDTO, pageable);
        return CommonWebResponse.success(uamAppPageResponse);
    }


    /**
     * 应用磁盘交付组-批量添加交付应用
     *
     * @param addDeliveryAppToDiskGroupRequest 添加参数
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用磁盘交付组-批量添加交付应用")
    @RequestMapping(value = "/appDisk/deliveryApp/add", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse addUamDeliveryAppToDiskGroup(AddDeliveryAppToDiskGroupRequest addDeliveryAppToDiskGroupRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(addDeliveryAppToDiskGroupRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID deliveryGroupId = addDeliveryAppToDiskGroupRequest.getDeliveryGroupId();
        // 判定交付组是否存在
        cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        // 判定是否有操作的数据权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        UUID appId = addDeliveryAppToDiskGroupRequest.getAppId();

        final BatchTaskItem batchTaskItem =
                new DefaultBatchTaskItem(appId, LocaleI18nResolver.resolve(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_SINGLE_TASK_NAME));

        AddUamDeliveryAppToDiskGroupBatchTaskHandler handler = new AddUamDeliveryAppToDiskGroupBatchTaskHandler(batchTaskItem);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppStoreMgmtAPI(cbbAppStoreMgmtAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setCbbAppSoftwarePackageMgmtAPI(cbbAppSoftwarePackageMgmtAPI);
        handler.setSessionContext(sessionContext);
        handler.setDeliveryGroupId(deliveryGroupId);

        CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(appId);
        String appName = cbbUamAppDTO.getAppName();
        handler.setAppName(appName);
        BatchTaskSubmitResult result = builder.setTaskName(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_SINGLE_TASK_NAME)
                .setTaskDesc(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_SINGLE_TASK_DESC, appName).registerHandler(handler).start();

        return CommonWebResponse.success(result);
    }


    /**
     * 推送安装包交付组-批量添加交付应用
     *
     * @param addDeliveryAppToPushGroupRequest 添加参数
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("推送安装包交付组-批量添加交付应用")
    @RequestMapping(value = "/pushInstallPackage/deliveryApp/add", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse addUamDeliveryAppToPushGroup(AddDeliveryAppToPushGroupRequest addDeliveryAppToPushGroupRequest, BatchTaskBuilder builder,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(addDeliveryAppToPushGroupRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID deliveryGroupId = addDeliveryAppToPushGroupRequest.getDeliveryGroupId();
        // 判定交付组是否存在
        cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        // 判定是否有操作的数据权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        List<UUID> appIdList = addDeliveryAppToPushGroupRequest.getAppIdList();
        // 批量添加任务
        final Iterator<DefaultBatchTaskItem> iterator = appIdList.stream().distinct() // 格式化换行
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_ITEM_NAME, new String[] {}).build())
                .iterator();

        AddUamDeliveryAppToPushGroupBatchTaskHandler handler = new AddUamDeliveryAppToPushGroupBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppStoreMgmtAPI(cbbAppStoreMgmtAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setCbbPushInstallPackageMgmtAPI(cbbPushInstallPackageMgmtAPI);
        handler.setSessionContext(sessionContext);
        handler.setDeliveryGroupId(deliveryGroupId);

        BatchTaskSubmitResult result = addBatchTask(appIdList, handler, builder);

        return CommonWebResponse.success(result);
    }


    private BatchTaskSubmitResult addBatchTask(List<UUID> appIdList, AddUamDeliveryAppToPushGroupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 添加单个应用
        BatchTaskSubmitResult result;
        if (appIdList.size() == 1) {
            CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(appIdList.get(0));
            handler.setBatchFlag(false);
            String appName = cbbUamAppDTO.getAppName();
            handler.setAppName(appName);
            result = builder.setTaskName(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_ADD_SINGLE_TASK_DESC, appName).registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_TASK_NAME).enableParallel()
                    .setTaskDesc(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_ADD_TASK_DESC).registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 批量删除交付应用
     *
     * @param idArrWebRequest 删除ids
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量删除交付应用")
    @RequestMapping(value = "/deliveryApp/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse deleteUamDeliveryApp(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID[] idArr = idArrWebRequest.getIdArr();
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct() // 格式化换行
                .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                        .itemName(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_DELETE_ITEM_NAME, new String[] {}).build())
                .iterator();

        DeleteUamDeliveryAppBatchTaskHandler handler = new DeleteUamDeliveryAppBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppDeliveryMgmtAPI(appDeliveryMgmtAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setSessionContext(sessionContext);

        BatchTaskSubmitResult result = deleteBatchTask(idArr, handler, builder);

        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult deleteBatchTask(UUID[] idArr, DeleteUamDeliveryAppBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单个应用
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            UamDeliveryAppDTO uamDeliveryAppDTO = appDeliveryMgmtAPI.findDeliveryAppById(idArr[0]);

            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail =
                    cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(uamDeliveryAppDTO.getDeliveryGroupId());

            handler.setBatchFlag(false);
            handler.setAppName(uamDeliveryAppDTO.getAppName());
            handler.setGroupName(cbbUamDeliveryGroupDetail.getDeliveryGroupName());
            result = builder.setTaskName(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryAppBusinessKey.RCDC_DELIVERY_APP_DELETE_SINGLE_TASK_DESC, uamDeliveryAppDTO.getAppName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_TASK_NAME)
                    .setTaskDesc(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_DELETE_TASK_DESC).enableParallel().registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 交付应用详情列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付应用-详情列表")
    @RequestMapping(value = "/deliveryAppDetail/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamDeliveryAppDetailDTO>> pageUamDeliveryAppDetail(GetDeliveryAppDetailPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, request.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.CREATE_TIME));
        orderList.add(new Sort.Order(Sort.Direction.ASC, DBConstants.ID));
        Sort sort = Sort.by(orderList);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDeliveryAppDetailDTO searchDeliveryAppDetailDTO = new SearchDeliveryAppDetailDTO();
        BeanUtils.copyProperties(request, searchDeliveryAppDetailDTO);
        searchDeliveryAppDetailDTO.setCloudDesktopName(request.getSearchKeyword());

        DefaultPageResponse<UamDeliveryAppDetailDTO> uamDeliveryObjectDetailPageResponse =
                appDeliveryMgmtAPI.pageUamDeliveryAppDetail(searchDeliveryAppDetailDTO, pageable);
        return CommonWebResponse.success(uamDeliveryObjectDetailPageResponse);
    }


    /**
     * 应用磁盘交付组-批量添加交付对象
     *
     * @param addDeliveryObjectToDiskGroupRequest 添加ids
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用磁盘交付组-批量添加交付对象")
    @RequestMapping(value = "/appDisk/deliveryObject/add", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse addUamDeliveryObjectToDiskGroup(AddDeliveryObjectToDiskGroupRequest addDeliveryObjectToDiskGroupRequest,
            BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(addDeliveryObjectToDiskGroupRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID deliveryGroupId = addDeliveryObjectToDiskGroupRequest.getDeliveryGroupId();
        // 判定交付组是否存在
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        LOGGER.info("添加交付组{}", JSON.toJSONString(cbbUamDeliveryGroupDetail));

        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        List<UUID> cloudDesktopIdList = addDeliveryObjectToDiskGroupRequest.getCloudDesktopIdList();
        // 检验是否存在多会话桌面池桌面
        checkSessionTypeDesktop(cloudDesktopIdList, cbbUamDeliveryGroupDetail.getDeliveryGroupName());
        // 批量添加任务
        final Iterator<DefaultBatchTaskItem> iterator = cloudDesktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_ITEM_NAME, new String[] {}).build()).iterator();

        AddUamDeliveryObjectToDiskGroupBatchTaskHandler handler = new AddUamDeliveryObjectToDiskGroupBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppDeliveryMgmtAPI(appDeliveryMgmtAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setDeliveryGroupId(deliveryGroupId);
        handler.setCloudDesktopWebService(cloudDesktopWebService);

        BatchTaskSubmitResult result = addBatchTask(cloudDesktopIdList, handler, builder);

        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult addBatchTask(List<UUID> idArr, AddUamDeliveryObjectToDiskGroupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 添加单个交付对象
        BatchTaskSubmitResult result;
        if (idArr.size() == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(idArr.get(0));
            handler.setBatchFlag(false);
            handler.setCloudDesktopName(cloudDesktopDetailDTO.getDesktopName());
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_NAME).enableParallel()
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_DESC).registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 批量添加交付对象
     *
     * @param addDeliveryObjectToPushGroupRequest 添加ids
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加交付对象")
    @RequestMapping(value = "/pushInstallPackage/deliveryObject/add", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse addUamDeliveryObjectToPushGroup(AddDeliveryObjectToPushGroupRequest addDeliveryObjectToPushGroupRequest,
            BatchTaskBuilder builder, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(addDeliveryObjectToPushGroupRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID deliveryGroupId = addDeliveryObjectToPushGroupRequest.getDeliveryGroupId();
        // 判定交付组是否存在
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);

        LOGGER.info("添加交付组{}", JSON.toJSONString(cbbUamDeliveryGroupDetail));
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        List<UUID> cloudDesktopIdList = addDeliveryObjectToPushGroupRequest.getCloudDesktopIdList();
        // 检验是否存在多会话桌面池桌面
        checkSessionTypeDesktop(cloudDesktopIdList, cbbUamDeliveryGroupDetail.getDeliveryGroupName());
        // 批量添加任务
        final Iterator<DefaultBatchTaskItem> iterator = cloudDesktopIdList.stream().distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_ITEM_NAME, new String[] {}).build()).iterator();

        AddUamDeliveryObjectToPushGroupBatchTaskHandler handler = new AddUamDeliveryObjectToPushGroupBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setUserDesktopMgmtAPI(userDesktopMgmtAPI);
        handler.setDeliveryGroupId(deliveryGroupId);

        BatchTaskSubmitResult result = addBatchTask(cloudDesktopIdList, handler, builder);

        return CommonWebResponse.success(result);
    }


    private BatchTaskSubmitResult addBatchTask(List<UUID> idArr, AddUamDeliveryObjectToPushGroupBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 添加单个交付对象
        BatchTaskSubmitResult result;
        if (idArr.size() == 1) {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(idArr.get(0));
            handler.setBatchFlag(false);
            handler.setCloudDesktopName(cloudDesktopDetailDTO.getDesktopName());
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_ADD_SINGLE_TASK_DESC, cloudDesktopDetailDTO.getDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_NAME).enableParallel()
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_ADD_TASK_DESC).registerHandler(handler).start();
        }
        return result;
    }


    /**
     * 重新交付-交付对象
     *
     * @param idWebRequest 交付对象id
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重新交付-交付对象")
    @RequestMapping(value = "/deliveryObject/redeliveryObject", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse redeliveryObjectFromDeliveryObject(IdWebRequest idWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(idWebRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = idWebRequest.getId();
        UamDeliveryObjectDTO uamDeliveryObject = appDeliveryMgmtAPI.findDeliveryObjectById(id);
        UUID deliveryGroupId = uamDeliveryObject.getDeliveryGroupId();
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        AppDeliveryTypeEnum appDeliveryType = cbbUamDeliveryGroupDetailDTO.getAppDeliveryType();
        List<DeliveryStatusEnum> deliveryStatusList = getDeliveryStatusList(appDeliveryType);

        List<CbbUamDeliveryDetailDTO> cbbUamDeliveryDetailList = cbbAppDeliveryMgmtAPI.findByDeliveryGroupIdAndCloudDesktopIdAndDeliveryStatusIn(
                deliveryGroupId, uamDeliveryObject.getCloudDesktopId(), deliveryStatusList);

        String cloudDesktopName = uamDeliveryObject.getCloudDesktopName();
        // 推送安装包
        if (appDeliveryType == AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE && ObjectUtils.isEmpty(cbbUamDeliveryDetailList)) {
            LOGGER.info("交付对象[{}{}]不存在交付失败或者交付成功的应用无需交付", uamDeliveryObject.getCloudDesktopId(), cloudDesktopName);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_FAIL_LOG, cloudDesktopName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }
        // 应用磁盘
        if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && ObjectUtils.isEmpty(cbbUamDeliveryDetailList)) {
            LOGGER.info("交付对象[{}{}]不存在交付失败的应用无需交付", uamDeliveryObject.getCloudDesktopId(), cloudDesktopName);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DISK_DELIVERY_OBJECT_REDELIVERY_FAIL_LOG, cloudDesktopName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }

        UUID cloudDesktopId = uamDeliveryObject.getCloudDesktopId();
        CbbRedeliveryObjectFromDeliveryObjectDTO cbbRedeliveryObjectFromDeliveryObjectDTO = new CbbRedeliveryObjectFromDeliveryObjectDTO();
        cbbRedeliveryObjectFromDeliveryObjectDTO.setDeliveryObjectId(uamDeliveryObject.getId());
        cbbRedeliveryObjectFromDeliveryObjectDTO.setDeliveryGroupId(uamDeliveryObject.getDeliveryGroupId());
        cbbRedeliveryObjectFromDeliveryObjectDTO.setCloudDesktopId(cloudDesktopId);
        cbbRedeliveryObjectFromDeliveryObjectDTO.setAppDeliveryType(cbbUamDeliveryGroupDetailDTO.getAppDeliveryType());

        cbbAppDeliveryMgmtAPI.redeliveryObject(cbbRedeliveryObjectFromDeliveryObjectDTO);

        auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_SUCCESS_LOG, cloudDesktopName);

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }


    /**
     * 批量删除交付对象
     *
     * @param idArrWebRequest 删除ids
     * @param builder 批量操作
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量删除交付对象")
    @RequestMapping(value = "/deliveryObject/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse deleteUamDeliveryObject(IdArrWebRequest idArrWebRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "request must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID[] idArr = idArrWebRequest.getIdArr();
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_DELETE_ITEM_NAME, new String[] {}).build()).iterator();

        DeleteUamDeliveryObjectBatchTaskHandler handler = new DeleteUamDeliveryObjectBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setAppDeliveryMgmtAPI(appDeliveryMgmtAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setSessionContext(sessionContext);

        BatchTaskSubmitResult result = deleteBatchTask(idArr, handler, builder);

        return CommonWebResponse.success(result);
    }

    private BatchTaskSubmitResult deleteBatchTask(UUID[] idArr, DeleteUamDeliveryObjectBatchTaskHandler handler, BatchTaskBuilder builder)
            throws BusinessException {
        // 删除单个交付对象
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            UamDeliveryObjectDTO uamDeliveryObjectDTO = appDeliveryMgmtAPI.findDeliveryObjectById(idArr[0]);
            handler.setBatchFlag(false);
            handler.setCloudDesktopName(uamDeliveryObjectDTO.getCloudDesktopName());
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_DELETE_SINGLE_TASK_NAME)
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_DELIVERY_OBJECT_DELETE_SINGLE_TASK_DESC,
                            uamDeliveryObjectDTO.getCloudDesktopName())
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_TASK_NAME)
                    .setTaskDesc(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_DELETE_TASK_DESC).enableParallel().registerHandler(handler)
                    .start();
        }
        return result;
    }


    /**
     * 交付对象列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付对象列表")
    @RequestMapping(value = "/deliveryObject/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamDeliveryObjectDTO>> pageUamDeliveryObject(GetDeliveryObjectPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, request.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.CREATE_TIME));
        orderList.add(new Sort.Order(Sort.Direction.ASC, DBConstants.ID));
        Sort sort = Sort.by(orderList);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDeliveryObjectDTO searchDeliveryObjectDTO = new SearchDeliveryObjectDTO();
        BeanUtils.copyProperties(request, searchDeliveryObjectDTO);
        searchDeliveryObjectDTO.setCloudDesktopName(request.getSearchKeyword());

        DefaultPageResponse<UamDeliveryObjectDTO> uamDeliveryObjectPageResponse =
                appDeliveryMgmtAPI.pageUamDeliveryObject(searchDeliveryObjectDTO, pageable);
        return CommonWebResponse.success(uamDeliveryObjectPageResponse);
    }


    /**
     * 交付对象详情列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付对象-详情列表")
    @RequestMapping(value = "/deliveryObjectDetail/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamDeliveryObjectDetailDTO>> pageUamDeliveryObjectDetail(
            GetDeliveryObjectDetailPageWebRequest request, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, request.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        List<Sort.Order> orderList = new ArrayList<>();
        orderList.add(new Sort.Order(Sort.Direction.DESC, DBConstants.CREATE_TIME));
        orderList.add(new Sort.Order(Sort.Direction.ASC, DBConstants.ID));
        Sort sort = Sort.by(orderList);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchDeliveryObjectDetailDTO searchDeliveryObjectDetailDTO = new SearchDeliveryObjectDetailDTO();
        BeanUtils.copyProperties(request, searchDeliveryObjectDetailDTO);
        searchDeliveryObjectDetailDTO.setAppName(request.getSearchKeyword());

        DefaultPageResponse<UamDeliveryObjectDetailDTO> uamDeliveryObjectPageResponse =
                appDeliveryMgmtAPI.pageUamDeliveryObjectDetail(searchDeliveryObjectDetailDTO, pageable);
        return CommonWebResponse.success(uamDeliveryObjectPageResponse);
    }


    /**
     * 交付对象详情：重新交付应用
     *
     * @param idWebRequest 交付列表id
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付对象详情：重新交付应用")
    @RequestMapping(value = "/deliveryObjectDetail/redeliveryApp", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse redeliveryAppFromDeliveryObjectDetail(IdWebRequest idWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idWebRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = idWebRequest.getId();
        UamDeliveryObjectDetailDTO deliveryObjectDetail = appDeliveryMgmtAPI.getDeliveryObjectDetail(id);

        UUID deliveryGroupId = deliveryObjectDetail.getDeliveryGroupId();
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        String cloudDesktopName = deliveryObjectDetail.getCloudDesktopName();
        String appName = deliveryObjectDetail.getAppName();

        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        AppDeliveryTypeEnum appDeliveryType = cbbUamDeliveryGroupDetailDTO.getAppDeliveryType();

        DeliveryStatusEnum deliveryStatus = deliveryObjectDetail.getDeliveryStatus();
        if (appDeliveryType == AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE && deliveryStatus == DeliveryStatusEnum.DELIVERING) {
            LOGGER.info("当前应用[{}]不存在交付失败或者交付成功的应用无需交付", id);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_APP_FAIL_LOG, cloudDesktopName, appName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }

        if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && deliveryStatus != DeliveryStatusEnum.DELIVERY_FAIL) {
            LOGGER.info("当前应用[{}]为交付中无需重新交付", id);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DISK_DELIVERY_OBJECT_REDELIVERY_APP_FAIL_LOG, cloudDesktopName, appName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }

        UUID cloudDesktopId = deliveryObjectDetail.getCloudDesktopId();
        CbbRedeliveryAppFromObjectDetailDTO cbbRedeliveryAppFromObjectDetailDTO = new CbbRedeliveryAppFromObjectDetailDTO();
        cbbRedeliveryAppFromObjectDetailDTO.setDeliveryDetailId(deliveryObjectDetail.getId());
        cbbRedeliveryAppFromObjectDetailDTO.setAppId(deliveryObjectDetail.getAppId());
        cbbRedeliveryAppFromObjectDetailDTO.setAppType(deliveryObjectDetail.getAppType());
        cbbRedeliveryAppFromObjectDetailDTO.setCloudDesktopId(cloudDesktopId);
        cbbRedeliveryAppFromObjectDetailDTO.setDeliveryGroupId(deliveryGroupId);
        cbbRedeliveryAppFromObjectDetailDTO.setAppDeliveryType(cbbUamDeliveryGroupDetailDTO.getAppDeliveryType());

        cbbAppDeliveryMgmtAPI.redeliveryApp(cbbRedeliveryAppFromObjectDetailDTO);

        auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_DELIVERY_OBJECT_REDELIVERY_APP_SUCCESS_LOG, cloudDesktopName, appName);

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }

    /**
     * 交付记录列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("交付记录列表")
    @RequestMapping(value = "/deliveryRecord/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<CbbUamDeliveryRecordDTO>> pageUamDeliveryRecord(GetDeliveryRecordPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, request.getDeliveryGroupId(), AdminDataPermissionType.DELIVERY_GROUP);

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        CbbSearchDeliveryRecordDTO cbbSearchDeliveryRecordDTO = new CbbSearchDeliveryRecordDTO();
        BeanUtils.copyProperties(request, cbbSearchDeliveryRecordDTO);
        cbbSearchDeliveryRecordDTO.setCloudDesktopName(request.getSearchKeyword());

        DefaultPageResponse<CbbUamDeliveryRecordDTO> cbbUamDeliveryRecordPageResponse =
                cbbAppDeliveryMgmtAPI.pageUamDeliveryRecord(cbbSearchDeliveryRecordDTO, pageable);
        List<CbbUamDeliveryRecordDTO> cbbUamDeliveryRecordList = Arrays.asList(cbbUamDeliveryRecordPageResponse.getItemArr());
        if (!CollectionUtils.isEmpty(cbbUamDeliveryRecordList)) {
            cloudDesktopWebService.convertDeskType(cbbUamDeliveryRecordList);
        }
        return CommonWebResponse.success(cbbUamDeliveryRecordPageResponse);
    }


    /**
     * 批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据")
    @RequestMapping(value = "/deliveryObject/terminalGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<TerminalGroupDesktopRelatedDTO>> pageTerminalGroupDesktop(GroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID terminalGroupId = request.getGroupId();
        if (Objects.nonNull(terminalGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, terminalGroupId, AdminDataPermissionType.TERMINAL_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();

        if (Objects.nonNull(request.getOsPlatform())) {
            searchGroupDesktopRelatedDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            searchGroupDesktopRelatedDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setFilterGroupId(deliveryGroupId);
            searchGroupDesktopRelatedDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        searchGroupDesktopRelatedDTO.setSearchName(request.getSearchKeyword());
        searchGroupDesktopRelatedDTO.setGroupId(terminalGroupId);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);
        searchGroupDesktopRelatedDTO.setDeskStateList(request.getDeskStateList());
        searchGroupDesktopRelatedDTO.setPlatformStatusList(request.getPlatformStatusList());

        generalPermissionHelper.setPermissionParam(sessionContext, searchGroupDesktopRelatedDTO);

        DefaultPageResponse<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedPageResponse =
                appDeliveryMgmtAPI.pageTerminalGroupDesktopRelated(searchGroupDesktopRelatedDTO, pageable);

        // 如果是创建应用磁盘交付组，列表过滤第三方桌面
        if (AppDeliveryTypeEnum.APP_DISK == request.getAppDeliveryType()) {
            CommonWebResponse.success(dealCanUsedThird(terminalGroupDesktopRelatedPageResponse));
        }
        return CommonWebResponse.success(terminalGroupDesktopRelatedPageResponse);
    }

    private DefaultPageResponse<TerminalGroupDesktopRelatedDTO> dealCanUsedThird(DefaultPageResponse<TerminalGroupDesktopRelatedDTO> response) {
        Assert.notNull(response, "response is null");
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            return response;
        }
        String message = LocaleI18nResolver.resolve(UamDeliveryAppBusinessKey.RCDC_UAM_DISK_DELIVERY_APP_DEALCANUSED_THIRD_DESKTOP);
        for (TerminalGroupDesktopRelatedDTO dto : response.getItemArr()) {
            if (CbbCloudDeskType.THIRD.name().equals(dto.getDeskType())) {
                dto.setNotOptionalTip(message);
            }
        }
        return response;
    }

    /**
     * 批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--终端组--获所有终端组的云桌面数量")
    @RequestMapping(value = "/deliveryObject/terminalGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopCountDTO>> listTerminalGroupDesktopCount(GetGroupDesktopCountRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        GetGroupDesktopCountDTO getGroupDesktopCountDTO = new GetGroupDesktopCountDTO();
        getGroupDesktopCountDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);

        if (Objects.nonNull(request.getOsPlatform())) {
            getGroupDesktopCountDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            getGroupDesktopCountDTO.setOsType(osType);
            getGroupDesktopCountDTO.setCbbImageType(cbbImageType);
            getGroupDesktopCountDTO.setFilterGroupId(deliveryGroupId);
            getGroupDesktopCountDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            getGroupDesktopCountDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            getGroupDesktopCountDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            getGroupDesktopCountDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            getGroupDesktopCountDTO.setOsType(osType);
            getGroupDesktopCountDTO.setCbbImageType(cbbImageType);
            getGroupDesktopCountDTO.setOsVersion(request.getOsVersion());
            getGroupDesktopCountDTO.setAppDeliveryType(request.getAppDeliveryType());
            getGroupDesktopCountDTO.setImageTemplateId(request.getImageTemplateId());
        }

        generalPermissionHelper.setPermissionParam(sessionContext, getGroupDesktopCountDTO);

        List<TerminalGroupDesktopCountDTO> userGroupDesktopCountList = appDeliveryMgmtAPI.listTerminalGroupDesktopCount(getGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 批量添加云桌面--终端组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--终端组--根据用户选中，获取该组下所有云桌面数据")
    @RequestMapping(value = "/deliveryObject/terminalGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<TerminalGroupDesktopRelatedDTO>> listTerminalGroupDesktop(SelectGroupDesktopRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID terminalGroupId = request.getGroupId();
        // 终端组id为空，则认为是选择了根【总览】
        List<UUID> terminalGroupIdList = new ArrayList<>();
        if (Objects.nonNull(terminalGroupId)) {
            // loadById 用于判定id是否存在
            CbbTerminalGroupDetailDTO cbbTerminalGroupDetailDTO = cbbTerminalGroupMgmtAPI.loadById(terminalGroupId);
            // 递归获取该节点含所有子节点id
            terminalGroupIdList = cbbTerminalGroupMgmtAPI.listByGroupIdRecursive(cbbTerminalGroupDetailDTO.getId());
        }

        // 如果非超管，则需要跟该用户数据权限进行过滤
        boolean isAllPermission = generalPermissionHelper.isAllPermission(sessionContext);
        if (!isAllPermission) {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            List<UUID> uuidList = generalPermissionHelper.listByPermissionType(baseAdminDTO.getId(), AdminDataPermissionType.TERMINAL_GROUP);
            // 需要数据权限
            if (CollectionUtils.isEmpty(uuidList)) {
                // 该用户没有获取该数据权限，直接返回空集合
                LOGGER.warn("该用户[{}]没有任何终端组数据权限！", baseAdminDTO.getUserName());
                return CommonWebResponse.success();
            }

            // 选择组id，获取交集
            if (Objects.nonNull(terminalGroupId)) {
                terminalGroupIdList.retainAll(uuidList);
            } else {
                // 选择了根【总览】，则获取该用户的终端组即为他所有的数据权限
                terminalGroupIdList.addAll(uuidList);
            }
        }

        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();
        searchGroupDesktopRelatedDTO.setGroupIdList(terminalGroupIdList);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);

        if (Objects.nonNull(request.getOsPlatform())) {
            searchGroupDesktopRelatedDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setFilterGroupId(deliveryGroupId);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        List<TerminalGroupDesktopRelatedDTO> terminalGroupDesktopRelatedList =
                appDeliveryMgmtAPI.listTerminalGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(terminalGroupDesktopRelatedList);
    }


    /**
     * 批量添加云桌面--用户组--根据用户点击，获取该组的云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--用户组--根据用户点击，获取该组的云桌面数据")
    @RequestMapping(value = "/deliveryObject/userGroup/desktop/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UserGroupDesktopRelatedDTO>> pageUserGroupDesktop(GroupDesktopPageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID userGroupId = request.getGroupId();
        if (Objects.nonNull(userGroupId)) {
            generalPermissionHelper.checkPermission(sessionContext, userGroupId, AdminDataPermissionType.USER_GROUP);
        }

        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }

        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();

        if (Objects.nonNull(request.getOsPlatform())) {
            searchGroupDesktopRelatedDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setFilterGroupId(deliveryGroupId);
            searchGroupDesktopRelatedDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        searchGroupDesktopRelatedDTO.setSearchName(request.getSearchKeyword());
        searchGroupDesktopRelatedDTO.setGroupId(userGroupId);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);
        searchGroupDesktopRelatedDTO.setDeskStateList(request.getDeskStateList());
        searchGroupDesktopRelatedDTO.setPlatformStatusList(request.getPlatformStatusList());

        generalPermissionHelper.setPermissionParam(sessionContext, searchGroupDesktopRelatedDTO);

        DefaultPageResponse<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedPageResponse =
                appDeliveryMgmtAPI.pageUserGroupDesktopRelated(searchGroupDesktopRelatedDTO, pageable);

        // 如果是创建应用磁盘交付组，列表过滤第三方桌面
        if (AppDeliveryTypeEnum.APP_DISK == request.getAppDeliveryType()) {
            CommonWebResponse.success(dealCanUsedThirdDesktop(userGroupDesktopRelatedPageResponse));
        }

        return CommonWebResponse.success(userGroupDesktopRelatedPageResponse);
    }

    private DefaultPageResponse<UserGroupDesktopRelatedDTO> dealCanUsedThirdDesktop(DefaultPageResponse<UserGroupDesktopRelatedDTO> response) {
        Assert.notNull(response, "response is null");
        if (ArrayUtils.isEmpty(response.getItemArr())) {
            return response;
        }
        String message = LocaleI18nResolver.resolve(UamDeliveryAppBusinessKey.RCDC_UAM_DISK_DELIVERY_APP_DEALCANUSED_THIRD_DESKTOP);
        for (UserGroupDesktopRelatedDTO userGroupDesktopRelatedDTO : response.getItemArr()) {
            if (CbbCloudDeskType.THIRD.name().equals(userGroupDesktopRelatedDTO.getDeskType())) {
                userGroupDesktopRelatedDTO.setNotOptionalTip(message);
            }
        }
        return response;
    }

    /**
     * 批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--用户组--根据用户点击，获所有组的云桌面数量")
    @RequestMapping(value = "/deliveryObject/userGroup/desktopCount/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopCountDTO>> listUserGroupDesktopCount(GetGroupDesktopCountRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        GetGroupDesktopCountDTO getGroupDesktopCountDTO = new GetGroupDesktopCountDTO();
        getGroupDesktopCountDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);

        if (Objects.nonNull(request.getOsPlatform())) {
            getGroupDesktopCountDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            getGroupDesktopCountDTO.setOsType(osType);
            getGroupDesktopCountDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(cbbImageType);
            getGroupDesktopCountDTO.setFilterGroupId(deliveryGroupId);
            getGroupDesktopCountDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            getGroupDesktopCountDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            getGroupDesktopCountDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            getGroupDesktopCountDTO.setOsType(osType);
            getGroupDesktopCountDTO.setOsVersion(request.getOsVersion());
            getGroupDesktopCountDTO.setCbbImageType(cbbImageType);
            getGroupDesktopCountDTO.setAppDeliveryType(request.getAppDeliveryType());
            getGroupDesktopCountDTO.setImageTemplateId(request.getImageTemplateId());
        }

        generalPermissionHelper.setPermissionParam(sessionContext, getGroupDesktopCountDTO);
        List<UserGroupDesktopCountDTO> userGroupDesktopCountList = appDeliveryMgmtAPI.listUserGroupDesktopCount(getGroupDesktopCountDTO);

        return CommonWebResponse.success(userGroupDesktopCountList);
    }


    /**
     * 批量添加云桌面--用户组--根据用户选中，获取该组下所有云桌面数据
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 桌面列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("批量添加云桌面--用户组--根据用户选中，获取该组下所有云桌面数据")
    @RequestMapping(value = "/deliveryObject/userGroup/selectDesktop/list", method = RequestMethod.POST)
    public CommonWebResponse<List<UserGroupDesktopRelatedDTO>> listUserGroupDesktop(SelectGroupDesktopRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID userGroupId = request.getGroupId();
        // 用户组id为空，则认为是选择了根【总览】
        List<UUID> userGroupIdList = new ArrayList<>();
        if (Objects.nonNull(userGroupId)) {
            // loadById 用于判定id是否存在
            IacUserGroupDetailDTO userGroupDetail = cbbUserGroupAPI.getUserGroupDetail(userGroupId);
            // 递归获取该节点含所有子节点id
            userGroupIdList = cbbUserGroupAPI.listByGroupIdRecursive(userGroupDetail.getId());
        }

        // 如果非超管，则需要跟该用户数据权限进行过滤
        boolean isAllPermission = generalPermissionHelper.isAllPermission(sessionContext);
        if (!isAllPermission) {
            IacAdminDTO baseAdminDTO = baseAdminMgmtAPI.getAdmin(sessionContext.getUserId());
            List<UUID> uuidList = generalPermissionHelper.listByPermissionType(baseAdminDTO.getId(), AdminDataPermissionType.USER_GROUP);
            // 需要数据权限
            if (CollectionUtils.isEmpty(uuidList)) {
                // 该用户没有获取该数据权限，直接返回空集合
                LOGGER.warn("该用户[{}]没有任何用户组数据权限！", baseAdminDTO.getUserName());
                return CommonWebResponse.success();
            }
            // 选择组id，获取交集
            if (Objects.nonNull(userGroupId)) {
                userGroupIdList.retainAll(uuidList);
            } else {
                // 选择了根【总览】，则获取该用户的用户组即为他所有的数据权限
                userGroupIdList.addAll(uuidList);
            }
        }

        SearchGroupDesktopRelatedDTO searchGroupDesktopRelatedDTO = new SearchGroupDesktopRelatedDTO();
        searchGroupDesktopRelatedDTO.setGroupIdList(userGroupIdList);
        searchGroupDesktopRelatedDTO.setDataSourceType(DataSourceTypeEnum.DELIVERY_GROUP);

        if (Objects.nonNull(request.getOsPlatform())) {
            searchGroupDesktopRelatedDTO.setOsPlatform(request.getOsPlatform());
        }

        UUID deliveryGroupId = request.getCurrentTaskId();
        // 交付组id存在，以交付组为准osType,cbbImageType
        if (Objects.nonNull(deliveryGroupId)) {
            CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetail = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
            CbbOsType osType = cbbUamDeliveryGroupDetail.getOsType();
            CbbImageType cbbImageType = cbbUamDeliveryGroupDetail.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(cbbUamDeliveryGroupDetail.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setFilterGroupId(deliveryGroupId);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(cbbUamDeliveryGroupDetail.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(cbbUamDeliveryGroupDetail.getImageTemplateId());
            searchGroupDesktopRelatedDTO.setOsPlatform(cbbUamDeliveryGroupDetail.getOsPlatform());
        } else {
            CbbOsType osType = request.getOsType();
            CbbImageType cbbImageType = request.getCbbImageType();
            searchGroupDesktopRelatedDTO.setOsType(osType);
            searchGroupDesktopRelatedDTO.setOsVersion(request.getOsVersion());
            searchGroupDesktopRelatedDTO.setCbbImageType(cbbImageType);
            searchGroupDesktopRelatedDTO.setAppDeliveryType(request.getAppDeliveryType());
            searchGroupDesktopRelatedDTO.setImageTemplateId(request.getImageTemplateId());
        }

        List<UserGroupDesktopRelatedDTO> userGroupDesktopRelatedList = appDeliveryMgmtAPI.listUserGroupDesktopRelated(searchGroupDesktopRelatedDTO);

        return CommonWebResponse.success(userGroupDesktopRelatedList);
    }


    /**
     * 重新交付-交付应用
     *
     * @param idWebRequest 交付应用id
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("重新交付-交付应用")
    @RequestMapping(value = "/deliveryApp/redeliveryApp", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse redeliveryAppFromDeliveryApp(IdWebRequest idWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(idWebRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = idWebRequest.getId();

        UamDeliveryAppDTO uamDeliveryApp = appDeliveryMgmtAPI.findDeliveryAppById(id);
        UUID deliveryGroupId = uamDeliveryApp.getDeliveryGroupId();

        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);

        AppDeliveryTypeEnum appDeliveryType = cbbUamDeliveryGroupDetailDTO.getAppDeliveryType();

        List<DeliveryStatusEnum> deliveryStatusList = getDeliveryStatusList(appDeliveryType);

        List<CbbUamDeliveryDetailDTO> cbbUamDeliveryDetailList = cbbAppDeliveryMgmtAPI
                .findByDeliveryGroupIdAndAppIdAndDeliveryStatusIn(deliveryGroupId, uamDeliveryApp.getAppId(), deliveryStatusList);

        String appName = uamDeliveryApp.getAppName();
        // 推送安装包
        if (appDeliveryType == AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE && ObjectUtils.isEmpty(cbbUamDeliveryDetailList)) {
            LOGGER.info("交付应用[{}{}]不存在交付失败或者交付成功的应用无需交付", uamDeliveryApp.getAppId(), appName);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_REDELIVERY_FAIL_LOG, appName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }
        // 应用磁盘
        if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && ObjectUtils.isEmpty(cbbUamDeliveryDetailList)) {
            LOGGER.info("交付应用[{}{}]不存在交付失败的应用无需交付", uamDeliveryApp.getAppId(), appName);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DISK_DELIVERY_APP_REDELIVERY_FAIL_LOG, appName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }

        UUID appId = uamDeliveryApp.getAppId();

        CbbRedeliveryAppFromDeliveryAppDTO cbbRedeliveryAppFromDeliveryAppDTO = new CbbRedeliveryAppFromDeliveryAppDTO();
        cbbRedeliveryAppFromDeliveryAppDTO.setAppId(appId);
        cbbRedeliveryAppFromDeliveryAppDTO.setDeliveryAppId(uamDeliveryApp.getId());
        cbbRedeliveryAppFromDeliveryAppDTO.setDeliveryGroupId(uamDeliveryApp.getDeliveryGroupId());
        cbbRedeliveryAppFromDeliveryAppDTO.setAppDeliveryType(cbbUamDeliveryGroupDetailDTO.getAppDeliveryType());

        cbbAppDeliveryMgmtAPI.redeliveryApp(cbbRedeliveryAppFromDeliveryAppDTO);

        auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_REDELIVERY_SUCCESS_LOG, appName);

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }

    private static List<DeliveryStatusEnum> getDeliveryStatusList(AppDeliveryTypeEnum appDeliveryType) {
        return appDeliveryType == AppDeliveryTypeEnum.APP_DISK ? Collections.singletonList(DeliveryStatusEnum.DELIVERY_FAIL)
                : Arrays.asList(DeliveryStatusEnum.DELIVERY_FAIL, DeliveryStatusEnum.DELIVERY_SUCCESS);
    }


    /**
     * 交付应用详情-交付对象重新交付
     *
     * @param idWebRequest 交付对象id
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("-")
    @RequestMapping(value = "/deliveryAppDetail/redeliveryObject", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse redeliveryObjectFromDeliveryAppDetail(IdWebRequest idWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idWebRequest, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = idWebRequest.getId();

        UamDeliveryObjectDetailDTO deliveryObjectDetail = appDeliveryMgmtAPI.getDeliveryObjectDetail(id);
        UUID deliveryGroupId = deliveryObjectDetail.getDeliveryGroupId();
        // 判定是否有编辑该交付组的权限
        generalPermissionHelper.checkPermission(sessionContext, deliveryGroupId, AdminDataPermissionType.DELIVERY_GROUP);

        String appName = deliveryObjectDetail.getAppName();
        String cloudDesktopName = deliveryObjectDetail.getCloudDesktopName();

        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(deliveryGroupId);
        DeliveryStatusEnum deliveryStatus = deliveryObjectDetail.getDeliveryStatus();
        AppDeliveryTypeEnum appDeliveryType = cbbUamDeliveryGroupDetailDTO.getAppDeliveryType();
        // 推送安装包处理
        if (appDeliveryType == AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE && deliveryStatus == DeliveryStatusEnum.DELIVERING) {
            LOGGER.info("交付应用详情-交付对象重新交付[{}{}]已在交付中，无需交付", deliveryObjectDetail.getCloudDesktopId(), cloudDesktopName);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_REDELIVERY_OBJECT_FAIL_LOG, appName, cloudDesktopName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }
        // 应用磁盘处理
        if (appDeliveryType == AppDeliveryTypeEnum.APP_DISK && deliveryStatus != DeliveryStatusEnum.DELIVERY_FAIL) {
            LOGGER.info("交付应用详情-交付对象重新交付[{}{}]非交付失败状态，无需交付", deliveryObjectDetail.getCloudDesktopId(), cloudDesktopName);
            auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DISK_DELIVERY_APP_REDELIVERY_OBJECT_FAIL_LOG, appName, cloudDesktopName);
            return CommonWebResponse.fail(AaaBusinessKey.RCDC_AAA_OPERATOR_FAIL, new String[] {});
        }

        CbbRedeliveryObjectFromAppDetailDTO cbbRedeliveryObjectFromAppDetailDTO = new CbbRedeliveryObjectFromAppDetailDTO();
        BeanUtils.copyProperties(deliveryObjectDetail, cbbRedeliveryObjectFromAppDetailDTO);
        cbbRedeliveryObjectFromAppDetailDTO.setDeliveryDetailId(deliveryObjectDetail.getId());
        cbbRedeliveryObjectFromAppDetailDTO.setAppDeliveryType(cbbUamDeliveryGroupDetailDTO.getAppDeliveryType());

        cbbAppDeliveryMgmtAPI.redeliveryObject(cbbRedeliveryObjectFromAppDetailDTO);

        auditLogAPI.recordLog(UamDeliveryAppBusinessKey.RCDC_UAM_DELIVERY_APP_REDELIVERY_OBJECT_SUCCESS_LOG, appName, cloudDesktopName);

        return CommonWebResponse.success(AaaBusinessKey.RCDC_AAA_OPERATOR_SUCCESS, new String[] {});
    }

    private void checkSessionTypeDesktop(List<UUID> cloudDesktopIdList, String deliveryGroupName)
            throws BusinessException {
        List<CloudDesktopDTO> cloudDesktopDTOList = userDesktopMgmtAPI.listDesktopByDesktopIds(cloudDesktopIdList);
        long count = cloudDesktopDTOList.stream().filter(dto -> CbbDesktopSessionType.MULTIPLE == dto.getSessionType()).count();
        if (count > 0) {
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CREATE_DELIVERY_GROUP_FAIL_MULTI_SESSIONTYPE, deliveryGroupName);
        }
    }

}
