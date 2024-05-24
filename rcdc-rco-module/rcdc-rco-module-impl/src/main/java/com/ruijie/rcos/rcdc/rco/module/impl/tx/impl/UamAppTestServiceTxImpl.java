package com.ruijie.rcos.rcdc.rco.module.impl.tx.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ruijie.rcos.rcdc.appcenter.module.def.AppCenterBusinessKey;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.*;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.*;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppResourceTypeEnum;
import com.ruijie.rcos.rcdc.appcenter.module.def.enums.AppTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.appcenter.dto.UamAppTestDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.tx.UamAppTestServiceTx;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年12月29日
 *
 * @author zhk
 */
@Service
public class UamAppTestServiceTxImpl implements UamAppTestServiceTx {

    @Autowired
    CbbUamAppTestAPI cbbUamAppTestAPI;

    @Autowired
    CbbUamAppTestApplicationAPI cbbUamAppTestApplicationAPI;

    @Autowired
    CbbUamAppTestTargetAPI cbbUamAppTestTargetAPI;

    @Autowired
    CbbUamAppTestDetailAPI cbbUamAppTestDetailAPI;

    @Autowired
    CbbAppStoreMgmtAPI cbbAppStoreMgmtAPI;

    @Override
    public UUID createAppTest(UamAppTestDTO dto) throws BusinessException {
        Assert.notNull(dto, "UpmAppTestDTO can not be null");

        CbbUamAppTestDTO cbbUamAppTestDTO = new CbbUamAppTestDTO();
        BeanUtils.copyProperties(dto, cbbUamAppTestDTO);
        final UUID testId = cbbUamAppTestAPI.createUamAppTest(cbbUamAppTestDTO);
        // 保存应用测试应用关联

        for (UUID appId : dto.getAppIdArr()) {
            CbbUamAppTestApplicationDTO appTestApplicationDTO = new CbbUamAppTestApplicationDTO();
            appTestApplicationDTO
                    .setAppType(appId.equals(dto.getAppSoftwarePackageId()) ? AppTypeEnum.APP_SOFTWARE_PACKAGE : AppTypeEnum.PUSH_INSTALL_PACKAGE);
            appTestApplicationDTO.setAppId(appId);
            appTestApplicationDTO.setTestId(testId);
            cbbUamAppTestApplicationAPI.createUamAppTestApplication(appTestApplicationDTO);
        }

        for (UUID deskId : dto.getDeskIdArr()) {
            CbbAppTestTargetDTO testTargetDTO = new CbbAppTestTargetDTO();
            testTargetDTO.setResourceId(deskId);
            testTargetDTO.setTestId(testId);
            testTargetDTO.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
            cbbUamAppTestTargetAPI.createUamAppTestTarget(testTargetDTO);
            for (UUID appId : dto.getAppIdArr()) {
                CbbUamAppTestDetailDTO cbbCreateUamAppDetailDTO = new CbbUamAppTestDetailDTO();
                cbbCreateUamAppDetailDTO.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
                cbbCreateUamAppDetailDTO.setAppId(appId);
                cbbCreateUamAppDetailDTO.setTestId(testId);
                cbbCreateUamAppDetailDTO.setResourceId(deskId);
                cbbCreateUamAppDetailDTO.setAppType(AppTypeEnum.APP_SOFTWARE_PACKAGE);
                cbbUamAppTestDetailAPI.createUamAppTestDetail(cbbCreateUamAppDetailDTO);
            }
        }
        return testId;
    }

    @Override
    public void addAppTestDesk(UUID testId, List<UUID> deskIdList) throws BusinessException {
        Assert.notNull(testId, "testId can not be null");
        Assert.notEmpty(deskIdList, "deskIdList can not be null");

        List<CbbUamAppTestApplicationDTO> appTestApplicationDTOList = cbbUamAppTestApplicationAPI.findByTestId(testId);
        if (CollectionUtils.isEmpty(appTestApplicationDTOList)) {
            throw new BusinessException(AppCenterBusinessKey.RCDC_APPCENTER_NOT_FIND_APPLICATION, testId.toString());
        }
        List<UUID> appIdList = appTestApplicationDTOList.stream().map(CbbUamAppTestApplicationDTO::getAppId).collect(Collectors.toList());
        List<CbbUamAppDTO> cbbUamAppDTOList = cbbAppStoreMgmtAPI.listUamApp(appIdList);

        Map<AppTypeEnum, List<CbbUamAppDTO>> cbbUamAppMap =
                cbbUamAppDTOList.stream().collect(Collectors.groupingBy(CbbUamAppDTO::getAppType, Collectors.toList()));

        List<CbbUamAppDTO> appSoftWarePackageList = cbbUamAppMap.get(AppTypeEnum.APP_SOFTWARE_PACKAGE);
        String appSoftWarePackageId = null;
        if (!CollectionUtils.isEmpty(appSoftWarePackageList)) {
            appSoftWarePackageId = appSoftWarePackageList.get(0).getId().toString();
        }
        for (UUID deskId : deskIdList) {
            CbbAppTestTargetDTO testTargetDTO = buildCbbAppTestTargetDTO(deskId, testId);
            cbbUamAppTestTargetAPI.createUamAppTestTarget(testTargetDTO);
            for (CbbUamAppTestApplicationDTO appTestApplication : appTestApplicationDTOList) {
                CbbUamAppTestDetailDTO cbbCreateUamAppDetailDTO = new CbbUamAppTestDetailDTO();
                cbbCreateUamAppDetailDTO.setTestId(testId);
                cbbCreateUamAppDetailDTO.setResourceId(deskId);
                cbbCreateUamAppDetailDTO.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
                cbbCreateUamAppDetailDTO.setAppId(appTestApplication.getAppId());
                cbbCreateUamAppDetailDTO
                        .setAppType(appTestApplication.getAppId().toString().equals(appSoftWarePackageId) ? AppTypeEnum.APP_SOFTWARE_PACKAGE
                                : AppTypeEnum.PUSH_INSTALL_PACKAGE);
                cbbUamAppTestDetailAPI.createUamAppTestDetail(cbbCreateUamAppDetailDTO);
            }
        }
    }

    private CbbAppTestTargetDTO buildCbbAppTestTargetDTO(UUID resourceId, UUID testId) {
        CbbAppTestTargetDTO testTargetDTO = new CbbAppTestTargetDTO();
        testTargetDTO.setResourceId(resourceId);
        testTargetDTO.setTestId(testId);
        testTargetDTO.setResourceType(AppResourceTypeEnum.CLOUD_DESKTOP);
        return testTargetDTO;
    }

}
