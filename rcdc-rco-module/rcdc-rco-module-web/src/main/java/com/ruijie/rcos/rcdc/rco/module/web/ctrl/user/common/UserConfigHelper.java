package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.common;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageTemplateMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVOIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.image.CbbGetImageTemplateInfoDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVOIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbOsType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbStrategyType;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ClusterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SoftwareControlMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserProfileValidateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.VDIDesktopValidateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskspec.request.DeskSpecRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.ImageDeskSpecGpuCheckParamDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.CasScanCodeAuthBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.AssistCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.vo.PrimaryCertificationVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.IdvDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.VoiDesktopConfigVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.vo.VdiDesktopConfigVO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.webmvc.api.vo.IdLabelEntry;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Description: 用户管理-用户、用户组配置协助类
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月19日
 *
 * @author linke
 */
@Service
public class UserConfigHelper {

    @Autowired
    private CbbImageTemplateMgmtAPI cbbImageTemplateMgmtAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private DeskSpecAPI deskSpecAPI;
    
    @Autowired
    private ClusterAPI clusterAPI;

    @Autowired
    private SoftwareControlMgmtAPI softwareControlMgmtAPI;

    @Autowired
    private UserProfileValidateAPI userProfileValidaAPI;
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    /**
     * 校验IDV配置和VOI配置配置正确性
     *
     * @param idvDesktopConfig IDV配置
     * @param voiDesktopConfig VOI配置
     * @throws BusinessException 业务异常
     */
    public void validateDesktopConfig(@Nullable IdvDesktopConfigVO idvDesktopConfig, @Nullable VoiDesktopConfigVO voiDesktopConfig)
            throws BusinessException {
        if (idvDesktopConfig != null) {
            validateUserIDVConfig(idvDesktopConfig.getImage().getId(), idvDesktopConfig.getStrategy().getId());
            //检测IDV的软控策略
            checkSoftwareStrategy(idvDesktopConfig.getSoftwareStrategy());
        }
        // 保存voi云桌面配置
        if (voiDesktopConfig != null) {
            validateUserVOIConfig(voiDesktopConfig);
            //检测IDV的软控策略
            checkSoftwareStrategy(voiDesktopConfig.getSoftwareStrategy());
        }
    }

    /**
     * 校验用户配置IDV时，镜像模板和策略是否符合要求
     *
     * @param imageId    镜像模板ID
     * @param strategyId 策略ID
     * @throws BusinessException 业务异常
     */
    public void validateUserIDVConfig(UUID imageId, UUID strategyId) throws BusinessException {
        Assert.notNull(imageId, "imageId must not be null");
        Assert.notNull(strategyId, "strategyId must not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        if (imageTemplateInfoDTO.getCbbImageType() != CbbImageType.IDV) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_IMAGE_ERROR, imageTemplateInfoDTO.getImageName(),
                    imageTemplateInfoDTO.getCbbImageType().name());
        }

        CbbDeskStrategyIDVDTO deskStrategyIDVDTO = cbbIDVDeskStrategyMgmtAPI.getDeskStrategyIDV(strategyId);
        if (deskStrategyIDVDTO.getStrategyType() != CbbStrategyType.IDV) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_STRATEGY_ERROR, deskStrategyIDVDTO.getName(),
                    deskStrategyIDVDTO.getStrategyType().name());
        }

        if (!Boolean.TRUE.equals(deskStrategyIDVDTO.getEnableFullSystemDisk()) &&
                imageTemplateInfoDTO.getImageSystemSize() > deskStrategyIDVDTO.getSystemSize()) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_SYSTEM_SIZE_ERROR, deskStrategyIDVDTO.getName(),
                    imageTemplateInfoDTO.getImageName());
        }
    }

    /**
     * 校验用户配置VOI时，镜像模板和策略是否符合要求
     *
     * @param voiDesktopConfig 策略配置
     * @throws BusinessException 业务异常
     */
    public void validateUserVOIConfig(VoiDesktopConfigVO voiDesktopConfig) throws BusinessException {
        Assert.notNull(voiDesktopConfig, "voiDesktopConfig must not be null");
        Assert.notNull(voiDesktopConfig.getImage().getId(), "imageId must not be null");
        Assert.notNull(voiDesktopConfig.getStrategy().getId(), "strategyId must not be null");

        CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(voiDesktopConfig.getImage().getId());
        if (imageTemplateInfoDTO.getCbbImageType() != CbbImageType.VOI) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_IMAGE_ERROR, imageTemplateInfoDTO.getImageName(),
                    imageTemplateInfoDTO.getCbbImageType().name());
        }

        CbbDeskStrategyVOIDTO deskStrategyVOIDTO = cbbVOIDeskStrategyMgmtAPI.getDeskStrategyVOI(voiDesktopConfig.getStrategy().getId());
        if (deskStrategyVOIDTO.getStrategyType() != CbbStrategyType.VOI) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_STRATEGY_ERROR, deskStrategyVOIDTO.getName(),
                    deskStrategyVOIDTO.getStrategyType().name());
        }

        if (!Boolean.TRUE.equals(deskStrategyVOIDTO.getEnableFullSystemDisk())
                && imageTemplateInfoDTO.getImageSystemSize() > deskStrategyVOIDTO.getSystemSize()) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_SYSTEM_SIZE_ERROR, deskStrategyVOIDTO.getName(),
                    imageTemplateInfoDTO.getImageName());
        }

        if ((imageTemplateInfoDTO.getCbbOsType() == CbbOsType.KYLIN_64 || imageTemplateInfoDTO.getCbbOsType() == CbbOsType.UOS_64)
                && Objects.nonNull(voiDesktopConfig.getUserProfileStrategy())) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_OS_TYPE_PERSONAL_STRATEGY_ERROR,
                    imageTemplateInfoDTO.getImageName());
        }
    }

    /**
     * 校验用户配置VDI时，镜像模板和策略是否符合要求
     *
     * @param vdiDesktopConfigVO VDI配置
     * @throws BusinessException 业务异常
     */
    public void validateUserVDIConfig(VdiDesktopConfigVO vdiDesktopConfigVO) throws BusinessException {
        Assert.notNull(vdiDesktopConfigVO, "vdiDesktopConfigVO must not be null");
        Assert.notNull(vdiDesktopConfigVO.getStrategy(), "strategy must not be null");
        Assert.notNull(vdiDesktopConfigVO.getStrategy().getId(), "strategyId must not be null");
        Assert.notNull(vdiDesktopConfigVO.getImage(), "image must not be null");
        Assert.notNull(vdiDesktopConfigVO.getImage().getId(), "imageId must not be null");
        DeskSpecRequest deskSpecRequest = vdiDesktopConfigVO.toDeskSpec();
        Assert.notNull(deskSpecRequest, "deskSpecRequest must not be null");

        UUID imageId = vdiDesktopConfigVO.getImage().getId();
        UUID strategyId = vdiDesktopConfigVO.getStrategy().getId();
        UUID imageEditionId = null;
        if (Objects.nonNull(vdiDesktopConfigVO.getImageEdition())) {
            imageEditionId = vdiDesktopConfigVO.getImageEdition().getId();
        }

        CbbGetImageTemplateInfoDTO imageTemplateInfoDTO = cbbImageTemplateMgmtAPI.getImageTemplateInfo(imageId);
        if (imageTemplateInfoDTO.getCbbImageType() != CbbImageType.VDI) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_IMAGE_ERROR, imageTemplateInfoDTO.getImageName(),
                    imageTemplateInfoDTO.getCbbImageType().name());
        }

        CbbDeskStrategyVDIDTO deskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(strategyId);
        if (deskStrategyVDIDTO.getStrategyType() != CbbStrategyType.VDI) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_STRATEGY_ERROR, deskStrategyVDIDTO.getName(),
                    deskStrategyVDIDTO.getStrategyType().name());
        }
        if (BooleanUtils.isTrue(deskStrategyVDIDTO.getOpenDesktopRedirect())
                && Optional.ofNullable(deskSpecRequest.getPersonalDisk()).orElse(0) <= 0) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_DESK_REDIRECT_MUST_PERSON_DISK);
        }
        if (!Boolean.TRUE.equals(deskStrategyVDIDTO.getEnableFullSystemDisk())
                && imageTemplateInfoDTO.getImageSystemSize() > deskSpecRequest.getSystemDisk()) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_CREATE_USER_CONFIG_VDI_SPEC_SYSTEM_SIZE_ERROR, imageTemplateInfoDTO.getImageName());
        }

        // 检测软控策略
        checkSoftwareStrategy(vdiDesktopConfigVO.getSoftwareStrategy());

        VDIDesktopValidateDTO vdiDesktopValidateDTO = new VDIDesktopValidateDTO();
        vdiDesktopValidateDTO.setClusterId(vdiDesktopConfigVO.getCluster().getId());
        vdiDesktopValidateDTO.setNetworkId(vdiDesktopConfigVO.getNetwork().getAddress().getId());
        vdiDesktopValidateDTO.setImageId(imageId);
        vdiDesktopValidateDTO.setStrategyId(strategyId);
        vdiDesktopValidateDTO.setPlatformId(vdiDesktopConfigVO.getCloudPlatform().getId());
        vdiDesktopValidateDTO.setDeskSpec(deskSpecAPI.buildCbbDeskSpec(vdiDesktopValidateDTO.getClusterId(), vdiDesktopConfigVO.toDeskSpec()));
        //校验镜像、运行位置、存储位置、云桌面策略、网络策略是否合规
        vdiDesktopValidateDTO.setImageEditionId(imageEditionId);
        checkDeskConfigAndVGPU(vdiDesktopValidateDTO);
    }

    /**
     * 校验镜像、运行位置、存储位置、云桌面策略、网络策略是否合规
     * @param vdiDesktopValidateDTO  云桌面配置
     * @throws BusinessException 业务异常
     */
    public void checkDeskConfigAndVGPU(VDIDesktopValidateDTO vdiDesktopValidateDTO) throws BusinessException {
        Assert.notNull(vdiDesktopValidateDTO, "vdiDesktopValidateDTO must not be null");
        // 校验VDI桌面策略选择
        VDIDesktopValidateDTO validateDTO = new VDIDesktopValidateDTO();
        validateDTO.setClusterId(vdiDesktopValidateDTO.getClusterId());
        validateDTO.setImageId(vdiDesktopValidateDTO.getImageId());
        validateDTO.setNetworkId(vdiDesktopValidateDTO.getNetworkId());
        validateDTO.setStrategyId(vdiDesktopValidateDTO.getStrategyId());
        validateDTO.setPlatformId(vdiDesktopValidateDTO.getPlatformId());
        validateDTO.setDeskSpec(vdiDesktopValidateDTO.getDeskSpec());
        clusterAPI.validateVDIDesktopConfig(validateDTO);

        // 检查镜像模板和策略里的显卡是否匹配
        ImageDeskSpecGpuCheckParamDTO checkParamDTO = new ImageDeskSpecGpuCheckParamDTO();
        checkParamDTO.setImageId(vdiDesktopValidateDTO.getImageId());
        checkParamDTO.setImageEditionId(vdiDesktopValidateDTO.getImageEditionId());
        checkParamDTO.setClusterId(vdiDesktopValidateDTO.getClusterId());
        checkParamDTO.setVgpuInfoDTO(vdiDesktopValidateDTO.getDeskSpec().getVgpuInfoDTO());
        checkParamDTO.setStrategyId(vdiDesktopValidateDTO.getStrategyId());
        deskSpecAPI.checkGpuSupportByImageAndSpec(checkParamDTO);
    }

    /**
     * 判断UPM策略是否符合条件
     *
     * @param idvDesktopConfig   IDV配置
     * @param voiDesktopConfig   TCI配置
     * @param vdiDesktopConfigVO VDI配置
     * @throws BusinessException 异常处理
     */
    public void checkUserProfile(@Nullable IdvDesktopConfigVO idvDesktopConfig, @Nullable VoiDesktopConfigVO voiDesktopConfig,
                                 @Nullable VdiDesktopConfigVO vdiDesktopConfigVO) throws BusinessException {
        if (idvDesktopConfig != null && idvDesktopConfig.getUserProfileStrategy() != null) {
            userProfileValidaAPI.validateUserProfileStrategyMustStorageLocal(idvDesktopConfig.getUserProfileStrategy().getId());
        }

        if (voiDesktopConfig != null && voiDesktopConfig.getUserProfileStrategy() != null) {
            userProfileValidaAPI.validateUserProfileStrategyMustStorageLocal(voiDesktopConfig.getUserProfileStrategy().getId());
        }

        if (vdiDesktopConfigVO != null && vdiDesktopConfigVO.getUserProfileStrategy() != null) {
            userProfileValidaAPI.validateUserProfileStrategyMustStoragePersonal(vdiDesktopConfigVO.getUserProfileStrategy().getId());
        }
    }

    /**
     * 判断UPM策略是否符合条件
     *
     * @param idvDesktopConfig IDV配置
     * @throws BusinessException 异常处理
     */
    public void checkUserProfile(@Nullable IdvDesktopConfigVO idvDesktopConfig) throws BusinessException {
        if (idvDesktopConfig != null && idvDesktopConfig.getUserProfileStrategy() != null) {
            userProfileValidaAPI.validateUserProfileStrategyMustStorageLocal(idvDesktopConfig.getUserProfileStrategy().getId());
        }
    }

    /**
     * 校验用户配置主要认证时，认证选择是否符合要求：主要认证CAS和账号密码不能都为空
     *
     * @param primaryCertificationVO 主要认证
     * @param assistCertificationVO  辅助认证
     * @throws BusinessException 参数不合法
     */
    public void validateUserCertification(PrimaryCertificationVO primaryCertificationVO, @Nullable AssistCertificationVO assistCertificationVO)
            throws BusinessException {
        Assert.notNull(primaryCertificationVO, "primaryCertificationVO is not null");
        // 主要认证不允许同时为空
        if (primaryCertificationVO.checkCloseAll()) {
            throw new BusinessException(BusinessKey.RCDC_USER_PRIMARY_CERTIFICATION_CONFIG_FAIL);
        }
        // 全局外部认证关闭时，不允许修改用户/用户组外部认证
        if (primaryCertificationVO.getOpenCasCertification() != null
                && !BooleanUtils.toBoolean(scanCodeAuthParameterAPI.getCasScanCodeAuthInfo().getApplyOpen())) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_CAS_FAIL_RESULT);
        }
        // 辅助认证为null，无需进行校验
        if (assistCertificationVO == null) {
            return;
        }
        // 动态口令开关不为null, 进行校验
        if (assistCertificationVO.getOpenOtpCertification() != null
                && !BooleanUtils.toBoolean(otpCertificationAPI.getOtpCertification().getOpenOtp())) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_OTP_FAIL_RESULT);
        }
        // Radius认证不为null，进行校验
        if (assistCertificationVO.getOpenRadiusCertification() != null
                && !BooleanUtils.toBoolean(thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable())) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_RADIUS_FAIL_RESULT);
        }
        // Radius认证和动态口令不能同时开启
        assistCertificationVO.checkParam();
        // 硬件特征码开关不为null, 进行校验
        if (assistCertificationVO.getOpenHardwareCertification() != null
                && !BooleanUtils.toBoolean(hardwareCertificationAPI.getHardwareCertification().getOpenHardware())) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_HARDWARE_FAIL_RESULT);
        }
        // 短信认证开关不为null, 进行校验
        if (assistCertificationVO.getOpenSmsCertification() != null
                && Boolean.FALSE.equals(smsCertificationAPI.getBusSmsCertificationStrategy().getEnable())) {
            throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_SMS_AUTH_FAIL_RESULT);
        }

        if (!BooleanUtils.isTrue(primaryCertificationVO.getOpenAccountPasswordCertification())) {
            //主要认证未开启本地密码和锐捷客户端扫码认证时,辅助认证不支持勾选短信认证和Radius
            if (BooleanUtils.isTrue(assistCertificationVO.getOpenSmsCertification())) {
                throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_SMS_AND_RADIUS_AUTH_FAIL_RESULT);
            }
            if (BooleanUtils.isTrue(assistCertificationVO.getOpenRadiusCertification())) {
                throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_SMS_AND_RADIUS_AUTH_FAIL_RESULT);
            }
            // 锐捷动态口令未开启直接登录时，不允许勾选
            if (BooleanUtils.isTrue(assistCertificationVO.getOpenOtpCertification())
                    && !BooleanUtils.isTrue(otpCertificationAPI.getOtpCertification().getHasOtpCodeTab())) {
                throw new BusinessException(CasScanCodeAuthBusinessKey.RCDC_RCO_NOT_OPEN_OTP_AUTH_FAIL_RESULT);
            }
        }
    }

    /**
     * @param softwareStrategy
     */
    private void checkSoftwareStrategy(IdLabelEntry softwareStrategy) throws BusinessException {
        if (softwareStrategy != null && softwareStrategy.getId() != null) {
            softwareControlMgmtAPI.findSoftwareStrategyById(softwareStrategy.getId());
        }
    }
}
