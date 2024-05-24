package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.enums.BaseFeatureStatus;
import com.ruijie.rcos.base.sysmanage.module.def.enums.BaseFeatureType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.RccpComputeMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.response.compute.ComputeHostListResponse;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.CommonPageQueryRequest;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.compute.ComputeHostInfoDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.HostAuthState;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbAuthInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseRequestDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.license.module.def.rest.response.LicenseInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.rest.response.LicenseListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.LicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.LicenseTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ObtainRcdcLicenseNumResponse;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.dto.ObtainEduLicenseInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.dashboardstatistics.response.ObtainEduLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.def.license.enums.LicenseAuthStateEnum;
import com.ruijie.rcos.rcdc.rco.module.def.license.response.ObtainIdvLicenseAuthStateResponse;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.response.ObtainCpuLicenseInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.LicenseGlobal;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.GeneralAuthDetailInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.LicenseFeatureIdEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.service.LicenseService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.pagekit.kernel.request.PageQueryConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/8/26
 *
 * @author nt
 */
public class LicenseAPIImpl implements LicenseAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseAPIImpl.class);

    @Autowired
    private CbbDeskLicenseMgmtAPI cbbDeskLicenseMgmtAPI;

    @Autowired
    private RccpComputeMgmtAPI computeMgmtAPI;


    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    private static final String DATE_FORMAT_SECOND = "yyyy-MM-dd HH:mm:ss";

    private String expiringDate = "";

    @Override
    public ObtainRcdcLicenseNumResponse acquireLicenseNum(LicenseTypeEnum licenseType) {
        Assert.notNull(licenseType, "type can not be null");
        LOGGER.debug("rco获取授权信息的参数为：{}", JSON.toJSONString(licenseType));
        ObtainRcdcLicenseNumResponse response = new ObtainRcdcLicenseNumResponse();

        switch (licenseType) {
            case DESKTOP:
                List<GeneralAuthDetailInfoDTO> generalAuthDetailInfoDTOList =
                    licenseService.getLicenseUsageDetail(CbbLicenseCategoryEnum.VDI.getSubCategory(1));
                if (!CollectionUtils.isEmpty(generalAuthDetailInfoDTOList)) {
                    response.setLicenseNum(generalAuthDetailInfoDTOList.get(0).getTotal());
                    response.setUsedNum(generalAuthDetailInfoDTOList.get(0).getUsed());
                    response.setIdvTerminalUsedLicenseNum(generalAuthDetailInfoDTOList.get(0).getUsedByIdv());
                    response.setTciTerminalUsedLicenseNum(generalAuthDetailInfoDTOList.get(0).getUsedByVoi());
                    response.setVdiCloudDesktopUsedLicenseNum(generalAuthDetailInfoDTOList.get(0).getUsedByVdi());
                    response.setRcaUsedLicenseNum(generalAuthDetailInfoDTOList.get(0).getUsedByRca());
                }

                break;
            case WINDOWS:
                response.setLicenseNum(LicenseGlobal.getWindowsLicenseNum());
                response.setUsedNum(LicenseGlobal.getWindowsActiveNum());
                break;
            default:
                throw new IllegalArgumentException("授权类型暂不支持查询" + licenseType);
        }
        return response;
    }

    @Override
    public Boolean obtainEnableOpenKms() {

        return LicenseGlobal.isIsOpenKms();
    }

    @Override
    public ObtainCpuLicenseInfoResponse obtainLicenseTrialRemainder() throws BusinessException {
        ComputeHostListResponse computeHostListResponse = 
                computeMgmtAPI.getHostList(buildRequest(cloudPlatformManageAPI.getDefaultCloudPlatform().getId()));
        LOGGER.info("获取主机列表，数据为：{}", JSON.toJSONString(computeHostListResponse));
        List<HostAuthState> hostAuthStateList =
            Stream.of(computeHostListResponse.getComputeHostInfoDTOArr()).map(ComputeHostInfoDTO::getAuthState).collect(Collectors.toList());
        CbbLicenseRequestDTO licenseRequestDTO = getCbbLicenseRequestDTO(Arrays.asList(LicenseFeatureIdEnums.RG_CML_PLATFORM_CPU.getFeatureId(),
            LicenseFeatureIdEnums.RG_CCP_DCP_LIC.getFeatureId(), LicenseFeatureIdEnums.RG_CCP_DCP_LIC_EDU.getFeatureId()), null, null);
        LicenseListResponse licenseListResponse = cbbLicenseCenterAPI.loadLicense(licenseRequestDTO);
        LOGGER.info("获取服务器CPU证书到期时间，数据为：{}", JSON.toJSONString(licenseListResponse));
        List<Long> licenseInfoDTOList =
            Stream.of(licenseListResponse.getLicenseFeatureResArr()).map(LicenseInfoDTO::getTrialRemainder).collect(Collectors.toList());
        if (expiringDate.isEmpty() && !licenseInfoDTOList.isEmpty()) {
            Long trialRemainder = Collections.max(licenseInfoDTOList);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT_SECOND);
            expiringDate = LocalDateTime.now().plus(trialRemainder, ChronoUnit.SECONDS).format(formatter);
        }
        ObtainCpuLicenseInfoResponse obtainCpuLicenseInfoResponse = new ObtainCpuLicenseInfoResponse();
        obtainCpuLicenseInfoResponse.setTrialRemainder(expiringDate);
        obtainCpuLicenseInfoResponse.setHasAllExpire(hostAuthStateList.stream().allMatch(hostAuthState -> HostAuthState.NOT_AUTH == hostAuthState));
        obtainCpuLicenseInfoResponse.setHasPartExpire(hostAuthStateList.stream().anyMatch(hostAuthState -> HostAuthState.NOT_AUTH == hostAuthState));
        return obtainCpuLicenseInfoResponse;
    }

    private CommonPageQueryRequest buildRequest(UUID platformId) {
        CommonPageQueryRequest pageQueryRequest = new CommonPageQueryRequest();
        pageQueryRequest.setPage(PageQueryConstant.DEFAULT_PAGE);
        pageQueryRequest.setLimit(PageQueryConstant.MAX_LIMIT);
        pageQueryRequest.setPlatformId(platformId);
        return pageQueryRequest;
    }

    @Override
    public ObtainEduLicenseInfoResponse obtainEduLicense() throws BusinessException {
        // 查询教育版服务器CPU证书
        CbbLicenseRequestDTO licenseRequestDTO =
            getCbbLicenseRequestDTO(Collections.singletonList(LicenseFeatureIdEnums.RG_CCP_DCP_LIC_EDU.getFeatureId()), BaseFeatureType.PERPETUAL,
                BaseFeatureStatus.AVALIABLE);
        LicenseListResponse licenseListResponse = cbbLicenseCenterAPI.loadLicense(licenseRequestDTO);
        LOGGER.info("获取教育版服务器CPU证书，数据为：{}", JSON.toJSONString(licenseListResponse));
        // 构造返回体
        ObtainEduLicenseInfoResponse obtainEduLicenseInfoResponse = new ObtainEduLicenseInfoResponse();

        // 设置教育版服务器CPU证书数量
        ObtainEduLicenseInfoDTO cpuEduLicenseInfoDTO = new ObtainEduLicenseInfoDTO();
        cpuEduLicenseInfoDTO.setTotal(licenseListResponse.getTotal());
        obtainEduLicenseInfoResponse.setCpuLicenseInfoDTO(cpuEduLicenseInfoDTO);
        // 查询教育版TCI证书
        List<CbbAuthInfoDTO> authInfoDTOList = licenseService.getLicenseUsageSnapshot(CbbLicenseCategoryEnum.VOI.getSubCategory(1));
        LOGGER.info("获取教育版TCI证书，数据为：{}", JSON.toJSONString(authInfoDTOList));
        // 设置TCI 教育版数量
        ObtainEduLicenseInfoDTO voiEduLicenseInfoDTO = new ObtainEduLicenseInfoDTO();
        int totalNum = 0;
        for (CbbAuthInfoDTO authInfoDTO : authInfoDTOList) {
            if (authInfoDTO.getTotal() > 0) {
                totalNum += authInfoDTO.getTotal();
            }
        }
        voiEduLicenseInfoDTO.setTotal(totalNum);
        obtainEduLicenseInfoResponse.setVoiLicenseInfoDTO(voiEduLicenseInfoDTO);


        LOGGER.info("教育版证书相应数据为：{}", JSON.toJSONString(obtainEduLicenseInfoResponse));
        return obtainEduLicenseInfoResponse;
    }

    @Override
    public ObtainIdvLicenseAuthStateResponse obtainIdvLicenseAuthState() throws BusinessException {
        ObtainIdvLicenseAuthStateResponse authStateResponse = new ObtainIdvLicenseAuthStateResponse();
        CbbLicenseRequestDTO licenseRequestDTO =
            getCbbLicenseRequestDTO(Collections.singletonList(LicenseFeatureIdEnums.IDV_TERMINAL.getFeatureId()), BaseFeatureType.PERPETUAL,
                BaseFeatureStatus.AVALIABLE);
        LicenseListResponse licenseListRes = cbbLicenseCenterAPI.loadLicense(licenseRequestDTO);
        if (licenseListRes.getTotal() != 0) {
            authStateResponse.setAuthState(LicenseAuthStateEnum.PERPETUAL);
            return authStateResponse;
        }

        licenseRequestDTO =
            getCbbLicenseRequestDTO(Collections.singletonList(LicenseFeatureIdEnums.IDV_TERMINAL.getFeatureId()), BaseFeatureType.TEMPORARY,
                BaseFeatureStatus.AVALIABLE);
        licenseListRes = cbbLicenseCenterAPI.loadLicense(licenseRequestDTO);
        if (licenseListRes.getTotal() != 0) {
            authStateResponse.setAuthState(LicenseAuthStateEnum.TEMPORARY);
        } else {
            authStateResponse.setAuthState(LicenseAuthStateEnum.UNAUTHORIZED);
        }
        return authStateResponse;
    }

    private static CbbLicenseRequestDTO getCbbLicenseRequestDTO(List<String> featureIdList, BaseFeatureType featureType,
                                                                BaseFeatureStatus featureStatus) {
        CbbLicenseRequestDTO licenseRequestDTO = new CbbLicenseRequestDTO();
        licenseRequestDTO.setEnsureRccpReady(false);
        licenseRequestDTO.setFeatureIdArr(featureIdList.toArray(new String[0]));
        if (featureType != null) {
            licenseRequestDTO.setFeatureType(featureType.toString());
        }
        if (featureStatus != null) {
            licenseRequestDTO.setFeatureStatus(featureStatus.toString());
        }
        return licenseRequestDTO;
    }
}
