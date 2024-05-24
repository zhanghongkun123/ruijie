package com.ruijie.rcos.rcdc.rco.module.impl.service.impl;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbWindowsLicenseAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbEnableWindowsLicenseDTO;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbAuthInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.dto.CbbLicenseRequestDTO;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseCategoryEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseTargetEnum;
import com.ruijie.rcos.rcdc.license.module.def.rest.response.LicenseInfoDTO;
import com.ruijie.rcos.rcdc.license.module.def.rest.response.LicenseListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.impl.cache.LicenseGlobal;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.GeneralAuthDetailInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.enums.LicenseFeatureIdEnums;
import com.ruijie.rcos.rcdc.rco.module.impl.service.LicenseService;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalLicenseMgmtAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalAuthUsageDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.bootstrap.SafetySingletonInitializer;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * license
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月18日
 *
 * @author lin
 */
@Service
public class LicenseServiceImpl implements LicenseService, SafetySingletonInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseServiceImpl.class);

    @Autowired
    private CbbWindowsLicenseAPI cbbWindowsLicenseAPI;


    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private CbbTerminalLicenseMgmtAPI cbbTerminalLicenseMgmtAPI;

    private static final int COUNT_ZERO = 0;

    private static final String WIN_10_64 = "WIN_10_64";

    private static final String WIN_10_32 = "WIN_10_32";

    @Override
    public void safeInit() {
        dealWindowsLicenseArr();
    }

    @Override
    public LicenseActiveResultDTO matchWindowsLicense(LicenseActiveDTO licenseActiveDTO) {
        Assert.notNull(licenseActiveDTO, "licenseActiveDTO can not be null");
        LicenseActiveResultDTO dto = new LicenseActiveResultDTO();
        // 当镜像模板启动编辑异常或者未编辑时，模板中osProType字段值为空
        if (licenseActiveDTO.getOsProType() == null) {
            return dto;
        }

        String osProType = licenseActiveDTO.getOsProType();
        if (LicenseGlobal.getWindowsLicenseOsProTypeSet().contains(osProType)) {
            dto.setLicenseOsProType(osProType);
        } else {
            dealWin10License(licenseActiveDTO, dto);
        }
        return dto;
    }


    @Override
    public void activeSuccess() {
        LicenseGlobal.activeWindowsActiveNum();
    }

    private LicenseActiveResultDTO dealWin10License(LicenseActiveDTO licenseActiveDTO, LicenseActiveResultDTO dto) {
        if (!isWin10(licenseActiveDTO.getOsType())) {
            return dto;
        }
        dto.setLicenseOsProType(LicenseGlobal.getWindowsLicenseOsProTypeSet().iterator().next());
        return dto;
    }

    private boolean isWin10(String osType) {
        return StringUtils.equals(osType, WIN_10_32) || StringUtils.equals(osType, WIN_10_64);
    }

    private void dealWindowsLicenseArr() {
        CbbLicenseRequestDTO licenseRequestDTO = new CbbLicenseRequestDTO();
        licenseRequestDTO.setEnsureRccpReady(false);
        licenseRequestDTO.setFeatureIdArr(getWindowLicenseFeatureList().toArray(new String[0]));
        LicenseListResponse windowsLicenseResponse = cbbLicenseCenterAPI.loadLicense(licenseRequestDTO);
        if (windowsLicenseResponse != null && windowsLicenseResponse.getTotal() > 0) {
            LicenseInfoDTO[] windowsLicenseArr = windowsLicenseResponse.getLicenseFeatureResArr();
            int total = 0;
            for (LicenseInfoDTO licenseInfoDTO : windowsLicenseArr) {
                if (-1 == licenseInfoDTO.getLicenseNum()) {
                    total = licenseInfoDTO.getLicenseNum();
                } else {
                    if (total >= 0) {
                        total += licenseInfoDTO.getLicenseNum();
                    }
                }
                dealOsProType(licenseInfoDTO.getFeatureId());
            }
            try {
                handlerWindowsLicenseNum(total);
            } catch (BusinessException e) {
                LOGGER.error("处理window证书异常", e);
            }
        }
    }

    private List<String> getWindowLicenseFeatureList() {
        return Arrays.asList(LicenseFeatureIdEnums.CMWIN_ENT.getFeatureId(), LicenseFeatureIdEnums.CMWINPRO_EDU.getFeatureId(),
            LicenseFeatureIdEnums.CMWINPRO_NOEDU.getFeatureId());
    }

    private void handlerWindowsLicenseNum(int licenseNum) throws BusinessException {
        CbbEnableWindowsLicenseDTO request = new CbbEnableWindowsLicenseDTO();
        request.setLicenseNum(licenseNum);
        request.setEnableOpenKms(licenseNum > COUNT_ZERO);
        // 设置rco的值
        LicenseGlobal.setWindowsLicenseNum(licenseNum);
        LicenseGlobal.setIsOpenKms(licenseNum > COUNT_ZERO);
        cbbWindowsLicenseAPI.enableLicense(request);
    }


    private void dealOsProType(String key) {
        // 添加windows授权版本
        LicenseFeatureIdEnums licenseEnum = LicenseFeatureIdEnums.getByFeatureId(key);
        if (licenseEnum != null) {
            LOGGER.info("计算windows授权获取的枚举为：{}", JSON.toJSONString(licenseEnum));
            LicenseGlobal.getWindowsLicenseOsProTypeSet().add(licenseEnum.getOsProType());
        }
    }

    @Override
    public List<CbbAuthInfoDTO> getLicenseUsageSnapshot() {
        List<CbbAuthInfoDTO> authInfoDTOList = new ArrayList<>();
        //桌面
        cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, CbbLicenseCategoryEnum.VDI.name());
        //终端
        cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, CbbLicenseCategoryEnum.VOI.name());
        cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, CbbLicenseCategoryEnum.IDV.name());
        cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, CbbLicenseCategoryEnum.VOI_PLUS_UPGRADED.name());

        return authInfoDTOList;
    }

    @Override
    public List<CbbAuthInfoDTO> getLicenseUsageSnapshot(String licenseType) {
        Assert.hasText(licenseType, "licenseCategory can not be empty");
        List<CbbAuthInfoDTO> authInfoDTOList = new ArrayList<>();
        cbbLicenseCenterAPI.appendAuthInfo(authInfoDTOList, licenseType);
        return authInfoDTOList;
    }

    @Override
    public List<GeneralAuthDetailInfoDTO> getLicenseUsageDetail(String licenseType) {
        Assert.hasText(licenseType, "licenseCategory can not be empty");
        List<GeneralAuthDetailInfoDTO> generalAuthDetailInfoDTOList = Lists.newArrayList();
        List<CbbAuthInfoDTO> authInfoDTOList = getLicenseUsageSnapshot(licenseType);
        if (CollectionUtils.isEmpty(authInfoDTOList)) {
            return generalAuthDetailInfoDTOList;
        }
        List<CbbTerminalAuthUsageDTO> authUsageDTOList = cbbTerminalLicenseMgmtAPI.countUsageGroupByLicenseTypeAndLicenseDurationAndPlatform();
        for (CbbAuthInfoDTO authInfoDTO : authInfoDTOList) {
            GeneralAuthDetailInfoDTO generalAuthDetailInfoDTO = new GeneralAuthDetailInfoDTO();
            generalAuthDetailInfoDTO.setAuthType(authInfoDTO.getAuthType());
            generalAuthDetailInfoDTO.setLicenseDurationType(authInfoDTO.getLicenseDurationType());
            generalAuthDetailInfoDTO.setTotal(authInfoDTO.getTotal());
            generalAuthDetailInfoDTO.setUsed(authInfoDTO.getUsed());
            generalAuthDetailInfoDTO.setUsedByVdi(0);
            generalAuthDetailInfoDTO.setUsedByIdv(0);
            generalAuthDetailInfoDTO.setUsedByVoi(0);
            generalAuthDetailInfoDTO.setUsedByRca(0);
            authUsageDTOList.stream().filter(authUsageDTO -> authUsageDTO.getDuration().equals(authInfoDTO.getLicenseDurationType()) &&
                    authUsageDTO.getLicenseType().equals(authInfoDTO.getAuthType()))
                .forEach(terminalAuthUsageDTO -> {
                    switch (terminalAuthUsageDTO.getPlatform()) {
                        case "IDV":
                            generalAuthDetailInfoDTO.setUsedByIdv(Math.toIntExact(terminalAuthUsageDTO.getUsedNum()));
                            break;
                        case "VOI":
                            generalAuthDetailInfoDTO.setUsedByVoi(Math.toIntExact(terminalAuthUsageDTO.getUsedNum()));
                            break;
                    }
                });
            generalAuthDetailInfoDTOList.add(generalAuthDetailInfoDTO);
            if (licenseType.startsWith(CbbLicenseCategoryEnum.VDI.name())) {
                Map<String, Integer> classifyOccupiedNumMap = authInfoDTO.getClassifyOccupiedNumMap();
                classifyOccupiedNumMap.forEach((licenseTag, count) -> {
                    if (licenseTag.startsWith(CbbLicenseTargetEnum.DESK.name())) {
                        int newCount = generalAuthDetailInfoDTO.getUsedByVdi() == null ? count : generalAuthDetailInfoDTO.getUsedByVdi() + count;
                        generalAuthDetailInfoDTO.setUsedByVdi(newCount);
                    } else if (licenseTag.startsWith(CbbLicenseTargetEnum.USER.name())) {
                        int newCount = generalAuthDetailInfoDTO.getUsedByVdi() == null ? count : generalAuthDetailInfoDTO.getUsedByVdi() + count;
                        generalAuthDetailInfoDTO.setUsedByVdi(newCount);
                    } else if (licenseTag.startsWith(CbbLicenseTargetEnum.RCA.name())) {
                        int newCount = generalAuthDetailInfoDTO.getUsedByRca() == null ? count : generalAuthDetailInfoDTO.getUsedByRca() + count;
                        generalAuthDetailInfoDTO.setUsedByRca(newCount);
                    }
                });
            }
        }
        return generalAuthDetailInfoDTOList;
    }

}
