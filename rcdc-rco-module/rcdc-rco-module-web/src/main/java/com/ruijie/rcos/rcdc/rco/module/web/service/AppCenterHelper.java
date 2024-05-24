package com.ruijie.rcos.rcdc.rco.module.web.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppStoreMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbUamAppTestTargetAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbAppTestTargetDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.CbbUamAppTestDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppStatusEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.DesktopTestStateEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.TestTaskStateEnum;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.OsPlatform;
import com.ruijie.rcos.rcdc.rca.module.def.enums.DesktopType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UamAppTestAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CountCloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DeskImageRelatedDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.ListRequestHelper;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryGroupBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年02月02日
 *
 * @author zhk
 */
@Service
public class AppCenterHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppCenterHelper.class);

    private static final Integer SUB_DESK_ID_MAX = 200;

    @Autowired
    CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    private CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    private UamAppTestAPI uamAppTestAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    /**
     * 校验应用软件id和软件安装包id
     *
     * @param appIdArr 应用id
     * @return 软件安装包id
     * @throws BusinessException 业务异常
     */
    public UUID checkAppId(UUID[] appIdArr) throws BusinessException {
        Assert.notNull(appIdArr, "appIdArr must not be null");

        List<UUID> appIdList = Stream.of(appIdArr).collect(Collectors.toList());
        List<CbbUamAppDTO> cbbUamAppDTOList = cbbAppStoreMgmtAPI.listUamApp(appIdList, AppStatusEnum.getUseStateList());
        if (cbbUamAppDTOList.size() < appIdList.size()) {
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_APP_NOT_EXISTS);
        }

        Map<AppTypeEnum, List<CbbUamAppDTO>> cbbUamAppMap =
                cbbUamAppDTOList.stream().collect(Collectors.groupingBy(CbbUamAppDTO::getAppType, Collectors.toList()));

        List<CbbUamAppDTO> appSoftWarePackageList = cbbUamAppMap.get(AppTypeEnum.APP_SOFTWARE_PACKAGE);
        if (CollectionUtils.isEmpty(appSoftWarePackageList)) {
            LOGGER.info("无应用软件包");
            // 无应用软件包
            return null;
        }
        if (appSoftWarePackageList.size() > 1) {
            LOGGER.error("应用软件包只允许添加一个,当前添加个数：{}", appSoftWarePackageList.size());
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_NOT_ALLOW_ADD_MOST_THAN_ONE_APP_SOFTWARE_PACKAGE);
        }
        return appSoftWarePackageList.get(0).getId();
    }

    /**
     * 云桌面校验
     *
     * @param deskIdArr 云桌面id
     * @param imageTemplateId 镜像模板id
     * @param osVersion 操作系统版本号
     * @param osType 操作系统
     * @param cbbImageType 镜像类型
     * @throws BusinessException 业务异常
     */
    public void checkSoftwarePackage(UUID[] deskIdArr, UUID imageTemplateId, String osVersion, CbbOsType osType, CbbImageType cbbImageType)
            throws BusinessException {
        Assert.notNull(deskIdArr, "deskIdArr must not be null");
        Assert.notNull(imageTemplateId, "imageTemplateId must not be null");
        Assert.hasText(osVersion, "osVersion must not be null or empty");
        Assert.notNull(osType, "osType must not be null");
        Assert.notNull(cbbImageType, "cbbImageType must not be null");

        List<UUID> deskIdList = Stream.of(deskIdArr).collect(Collectors.toList());
        CountCloudDesktopDTO countCloudDesktopDTO = new CountCloudDesktopDTO();
        countCloudDesktopDTO.setIdList(deskIdList);
        countCloudDesktopDTO.setPattern(CbbCloudDeskPattern.PERSONAL.name());
        countCloudDesktopDTO.setImageTemplateId(imageTemplateId);
        countCloudDesktopDTO.setOsVersion(osVersion);
        countCloudDesktopDTO.setOsType(osType.name());
        countCloudDesktopDTO.setCbbImageType(cbbImageType.name());
        countCloudDesktopDTO.setIsDelete(false);
        Integer countNumber = userDesktopMgmtAPI.countByCloudDesktop(countCloudDesktopDTO);
        if (countNumber < deskIdList.size()) {
            LOGGER.error("存在不符合规格的云桌面，不允许添加到该测试组！云桌面id列表：{}", JSON.toJSONString(deskIdArr));
            throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_SOME_CLOUD_DESKTOPS_NOT_CONFORM_USED);
        }
    }


    /**
     * 添加测试桌面校验
     *
     * @param testId 测试id
     * @param deskId 桌面id
     * @throws BusinessException 业务异常
     */
    public void testDeskCheck(UUID testId, UUID deskId) throws BusinessException {
        Assert.notNull(testId, "testId must not be null");
        Assert.notNull(deskId, "deskId must not be null");

        final CbbUamAppTestDTO cbbUamAppTestDTO = cbbUamAppTestAPI.getUamAppTestInfo(testId);
        CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(deskId);
        // 判定测试组是否存在
        if (!CbbCloudDeskPattern.PERSONAL.name().equals(cloudDesktopDetailDTO.getDesktopType())) {
            LOGGER.error("云桌面[{}]为非个性桌面，不允许添加到测试组[{}]", cloudDesktopDetailDTO.getDesktopName(), cbbUamAppTestDTO.getName());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_ADD_DESKTOP_NOT_PERSONAL, cloudDesktopDetailDTO.getDesktopName(),
                    cbbUamAppTestDTO.getName());
        }

        if (!CbbOsType.isWindowsOs(cloudDesktopDetailDTO.getDesktopImageType())) {
            LOGGER.error("云桌面[{}]不是windows系统，不允许添加到测试组[{}]", cloudDesktopDetailDTO.getDesktopName(), cbbUamAppTestDTO.getName());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_DESK_NO_WINDOWS, cloudDesktopDetailDTO.getDesktopName(),
                    cbbUamAppTestDTO.getName());
        }

        if (Objects.nonNull(cbbUamAppTestDTO.getAppSoftwarePackageType()) && Objects.nonNull(cbbUamAppTestDTO.getOsType())) {
            if (cloudDesktopDetailDTO.getDesktopImageType() != cbbUamAppTestDTO.getOsType()
                    || !cbbUamAppTestDTO.getAppSoftwarePackageType().name().equals(cloudDesktopDetailDTO.getCbbImageType())) {
                LOGGER.error("云桌面[{}]的虚机类型或者云桌面类型不匹配，不允许添加到测试组[{}]", cloudDesktopDetailDTO.getDesktopName(), cbbUamAppTestDTO.getName());
                throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_MISMATCH, cloudDesktopDetailDTO.getDesktopName(),
                        cbbUamAppTestDTO.getName());
            }
        }
        checkTestingDesk(deskId);
        final CbbAppTestTargetDTO appTestTargetDTO = cbbUamAppTestTargetAPI.findByTestIdAndResourceId(testId, deskId);
        if (Objects.nonNull(appTestTargetDTO)) {
            LOGGER.error("测试组[{}]已有该云桌面[{}]，请勿重复添加", cbbUamAppTestDTO.getName(), cloudDesktopDetailDTO.getDesktopName());
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_ALREADY_EXIST, cbbUamAppTestDTO.getName(),
                    cloudDesktopDetailDTO.getDesktopName());
        }
    }

    /**
     * 测试桌面信息
     *
     * @param deskIdArr 桌面id
     * @throws BusinessException 业务异常
     */
    public void checkTestingDesk(UUID[] deskIdArr) throws BusinessException {
        Assert.notEmpty(deskIdArr, "deskIdArr must not be null");
        List<UUID> deskIdList = Arrays.asList(deskIdArr);
        for (UUID deskId : deskIdList) {
            cloudDesktopWebService.checkThirdPartyDesktop(deskId, BusinessKey.RCDC_RCO_APPCENTER_TEST_THIRD_PARTY_DESKTOP);
        }
        final List<List<UUID>> partitionList = Lists.partition(deskIdList, 1000);
        for (List<UUID> idList : partitionList) {
            if (uamAppTestAPI.existTestingDesk(idList)) {
                LOGGER.error("存在测试中的云桌面，不允许操作", JSON.toJSONString(deskIdArr));
                throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_EXIST_TESTING_DESKTOP);
            }
        }
    }

    /**
     * 测试桌面信息
     * 
     * @param deskId 桌面id
     * @throws BusinessException 业务异常
     */
    public void checkTestingDesk(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId must not be null");

        uamAppTestAPI.checkTestingDesk(deskId);
    }


    /**
     * 桌面是否是window系统
     *
     * @param deskIdArr 桌面id
     * @return true/false
     * @throws BusinessException 业务异常
     */
    public boolean isWindowsOs(UUID[] deskIdArr) throws BusinessException {
        Assert.notEmpty(deskIdArr, "deskIdArr must not be null");

        List<List<UUID>> deskIdSubList = ListRequestHelper.subArray(deskIdArr, SUB_DESK_ID_MAX);
        for (List deskIdList : deskIdSubList) {
            List<DeskImageRelatedDTO> deskImageRelatedDTOList = userDesktopMgmtAPI.findByDeskIdIn(deskIdList);
            for (DeskImageRelatedDTO deskImageRelatedDTO : deskImageRelatedDTOList) {
                if (!CbbOsType.isWindowsOs(deskImageRelatedDTO.getDesktopImageType())) {
                    LOGGER.error("桌面[{}]当前操作系统为[{}]", deskImageRelatedDTO.getDeskId(), deskImageRelatedDTO.getDesktopImageType());
                    return false;
                }
            }

        }
        return true;
    }

    /**
     * 桌面操作系统是否与交付组匹配
     *
     * @param deskIdArr 桌面id
     * @param osPlatform 操作系统类型【WINDOWS,LINUX】
     * @throws BusinessException 业务异常
     */
    public void checkDeliveryGroupObject(UUID[] deskIdArr, OsPlatform osPlatform) throws BusinessException {
        Assert.notEmpty(deskIdArr, "deskIdArr must not be null");
        Assert.notNull(osPlatform, "osPlatform must not be null");

        List<List<UUID>> deskIdSubList = ListRequestHelper.subArray(deskIdArr, SUB_DESK_ID_MAX);
        for (List deskIdList : deskIdSubList) {
            List<DeskImageRelatedDTO> deskImageRelatedDTOList = userDesktopMgmtAPI.findByDeskIdIn(deskIdList);
            for (DeskImageRelatedDTO deskImageRelatedDTO : deskImageRelatedDTOList) {
                if (OsPlatform.WINDOWS == osPlatform && !CbbOsType.isWindowsOs(deskImageRelatedDTO.getDesktopImageType())) {
                    LOGGER.error("交付桌面与交付组操作系统不匹配");
                    throw new BusinessException(
                            UamDeliveryGroupBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_AND_DESKTOP_OS_PLATFORM_DIFFERENT);
                }
                if (OsPlatform.LINUX == osPlatform && !CbbOsType.isLinuxOs(deskImageRelatedDTO.getDesktopImageType())) {
                    LOGGER.error("交付桌面与交付组操作系统不匹配");
                    throw new BusinessException(
                            UamDeliveryGroupBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_AND_DESKTOP_OS_PLATFORM_DIFFERENT);
                }
                if (!DesktopType.PERSONAL.name().equals(deskImageRelatedDTO.getDesktopType())) {
                    LOGGER.error("仅支持下发个性桌面");
                    throw new BusinessException(UamDeliveryGroupBusinessKey.RCDC_UAM_PUSH_INSTALL_PACKAGE_DELIVERY_GROUP_ONLY_SUPPORT_PERSONAL);
                }
            }

        }
    }

    /**
     * 桌面变更镜像/策略时校验
     *
     * @param desktopId 桌面id
     * @throws BusinessException 业务异常
     */
    public void validateDesktopModifyStrategy(UUID desktopId) throws BusinessException {
        Assert.notNull(desktopId, "desktopId must not be null");

        final List<CbbAppTestTargetDTO> testTargetDTOList =
                cbbUamAppTestTargetAPI.findByResourceIdAndInState(desktopId, DesktopTestStateEnum.getProcessingStateList());
        if (CollectionUtils.isNotEmpty(testTargetDTOList)) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_DESKTOP_NOT_OPERATE);
        }
    }

    /**
     * 校验测试任务是否完成
     *
     * @param testId 测试id
     * @throws BusinessException 业务异常
     */
    public void validateTestTaskFinished(UUID testId) throws BusinessException {
        Assert.notNull(testId, "testId must not be null");
        final CbbUamAppTestDTO cbbUamAppTestDTO = cbbUamAppTestAPI.getUamAppTestInfo(testId);
        if (cbbUamAppTestDTO.getState() == TestTaskStateEnum.FINISHED) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_TASK_FINISHED, cbbUamAppTestDTO.getName());
        }
    }

    /**
     * 校验测试任务是否处在测试中
     *
     * @param testId 测试id
     * @throws BusinessException 业务异常
     */
    public void validateTestTaskPausingAndFinished(UUID testId) throws BusinessException {
        Assert.notNull(testId, "testId must not be null");
        final CbbUamAppTestDTO cbbUamAppTestDTO = cbbUamAppTestAPI.getUamAppTestInfo(testId);

        if (cbbUamAppTestDTO.getState() == TestTaskStateEnum.FINISHED) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_TASK_FINISHED, cbbUamAppTestDTO.getName());
        }

        if (cbbUamAppTestDTO.getState() == TestTaskStateEnum.PAUSING) {
            throw new BusinessException(BusinessKey.RCDC_RCO_APPCENTER_TEST_TASK_PAUSING, cbbUamAppTestDTO.getName());
        }

    }
}
