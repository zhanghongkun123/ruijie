package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacValidateAdOuRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAdConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacValidateAdOuDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbWatermarkConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.*;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.strategy.CbbClipBoardSupportTypeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageDataRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.common.DeskStrategyHelper;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.entity.UnifiedManageDataEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.UnifiedManageDataService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditfile.service.AuditFileUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.security.auditprinter.service.AuditPrinterUpdateNotifyService;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.constant.UserProfileBusinessKey;
import com.ruijie.rcos.sk.base.exception.AnnotationValidationException;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.IPv4Util;
import com.ruijie.rcos.sk.base.validation.BeanValidationUtil;
import com.ruijie.rcos.sk.base.validator.ValidatorUtil;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.ruijie.rcos.rcdc.clouddesktop.module.def.constants.Constants.CROSS_BAR;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年3月1日
 *
 * @author wjp
 */
public class DeskStrategyAPIImpl implements DeskStrategyAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeskStrategyAPIImpl.class);

    /**
     * 域名正则表达式 "^(((([a-z0-9\\u4e00-\\u9fa5-]{1,255})|\\*)\\.)+[a-z|\\*]{1,255})|\\*$"
     */
    private static final String DOMAIN_NAME_REG = "^(((([a-z0-9\\u4e00-\\u9fa5-\\*]{1,200}))\\.)+[a-z\\u4e00-\\u9fa5|\\*]{1,55})|\\*$";

    private static final String CONTINUOUS_STAR_REGEX = "(\\*){2}";

    @Autowired
    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    @Autowired
    private WatermarkMessageAPI watermarkMessageAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private DeskSnapshotAPI deskSnapshotAPI;

    @Autowired
    private IacAdMgmtAPI cbbAdMgmtAPI;

    @Autowired
    private UnifiedManageDataService unifiedManageDataService;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private CbbDeskStrategyCommonAPI cbbDeskStrategyCommonAPI;

    @Autowired
    private CloudDeskComputerNameConfigAPI cloudDeskComputerNameConfigAPI;

    @Autowired
    private CbbIDVDeskStrategyMgmtAPI cbbIDVDeskStrategyMgmtAPI;

    @Autowired
    private CbbVOIDeskStrategyMgmtAPI cbbVOIDeskStrategyMgmtAPI;

    @Autowired
    private CbbThirdPartyDeskStrategyMgmtAPI cbbThirdPartyDeskStrategyMgmtAPI;

    @Autowired
    private AuditFileUpdateNotifyService auditFileUpdateNotifyService;

    @Autowired
    private AuditPrinterUpdateNotifyService auditPrinterUpdateNotifyService;

    @Autowired
    private AuditFileMgmtAPI auditFileMgmtAPI;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private AdminDataPermissionAPI adminDataPermissionAPI;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;


    @Override
    public PageQueryResponse<ViewDeskStrategyDTO> pageQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "Param [pageQueryRequest] must not be null");

        return DeskStrategyAPI.super.pageQuery(pageQueryRequest);
    }

    @Override
    public PageQueryResponse<DeskStrategyDTO> pageDeskStrategyQuery(PageQueryRequest pageQueryRequest) throws BusinessException {
        Assert.notNull(pageQueryRequest, "Param [pageQueryRequest] must not be null");
        PageQueryResponse<ViewDeskStrategyDTO> pageQueryResponse = DeskStrategyAPI.super.pageQuery(pageQueryRequest);

        PageQueryResponse<DeskStrategyDTO> deskStrategyResponse = new PageQueryResponse<>();
        deskStrategyResponse.setTotal(pageQueryResponse.getTotal());

        if (pageQueryResponse.getItemArr() == null || pageQueryResponse.getItemArr().length == 0) {
            deskStrategyResponse.setItemArr(new DeskStrategyDTO[]{});
            return deskStrategyResponse;
        }

        DeskStrategyDTO[] deskStrategyArr = convert2DeskStrategyDTOArr(pageQueryResponse.getItemArr());
        deskStrategyResponse.setItemArr(deskStrategyArr);

        return deskStrategyResponse;
    }

    private DeskStrategyDTO[] convert2DeskStrategyDTOArr(ViewDeskStrategyDTO[] viewDeskStrategyArr) {
        // 多计算集群下是否支持锁定
        boolean canUnifiedManageLock = rccmManageAPI.needUnifiedManageLock(UnifiedManageFunctionKeyEnum.DESK_STRATEGY);
        return Arrays.stream(viewDeskStrategyArr).map(viewDeskStrategy -> {
            DeskStrategyDTO deskStrategyDTO = new DeskStrategyDTO();
            BeanUtils.copyProperties(viewDeskStrategy, deskStrategyDTO);

            //协议代理
            deskStrategyDTO.setEnableAgreementAgency(viewDeskStrategy.getEnableAgreementAgency());
            deskStrategyDTO.setStrategyType(viewDeskStrategy.getStrategyType().name());

            // 水印配置
            if (StringUtils.isNotBlank(viewDeskStrategy.getWatermarkInfo())) {
                deskStrategyDTO.setWatermarkInfo(JSON.parseObject(viewDeskStrategy.getWatermarkInfo(), CbbWatermarkConfigDTO.class));
            }

            // 是否支持前端修改
            deskStrategyDTO.setCanLock(canUnifiedManageLock && Objects.nonNull(viewDeskStrategy.getUnifiedManageDataId()));

            // 剪切板支持的类型
            CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr = StringUtils.isNotBlank(viewDeskStrategy.getClipBoardSupportType()) ?
                    JSONObject.parseArray(viewDeskStrategy.getClipBoardSupportType(), CbbClipBoardSupportTypeDTO.class)
                            .toArray(new CbbClipBoardSupportTypeDTO[0]) : new CbbClipBoardSupportTypeDTO[]{};
            deskStrategyDTO.setClipBoardSupportTypeArr(clipBoardSupportTypeArr);
            // IP网段
            List<CbbIpLimitDTO> ipSegmentDTOList = StringUtils.isNotBlank(viewDeskStrategy.getIpLimitInfo()) ?
                    JSON.parseArray(viewDeskStrategy.getIpLimitInfo(), CbbIpLimitDTO.class) : new ArrayList<>();
            deskStrategyDTO.setIpSegmentDTOList(ipSegmentDTOList);
            deskStrategyDTO.setIpLimitMode(Optional.ofNullable(viewDeskStrategy.getIpLimitMode()).orElse(CbbIpLimitModeEnum.NOT_USE));
            return deskStrategyDTO;
        }).toArray(DeskStrategyDTO[]::new);
    }

    @Override
    public String obtainStrategyName(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId is null");
        String resultName = strategyId.toString();
        try {
            CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
            resultName = deskStrategyDTO.getName();
        } catch (BusinessException e) {
            LOGGER.error("获取策略数据失败", e);
        }
        return resultName;
    }

    @Override
    public String getDeskStrategyUsedMessageByUserProfileStrategyId(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId must not be null");
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);
        if (CbbCloudDeskPattern.RECOVERABLE != deskStrategy.getPattern() && CbbStrategyType.THIRD != deskStrategy.getStrategyType()) {
            return LocaleI18nResolver.resolve(UserProfileBusinessKey.RCDC_RCO_USER_PROFILE_STRATEGY_DISABLE_DESK_STRATEGY);
        }
        return StringUtils.EMPTY;
    }

    @Override
    public CbbCloudDeskPattern findPatternById(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId must not be null");

        return cbbVDIDeskStrategyMgmtAPI.findPatternById(strategyId);
    }

    @Override
    public CbbDesktopSessionType findSessionTypeById(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId must not be null");

        return cbbVDIDeskStrategyMgmtAPI.findSessionTypeById(strategyId);
    }

    @Override
    public void doWatermarkAfterUpdateStrategy(UUID strategyId, @Nullable CbbWatermarkConfigDTO oldWatermarkConfig) {
        Assert.notNull(strategyId, "strategyId must not be null");

        CbbDeskStrategyDTO deskStrategyDTO;
        try {
            deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        } catch (BusinessException e) {
            LOGGER.error("获取策略[{}]数据失败", strategyId, e);
            return;
        }

        // 获取云桌面策略关联的桌面列表
        List<CloudDesktopDTO> desktopList = userDesktopMgmtAPI.getAllDesktopByStrategyId(strategyId);
        desktopList = desktopList.stream().filter(desktop -> Objects.equals(desktop.getDesktopState(), CbbCloudDeskState.RUNNING.name()))
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("云桌面策略[{}]没有运行中的桌面，无需发送水印配置", deskStrategyDTO.getName());
            return;
        }

        List<CloudDesktopDetailDTO> desktopDetailList = desktopList.stream().map(desktop -> {
            CloudDesktopDetailDTO desktopDetail = new CloudDesktopDetailDTO();
            BeanUtils.copyProperties(desktop, desktopDetail);
            return desktopDetail;
        }).collect(Collectors.toList());

        // 云桌面策略开启了水印就直接发云桌面策略的水印配置
        if (Boolean.TRUE.equals(deskStrategyDTO.getEnableWatermark())) {
            watermarkMessageAPI.sendToDesktopList(desktopDetailList, deskStrategyDTO.getWatermarkInfo());
            return;
        }
        // 如果修改前云桌面策略开了水印功能，且修改后关闭水印功能，需要发送全局的水印策略
        if (Objects.nonNull(oldWatermarkConfig) && Boolean.TRUE.equals(oldWatermarkConfig.getEnable())) {
            watermarkMessageAPI.sendToDesktopList(desktopDetailList, null);
        }
    }

    @Override
    public void sendDesktopStrategyWatermark(CloudDesktopDetailDTO desktopDetail) {
        Assert.notNull(desktopDetail, "desktopDetail must not be null");
        Assert.notNull(desktopDetail.getDesktopStrategyId(), "desktopDetail.getDesktopStrategyId() must not be null");

        CbbWatermarkConfigDTO watermarkConfig = getStrategyWatermarkConfig(desktopDetail.getDesktopStrategyId());
        if (Objects.isNull(watermarkConfig) || !Boolean.TRUE.equals(watermarkConfig.getEnable())) {
            // 云桌面策略未开启水印，置为null，调发送方法会发送全局策略的水印配置
            watermarkConfig = null;
        }
        watermarkMessageAPI.sendToDesktopList(Collections.singletonList(desktopDetail), watermarkConfig);
    }

    @Override
    public CbbWatermarkConfigDTO getStrategyWatermarkConfig(UUID strategyId) {
        Assert.notNull(strategyId, "strategyId must not be null");

        CbbDeskStrategyDTO deskStrategyDTO;
        try {
            deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        } catch (BusinessException e) {
            LOGGER.error("获取策略[{}]数据失败", strategyId, e);
            // 返回null
            return null;
        }
        return deskStrategyDTO.getWatermarkInfo();
    }

    @Override
    public void createDeskStrategyValidate(DeskStrategyCheckDTO request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        // 校验OU
        validateAdOu(request.getAdOu());
        // 应用分发类型是否屏蔽
        validateAppLayerHidden(request);

        CbbStrategyType strategyType = request.getStrategyType();
        if (strategyType == CbbStrategyType.VDI) {
            // 服务器非VDI模式,不允许创建
            if (!serverModelAPI.isVdiModel()) {
                throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_SUPPORT_SERVER_MODEL);
            }
            // 校验计算机名称
            DeskStrategyHelper.computerNameValueValidate(request.getComputerName());
            // 快照相关
            validateUserSnapshotNum(request.getUserMaxSnapshotNum());
            // 校验云桌面允许登录时间
            if (ArrayUtils.isNotEmpty(request.getDesktopAllowLoginTimeArr())) {
                DeskStrategyHelper.verifyDesktopAllowLoginTime(request.getDesktopAllowLoginTimeArr());
            }
            // 校验安全审计配置
            auditEnableExtStorageValidate(request.getAuditFileInfo(), request.getAuditPrinterInfo());
            // 剪切板
            request.setClipBoardMode(clipBoardArrValidate(request.getClipBoardSupportTypeArr(), false));
            // 协议配置
            agreementConfigAndProtocolValidate(request.getEstProtocolType(), request.getAgreementInfo(), strategyType, request.getSessionType());
            // 透明加解密
            transparentEncryptValidate(request);
            // 带宽控制配置
            usbBandWidthValidation(request.getEnableUsbBandwidth(), request.getUsbBandwidthInfo());
        }
        if (strategyType == CbbStrategyType.VOI || strategyType == CbbStrategyType.IDV) {
            // 校验计算机名称
            DeskStrategyHelper.computerNameValueValidate(request.getComputerName());
            // 校验本地磁盘
            DeskStrategyHelper.enableAllowLocalDiskValueValidate(request.getEnableAllowLocalDisk(),
                    request.getEnableOpenDesktopRedirect(), request.getStrategyName());

            if (Objects.isNull(request.getSystemDisk())) {
                throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_VDI_MUST_SYSTEM_DISK);
            }
        }
        if (strategyType == CbbStrategyType.VOI) {
            // 校验策略类型
            DeskStrategyHelper.voiValidateCloudDeskPattern(request.getDesktopType());
        }
        if (strategyType == CbbStrategyType.THIRD) {
            // 校验云桌面允许登录时间
            if (ArrayUtils.isNotEmpty(request.getDesktopAllowLoginTimeArr())) {
                DeskStrategyHelper.verifyDesktopAllowLoginTime(request.getDesktopAllowLoginTimeArr());
            }
            // 剪切板
            request.setClipBoardMode(clipBoardArrValidate(request.getClipBoardSupportTypeArr(), false));
            // 协议配置
            agreementConfigAndProtocolValidate(request.getEstProtocolType(), request.getAgreementInfo(), strategyType, request.getSessionType());
        }
    }

    private void validateAppLayerHidden(DeskStrategyCheckDTO request) throws BusinessException {
        if (cbbGlobalStrategyMgmtAPI.getAppLayerHidden() && request.getDesktopType() == CbbCloudDeskPattern.APP_LAYER) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_SUPPORT_CREATE_APP_LAYER);
        }
    }

    @Override
    public void updateDeskStrategyValidate(DeskStrategyCheckDTO request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        // 1.获取策略信息
        CbbDeskStrategyDTO oldDeskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(request.getId());
        if (oldDeskStrategy.getState() != CbbDeskStrategyState.AVAILABLE) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE);
        }
        // 校验OU
        validateUpdateAdOu(oldDeskStrategy.getStrategyType(), request.getId(), request.getAdOu());
        // 校验策略是否可修改
        checkDeskStrategyCanChange(request.getId(), request.getUnifiedManageDataId());
        CbbStrategyType strategyType = oldDeskStrategy.getStrategyType();
        if (strategyType != CbbStrategyType.THIRD) {
            // 校验计算机名称
            DeskStrategyHelper.computerNameValueValidate(request.getComputerName());
        }
        if (strategyType == CbbStrategyType.THIRD) {
            // 剪切板
            request.setClipBoardMode(clipBoardArrValidate(request.getClipBoardSupportTypeArr(), false));
            // 协议配置
            agreementConfigAndProtocolValidate(request.getEstProtocolType(), request.getAgreementInfo(),
                    strategyType, oldDeskStrategy.getSessionType());
        }
        if (strategyType == CbbStrategyType.VDI) {
            // 快照相关
            validateUserSnapshotNum(request.getUserMaxSnapshotNum());
            // 校验云桌面允许登录时间
            if (ArrayUtils.isNotEmpty(request.getDesktopAllowLoginTimeArr())) {
                DeskStrategyHelper.verifyDesktopAllowLoginTime(request.getDesktopAllowLoginTimeArr());
            }
            // 校验安全审计配置
            auditEnableExtStorageValidate(request.getAuditFileInfo(), request.getAuditPrinterInfo());
            // 剪切板
            request.setClipBoardMode(clipBoardArrValidate(request.getClipBoardSupportTypeArr(), false));
            // 带宽控制配置
            usbBandWidthValidation(request.getEnableUsbBandwidth(), request.getUsbBandwidthInfo());
            // 协议配置
            agreementConfigAndProtocolValidate(request.getEstProtocolType(), request.getAgreementInfo(),
                    strategyType, oldDeskStrategy.getSessionType());
            // 更新VDI策略时,前端不会传DesktopType,需要使用数据库值
            request.setDesktopType(oldDeskStrategy.getPattern());
            request.setSessionType(oldDeskStrategy.getSessionType());
            // 透明加解密
            transparentEncryptValidate(request);
        }
        if (strategyType != CbbStrategyType.VDI) {
            // 校验本地磁盘
            DeskStrategyHelper.enableAllowLocalDiskValueValidate(request.getEnableAllowLocalDisk(),
                    request.getEnableOpenDesktopRedirect(), request.getStrategyName());
        }
        if (strategyType == CbbStrategyType.VOI) {
            // 校验策略类型
            DeskStrategyHelper.voiValidateCloudDeskPattern(request.getDesktopType());
        }
    }

    private void auditEnableExtStorageValidate(CbbAuditFileConfigDTO auditFileInfo, CbbAuditPrinterConfigDTO auditPrinterInfo)
            throws BusinessException {
        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = auditFileMgmtAPI.obtainAuditFileGlobalConfig();
        // 已经配置文件服务器,直接跳过
        if (auditFileGlobalConfigDTO.getEnableExtStorage() && Objects.nonNull(auditFileGlobalConfigDTO.getExternalStorageId())) {
            return;
        }

        if (Objects.nonNull(auditFileInfo) && auditFileInfo.getEnableAuditFileContent()) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_ENABLE_AUDIT_FILE_AND_ENABLE_STORAGE);
        }
        if (Objects.nonNull(auditPrinterInfo) && auditPrinterInfo.getEnableAuditPrintContent()) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_ENABLE_AUDIT_PRINTER_AND_ENABLE_STORAGE);
        }
    }

    private void agreementConfigAndProtocolValidate(CbbEstProtocolType protocolType, @Nullable AgreementDTO agreementInfo,
                                                    CbbStrategyType strategyType, CbbDesktopSessionType sessionType) throws BusinessException {
        // 第三方和多会话只支持HEST，不支持EST
        if ((CbbStrategyType.THIRD == strategyType || CbbDesktopSessionType.MULTIPLE == sessionType) &&
                CbbEstProtocolType.EST == protocolType) {
            throw new BusinessException(com.ruijie.rcos.rcdc.rco.module.def.BusinessKey.RCO_DESK_STRATEGY_AGREEMENT_NOT_SUPPORTED_SESSION_TYPE);
        }
        // 协议配置校验
        agreementConfigValidate(protocolType, agreementInfo);
    }

    @Override
    public void agreementConfigValidate(CbbEstProtocolType protocolType, @Nullable AgreementDTO agreementInfo) throws BusinessException {
        Assert.notNull(protocolType, "protocolType is not null");
        // 前端未就绪,暂时注释不校验
        if (Objects.isNull(agreementInfo)) {
            return;
        }
        try {
            if (CbbEstProtocolType.EST == protocolType) {
                agreementValidate(protocolType, agreementInfo.getLanEstConfig());
                agreementValidate(protocolType, agreementInfo.getWanEstConfig());
                return;
            }
            if (CbbEstProtocolType.HEST == protocolType) {
                agreementValidate(protocolType, agreementInfo.getLanEstConfig());
                agreementValidate(protocolType, agreementInfo.getWanEstConfig());
                return;
            }
        } catch (AnnotationValidationException e) {
            LOGGER.error("协议配置校验异常:", e);
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_AGREEMENT_PARAMETER_ERROR, e);
        }
        throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_AGREEMENT_PARAMETER_ERROR);
    }

    private void agreementValidate(CbbEstProtocolType protocolType, AgreementConfigRequestDTO estConfig) throws AnnotationValidationException,
            BusinessException {
        if (CbbEstProtocolType.EST == protocolType) {
            EstConfigCheckDTO estConfigCheck = new EstConfigCheckDTO();
            BeanUtils.copyProperties(estConfig, estConfigCheck);
            BeanValidationUtil.validateObject(EstConfigCheckDTO.class, estConfigCheck);
        } else if (CbbEstProtocolType.HEST == protocolType) {
            HestConfigCheckDTO hestConfigCheck = new HestConfigCheckDTO();
            BeanUtils.copyProperties(estConfig, hestConfigCheck);
            BeanValidationUtil.validateObject(HestConfigCheckDTO.class, hestConfigCheck);
        }

        try {
            JSON.parseObject(estConfig.getWebAdvanceSettingInfo());
        } catch (Exception e) {
            LOGGER.error("协议配置-高级配置JSON:{} 格式校验异常:", JSON.toJSONString(estConfig.getWebAdvanceSettingInfo()), e);
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_AGREEMENT_PARAMETER_ERROR, e);
        }
    }

    private void transparentEncryptValidate(DeskStrategyCheckDTO checkDTO) throws BusinessException {
        // 为空或未开启则不校验
        Boolean enable = checkDTO.getEnableTransparentEncrypt();
        if (!Boolean.TRUE.equals(enable)) {
            return;
        }
        // 只支持个性,还原
        if (CbbCloudDeskPattern.PERSONAL != checkDTO.getDesktopType() && CbbCloudDeskPattern.RECOVERABLE != checkDTO.getDesktopType()) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SUPPORT_PATTERN);
        }
        // 只支持单会话
        if (CbbDesktopSessionType.SINGLE != checkDTO.getSessionType()) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SUPPORT_SESSION);
        }

        CbbTransparentEncryptDTO transparentEncrypt = checkDTO.getTransparentEncryptInfo();
        if (Objects.isNull(transparentEncrypt)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_PARAMETER_ERROR);
        }

        // 校验全局策略中是否启用密钥
        List<CbbEncryptionKeyDTO> keyList = cbbGlobalStrategyMgmtAPI.queryEncryptionList();
        if (keyList.stream().noneMatch(CbbEncryptionKeyDTO::isEnable)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ENCRYPTION_NOT_EXIST);
        }

        // 校验软件ID列表真实性
        PageQueryResponse<CbbEncryptionSoftwareDTO> response = cbbGlobalStrategyMgmtAPI.getEncryptionSoftwareList(null);
        List<UUID> idList = Arrays.stream(response.getItemArr()).map(CbbEncryptionSoftwareDTO::getId).collect(Collectors.toList());
        Collection<UUID> intersection = CollectionUtils.intersection(idList, transparentEncrypt.getControlledSoftwareList());
        if (intersection.size() != transparentEncrypt.getControlledSoftwareList().size()) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_SOFTWARE_ERROR);
        }
        List<UUID> browserList = transparentEncrypt.getControlledBrowserList();
        if (CollectionUtils.isNotEmpty(browserList)) {
            intersection = CollectionUtils.intersection(idList, browserList);
            if (intersection.size() != browserList.size()) {
                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_BROWSER_ERROR);
            }
        }

        // 校验放行地址格式
        if (transparentEncrypt.getEnableRelease()) {
            List<CbbReleaseAddressDTO> addressList = transparentEncrypt.getReleaseAddressList();
            if (CollectionUtils.isEmpty(addressList)) {
                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_PARAMETER_ERROR);
            }

            // 重复路径校验
            long count = addressList.stream().map(CbbReleaseAddressDTO::getAddress).distinct().count();
            if (count < addressList.size()) {
                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_REPEAT);
            }

            // 地址校验ip,ip网段,域名
            for (CbbReleaseAddressDTO addressDTO : addressList) {
                String address = addressDTO.getAddress();
                switch (addressDTO.getType()) {
                    case IP:
                        if (!ValidatorUtil.isIPv4Address(address)) {
                            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, address);
                        }
                        break;
                    case IP_SEGMENT:
                        String[] ipSegmentArr = address.split(CROSS_BAR);
                        try {
                            if (IPv4Util.compareIp(ipSegmentArr[0], ipSegmentArr[1]) > 0) {
                                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, address);
                            }
                        } catch (Exception e) {
                            LOGGER.error("放行地址IP网段:{} 校验异常:", address, e);
                            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, e, address);
                        }
                        break;
                    case DOMAIN_NAME:
                        if (!address.matches(DOMAIN_NAME_REG)) {
                            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, address);
                        }
                        Pattern p = Pattern.compile(CONTINUOUS_STAR_REGEX);
                        Matcher m = p.matcher(address);
                        if (m.find()) {
                            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, address);
                        }
                        break;
                    default:
                        throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_TRANSPARENT_ENCRYPT_ADDRESS_ERROR, address);
                }
            }
        }
    }

    @Override
    public void checkDeskStrategyCanChange(UUID deskStrategyId, @Nullable UUID unifiedManageDataId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deskStrategyId can not null");
        CbbDeskStrategyDTO deskStrategy = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);
        UnifiedManageDataEntity manageDataEntity = unifiedManageDataService.findByRelatedIdAndRelatedType(
                new UnifiedManageDataRequest(deskStrategyId, UnifiedManageFunctionKeyEnum.DESK_STRATEGY));
        // 存在统一管理标识、且外部传unifiedManageDataId和库里面不相等、多计算集群支持锁定，则系统不允许修改策略
        if (Objects.nonNull(manageDataEntity) && !Objects.equals(manageDataEntity.getUnifiedManageDataId(), unifiedManageDataId) &&
                rccmManageAPI.needUnifiedManageLock(UnifiedManageFunctionKeyEnum.DESK_STRATEGY)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_SYNC_NOT_SUPPORTED, deskStrategy.getName());
        }
    }

    @Override
    public void deleteDeskStrategy(UUID deskStrategyId) throws BusinessException {
        Assert.notNull(deskStrategyId, "deleteDeskStrategy deskStrategyId can not null");
        CbbDeskStrategyDTO deskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(deskStrategyId);
        cbbDeskStrategyCommonAPI.updateDeskStrategyState(deskStrategyId, CbbDeskStrategyState.DELETING);
        CbbStrategyType cbbStrategyType = deskStrategyDTO.getStrategyType();
        switch (cbbStrategyType) {
            case VDI:
                cbbVDIDeskStrategyMgmtAPI.deleteDeskStrategyVDI(deskStrategyId);
                break;
            case IDV:
                cbbIDVDeskStrategyMgmtAPI.deleteDeskStrategyIDV(deskStrategyId);
                break;
            case VOI:
                cbbVOIDeskStrategyMgmtAPI.deleteDeskStrategyVOI(deskStrategyId);
                break;
            case THIRD:
                cbbThirdPartyDeskStrategyMgmtAPI.deleteDeskStrategyThirdParty(deskStrategyId);
                break;
            default:
                throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_TYPE_NOT_EXIST, cbbStrategyType.name());
        }
        // 删除数据权限
        adminDataPermissionAPI.deleteByPermissionDataId(deskStrategyId.toString());
        if (CbbStrategyType.THIRD != cbbStrategyType) {
            cloudDeskComputerNameConfigAPI.deleteCloudDeskComputerNameConfig(deskStrategyId);
        }
    }

    @Override
    public void doAuditUpdateStrategy(UUID id) {
        Assert.notNull(id, "deskStrategyId can not null");
        auditFileUpdateNotifyService.notifyDeskAuditFileStrategyAndStrategyId(id);
        auditPrinterUpdateNotifyService.notifyDeskAuditPrinterStrategyAndStrategyId(id);
    }

    private void validateUserSnapshotNum(Integer userMaxSnapshotNum) throws BusinessException {
        Integer num = Optional.ofNullable(userMaxSnapshotNum).orElse(0);
        Integer cur = deskSnapshotAPI.getMaxSnapshots();
        if (num > cur) {
            String numStr = String.valueOf(num);
            String curStr = String.valueOf(cur);
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_USER_SNAPHOST_NUM_LIMIT, numStr, curStr);
        }
    }

    /**
     * 修改策略是校验并返回最终adOu值
     *
     * @param strategyType 策略类型
     * @param strategyId   策略ID
     * @param adOu         传入的adOu值
     * @throws BusinessException 业务异常
     */
    private void validateUpdateAdOu(CbbStrategyType strategyType, UUID strategyId, @Nullable String adOu)
            throws BusinessException {
        Assert.notNull(strategyType, "strategyType must not be null");
        Assert.notNull(strategyId, "strategyId must not be null");

        adOu = StringUtils.isEmpty(adOu) ? StringUtils.EMPTY : adOu;
        CbbDeskStrategyDTO cbbDeskStrategyDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategy(strategyId);
        String originalAdOu = cbbDeskStrategyDTO.getAdOu();
        if (!Objects.equals(adOu, originalAdOu)) {
            // 有调整adOu才验证
            validateAdOu(adOu);
        }
    }

    /**
     * 根据ad域的配置检查adOu的值: ad域未配置或未开启自动加域默认返回空或者原来的adOu值；不存在OU时报错；
     *
     * @param adOu 新adOu
     * @throws BusinessException 业务异常
     */
    private void validateAdOu(@Nullable String adOu) throws BusinessException {
        if (StringUtils.isEmpty(adOu)) {
            return;
        }
        IacDomainConfigDetailDTO adConfig = cbbAdMgmtAPI.getAdConfig();
        // AD域连接开启且自动加域开启时 才能验证adOu
        if (adConfig == null || !adConfig.getEnable() || Boolean.FALSE == adConfig.getAutoJoin()) {
            throw new BusinessException(BusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_UNOPENED_AD_AUTO_JOIN);
        }
        IacAdConfigDTO adConfigDTO = new IacAdConfigDTO();
        BeanUtils.copyProperties(adConfig, adConfigDTO);
        IacValidateAdOuRequest iacValidateAdOuRequest = new IacValidateAdOuRequest();
        iacValidateAdOuRequest.setValidateAdOuDTO(new IacValidateAdOuDTO(adOu, StringUtils.EMPTY));
        iacValidateAdOuRequest.setAdConfigDTO(adConfigDTO);
        cbbAdMgmtAPI.validateAdOu(iacValidateAdOuRequest);
    }

    /**
     * 剪贴板数据格式校验
     *
     * @param clipBoardSupportTypeArr clipBoardSupportTypeArr
     * @param allowArrEmpty           临时权限剪切板允许clipBoardSupportTypeArr空
     * @return 新版本根据clipBoardSupportTypeArr给clipBoardMode赋值
     * @throws BusinessException 业务异常
     */
    @Override
    public CbbClipboardMode clipBoardArrValidate(@Nullable CbbClipBoardSupportTypeDTO[] clipBoardSupportTypeArr, Boolean allowArrEmpty)
            throws BusinessException {
        Assert.notNull(allowArrEmpty, "allowArrEmpty must not null");

        CbbClipboardMode fileMode = CbbClipboardMode.FORBIDDEN;
        CbbClipboardMode textMode = CbbClipboardMode.FORBIDDEN;

        // 不是临时权限，Arr字段不能为空
        if (!allowArrEmpty && ArrayUtils.isEmpty(clipBoardSupportTypeArr)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
        }

        // 临时权限Arr为Null，则表示剪切板功能未开启(沿用之前逻辑，根据clip_board_mode字段是否Null判断剪切板是否开启)
        if (Objects.isNull(clipBoardSupportTypeArr)) {
            // 表示剪贴板配置未开启
            return null;
        }

        if (clipBoardSupportTypeArr[0].getType() == clipBoardSupportTypeArr[1].getType()) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
        }

        for (CbbClipBoardSupportTypeDTO dto : clipBoardSupportTypeArr) {
            if (Objects.isNull(dto.getType())) {
                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
            }

            if (Objects.isNull(dto.getMode())) {
                throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
            }

            if (CbbClipBoardSupportTypeEnum.FILE == dto.getType()) {
                fileMode = dto.getMode();
            }

            if (CbbClipBoardSupportTypeEnum.TEXT == dto.getType()) {
                if (CbbClipboardMode.VM_TO_HOST == dto.getMode() && Objects.isNull(dto.getVmToHostCharLimit())) {
                    throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
                }

                if (CbbClipboardMode.HOST_TO_VM == dto.getMode() && Objects.isNull(dto.getHostToVmCharLimit())) {
                    throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
                }

                if (CbbClipboardMode.NO_LIMIT == dto.getMode() &&
                        (Objects.isNull(dto.getVmToHostCharLimit()) || Objects.isNull(dto.getHostToVmCharLimit()))) {
                    throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_CLIPBOARD_PARAMETER_ERROR);
                }

                textMode = dto.getMode();
            }
        }

        // 取范围小的
        if (fileMode.getLevel() > textMode.getLevel()) {
            return textMode;
        }

        if (fileMode.getLevel() < textMode.getLevel()) {
            return fileMode;
        }

        if ((fileMode.getLevel() == textMode.getLevel())) {
            // 文件和文本都是单向且方向交叉，取禁止传输
            if ((fileMode == CbbClipboardMode.HOST_TO_VM && textMode == CbbClipboardMode.VM_TO_HOST) ||
                    (fileMode == CbbClipboardMode.VM_TO_HOST && textMode == CbbClipboardMode.HOST_TO_VM)) {
                return CbbClipboardMode.FORBIDDEN;
            } else {
                return fileMode;
            }
        }

        return CbbClipboardMode.FORBIDDEN;
    }

    @Override
    public void usbBandWidthValidation(@Nullable Boolean enableUsbBandwidth, @Nullable CbbUsbBandwidthDTO usbBandwidthInfo) throws BusinessException {
        if (Objects.isNull(enableUsbBandwidth)) {
            return;
        }

        if (enableUsbBandwidth && Objects.isNull(usbBandwidthInfo)) {
            throw new BusinessException(BusinessKey.RCO_DESK_STRATEGY_USB_BANDWIDTH_INFO_NOT_NULL);
        }
    }
}
