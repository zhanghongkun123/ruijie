package com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.impl;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbImageTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateGuestToolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageTemplateState;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.SystemVersionMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CommonUpgradeAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CustomerInfoAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.LicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.commonupgrade.dto.GuideImageTemplateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.customerinfo.dto.CustomerInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.license.response.ObtainIdvLicenseAuthStateResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.HaloDetectRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalConfigAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalConfigVersionDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.TerminalConfigFileDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.enums.VersionCompareResultEnum;
import com.ruijie.rcos.rcdc.terminal.module.def.api.request.CbbTerminalConfigImportRequest;
import com.ruijie.rcos.rcdc.terminal.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.terminal.module.def.enums.TerminalConfigSourceType;
import com.ruijie.rcos.rcdc.terminal.module.def.util.FileOperateUtil;
import com.ruijie.rcos.rcdc.terminal.module.def.util.VersionUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryBuilderFactory;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.File;
import java.util.*;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/23 20:03
 *
 * @author ketb
 */
@Service
public class HaloDetectRestServerImpl implements HaloDetectRestServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HaloDetectRestServerImpl.class);

    private static final int PARAM_PAGE = 0;

    private static final int PARAM_LIMIE = 1000;

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private PageQueryBuilderFactory pageQueryBuilderFactory;

    @Autowired
    private LicenseAPI licenseAPI;

    @Autowired
    private SystemVersionMgmtAPI systemVersionMgmtAPI;

    @Autowired
    private CbbTerminalConfigAPI terminalConfigAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CloudPlatformManageAPI cloudPlatformManageAPI;

    @Autowired
    private CustomerInfoAPI customerInfoAPI;

    @Autowired
    private CommonUpgradeAPI commonUpgradeAPI;

    /**
     * 检测镜像gt版本是否低于服务器gt版本
     *
     * @return 检测结果
     */
    @Override
    public CommonWebResponse<String[]> checkGtVersion() {
        try {
            PageQueryRequest pageQueryRequest = pageQueryBuilderFactory.newRequestBuilder()
                    .setPageLimit(PARAM_PAGE, PARAM_LIMIE).build();
            PageQueryResponse<CbbImageTemplateDTO> response = cbbImageTemplateMgmtAPI.pageQuery(pageQueryRequest);
            String[] imageArr = new String[]{};
            if (response.getTotal() > 0) {
                List<String> imageNameList = new ArrayList<>();
                Arrays.stream(response.getItemArr()).forEach(cbbImageTemplateDTO -> {
                    if (needUpgradeGuestVersion(cbbImageTemplateDTO.getGuestToolState())
                            && canStartVm(cbbImageTemplateDTO.getImageTemplateState())) {
                        imageNameList.add(cbbImageTemplateDTO.getImageTemplateName());
                    }
                });
                if (imageNameList.size() > 0) {
                    imageArr = imageNameList.toArray(new String[imageNameList.size()]);
                }
            }
            LOGGER.debug("image gt version is low:{}", imageArr);

            return CommonWebResponse.success(imageArr);
        } catch (BusinessException e) {
            LOGGER.error("查询失败", e.getI18nMessage());
            return CommonWebResponse.fail(HealthBusinessKey.RCO_CHECK_IMAGE_GUESTTOOL_VERSION_FAIL, new String[] {e.getI18nMessage()});
        }
    }

    private boolean canStartVm(ImageTemplateState imageState) {
        switch (imageState) {
            case DISABLED:
            case AVAILABLE:
            case DRIVER_UN_INSTALL:
                return true;
            default:
                return false;
        }
    }

    private boolean needUpgradeGuestVersion(ImageTemplateGuestToolState guestToolState) {
        if (guestToolState == null) {
            return true;
        }

        if (ImageTemplateGuestToolState.GUEST_VERSION_LOW == guestToolState) {
            return true;
        }

        return false;
    }

    @Override
    public CommonWebResponse<String[]> checkRCDCVersion() {
        String versionInfo = "";
        try {
            versionInfo = systemVersionMgmtAPI.obtainSystemReleaseVersion(new DefaultPageRequest()).getDto();
        } catch (Exception e) {
            LOGGER.error("HALO体检获取RCDC版本号失败", e);
        }
        return CommonWebResponse.success(new String[]{versionInfo});
    }

    @Override
    public CommonWebResponse<ObtainIdvLicenseAuthStateResponse> checkIdvAuth() {
        try {
            ObtainIdvLicenseAuthStateResponse idvLicenseAuthStateResponse = licenseAPI.obtainIdvLicenseAuthState();
            return CommonWebResponse.success(idvLicenseAuthStateResponse);
        } catch (BusinessException e) {
            LOGGER.error("查询idv授权信息失败：", e);
            return CommonWebResponse.fail(HealthBusinessKey.RCO_CHECK_IDV_LICENSE_AUTH_STATE_FAIL, new String[]{e.getI18nMessage()});
        }
    }

    @Override
    public CommonWebResponse<String[]> importTerminalConfig() {
        try {
            CbbTerminalConfigImportRequest dto = generateTerminalConfigImportRequest();
            terminalConfigAPI.importTerminalConfig(dto);
            auditLogAPI.recordLog(HealthBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS);
            return CommonWebResponse.success(HealthBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_SUCCESS, new String[0]);
        } catch (Exception ex) {
            LOGGER.error("halo导入终端配置文件失败", ex);
            String message = ex.getMessage();
            if (ex instanceof BusinessException) {
                message = ((BusinessException) ex).getI18nMessage();
            }
            auditLogAPI.recordLog(HealthBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL, message);
            return CommonWebResponse.fail(HealthBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_RESULT_FAIL, new String[]{message});
        }
    }

    @Override
    public CommonWebResponse<String> getTerminalConfigVersion() {
        CbbTerminalConfigImportRequest dto;
        try {
            dto = generateTerminalConfigImportRequest();
            TerminalConfigFileDTO terminalConfigFileDTO = terminalConfigAPI.decompressionPackageFile(dto);
            CbbTerminalConfigVersionDTO currentVersion = terminalConfigAPI.getVersionInfo();
            // 校验版本
            VersionCompareResultEnum result = VersionUtil.compare(terminalConfigFileDTO.getVersion(),
                    currentVersion.getVersion());
            return CommonWebResponse.success(result.name());
        } catch (BusinessException e) {
            return CommonWebResponse.fail(HealthBusinessKey.RCDC_TERMINAL_CONFIG_IMPORT_CHECK_VERSION_FAIL);
        }
    }

    @Override
    public PageQueryResponse<CloudPlatformDTO> cloudPlatformList(PageQueryRequest request) throws BusinessException {
        Assert.notNull(request, "request can not be null");
        return cloudPlatformManageAPI.pageQuery(request);
    }

    @Override
    public CustomerInfoDTO getCustomInfo() {
        return customerInfoAPI.getCurrentCustomerInfo();
    }

    @Override
    public CommonWebResponse<List<GuideImageTemplateDTO>> commonUpgradeGuide() throws BusinessException {
        List<GuideImageTemplateDTO> guideImageTemplateDTOList = commonUpgradeAPI.getLowImageTemplateVersion();
        return CommonWebResponse.success(guideImageTemplateDTOList);
    }

    private CbbTerminalConfigImportRequest generateTerminalConfigImportRequest() throws BusinessException {
        List<File> fileList = FileOperateUtil.listFile(Constants.TERMINAL_CONFIG_HALO_PATH);
        File pkg = fileList.stream()
                .filter(file -> {
                    return file.isFile() && file.getName().contains(Constants.PACKAGE_PRIFIX);
                }).max(Comparator.comparing(File::getName)).orElse(null);

        if (Objects.isNull(pkg)) {
            throw new BusinessException(HealthBusinessKey.RCDC_TERMINAL_CONFIG_PACKAGE_NOT_EXIST);
        }
        // 导入配置文件
        CbbTerminalConfigImportRequest dto = new CbbTerminalConfigImportRequest();
        dto.setFilePath(pkg.getAbsolutePath());
        dto.setSourceType(TerminalConfigSourceType.HALO.name());
        return dto;
    }
}
