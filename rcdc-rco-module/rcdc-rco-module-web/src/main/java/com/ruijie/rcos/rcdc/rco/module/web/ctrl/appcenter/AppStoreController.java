package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruijie.rcos.rcdc.appcenter.module.def.CbbUamAppBusinessKey;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.CbbAppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppPushPackageSupportExtensionDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbSoftwareIdentificationDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbCreatePushInstallPackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbEditPushInstallPackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbPushInstallPackageDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CheckAppNameDuplicationDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSoftMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbUamSearchDeskSoftDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchAppDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.SearchPushInstallPackageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamAppDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamPushInstallPackageDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.DBConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RequestSourceEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask.DeleteUamAppBatchTaskHandler;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.contants.UamAppConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamAppBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamPushInstallPackageBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.CheckAppNameDuplicationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.CreatePushInstallPackageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.EditPushInstallPackageRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.response.CheckAppNameDuplicationResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.util.SortUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.request.MutilPlatformIdArrWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.request.IdWebRequest;
import com.ruijie.rcos.sk.webmvc.api.request.PageWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;
import com.ruijie.rcos.sk.webmvc.api.vo.ExactMatch;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 应用仓库
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/12/22 22:40
 *
 * @author coderLee23
 */
@Api(tags = "应用中心-应用仓库")
@Controller
@RequestMapping("/rco/appCenter/appStore")
public class AppStoreController {


    private static final Logger LOGGER = LoggerFactory.getLogger(AppStoreController.class);

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbDeskSoftMgmtAPI cbbDeskSoftMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;


    /**
     * 获取UAM应用磁盘列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("应用磁盘列表")
    @RequestMapping(value = "/appDisk/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamAppDiskDTO>> pageAppDisk(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 如果排序规则为空，则默认采用创建时间倒序
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchAppDiskDTO searchAppDiskDTO = new SearchAppDiskDTO();
        ExactMatch[] exactMatchArr = request.getExactMatchArr();
        for (ExactMatch exactMatch : exactMatchArr) {
            if (DBConstants.ID.equals(exactMatch.getName())) {
                String idStr = exactMatch.getValueArr()[0];
                searchAppDiskDTO.setId(UUID.fromString(idStr));
            }

            if (DBConstants.APP_STATUS.equals(exactMatch.getName())) {
                List<AppStatusEnum> appStatusList = Stream.of(exactMatch.getValueArr()).map(AppStatusEnum::valueOf).collect(Collectors.toList());
                searchAppDiskDTO.setAppStatusList(appStatusList);
            }

            if (DBConstants.OS_TYPE.equals(exactMatch.getName())) {
                List<CbbOsType> cbbOsTypeList = Stream.of(exactMatch.getValueArr()).map(CbbOsType::valueOf).collect(Collectors.toList());
                searchAppDiskDTO.setCbbOsTypeList(cbbOsTypeList);
            }

            if (DBConstants.CBB_IMAGE_TYPE.equals(exactMatch.getName())) {
                List<CbbImageType> cbbImageTypeList = Stream.of(exactMatch.getValueArr()).map(CbbImageType::valueOf).collect(Collectors.toList());
                searchAppDiskDTO.setCbbImageTypeList(cbbImageTypeList);
            }
            if (DBConstants.PLATFORM_STATUS.equals(exactMatch.getName())) {
                List<CloudPlatformStatus> platformStatusList = Stream.of(exactMatch.getValueArr())
                        .map(CloudPlatformStatus::valueOf)
                        .collect(Collectors.toList());
                searchAppDiskDTO.setPlatformStatusList(platformStatusList);
            }

        }

        searchAppDiskDTO.setAppName(request.getSearchKeyword());
        searchAppDiskDTO.setRequestSource(RequestSourceEnum.APP_DISK);

        generalPermissionHelper.setPermissionParam(sessionContext, searchAppDiskDTO);

        DefaultPageResponse<UamAppDiskDTO> uamAppDiskPageResponse = appDeliveryMgmtAPI.pageAppDisk(searchAppDiskDTO, pageable);
        return CommonWebResponse.success(uamAppDiskPageResponse);
    }


    /**
     * 推送安装包列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("推送安装包列表")
    @RequestMapping(value = "/pushInstallPackage/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<UamPushInstallPackageDTO>> pagePushInstallPackage(PageWebRequest request,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 如果排序规则为空，则默认采用创建时间倒序
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.UPDATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        SearchPushInstallPackageDTO searchPushInstallPackageDTO = new SearchPushInstallPackageDTO();
        ExactMatch[] exactMatchArr = request.getExactMatchArr();
        for (ExactMatch exactMatch : exactMatchArr) {
            if (DBConstants.ID.equals(exactMatch.getName())) {
                String idStr = exactMatch.getValueArr()[0];
                searchPushInstallPackageDTO.setId(UUID.fromString(idStr));
            }

            if (DBConstants.APP_STATUS.equals(exactMatch.getName())) {
                List<AppStatusEnum> appStatusList = Stream.of(exactMatch.getValueArr()).map(AppStatusEnum::valueOf).collect(Collectors.toList());
                searchPushInstallPackageDTO.setAppStatusList(appStatusList);
            }
        }

        searchPushInstallPackageDTO.setAppName(request.getSearchKeyword());

        generalPermissionHelper.setPermissionParam(sessionContext, searchPushInstallPackageDTO);

        DefaultPageResponse<UamPushInstallPackageDTO> uamPushInstallPackagePageResponse =
                appDeliveryMgmtAPI.pagePushInstallPackage(searchPushInstallPackageDTO, pageable);
        return CommonWebResponse.success(uamPushInstallPackagePageResponse);
    }


    /**
     * 软件静默安装参数识别
     *
     * @param idWebRequest 软件安装包id
     * @return CommonWebResponse<SoftwareIdentificationDTO> 软件静默安装参数识别
     * @throws BusinessException 业务异常
     */
    @ApiOperation("软件静默安装参数识别")
    @RequestMapping(value = "/pushInstallPackage/softwareIdentify", method = RequestMethod.POST)
    public CommonWebResponse<CbbSoftwareIdentificationDTO> identifySoftware(IdWebRequest idWebRequest) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        CbbSoftwareIdentificationDTO softwareIdentificationDTO = cbbPushInstallPackageMgmtAPI.identifySoftWare(idWebRequest.getId());
        return CommonWebResponse.success(softwareIdentificationDTO);
    }


    /**
     * 创建应用时选择软件列表
     *
     * @param request 分页查询web请求
     * @param sessionContext 上下文
     * @return 应用列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建应用时选择软件列表")
    @RequestMapping(value = "/pushInstallPackage/deskSoft/list", method = RequestMethod.POST)
    public CommonWebResponse<DefaultPageResponse<CbbDeskSoftDTO>> pageDeskSoft(PageWebRequest request, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(request, "request must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        AppPushPackageSupportExtensionDTO extensionDTO = cbbAppStoreMgmtAPI.getSupportExtensionGlobalConfig();
        ArrayList<String> allSupportExtensionList = new ArrayList<>();
        ArrayList<String> linuxSupportExtensionList = new ArrayList<>();
        allSupportExtensionList.addAll(extensionDTO.getWindows());
        linuxSupportExtensionList.addAll(extensionDTO.getLinux());
        linuxSupportExtensionList.addAll(extensionDTO.getLinuxZip());
        allSupportExtensionList.addAll(extensionDTO.getCommon());
        // 如果排序规则为空，则默认采用创建时间倒序
        com.ruijie.rcos.sk.webmvc.api.vo.Sort requestSort = request.getSort();
        Sort sort = Sort.by(Sort.Direction.DESC, DBConstants.CREATE_TIME);
        if (Objects.nonNull(requestSort)) {
            sort = SortUtils.transforSort(request.getSort());
        }
        Pageable pageable = PageRequest.of(request.getPage(), request.getLimit(), sort);

        CbbUamSearchDeskSoftDTO cbbUamSearchDeskSoftDTO = new CbbUamSearchDeskSoftDTO();
        cbbUamSearchDeskSoftDTO.setFileName(request.getSearchKeyword());
        cbbUamSearchDeskSoftDTO.setFileExtensionList(allSupportExtensionList);
        cbbUamSearchDeskSoftDTO.setLinuxFileExtensionList(linuxSupportExtensionList);

        DefaultPageResponse<CbbDeskSoftDTO> cbbDeskSoftPageResponse = cbbDeskSoftMgmtAPI.pageDeskSoft(cbbUamSearchDeskSoftDTO, pageable);
        return CommonWebResponse.success(cbbDeskSoftPageResponse);
    }


    /**
     * 创建推送安装包
     *
     * @param createPushInstallPackageRequest 推送安装包参数
     * @param sessionContext 上下文
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("创建推送安装包")
    @RequestMapping(value = "/pushInstallPackage/create", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse createUamPushInstallPackage(CreatePushInstallPackageRequest createPushInstallPackageRequest,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(createPushInstallPackageRequest, "createPushInstallPackageRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        String appName = createPushInstallPackageRequest.getAppName().trim();
        try {
            // 判定应用名称是否已存在
            CheckAppNameDuplicationDTO checkAppNameDuplicationDTO = new CheckAppNameDuplicationDTO();
            checkAppNameDuplicationDTO.setAppName(appName);
            checkAppNameDuplicationDTO.setAppType(AppTypeEnum.PUSH_INSTALL_PACKAGE);

            Boolean hasDuplication = cbbAppStoreMgmtAPI.checkAppNameDuplication(checkAppNameDuplicationDTO);
            if (Boolean.TRUE.equals(hasDuplication)) {
                throw new BusinessException(CbbUamAppBusinessKey.RCDC_UAM_APP_NAME_EXISTS, appName);
            }

            CbbCreatePushInstallPackageDTO cbbCreatePushInstallPackageDTO = new CbbCreatePushInstallPackageDTO();
            BeanUtils.copyProperties(createPushInstallPackageRequest, cbbCreatePushInstallPackageDTO);
            createPushInstallPackageRequest.setAppName(appName);
            String manualQuietInstallParam = createPushInstallPackageRequest.getManualQuietInstallParam();
            if (StringUtils.hasText(manualQuietInstallParam)) {
                createPushInstallPackageRequest.setManualQuietInstallParam(manualQuietInstallParam.trim());
            }

            String executeFilePath = createPushInstallPackageRequest.getExecuteFilePath();
            if (StringUtils.hasText(executeFilePath)) {
                createPushInstallPackageRequest.setExecuteFilePath(executeFilePath.trim());
            }
            UUID appId = cbbPushInstallPackageMgmtAPI.createPushInstallPackage(cbbCreatePushInstallPackageDTO);

            // 保存权限数据
            generalPermissionHelper.savePermission(sessionContext, appId, AdminDataPermissionType.UAM_APP);

            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_CREATE_PUSH_INSTALL_PACKAGE_SUCCESS_LOG, appName);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(DBConstants.APP_ID, appId);

            return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, jsonObject);

        } catch (BusinessException e) {
            LOGGER.error("创建推送安装包失败！", e);
            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_CREATE_PUSH_INSTALL_PACKAGE_FAIL_LOG, appName, e.getI18nMessage());
            throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_CREATE_PUSH_INSTALL_PACKAGE_FAIL_LOG, e, appName,
                    e.getI18nMessage());
        }

    }


    /**
     * 编辑推送安装包
     *
     * @param editPushInstallPackageRequest 推送安装包参数
     * @param sessionContext 上下文
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑推送安装包")
    @RequestMapping(value = "/pushInstallPackage/edit", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse editUamPushInstallPackage(EditPushInstallPackageRequest editPushInstallPackageRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(editPushInstallPackageRequest, "editPushInstallPackageRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        String appName = editPushInstallPackageRequest.getAppName().trim();

        UUID id = editPushInstallPackageRequest.getId();
        // 判定应用是否存在
        CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(id);
        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, id, AdminDataPermissionType.UAM_APP);
        // 判定应用是否被使用
        Boolean existsUsed = cbbAppDeliveryMgmtAPI.existsUsedDeliveryApp(id);
        // 获取推送安装包详情
        CbbPushInstallPackageDetailDTO cbbPushInstallPackageDetailDTO = cbbPushInstallPackageMgmtAPI.getPushInstallPackageDetail(id);

        try {
            // 判定应用名称是否已存在，排除自己的名称
            CheckAppNameDuplicationDTO checkAppNameDuplicationDTO = new CheckAppNameDuplicationDTO();
            checkAppNameDuplicationDTO.setAppName(appName);
            checkAppNameDuplicationDTO.setId(cbbUamAppDTO.getId());
            checkAppNameDuplicationDTO.setAppType(AppTypeEnum.PUSH_INSTALL_PACKAGE);

            Boolean hasDuplication = cbbAppStoreMgmtAPI.checkAppNameDuplication(checkAppNameDuplicationDTO);
            if (Boolean.TRUE.equals(hasDuplication) && !cbbUamAppDTO.getAppName().equals(appName)) {
                LOGGER.error("推送安装包[{}]与其他安装包名称重复，不允许更新", appName);
                throw new BusinessException(CbbUamAppBusinessKey.RCDC_UAM_APP_NAME_EXISTS, appName);
            }

            // 判定应用状态是否允许编辑
            if (!UamAppConstants.ALLOW_EDIT_PUSH_INSTALL_PACKAGE_STATUS.contains(cbbUamAppDTO.getAppStatus())) {
                LOGGER.error("推送安装包[{0}]非待发布或已发布状态，不允许更新", appName);
                throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_STATUS_ERROR);
            }

            // 推送安装包已绑定交付组，不支持修改操作系统类型
            if (existsUsed && editPushInstallPackageRequest.getOsPlatform() != cbbPushInstallPackageDetailDTO.getOsPlatform()) {
                LOGGER.error("推送安装包[{0}]已绑定交付组，不支持修改操作系统类型", appName);
                throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_INSTALL_PACKAGE_EXIST_USED, appName);
            }

            CbbEditPushInstallPackageDTO cbbEditPushInstallPackageDTO = new CbbEditPushInstallPackageDTO();
            BeanUtils.copyProperties(editPushInstallPackageRequest, cbbEditPushInstallPackageDTO);
            editPushInstallPackageRequest.setAppName(appName);
            String manualQuietInstallParam = editPushInstallPackageRequest.getManualQuietInstallParam();
            if (StringUtils.hasText(manualQuietInstallParam)) {
                cbbEditPushInstallPackageDTO.setManualQuietInstallParam(manualQuietInstallParam.trim());
            }

            String executeFilePath = editPushInstallPackageRequest.getExecuteFilePath();
            if (StringUtils.hasText(executeFilePath)) {
                cbbEditPushInstallPackageDTO.setExecuteFilePath(executeFilePath.trim());
            }

            cbbPushInstallPackageMgmtAPI.editPushInstallPackage(cbbEditPushInstallPackageDTO);
            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_SUCCESS_LOG, appName);
        } catch (BusinessException e) {
            LOGGER.error("编辑推送安装包失败！", e);
            if (CbbAppCenterBusinessKey.RCDC_UAM_APP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG, e.getI18nMessage());
                throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG, e,
                        e.getI18nMessage());

            }
            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_FAIL_LOG, appName, e.getI18nMessage());
            throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_EDIT_PUSH_INSTALL_PACKAGE_FAIL_LOG, e, appName, e.getI18nMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 推送安装包-重新制作种子文件【该方法未被使用】
     *
     * @param idWebRequest 推送安装包id
     * @param sessionContext 上下文
     * @return DefaultWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("推送安装包-重新制作种子文件")
    @RequestMapping(value = "/pushInstallPackage/reMakeSeed", method = RequestMethod.POST)
    @EnableAuthority
    public DefaultWebResponse reMakeSeed(IdWebRequest idWebRequest, SessionContext sessionContext) throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        // 判定应用是否存在
        CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(idWebRequest.getId());
        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, idWebRequest.getId(), AdminDataPermissionType.UAM_APP);
        String appName = cbbUamAppDTO.getAppName();

        try {
            // 判定应用状态是否允许编辑
            if (AppStatusEnum.FINISH_ERROR != cbbUamAppDTO.getAppStatus()) {
                LOGGER.error("推送安装包[{}]状态为非做种失败，不允许重新制作", JSON.toJSONString(cbbUamAppDTO));
                throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_NOT_NEED_REMAKE_SEED,
                        cbbUamAppDTO.getAppName());
            }

            cbbPushInstallPackageMgmtAPI.remakeSeed(idWebRequest.getId());
            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_SUCCESS_LOG, appName);
        } catch (BusinessException e) {
            LOGGER.error("推送安装包重新制作种子文件失败！", e);
            if (CbbAppCenterBusinessKey.RCDC_UAM_APP_NOT_EXIST.equals(e.getKey())) {
                auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG, e.getI18nMessage());
                throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_NOT_EXISTS_LOG, e,
                        e.getI18nMessage());
            }
            auditLogAPI.recordLog(UamPushInstallPackageBusinessKey.RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_FAIL_LOG, appName, e.getI18nMessage());
            throw new BusinessException(UamPushInstallPackageBusinessKey.RCDC_UAM_REMAKE_SEED_PUSH_INSTALL_PACKAGE_FAIL_LOG, e, appName,
                    e.getI18nMessage());
        }

        return DefaultWebResponse.Builder.success(UserBusinessKey.RCDC_RCO_MODULE_OPERATE_SUCCESS, new String[] {});
    }

    /**
     * 获取推送安装包详情
     *
     * @param idWebRequest 推送安装包-应用id
     * @param sessionContext 上下文
     * @return UamPushInstallPackageDetailDTO 推送安装包详情
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取推送安装包详情")
    @RequestMapping(value = "/pushInstallPackage/detail", method = RequestMethod.POST)
    public CommonWebResponse<CbbPushInstallPackageDetailDTO> getUamPushInstallPackageDetail(IdWebRequest idWebRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idWebRequest, "idWebRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        // 校验是否有该记录权限
        generalPermissionHelper.checkPermission(sessionContext, idWebRequest.getId(), AdminDataPermissionType.UAM_APP);

        CbbPushInstallPackageDetailDTO cbbPushInstallPackageDetailDTO =
                cbbPushInstallPackageMgmtAPI.getPushInstallPackageDetail(idWebRequest.getId());
        return CommonWebResponse.success(cbbPushInstallPackageDetailDTO);
    }


    /**
     * 检测应用名称是否重复
     *
     * @param checkAppNameDuplicationRequest 入参
     * @return 响应
     */
    @ApiOperation("检测应用名称是否重复")
    @RequestMapping(value = "/checkNameDuplication", method = RequestMethod.POST)
    public CommonWebResponse<CheckAppNameDuplicationResponse> checkNameDuplication(CheckAppNameDuplicationRequest checkAppNameDuplicationRequest) {
        Assert.notNull(checkAppNameDuplicationRequest, "checkAppNameDuplicationRequest must not be null");
        CheckAppNameDuplicationDTO checkAppNameDuplicationDTO = new CheckAppNameDuplicationDTO();
        BeanUtils.copyProperties(checkAppNameDuplicationRequest, checkAppNameDuplicationDTO);
        checkAppNameDuplicationDTO.setAppType(AppTypeEnum.PUSH_INSTALL_PACKAGE);
        Boolean hasDuplication = cbbAppStoreMgmtAPI.checkAppNameDuplication(checkAppNameDuplicationDTO);
        return CommonWebResponse.success(new CheckAppNameDuplicationResponse(hasDuplication));
    }


    /**
     * 支持批量删除应用
     *
     * @param idArrWebRequest 应用id列表
     * @param builder 批处理任务
     * @param sessionContext 上下文
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("支持批量删除UAM应用")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse delete(MutilPlatformIdArrWebRequest idArrWebRequest, BatchTaskBuilder builder, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(idArrWebRequest, "idWebRequest must not be null");
        Assert.notNull(builder, "builder must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");

        UUID[] idArr = idArrWebRequest.getIdArr();
        Boolean shouldOnlyDeleteDataFromDb = idArrWebRequest.getShouldOnlyDeleteDataFromDb();
        String prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
        // 批量删除任务
        final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()//
                .map(id -> DefaultBatchTaskItem.builder().itemId(id).itemName(UamAppBusinessKey.RCDC_UAM_APP_DELETE_ITEM_NAME, new String[] {prefix})
                        .build())
                .iterator();

        DeleteUamAppBatchTaskHandler handler = new DeleteUamAppBatchTaskHandler(iterator);
        handler.setAuditLogAPI(auditLogAPI);
        handler.setCbbbAppStoreMgmtAPI(cbbAppStoreMgmtAPI);
        handler.setCbbPushInstallPackageMgmtAPI(cbbPushInstallPackageMgmtAPI);
        handler.setAppSoftwarePackageMgmtAPI(cbbAppSoftwarePackageMgmtAPI);
        handler.setCbbAppDeliveryMgmtAPI(cbbAppDeliveryMgmtAPI);
        handler.setCbbUamAppTestAPI(cbbUamAppTestAPI);
        handler.setAppCenterPermissionHelper(generalPermissionHelper);
        handler.setSessionContext(sessionContext);
        handler.setShouldOnlyDeleteDataFromDb(shouldOnlyDeleteDataFromDb);

        BatchTaskSubmitResult result = startDeleteBatchTask(idArr, handler, builder, prefix);

        return CommonWebResponse.success(result);
    }


    private BatchTaskSubmitResult startDeleteBatchTask(UUID[] idArr, DeleteUamAppBatchTaskHandler handler, BatchTaskBuilder builder,
                                                      String prefix) throws BusinessException {
        // 删除单个应用
        BatchTaskSubmitResult result;
        if (idArr.length == 1) {
            CbbUamAppDTO cbbUamAppDTO = cbbAppStoreMgmtAPI.getUamApp(idArr[0]);
            handler.setBatchFlag(false);
            handler.setAppName(cbbUamAppDTO.getAppName());
            result = builder.setTaskName(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SINGLE_TASK_NAME, prefix)
                    .setTaskDesc(UamAppBusinessKey.RCDC_UAM_APP_DELETE_SINGLE_TASK_DESC, cbbUamAppDTO.getAppName(), prefix) //
                    .registerHandler(handler).start();
        } else {
            result = builder.setTaskName(UamAppBusinessKey.RCDC_UAM_APP_DELETE_TASK_NAME, prefix).setTaskDesc(UamAppBusinessKey.RCDC_UAM_APP_DELETE_TASK_DESC, prefix)
                    .enableParallel().registerHandler(handler).start();
        }
        return result;
    }

    /**
     * 获取安装包支持格式
     *
     * @return 响应
     */
    @ApiOperation("获取安装包支持格式")
    @RequestMapping(value = "/supportExtension", method = RequestMethod.POST)
    public CommonWebResponse<AppPushPackageSupportExtensionDTO> checkNameDuplication() {
        AppPushPackageSupportExtensionDTO response = cbbAppStoreMgmtAPI.getSupportExtensionGlobalConfig();
        return CommonWebResponse.success(response);
    }
}
