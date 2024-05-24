package com.ruijie.rcos.rcdc.rco.module.web.validation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbPushInstallPackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamDeliveryGroupDetailDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbPushInstallPackageDetailDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.rco.module.web.util.LinuxPathUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CheckDeliveryGroupNameDuplicationDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppDeliveryTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.LocationTypeEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.rco.module.def.api.AppDeliveryMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.request.*;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.GeneralPermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WindowsPathUtils;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

/**
 * Description:
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/01/14 14:49
 *
 * @author coderLee23
 */
@Service
public class AppDeliveryValidation {


    private static final Logger LOGGER = LoggerFactory.getLogger(AppDeliveryValidation.class);

    /**
     * 分页大小
     */
    private static final Integer QUERY_BATCH_SIZE = 1000;

    @Autowired
    private CbbAppDeliveryMgmtAPI cbbAppDeliveryMgmtAPI;

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private GeneralPermissionHelper generalPermissionHelper;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private AppCenterHelper appCenterHelper;

    @Autowired
    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    @Autowired
    private CbbPushInstallPackageMgmtAPI cbbPushInstallPackageMgmtAPI;

    /**
     * 创建软件安装包交付组，数据合法性校验
     *
     * @param createDeliveryGroupRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void validateCreateUamPushInstallPackageDeliveryGroup(CreatePushInstallPackageAppDiskDeliveryGroupRequest createDeliveryGroupRequest)
            throws BusinessException {
        Assert.notNull(createDeliveryGroupRequest, "createDeliveryGroupRequest must not null");

        PushAppConfig pushAppConfig = createDeliveryGroupRequest.getPushAppConfig();

        // 校验名称是否重复
        // 判定交付组名称是否已存在
        String deliveryGroupName = createDeliveryGroupRequest.getDeliveryGroupName();
        CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO = new CheckDeliveryGroupNameDuplicationDTO();
        checkDeliveryGroupNameDuplicationDTO.setDeliveryGroupName(deliveryGroupName);
        checkDeliveryGroupNameDuplicationDTO.setAppDeliveryType(AppDeliveryTypeEnum.PUSH_INSTALL_PACKAGE);
        checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);

        // 校验数据合法性，防止接口测试问题
        List<UUID> appIdList = createDeliveryGroupRequest.getAppIdList();
        List<CbbUamAppDTO> cbbUamAppDTOList = cbbAppStoreMgmtAPI.listUamApp(appIdList, AppStatusEnum.getUseStateList());
        if (cbbUamAppDTOList.size() < appIdList.size()) {
            LOGGER.error("部分应用已不存在");
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_APP_NOT_EXISTS);
        }

        if (Objects.isNull(createDeliveryGroupRequest.getOsPlatform())) {
            LOGGER.error("操作系统类型不能为空");
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_OS_PLATFORM_NOT_NULL);
        }

        List<CbbPushInstallPackageDetailDTO> dtoList = cbbPushInstallPackageMgmtAPI.listPushInstallPackageByUamAppIds(appIdList);
        for (CbbPushInstallPackageDetailDTO pushInstallPackageDetailDTO : dtoList) {
            if (createDeliveryGroupRequest.getOsPlatform() != pushInstallPackageDetailDTO.getOsPlatform()) {
                LOGGER.error("交付应用与交付组所选操作系统不匹配，");
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_AND_APP_OS_PLATFORM_DIFFERENT);
            }
        }

        // 交付对象相关校验
        appCenterHelper.checkDeliveryGroupObject(createDeliveryGroupRequest.getCloudDesktopIdList().toArray(new UUID[0]),
                createDeliveryGroupRequest.getOsPlatform());

        String location = pushAppConfig.getLocation();
        // 如果是自定义 校验输入的windows路径是否合法
        if (pushAppConfig.getLocationType() == LocationTypeEnum.CUSTOM && createDeliveryGroupRequest.getOsPlatform() == OsPlatform.WINDOWS) {
            WindowsPathUtils.checkWindowsPath(location);
        }
        // 如果是自定义 校验输入的linux路径是否合法
        if (pushAppConfig.getLocationType() == LocationTypeEnum.CUSTOM && createDeliveryGroupRequest.getOsPlatform() == OsPlatform.LINUX) {
            LinuxPathUtils.checkLinuxPath(location);
        }
    }



    /**
     * 创建应用磁盘交付组，数据合法性校验
     *
     * @param createAppDiskDeliveryGroupRequest 请求参数
     * @throws BusinessException 业务异常
     */
    public void validateCreateUamAppDiskDeliveryGroup(CreateAppDiskDeliveryGroupRequest createAppDiskDeliveryGroupRequest) throws BusinessException {
        Assert.notNull(createAppDiskDeliveryGroupRequest, "createDeliveryGroupRequest must not null");

        // 校验名称是否重复
        // 判定交付组名称是否已存在
        String deliveryGroupName = createAppDiskDeliveryGroupRequest.getDeliveryGroupName();
        CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO = new CheckDeliveryGroupNameDuplicationDTO();
        checkDeliveryGroupNameDuplicationDTO.setDeliveryGroupName(deliveryGroupName);
        checkDeliveryGroupNameDuplicationDTO.setAppDeliveryType(AppDeliveryTypeEnum.APP_DISK);

        checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);
        // 校验数据合法性，防止接口测试问题
        List<UUID> appIdList = createAppDiskDeliveryGroupRequest.getAppIdList();

        List<CbbUamAppDTO> cbbUamAppDTOList = cbbAppStoreMgmtAPI.listUamApp(appIdList, AppStatusEnum.getUseStateList());
        if (cbbUamAppDTOList.size() < appIdList.size()) {
            LOGGER.error("部分应用已不存在");
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_APP_NOT_EXISTS);
        }

        // 判断是否是windows系统
        final boolean isWindowsOs = appCenterHelper.isWindowsOs(createAppDiskDeliveryGroupRequest.getCloudDesktopIdList().toArray(new UUID[0]));
        if (!isWindowsOs) {
            LOGGER.error("存在不是windows的云桌面");
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_CONTAINS_DESK_NO_WINDOWS);
        }
        // 判定云桌面是否被其他交付组使用
        List<List<UUID>> partitionIdList = Lists.partition(createAppDiskDeliveryGroupRequest.getCloudDesktopIdList(), QUERY_BATCH_SIZE);
        for (List<UUID> cloudDesktopIdList : partitionIdList) {
            Boolean existUsed = appDeliveryMgmtAPI.existsByAppDeliveryTypeAndCloudDesktopIdIn(AppDeliveryTypeEnum.APP_DISK, cloudDesktopIdList);
            if (Boolean.TRUE.equals(existUsed)) {
                LOGGER.error("存在已经被使用的云桌面，不允许添加到该交付组！云桌面id列表：{}", JSON.toJSONString(cloudDesktopIdList));
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_CLOUD_DESKTOPS_EXISTS_USED);
            }
        }

        Map<AppTypeEnum, List<CbbUamAppDTO>> cbbUamAppMap =
                cbbUamAppDTOList.stream().collect(Collectors.groupingBy(CbbUamAppDTO::getAppType, Collectors.toList()));

        List<CbbUamAppDTO> appSoftWarePackageList = cbbUamAppMap.get(AppTypeEnum.APP_SOFTWARE_PACKAGE);
        if (CollectionUtils.isEmpty(appSoftWarePackageList)) {
            LOGGER.info("添加的不存在应用软件包");
            return;
        }

        if (CollectionUtils.isNotEmpty(appSoftWarePackageList) && appSoftWarePackageList.size() > 1) {
            LOGGER.error("应用软件包只允许添加一个,当前添加个数：{}", appSoftWarePackageList.size());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_NOT_ALLOW_ADD_MOST_THAN_ONE_APP_SOFTWARE_PACKAGE);
        }

        CbbUamAppDTO cbbUamAppDTO = appSoftWarePackageList.get(0);
        // 判定软件是否存在
        AppSoftwarePackageDTO appSoftwarePackageDTO = cbbAppSoftwarePackageMgmtAPI.findAppSoftwarePackageById(cbbUamAppDTO.getId());

        createAppDiskDeliveryGroupRequest.setOsType(appSoftwarePackageDTO.getOsType());
        createAppDiskDeliveryGroupRequest.setCbbImageType(appSoftwarePackageDTO.getAppSoftwarePackageType());
        createAppDiskDeliveryGroupRequest.setOsVersion(appSoftwarePackageDTO.getOsVersion());
        createAppDiskDeliveryGroupRequest.setImageTemplateId(appSoftwarePackageDTO.getImageTemplateId());

        // 校验添加云桌面是否匹配规格
        for (List<UUID> cloudDesktopIdList : partitionIdList) {
            CountCloudDesktopDTO countCloudDesktopDTO = new CountCloudDesktopDTO();
            countCloudDesktopDTO.setIdList(cloudDesktopIdList);
            countCloudDesktopDTO.setPattern(CbbCloudDeskPattern.PERSONAL.name());
            countCloudDesktopDTO.setOsType(appSoftwarePackageDTO.getOsType().name());
            countCloudDesktopDTO.setOsVersion(appSoftwarePackageDTO.getOsVersion());
            countCloudDesktopDTO.setCbbImageType(appSoftwarePackageDTO.getAppSoftwarePackageType().name());
            countCloudDesktopDTO.setImageTemplateId(appSoftwarePackageDTO.getImageTemplateId());
            countCloudDesktopDTO.setIsDelete(false);
            Integer countNumber = userDesktopMgmtAPI.countByCloudDesktop(countCloudDesktopDTO);
            if (countNumber < cloudDesktopIdList.size()) {
                LOGGER.error("存在不符合规格的云桌面，不允许添加到该交付组！云桌面id列表：{}", JSON.toJSONString(cloudDesktopIdList));
                throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_CLOUD_DESKTOPS_NOT_CONFORM_USED);
            }
        }

    }


    /**
     * 校验数据权限和合法性
     * 
     * @param editPushInstallPackageDeliveryGroupRequest 编辑推动安装包交付组参数
     * @param sessionContext 上下文
     * @throws BusinessException 业务异常
     */
    public void validateEditUamPushInstallPackageDeliveryGroup(EditPushInstallPackageDeliveryGroupRequest editPushInstallPackageDeliveryGroupRequest,
            SessionContext sessionContext) throws BusinessException {
        Assert.notNull(editPushInstallPackageDeliveryGroupRequest, "editDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = editPushInstallPackageDeliveryGroupRequest.getId();
        // 获取交付组系统类型
        CbbUamDeliveryGroupDetailDTO cbbUamDeliveryGroupDetailDTO = cbbAppDeliveryMgmtAPI.getCbbUamDeliveryGroupDetail(id);
        OsPlatform osPlatform = cbbUamDeliveryGroupDetailDTO.getOsPlatform();

        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, id, AdminDataPermissionType.DELIVERY_GROUP);

        PushAppConfig pushAppConfig = editPushInstallPackageDeliveryGroupRequest.getPushAppConfig();
        String location = pushAppConfig.getLocation();
        // 如果是自定义 校验输入的windows路径是否合法
        if (pushAppConfig.getLocationType() == LocationTypeEnum.CUSTOM && osPlatform == OsPlatform.WINDOWS) {
            WindowsPathUtils.checkWindowsPath(location);
        }
        // 如果是自定义 校验输入的linux路径是否合法
        if (pushAppConfig.getLocationType() == LocationTypeEnum.CUSTOM && osPlatform == OsPlatform.LINUX) {
            LinuxPathUtils.checkLinuxPath(location);
        }

        // 校验名称是否重复
        CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO = new CheckDeliveryGroupNameDuplicationDTO();
        checkDeliveryGroupNameDuplicationDTO.setId(id);
        checkDeliveryGroupNameDuplicationDTO.setDeliveryGroupName(editPushInstallPackageDeliveryGroupRequest.getDeliveryGroupName());

        checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);
    }

    /**
     * 校验数据权限和合法性
     *
     * @param editAppDiskDeliveryGroupRequest 编辑应用磁盘交付组参数
     * @param sessionContext 上下文
     * @throws BusinessException 业务异常
     */
    public void validateEditUamAppDiskDeliveryGroup(EditAppDiskDeliveryGroupRequest editAppDiskDeliveryGroupRequest, SessionContext sessionContext)
            throws BusinessException {
        Assert.notNull(editAppDiskDeliveryGroupRequest, "editDeliveryGroupRequest must not be null");
        Assert.notNull(sessionContext, "sessionContext must not be null");
        UUID id = editAppDiskDeliveryGroupRequest.getId();
        // 校验是否有编辑该记录权限
        generalPermissionHelper.checkPermission(sessionContext, id, AdminDataPermissionType.DELIVERY_GROUP);

        // 校验名称是否重复
        CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO = new CheckDeliveryGroupNameDuplicationDTO();
        checkDeliveryGroupNameDuplicationDTO.setId(id);
        checkDeliveryGroupNameDuplicationDTO.setDeliveryGroupName(editAppDiskDeliveryGroupRequest.getDeliveryGroupName());

        checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);
    }



    private void checkDeliveryGroupNameDuplication(CheckDeliveryGroupNameDuplicationDTO checkDeliveryGroupNameDuplicationDTO)
            throws BusinessException {
        // 判定交付组名称是否已存在
        Boolean hasDuplication = cbbAppDeliveryMgmtAPI.checkDeliveryGroupNameDuplication(checkDeliveryGroupNameDuplicationDTO);
        if (Boolean.TRUE.equals(hasDuplication)) {
            LOGGER.warn("交付组名称{}已存在", checkDeliveryGroupNameDuplicationDTO.getDeliveryGroupName());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_DELIVERY_GROUP_NAME_EXISTS,
                    checkDeliveryGroupNameDuplicationDTO.getDeliveryGroupName());
        }
    }

}
