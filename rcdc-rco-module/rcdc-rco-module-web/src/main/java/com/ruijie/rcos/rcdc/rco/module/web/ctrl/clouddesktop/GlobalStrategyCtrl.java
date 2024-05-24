package com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop;

import static com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.GlobalStrategyBussinessKey.RCDC_IMAGE_SYNC_QOS_NOT_LIMIT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyUserAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbExternalStorageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbGlobalStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbSessionPortStrategyAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.CbbConfigureGlobalStrategyRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.CbbCreatePartDisplayStrategyRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.CbbUpdatePartDisplayStrategyRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.globalstrategy.SessionPortConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbGlobalStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbPartDisplayStrategyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbAgreementTemplateDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbEncryptionKeyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbEncryptionSoftwareDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbHestConfigDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbImageSyncStrategyDTO;
import com.ruijie.rcos.rcdc.license.module.def.api.CbbLicenseCenterAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.NotifyConfigChangeDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.NotifyTargetDTO;
import com.ruijie.rcos.rcdc.rca.module.def.constants.RcaApiActionConstant;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.AgreementConfigRequestDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.OneAgentGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesksoftUseConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.EvaluationStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.IpLimitStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalSimplifyDeploymentConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.CertificationStrategyRegexEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UpdateParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterListResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoGlobalParameterConstants;
import com.ruijie.rcos.rcdc.rco.module.def.enums.OperationControlType;
import com.ruijie.rcos.rcdc.rco.module.def.enums.RcoGlobalParameterEnum;
import com.ruijie.rcos.rcdc.rco.module.def.security.auditfile.dto.AuditFileGlobalConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.rcdc.rco.module.def.utils.RedLineUtil;
import com.ruijie.rcos.rcdc.rco.module.web.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.AppDisplayModeStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.PartDisplayStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.dto.ProtocolTransferConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.enums.SpiceDisplayStrategyType;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.est.EstConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.globalstrategy.*;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.EncryptionSoftwareListRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.request.strategy.SaveEncryptionRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.CheckDuplicationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.DesksoftUseConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetAdvanceConfigStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetAuditFileGlobalConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetEnableFullSystemDiskResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetGlobalStrategyWebVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetPartDisplayStrategyResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.globalstrategy.GetTerminalSimplifyDeploymentConfigResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.AgreementTemplateVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.response.strategy.EncryptionDetailResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.AuthCompatibleVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.vo.CertificationStrategyVO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.security.auditfile.AuditFileBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.crypto.AesUtil;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import com.ruijie.rcos.sk.webmvc.api.session.SessionContext;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 全局策略Controller
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月1日
 *
 * @author Ghang
 */
@Api(tags = "全局策略管理")
@Controller
@RequestMapping("/rco/globalStrategy")
public class GlobalStrategyCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalStrategyCtrl.class);

    private static final int TIME_OUT = 6000;

    @Autowired
    private CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

    @Autowired
    private IpLimitAPI ipLimitAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private EvaluationAPI evaluationAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private DesksoftUseConfigNotifyAPI desksoftUseConfigNotifyAPI;

    @Autowired
    private ConfigurationWizardAPI configurationWizardAPI;

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Autowired
    private EnableFullSystemDiskAPI enableFullSystemDiskAPI;

    @Autowired
    private TerminalSimplifyDeploymentConfigAPI terminalSimplifyDeploymentConfigAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;


    @Autowired
    private DesktopAPI desktopAPI;

    @Autowired
    private CbbLicenseCenterAPI cbbLicenseCenterAPI;

    @Autowired
    private RcaSupportAPI rcaSupportAPI;

    @Autowired
    private CbbExternalStorageMgmtAPI cbbExternalStorageMgmtAPI;

    @Autowired
    private AuditFileMgmtAPI auditFileMgmtAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private IacThirdPartyUserAPI cbbThirdPartyUserAPI;

    @Autowired
    private CbbImageStrategyMgmtAPI imageStrategyAPI;
    
    @Autowired
    private CbbSessionPortStrategyAPI cbbSessionPortStrategyAPI;

    @Autowired
    private RcaNotifyAPI rcaNotifyAPI;

    @Autowired
    private PermissionHelper permissionHelper;

    @Autowired
    private ServerModelAPI serverModelAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    /**
     * 配置全局策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("edit")
    @ApiVersions(value = {@ApiVersion(value = Version.V1_0_0, descriptions = {"配置全局策略"}),
            @ApiVersion(value = Version.V3_1_1, descriptions = {"去除应用显示模式策略配置"})})
    public DefaultWebResponse configureGlobalStrategy(ConfigureGlobalStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "ConfigureGlobalStrategyWebRequest is null");
        CbbConfigureGlobalStrategyRequest apiRequest = webRequestToAPIRequest(request);
        try {
            cbbGlobalStrategyMgmtAPI.configureGlobalStrategy(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_CONFIG_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑全局配置", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_CONFIG_FAIL, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }


    /**
     * * 恢复全局策略配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("reset")
    public DefaultWebResponse resetGlobalStrategy(ResetGlobalStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "ConfigureGlobalStrategyWebRequest is null");
        try {
            CbbGlobalStrategyDTO globalStrategyDTO = cbbGlobalStrategyMgmtAPI.getDefaultGlobalStrategy();
            CbbConfigureGlobalStrategyRequest apiRequest = defaultInfoToAPIRequest(globalStrategyDTO);
            cbbGlobalStrategyMgmtAPI.configureGlobalStrategy(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("全局策略默认配置恢复失败", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_FAILED, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_GLOBAL_STRATEGY_RESET_FAILED, e, e.getI18nMessage());
        }
    }

    /**
     * 获取全局配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @RequestMapping("detail")
    public DefaultWebResponse getGlobalStrategy(GetGlobalStrategyWebRequest request) {
        Assert.notNull(request, "GetGlobalStrategyWebRequest is null");
        CbbGlobalStrategyDTO globalStrategyDTO = cbbGlobalStrategyMgmtAPI.getGlobalStrategy();
        GetGlobalStrategyWebVO vo = constractGlobalStrategyVO(globalStrategyDTO);
        return DefaultWebResponse.Builder.success(vo);
    }

    /**
     * 获取统一登录限制功能是否可编辑配置
     *
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "unifiedLoginLimitEdit", method = RequestMethod.GET)
    public DefaultWebResponse getUnifiedLoginLimitEdit() throws BusinessException {
        if (!rccmManageAPI.canModifyGlobalSettings()) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING);
        }
        return DefaultWebResponse.Builder.success();
    }


    /**
     * 更新高级配置
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑高级配置")
    @RequestMapping(value = "/advanceConfig/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = "休眠策略"),
            @ApiVersion(value = Version.V3_1_1, descriptions = "在原先的休眠策略基础上加上评测功能策略")})
    public DefaultWebResponse updateAutoSleepStrategy(UpdateAdvanceConfigRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        try {
            evaluationAPI.modifyEvaluationAndSyncTerminal(new EvaluationStrategyDTO(webRequest.getEnableEvaluation()));
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_SUCCESS);

            if (webRequest.getEnableVirtualApplication() != null) {
                rcaSupportAPI.modifyVirtualApplicationState(webRequest.getEnableVirtualApplication());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_ADVANCE_VIRTUAL_APPLICATION_SUCCESS,
                        webRequest.getEnableVirtualApplication().toString());
            }

            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_SUCCESS,
                    new String[]{});
        } catch (Exception e) {
            LOGGER.error("配置高级配置失败", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_FAIL, e.getMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_EDIT_ADVANCE_CONFIGURATION_FAIL, e, e.getMessage());
        }
    }

    /**
     * 获取高级配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取高级配置")
    @RequestMapping(value = "/advanceConfig/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "在原先获取休眠策略基础上加上评测功能策略")})
    public DefaultWebResponse getAutoSleepStrategy(DefaultWebRequest request) {
        Assert.notNull(request, "DefaultWebRequest is null");

        Boolean enableEvaluationStrategy = evaluationAPI.getEvaluationStrategy();
        Boolean isVirtualApplicationState = rcaSupportAPI.getVirtualApplicationState();
        GetAdvanceConfigStrategyResponse webResponse = new GetAdvanceConfigStrategyResponse();
        webResponse.setEnableEvaluation(enableEvaluationStrategy);
        webResponse.setEnableVirtualApplication(isVirtualApplicationState);
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 编辑全局软件策略
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("编辑全局软件策略")
    @RequestMapping(value = "/softwareGlobalConfig/edit", method = RequestMethod.POST)
    public DefaultWebResponse editSoftwareGlobalConfig(EditSoftwareGlobalConfigRequest webRequest) {
        Assert.notNull(webRequest, "updateParameterRequest must not be null");

        String key = RcoGlobalParameterEnum.ENABLE_SOFTWARE_STRATEGY.name();
        String value = String.valueOf(webRequest.getValue());
        UpdateParameterRequest updateParameterRequest = new UpdateParameterRequest(key, value);
        rcoGlobalParameterAPI.updateParameter(updateParameterRequest);
        String status = webRequest.getValue() ? LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_COMMON_STATUS_ON) :
                LocaleI18nResolver.resolve(BusinessKey.RCDC_RCO_COMMON_STATUS_OFF);
        auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_GLOBAL_CONFIGURATION_LOG,
                status, RcoGlobalParameterEnum.ENABLE_SOFTWARE_STRATEGY.getI18nMessage());
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_RCO_EDIT_GLOBAL_CONFIGURATION_SUCCESS, new String[]{});
    }

    /**
     * 获取全局文件导出审批策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取全局文件导出审批策略")
    @RequestMapping(value = "/auditFileGlobalConfig/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "文件导出审计新增功能")})
    public DefaultWebResponse getAuditFileStrategy(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "DefaultWebRequest is null");
        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = auditFileMgmtAPI.obtainAuditFileGlobalConfig();
        GetAuditFileGlobalConfigResponse response = new GetAuditFileGlobalConfigResponse();
        BeanUtils.copyProperties(auditFileGlobalConfigDTO, response);
        // 外置存储打开，且配置对应的存储
        if (BooleanUtils.toBoolean(auditFileGlobalConfigDTO.getEnableExtStorage())
                && Objects.nonNull(auditFileGlobalConfigDTO.getExternalStorageId())) {
            response.setExternalStorageDTO(cbbExternalStorageMgmtAPI.getExternalStorageDetail(auditFileGlobalConfigDTO.getExternalStorageId()));
        }
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 编辑全局文件导出审批策略
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑全局文件导出审批策略")
    @RequestMapping(value = "/auditFileGlobalConfig/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = "文件导出审计新增功能")})
    public DefaultWebResponse editAuditFileGlobalConfig(EditAuditFileGlobalConfigRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "updateParameterRequest must not be null");

        AuditFileGlobalConfigDTO auditFileGlobalConfigDTO = new AuditFileGlobalConfigDTO();
        BeanUtils.copyProperties(webRequest, auditFileGlobalConfigDTO);
        String auditLogContent = buildAuditLogContent(auditFileGlobalConfigDTO);
        try {
            auditFileMgmtAPI.editAuditFileGlobalStrategy(auditFileGlobalConfigDTO);
        } catch (BusinessException e) {
            LOGGER.error("编辑全局文件流转审计策略失败，入参对象为：{},失败原因:{}，堆栈信息\n{}", JSON.toJSONString(webRequest), e.getI18nMessage(), e);
            throw new BusinessException(AuditFileBusinessKey.RCDC_AUDIT_FILE_EDIT_GLOBAL_CONFIGURATION_FAILURE, e, e.getI18nMessage());
        }

        auditLogAPI.recordI18nLog(auditLogContent);
        return DefaultWebResponse.Builder.success(AuditFileBusinessKey.RCDC_AUDIT_FILE_EDIT_GLOBAL_CONFIGURATION_SUCCESS, new String[]{});
    }

    private String buildAuditLogContent(AuditFileGlobalConfigDTO auditFileGlobalConfigDTO) {
        AuditFileGlobalConfigDTO oldAuditFileGlobalConfigDTO = auditFileMgmtAPI.obtainAuditFileGlobalConfig();
        try {
            Boolean enableExtStorage = auditFileGlobalConfigDTO.getEnableExtStorage();
            Boolean enableOldExtStorage = oldAuditFileGlobalConfigDTO.getEnableExtStorage();
            // 文件服务器开关一致，编辑场景
            if (Objects.equals(enableOldExtStorage, enableExtStorage)) {
                // 开关为true且文件服务器ID不一致，存在文件服务器变更
                if (enableExtStorage && !Objects.equals(auditFileGlobalConfigDTO.getExternalStorageId(),
                        oldAuditFileGlobalConfigDTO.getExternalStorageId())) {
                    return LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_UPDATE_GLOBAL_CONFIGURATION_SUCCESS,
                            cbbExternalStorageMgmtAPI.getExternalStorageDetail(oldAuditFileGlobalConfigDTO.getExternalStorageId()).getName(),
                            cbbExternalStorageMgmtAPI.getExternalStorageDetail(auditFileGlobalConfigDTO.getExternalStorageId()).getName());
                }
                // 开关为false或文件服务器ID一致，不存在文件服务器变更
                return LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_EDIT_GLOBAL_CONFIGURATION_SUCCESS);
            }
            // 开关不一致
            return enableExtStorage ? LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_OPEN_GLOBAL_CONFIGURATION_SUCCESS,
                    cbbExternalStorageMgmtAPI.getExternalStorageDetail(auditFileGlobalConfigDTO.getExternalStorageId()).getName())
                    : LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_CLOSE_GLOBAL_CONFIGURATION_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("编辑全局安全设置提示语出现异常", e);
            return LocaleI18nResolver.resolve(AuditFileBusinessKey.RCDC_AUDIT_FILE_EDIT_GLOBAL_CONFIGURATION_SUCCESS);
        }
    }

    /**
     * 获取rco全局配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取rco全局配置")
    @RequestMapping(value = "/globalConfig/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取rco全局配置")})
    public DefaultWebResponse getRcoGlobalParam(FindParameterRequest request) {
        Assert.notNull(request, "FindParameterRequest is null");

        FindParameterResponse findParameterResponse = rcoGlobalParameterAPI.findParameter(request);
        return DefaultWebResponse.Builder.success(findParameterResponse);
    }

    /**
     * 获取rco全局配置（批量）
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取rco全局配置（批量）")
    @RequestMapping(value = "/globalConfig/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取rco全局配置（批量）")})
    public DefaultWebResponse getRcoGlobalParamList(FindParameterListRequest request) {
        Assert.notNull(request, "FindParameterListRequest is null");

        List<FindParameterListResponse> findParameterListResponseList = rcoGlobalParameterAPI.findParameters(request);
        return DefaultWebResponse.Builder.success(findParameterListResponseList);
    }


    /**
     * 编辑终端极简部署模式
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("编辑终端极简部署模式")
    @RequestMapping(value = "/terminalSimplifyDeploymentConfig/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V1_0_0, descriptions = "编辑终端极简部署模式"),
            @ApiVersion(value = Version.V3_1_1, descriptions = "编辑终端极简部署模式")})
    public DefaultWebResponse updateTerminalSimplifyDeploymentConfig(EditTerminalSimplifyDeploymentConfigRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is null");
        TerminalSimplifyDeploymentConfigDTO terminalSimplifyDeploymentConfigDTO = new TerminalSimplifyDeploymentConfigDTO();
        terminalSimplifyDeploymentConfigDTO.setEnableTerminalSimplifyDeployment(webRequest.getEnableTerminalSimplifyDeployment());
        // 修改终端极简部署模式配置
        terminalSimplifyDeploymentConfigAPI.modifyTerminalSimplifyDeploymentConfig(terminalSimplifyDeploymentConfigDTO);
        auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG_SUCCESS);
        return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_RCO_TERMINAL_SIMPLIFY_DEPLOYMENT_CONFIG_SUCCESS, new String[]{});
    }

    /**
     * 获取终端极简部署模式
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取终端极简部署模式")
    @RequestMapping(value = "/terminalSimplifyDeploymentConfig/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取终端极简部署模式")})
    public DefaultWebResponse getTerminalSimplifyDeploymentConfig(DefaultWebRequest request) {
        Assert.notNull(request, "request is null");
        TerminalSimplifyDeploymentConfigDTO terminalSimplifyDeploymentConfig =
                terminalSimplifyDeploymentConfigAPI.getTerminalSimplifyDeploymentConfig();
        // 获取终端极简部署模式配置
        GetTerminalSimplifyDeploymentConfigResponse webResponse = new GetTerminalSimplifyDeploymentConfigResponse();
        webResponse.setEnableTerminalSimplifyDeployment(terminalSimplifyDeploymentConfig.getEnableTerminalSimplifyDeployment());
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 更新高级配置
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑CMC配置")
    @RequestMapping(value = "/cmc/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "编辑CMC配置")})
    public DefaultWebResponse updateCmcStrategy(UpdateCmcConfigRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        try {
            GetConfigurationWizardResponse configurationWizard = configurationWizardAPI.getConfigurationWizardResponse();
            // 如果用户没有加入体验计划 ,并且前端传来 开启CMC 则报错
            if (!configurationWizard.getIsJoinUserExperiencePlan() && webRequest.getDesksoftMsgStatus()) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_USER_EXPERIENCE_PLAN_CLOSE_CAN_NOT_EDIT_CMC);
            }
            desksoftUseConfigNotifyAPI.updateConfig(webRequest.getDesksoftMsgStatus().toString());
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_CMC_CONFIGURATION_SUCCESS);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_RCO_EDIT_CMC_CONFIGURATION_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("配置高级配置失败", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_EDIT_CMC_CONFIGURATION_FAIL, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_EDIT_CMC_CONFIGURATION_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 获取高级配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取CMC配置")
    @RequestMapping(value = "/cmc/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取CMC配置")})
    public DefaultWebResponse getCmcStrategy(DefaultWebRequest request) {
        Assert.notNull(request, "DefaultWebRequest is null");

        DesksoftUseConfigDTO desksoftUseConfigDTO = desksoftUseConfigNotifyAPI.getGlobalCmcStrategy();
        DesksoftUseConfigResponse desksoftUseConfigResponse = new DesksoftUseConfigResponse();
        BeanUtils.copyProperties(desksoftUseConfigDTO, desksoftUseConfigResponse);

        return DefaultWebResponse.Builder.success(desksoftUseConfigResponse);
    }

    /**
     * 获取所有局部显示模式策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/list")
    public DefaultWebResponse getAllPartDisplayStrategy(GetAllPartDisplayStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "GetAllPartDisplayStrategyWebRequest is null");
        // 数据转换为前端所需结构
        CbbPartDisplayStrategyDTO[] apiDTOArr = cbbGlobalStrategyMgmtAPI.getAllPartDisplayStrategy();
        GetPartDisplayStrategyResponse webResponse = createWebDataForPartDisplayStrategy(apiDTOArr);
        return DefaultWebResponse.Builder.success(webResponse);
    }

    private GetPartDisplayStrategyResponse createWebDataForPartDisplayStrategy(CbbPartDisplayStrategyDTO[] cbbPartDisplayStrategyDTOArr) {
        PartDisplayStrategyDTO[] itemArr = new PartDisplayStrategyDTO[cbbPartDisplayStrategyDTOArr.length];
        for (int i = 0; i < cbbPartDisplayStrategyDTOArr.length; i++) {
            PartDisplayStrategyDTO dto = new PartDisplayStrategyDTO();
            dto.setId(cbbPartDisplayStrategyDTOArr[i].getId());
            dto.setAppName(cbbPartDisplayStrategyDTOArr[i].getAppName());
            // 通过枚举code返回枚举给前端
            SpiceDisplayStrategyType displayStrategyType =
                    SpiceDisplayStrategyType.findEnumByCode(cbbPartDisplayStrategyDTOArr[i].getDisplayStrategyValue());
            dto.setDisplayStrategy(displayStrategyType);
            OperationControlType operationControlType =
                    OperationControlType.findEnumByCode(cbbPartDisplayStrategyDTOArr[i].getOperationControlValue());
            dto.setOperationStrategy(operationControlType);
            // 写入数据库中存的code值，而非枚举中的code值。且仅当操作控制值为CUSTOM，才返回值给前端。
            if (operationControlType == OperationControlType.CUSTOM) {
                dto.setOperationControlValue(cbbPartDisplayStrategyDTOArr[i].getOperationControlValue());
            } else {
                dto.setOperationControlValue(StringUtils.EMPTY);
            }
            itemArr[i] = dto;
        }
        GetPartDisplayStrategyResponse getPartDisplayStrategyResponse = new GetPartDisplayStrategyResponse();
        getPartDisplayStrategyResponse.setItemArr(itemArr);
        return getPartDisplayStrategyResponse;
    }

    /**
     * 新增局部显示模式策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/create")
    public DefaultWebResponse createPartDisplayStrategy(CreatePartDisplayStrategyWebRequest request)
            throws BusinessException {
        Assert.notNull(request, "CreatePartDisplayStrategyWebRequest is null");
        CbbCreatePartDisplayStrategyRequest apiRequest = new CbbCreatePartDisplayStrategyRequest();
        apiRequest.setAppName(request.getAppName());
        SpiceDisplayStrategyType displayStrategyType = SpiceDisplayStrategyType.valueOf(request.getDisplayStrategy());
        apiRequest.setDisplayStrategyValue(displayStrategyType.getCode());
        // 不能直接使用枚举中的code值，如果是自定义的操作控制，那么就使用前端的自定义值，否则使用枚举中对应的code值。
        OperationControlType operationControlType = OperationControlType.DEFAULT;
        apiRequest.setOperationControlValue(operationControlType.getCode());
        try {
            cbbGlobalStrategyMgmtAPI.createPartDisplayStrategy(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_CONFIG_SUCCESS, request.getAppName());
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("创建局部显示模式", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_CONFIG_FAIL, request.getAppName(),
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 删除局部显示模式策略
     *
     * @param request 请求参数
     * @param builder 批处理参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/delete")
    public DefaultWebResponse deletePartDisplayStrategy(DeletePartDisplayStrategyWebRequest request,
                                                        BatchTaskBuilder builder) throws BusinessException {
        Assert.notNull(request, "DeletePartDisplayStrategyWebRequest is null");
        Assert.notNull(builder, "builder is null");
        final UUID[] idArr = request.getIdArr();
        if (idArr.length == 1) {
            return deleteSingleRecord(idArr[0]);
        } else {
            final Iterator<DefaultBatchTaskItem> iterator = Stream.of(idArr).distinct()
                    .map(id -> DefaultBatchTaskItem.builder().itemId(id)
                            .itemName(LocaleI18nResolver.resolve(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_ITEM_NAME))
                            .build())
                    .iterator();
            PartDisplayDeleteBatchHandler handler = new PartDisplayDeleteBatchHandler(cbbGlobalStrategyMgmtAPI, iterator, auditLogAPI);

            BatchTaskSubmitResult result = builder.setTaskName(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_TASK_NAME)
                    .setTaskDesc(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_TASK_DESC).registerHandler(handler).start();
            return DefaultWebResponse.Builder.success(result);
        }
    }

    private DefaultWebResponse deleteSingleRecord(UUID id) throws BusinessException {
        String logName = getPartDisplayStrategyName(id);
        try {
            cbbGlobalStrategyMgmtAPI.deletePartDisplayStrategy(id);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_SUCCESS, logName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("异步删除局部显示策略", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_FAIL, logName, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }


    /**
     * 更新局部显示模式策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/edit")
    public DefaultWebResponse updatePartDisplayStrategy(UpdatePartDisplayStrategyWebRequet request) throws BusinessException {
        Assert.notNull(request, "UpdatePartDisplayStrategyWebRequet is null");
        CbbUpdatePartDisplayStrategyRequest apiRequest = new CbbUpdatePartDisplayStrategyRequest();
        apiRequest.setId(request.getId());
        apiRequest.setAppName(request.getAppName());
        SpiceDisplayStrategyType displayStrategyType = SpiceDisplayStrategyType.valueOf(request.getDisplayStrategy());
        // 使用枚举中对应的code值传给后台。
        apiRequest.setDisplayStrategyValue(displayStrategyType.getCode());
        OperationControlType operationControlType = OperationControlType.DEFAULT;
        apiRequest.setOperationControlValue(operationControlType.getCode());
        String currentPartAppName = obtainAppName(apiRequest.getId());
        try {
            cbbGlobalStrategyMgmtAPI.updatePartDisplayStrategy(apiRequest);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_UPDATE_SUCCESS, currentPartAppName);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑局部显示模式", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_UPDATE_FAIL, currentPartAppName,
                    e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    private String obtainAppName(UUID partDisplayStrategyId) {
        String appName = partDisplayStrategyId.toString();
        try {
            CbbPartDisplayStrategyDTO partDisplayStrategyDTO = cbbGlobalStrategyMgmtAPI.getPartDisplayStrategy(partDisplayStrategyId);
            appName = partDisplayStrategyDTO.getAppName();
        } catch (BusinessException e) {
            LOGGER.error("获取局部显示策略数据失败", e);
        }
        return appName;
    }

    /**
     * 获取局部显示策略
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/detail")
    public DefaultWebResponse getPartDisplayStrategy(GetPartDisplayStrategyWebRequest request) throws BusinessException {
        Assert.notNull(request, "GetPartDisplayStrategyWebRequest is null");
        CbbPartDisplayStrategyDTO cbbDto = cbbGlobalStrategyMgmtAPI.getPartDisplayStrategy(request.getId());
        PartDisplayStrategyDTO resultData = new PartDisplayStrategyDTO();
        resultData.setId(cbbDto.getId());
        resultData.setAppName(cbbDto.getAppName());
        resultData.setDisplayStrategy(SpiceDisplayStrategyType.findEnumByCode(cbbDto.getDisplayStrategyValue()));
        resultData.setOperationStrategy(OperationControlType.findEnumByCode(cbbDto.getOperationControlValue()));
        resultData.setOperationControlValue(cbbDto.getOperationControlValue());
        return DefaultWebResponse.Builder.success(resultData);
    }

    /**
     * 命名唯一性校验
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @RequestMapping("/partDisplay/checkDuplication")
    public DefaultWebResponse checkDuplication(PartDisplayCheckDuplicationWebRequest request) throws BusinessException {

        CbbPartDisplayStrategyDTO dto = cbbGlobalStrategyMgmtAPI.findByName(request.getAppName());
        CheckDuplicationWebResponse response = new CheckDuplicationWebResponse();
        response.setHasDuplication(false);
        if ((dto != null) && (!(request.getId() != null && request.getId().equals(dto.getId())))) {
            response.setHasDuplication(true);
        }
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 编辑VDI网段限制策略
     *
     * @param request 请求
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑VDI网段限制策略")
    @RequestMapping(value = "/ipLimit/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "编辑VDI登录限制网段")})
    public DefaultWebResponse editIpLimit(EditLimitRequest request) throws BusinessException {
        Assert.notNull(request, "request must not null");

        try {
            IpLimitStrategyDTO ipLimitStrategyDTO = editIpLimitRequestToDTO(request);
            ipLimitAPI.updateIpLimit(ipLimitStrategyDTO);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_IP_LIMIT_EDIT_SUCCESS);

            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException e) {
            LOGGER.error("编辑IP网段限制失败，失败原因：{}", e);
            auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_RCO_IP_LIMIT_EDIT_FAIL, e.getI18nMessage());
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_FAIL, e, e.getI18nMessage());
        }
    }

    /**
     * 获取VDI网段限制策略
     *
     * @param request 请求
     * @return VDI网段限制策略
     */
    @ApiOperation("获取VDI网段限制策略")
    @RequestMapping(value = "/ipLimit/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取VDI登录限制网段策略")})
    public DefaultWebResponse getIpLimit(DefaultWebRequest request) {
        Assert.notNull(request, "request must not null");

        IpLimitStrategyDTO response = ipLimitAPI.getIpLimitStrategy();
        return DefaultWebResponse.Builder.success(response);
    }

    private IpLimitStrategyDTO editIpLimitRequestToDTO(EditLimitRequest request) throws BusinessException {
        List<IpLimitDTO> ipLimitDTOList = request.getIpLimitDTOList();
        if (!CollectionUtils.isEmpty(ipLimitDTOList) && ipLimitDTOList.size() > Constants.IP_LIMIT_MAX_SIZE) {
            throw new BusinessException(CloudDesktopBusinessKey.RCDC_RCO_IP_LIMIT_TOO_MANY, String.valueOf(Constants.IP_LIMIT_MAX_SIZE));
        }
        return new IpLimitStrategyDTO(request.getEnableIpLimit(), ipLimitDTOList);
    }

    /**
     * 将web中request参数转换成API中request。
     *
     * @param request 请求参数
     */
    private CbbConfigureGlobalStrategyRequest webRequestToAPIRequest(ConfigureGlobalStrategyWebRequest request) {
        CbbConfigureGlobalStrategyRequest cbbConfigureGlobalStrategyRequest = new CbbConfigureGlobalStrategyRequest();

        // 前端已经放弃这个功能，这边插入默认值，减少修改量
        cbbConfigureGlobalStrategyRequest.setEnableAppDisplayMode(Boolean.TRUE);
        cbbConfigureGlobalStrategyRequest.setGlobalOperationControl(OperationControlType.DEFAULT.getCode());

        cbbConfigureGlobalStrategyRequest.setEnableAudioCompression(request.getProtocolTransferConfig().getEnableAudioCompression());
        cbbConfigureGlobalStrategyRequest.setEnableQualityEnhance(request.getProtocolTransferConfig().getEnableQualityEnhance());
        cbbConfigureGlobalStrategyRequest.setDisplayQuality(request.getProtocolTransferConfig().getDisplayQuality());
        cbbConfigureGlobalStrategyRequest.setEnableEncryption(request.getProtocolTransferConfig().getEnableEncryption());
        cbbConfigureGlobalStrategyRequest.setFrameRate(request.getProtocolTransferConfig().getFrameRate());
        cbbConfigureGlobalStrategyRequest.setSamplingRate(request.getProtocolTransferConfig().getSamplingRate());
        return cbbConfigureGlobalStrategyRequest;
    }


    /**
     * 拼接前端所需数据结构
     *
     * @param globalStrategyDTO api响应参数
     * @return GetGlobalStrategyWebVO 最终数据结构
     */
    private GetGlobalStrategyWebVO constractGlobalStrategyVO(CbbGlobalStrategyDTO globalStrategyDTO) {
        GetGlobalStrategyWebVO vo = new GetGlobalStrategyWebVO();
        ProtocolTransferConfigDTO protocolTransferConfig = new ProtocolTransferConfigDTO();
        protocolTransferConfig.setDisplayQuality(globalStrategyDTO.getDisplayQuality());
        protocolTransferConfig.setEnableAudioCompression(globalStrategyDTO.getEnableAudioCompression());
        protocolTransferConfig.setEnableQualityEnhance(globalStrategyDTO.getEnableQualityEnhance());
        protocolTransferConfig.setEnableEncryption(globalStrategyDTO.getEnableEncryption());
        protocolTransferConfig.setFrameRate(globalStrategyDTO.getFrameRate());
        protocolTransferConfig.setSamplingRate(globalStrategyDTO.getSamplingRate());
        AppDisplayModeStrategyDTO appDisplayModeStrategy = new AppDisplayModeStrategyDTO();
        appDisplayModeStrategy.setEnableAppDisplayMode(globalStrategyDTO.getEnableAppDisplayMode());
        vo.setProtocolTransferConfig(protocolTransferConfig);
        vo.setAppLayerHidden(globalStrategyDTO.getAppLayerHidden());
        return vo;
    }

    /**
     * Description: 局部显示策略批量删除Handler
     * Copyright: Copyright (c) 2019
     * Company: Ruijie Co., Ltd.
     * Create Time: 2019年1月17日
     *
     * @author Ghang
     */
    protected class PartDisplayDeleteBatchHandler extends AbstractBatchTaskHandler {

        private BaseAuditLogAPI auditLogAPI;

        private final CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI;

        protected PartDisplayDeleteBatchHandler(CbbGlobalStrategyMgmtAPI cbbGlobalStrategyMgmtAPI, Iterator<? extends BatchTaskItem> iterator,
                                                BaseAuditLogAPI auditLogAPI) {
            super(iterator);
            this.cbbGlobalStrategyMgmtAPI = cbbGlobalStrategyMgmtAPI;
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                    CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_BATCH_DELETE_RESULT);
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            Assert.notNull(item, "BatchTaskItem is null");
            String logName = getPartDisplayStrategyName(item.getItemID());
            try {
                cbbGlobalStrategyMgmtAPI.deletePartDisplayStrategy(item.getItemID());
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_SUCCESS, logName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_SUCCESS).msgArgs(new String[]{logName})
                        .build();
            } catch (BusinessException e) {
                LOGGER.error("异步删除局部显示策略", e);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_FAIL, logName, e.getI18nMessage());
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_PART_DISPLAY_STRATEGY_DELETE_FAIL, e, logName,
                        e.getI18nMessage());
            }

        }
    }

    private String getPartDisplayStrategyName(UUID id) {
        String strategyName = id.toString();
        try {
            CbbPartDisplayStrategyDTO partDisplayStrategyDTO = cbbGlobalStrategyMgmtAPI.getPartDisplayStrategy(id);
            strategyName = partDisplayStrategyDTO.getAppName();
        } catch (Exception e) {
            LOGGER.error("获取局部显示策略名称", e);
        }
        return strategyName;
    }

    /**
     * 获取协议配置模板列表
     *
     * @param request 请求
     * @return 协议配置模板列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取协议配置模板列表")
    @RequestMapping(value = "/agreement/template/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = "获取协议配置模板列表")})
    public DefaultWebResponse getAgreementTemplate(EstConfigRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        CbbAgreementTemplateDTO agreementTemplate = cbbGlobalStrategyMgmtAPI.getAgreementTemplate(request.getProtocolType());
        AgreementTemplateVO agreementTemplateVO = new AgreementTemplateVO();
        agreementTemplateVO.setLanTemplateList(handleAgreementTemplate(agreementTemplate.getLanTemplateList()));
        agreementTemplateVO.setWanTemplateList(handleAgreementTemplate(agreementTemplate.getWanTemplateList()));
        return DefaultWebResponse.Builder.success(agreementTemplateVO);
    }

    private List<AgreementConfigRequestDTO> handleAgreementTemplate(List<CbbHestConfigDTO> configList) {
        List<AgreementConfigRequestDTO> agreementConfigList = new ArrayList<>();
        if (CollectionUtils.isEmpty(configList)) {
            return agreementConfigList;
        }
        for (CbbHestConfigDTO hestConfig : configList) {
            agreementConfigList.add(buildEstConfig(hestConfig));
        }
        return agreementConfigList;
    }

    private AgreementConfigRequestDTO buildEstConfig(CbbHestConfigDTO estConfig) {
        AgreementConfigRequestDTO agreementConfig = new AgreementConfigRequestDTO();
        BeanUtils.copyProperties(estConfig, agreementConfig);
        if (estConfig.getWebAdvanceSettingInfo() != null) {
            agreementConfig.setWebAdvanceSettingInfo(JSON.toJSONString(estConfig.getWebAdvanceSettingInfo()));
        }
        return agreementConfig;
    }

    /**
     * 转换成API中request。
     *
     * @param globalStrategyDTO 请求参数
     */
    private CbbConfigureGlobalStrategyRequest defaultInfoToAPIRequest(CbbGlobalStrategyDTO globalStrategyDTO) {
        CbbConfigureGlobalStrategyRequest cbbConfigureGlobalStrategyRequest = new CbbConfigureGlobalStrategyRequest();
        cbbConfigureGlobalStrategyRequest.setEnableAppDisplayMode(globalStrategyDTO.getEnableAppDisplayMode());
        // 此处需要把全局控制策略GlobalOperationControl转换为对应的枚举code值。
        if (globalStrategyDTO.getEnableAppDisplayMode()) {
            OperationControlType operationControlType = OperationControlType.DEFAULT;
            cbbConfigureGlobalStrategyRequest.setGlobalOperationControl(operationControlType.getCode());
        }
        cbbConfigureGlobalStrategyRequest.setEnableAudioCompression(globalStrategyDTO.getEnableAudioCompression());
        cbbConfigureGlobalStrategyRequest.setEnableQualityEnhance(globalStrategyDTO.getEnableQualityEnhance());
        cbbConfigureGlobalStrategyRequest.setDisplayQuality(globalStrategyDTO.getDisplayQuality());
        cbbConfigureGlobalStrategyRequest.setEnableEncryption(globalStrategyDTO.getEnableEncryption());
        cbbConfigureGlobalStrategyRequest.setFrameRate(globalStrategyDTO.getFrameRate());
        cbbConfigureGlobalStrategyRequest.setSamplingRate(globalStrategyDTO.getSamplingRate());
        cbbConfigureGlobalStrategyRequest.setEnableTwoStream(globalStrategyDTO.getEnableTwoStream());
        return cbbConfigureGlobalStrategyRequest;
    }

    /**
     * 获取硬件特征码、动态口令、CAS认证开关
     * 
     * @return 操作结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取硬件特征码、动态口令、CAS认证开关")
    @RequestMapping(value = "/getCertificationStrategy", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = "获取硬件特征码、动态口令、CAS认证开关")})
    public DefaultWebResponse getCertificationStrategy() throws BusinessException {
        IacHardwareCertificationDTO hardwareCertification = hardwareCertificationAPI.getHardwareCertification();
        IacMfaConfigDTO mfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
        CasScanCodeAuthDTO casScanCodeAuthInfo = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo();
        IacSmsCertificationDTO smsCertificationDTO = smsCertificationAPI.getBusSmsCertificationStrategy();
        Boolean enableRadius = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable();
        CertificationStrategyVO response = new CertificationStrategyVO();
        response.setOpenOtp(mfaConfigDTO.getEnableMfa());
        response.setOpenCas(casScanCodeAuthInfo.getApplyOpen());
        response.setOpenHardware(hardwareCertification.getOpenHardware());
        response.setOpenSmsCertification(smsCertificationDTO.getEnable());
        response.setOpenRadiusCertification(BooleanUtils.isTrue(enableRadius));
        response.setOpenThirdPartyCertification(cbbThirdPartyUserAPI.getThirdPartyAuthState());
        return DefaultWebResponse.Builder.success(response);
    }


    /**
     * 编辑自动扩容全局配置
     *
     * @param webRequest 请求参数
     * @return DefaultWebResponse 响应参数
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑自动扩容全局配置")
    @RequestMapping(value = "/fullSystemDisk/edit", method = RequestMethod.POST)
    public DefaultWebResponse editFullSystemDiskConfig(EditFullSystemDiskRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest must not be null");

        Boolean enableFullSystemDisk = enableFullSystemDiskAPI.getEnableFullSystemDisk();
        // 不允许关闭。如果数据库中的配置为true，前端传的为false，则证明是进行关闭操作
        if (Boolean.TRUE.equals(enableFullSystemDisk) && Boolean.FALSE.equals(webRequest.getEnableFullSystemDisk())) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_EDIT_FULL_SYSTEM_DISK_FAIL);
        }

        enableFullSystemDiskAPI.editEnableFullSystemDisk(webRequest.getEnableFullSystemDisk());
        auditLogAPI.recordLog(GlobalStrategyBussinessKey.RCDC_EDIT_FULL_SYSTEM_DISK_SUCCESS);

        return DefaultWebResponse.Builder.success(GlobalStrategyBussinessKey.RCDC_EDIT_FULL_SYSTEM_DISK_SUCCESS, new String[]{});
    }

    /**
     * 获取自动扩容全局配置
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取自动扩容全局配置")
    @RequestMapping(value = "/fullSystemDisk/detail", method = RequestMethod.POST)
    public DefaultWebResponse getFullSystemDiskConfig(DefaultWebRequest request) {
        Assert.notNull(request, "request must not be null");

        Boolean enableFullSystemDisk = enableFullSystemDiskAPI.getEnableFullSystemDisk();
        GetEnableFullSystemDiskResponse webResponse = new GetEnableFullSystemDiskResponse();
        webResponse.setEnableFullSystemDisk(enableFullSystemDisk);
        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 编辑VDI授权
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("编辑VDI授权兼容")
    @RequestMapping(value = "/auth/edit")
    public DefaultWebResponse authEdit(AuthCompatibleVO request) {
        Assert.notNull(request, "request must not be null");

        globalParameterAPI.updateParameter(RcoGlobalParameterConstants.ENABLE_AUTH_COMPATIBLE, String.valueOf(request.getEnableAuthCompatible()));
        auditLogAPI.recordLog(request.getEnableAuthCompatible() ? GlobalStrategyBussinessKey.RCDC_OPEN_ENABLE_AUTH_COMPATIBLE_SUCCESS
                : GlobalStrategyBussinessKey.RCDC_CLOSE_ENABLE_AUTH_COMPATIBLE_SUCCESS);
        cbbLicenseCenterAPI.reloadLicenseConfig();
        return DefaultWebResponse.Builder
                .success(request.getEnableAuthCompatible() ? GlobalStrategyBussinessKey.RCDC_OPEN_ENABLE_AUTH_COMPATIBLE_SUCCESS
                        : GlobalStrategyBussinessKey.RCDC_CLOSE_ENABLE_AUTH_COMPATIBLE_SUCCESS, new String[]{});
    }

    /**
     * 查询VDI授权
     *
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("查询VDI授权兼容")
    @RequestMapping(value = "/auth/detail")
    public DefaultWebResponse detail() {
        final String parameterResponse = globalParameterAPI.findParameter(RcoGlobalParameterConstants.ENABLE_AUTH_COMPATIBLE);
        AuthCompatibleVO authCompatibleVO = new AuthCompatibleVO();
        authCompatibleVO.setEnableAuthCompatible(BooleanUtils.toBoolean(parameterResponse));
        return DefaultWebResponse.Builder.success(authCompatibleVO);
    }

    /**
     * 查詢全局镜像同步策略配置
     *
     * @return 配置详情
     * @throws BusinessException 查询业务异常
     */
    @ApiOperation("镜像同步全局配置详情")
    @RequestMapping(value = "/imageSync/detail")
    public DefaultWebResponse getImageSyncStrategy() throws BusinessException {
        CbbImageSyncStrategyDTO imageSyncStrategy = imageStrategyAPI.getImageSyncStrategy();
        return DefaultWebResponse.Builder.success(imageSyncStrategy);
    }

    /**
     * 更新全局镜像同步策略
     *
     * @param strategyDTO 策略配置
     * @return 保存结果
     * @throws BusinessException 保存失败，可能是校验问题或存在正在执行的任务
     */
    @ApiOperation("更新镜像同步全局配置")
    @RequestMapping(value = "/imageSync/edit")
    public DefaultWebResponse updateImageSyncStrategy(CbbImageSyncStrategyDTO strategyDTO) throws BusinessException {
        Assert.notNull(strategyDTO, "imageSyncStrategyDTO cannot be null");

        try {
            CbbImageSyncStrategyDTO oldStrategy = imageStrategyAPI.getImageSyncStrategy();
            String successTip = resolveImageSyncStrategyChangeTip(oldStrategy, strategyDTO);
            imageStrategyAPI.updateImageSyncStrategy(strategyDTO);
            auditLogAPI.recordLog(GlobalStrategyBussinessKey.RCDC_IMAGE_SYNC_STRATEGY_OP_SUCCESS_LOG, successTip);
            return DefaultWebResponse.Builder.success(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_MODULE_OPERATE_SUCCESS, new String[]{});
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(GlobalStrategyBussinessKey.RCDC_IMAGE_SYNC_STRATEGY_OP_FAIL_LOG, ex, ex.getI18nMessage());
            throw ex;
        }
    }

    private String resolveImageSyncStrategyChangeTip(CbbImageSyncStrategyDTO oldStrategy, CbbImageSyncStrategyDTO newStrategy) {
        StringBuilder tipBuilder = new StringBuilder();
        if (!Objects.equals(oldStrategy.getMaxQos(), newStrategy.getMaxQos())) {
            String qosChangeLog = LocaleI18nResolver.resolve(GlobalStrategyBussinessKey.RCDC_IMAGE_SYNC_QOS_LOG, //
                    resolveQosValueTip(oldStrategy.getMaxQos()), resolveQosValueTip(newStrategy.getMaxQos()));
            tipBuilder.append(qosChangeLog);
        }
        if (!Objects.equals(oldStrategy.getMaxSyncNum(), newStrategy.getMaxSyncNum())) {
            String syncNumChangeLog = LocaleI18nResolver.resolve(GlobalStrategyBussinessKey.RCDC_IMAGE_SYNC_TASK_NUM_LOG, //
                    Objects.toString(oldStrategy.getMaxSyncNum()), Objects.toString(newStrategy.getMaxSyncNum()));
            tipBuilder.append(syncNumChangeLog);
        }
        return tipBuilder.toString();
    }

    private String resolveQosValueTip(Integer qosValue) {
        if (Objects.equals(0, qosValue)) {
            return LocaleI18nResolver.resolve(RCDC_IMAGE_SYNC_QOS_NOT_LIMIT);
        }

        return Objects.toString(qosValue);
    }

    /**
     * 获取透明加解密受控软件列表
     *
     * @param request 请求
     * @return 受控软件列表
     * @throws BusinessException 业务异常
     */
    @ApiOperation("获取透明加解密受控软件列表")
    @RequestMapping(value = "/encryption/software/list", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = "获取透明加解密受控软件列表")})
    public DefaultWebResponse getEncryptionSoftwareList(EncryptionSoftwareListRequest request) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        PageQueryResponse<CbbEncryptionSoftwareDTO> response = cbbGlobalStrategyMgmtAPI.getEncryptionSoftwareList(request.getType());
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * 保存透明加解密的密钥
     *
     * @param request 请求
     * @param session session
     * @return 结果
     * @throws BusinessException 业务异常
     */
    @ApiOperation("保存透明加解密的密钥")
    @RequestMapping(value = "/encryption/save", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = "保存透明加解密的密钥")})
    public DefaultWebResponse saveEncryption(SaveEncryptionRequest request, SessionContext session) throws BusinessException {
        Assert.notNull(request, "request cannot be null!");
        Assert.notNull(session, "session cannot be null!");
        // admin 账号才有权限保存
        if (!permissionHelper.isAdminName(session)) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_RCO_NOT_DEFAULT_ADMIN_NOT_ALLOW);
        }
        // VDI服务器才支持保存
        if (!serverModelAPI.isVdiModel()) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_RCO_ENCRYPTION_NOT_SUPPORT_SERVER_MODEL);
        }

        String keyStr = AesUtil.descrypt(request.getSeed(), RedLineUtil.getRealAdminRedLine());
        if (!Pattern.matches(CertificationStrategyRegexEnum.PATTERN_THREE.getPattern(), keyStr)) {
            throw new BusinessException(GlobalStrategyBussinessKey.RCDC_RCO_SAVE_ENCRYPTION_KEY_UNABLE_REQUIRE);
        }
        cbbGlobalStrategyMgmtAPI.saveEncryption(request.getSeed(), request.getRestore());
        auditLogAPI.recordLog(GlobalStrategyBussinessKey.RCDC_RCO_SAVE_ENCRYPTION);
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 查询透明加解密的密钥
     *
     * @return true:启用 false:停用
     */
    @ApiOperation("查询透明加解密的密钥")
    @RequestMapping(value = "/encryption/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V6_0_1, descriptions = "查询透明加解密的密钥")})
    public DefaultWebResponse encryptionDetail() {
        List<CbbEncryptionKeyDTO> keyList = cbbGlobalStrategyMgmtAPI.queryEncryptionList();
        EncryptionDetailResponse response = new EncryptionDetailResponse();
        boolean enable = keyList.stream().anyMatch(CbbEncryptionKeyDTO::isEnable);
        response.setEnable(enable);
        return DefaultWebResponse.Builder.success(response);
    }

    /**
     * @return 会话端口详情
     */
    @ApiOperation("会话端口配置")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"会话端口配置"})})
    @RequestMapping(value = "/sessionPort/detail", method = RequestMethod.POST)
    public DefaultWebResponse sessionPortDetail() {
        SessionPortConfigDTO sessionPortConfigDTO = cbbSessionPortStrategyAPI.getStrategyDetail();
        return DefaultWebResponse.Builder.success(sessionPortConfigDTO);
    }

    /**
     * @param sessionPortConfigDTO 会话端口配置
     * @return 编辑结果
     */
    @ApiOperation("编辑会话端口配置")
    @ApiVersions({@ApiVersion(value = Version.V6_0_20, descriptions = {"编辑会话端口配置"})})
    @RequestMapping(value = "/sessionPort/edit", method = RequestMethod.POST)
    public DefaultWebResponse sessionPortDetail(SessionPortConfigDTO sessionPortConfigDTO) {
        Boolean isUpdated = cbbSessionPortStrategyAPI.updateStrategy(sessionPortConfigDTO);
        if (Boolean.TRUE.equals(isUpdated)) {
            //通知应用主机
            NotifyConfigChangeDTO notifyConfigChangeDTO = new NotifyConfigChangeDTO();
            OneAgentGlobalConfigDTO oneAgentGlobalConfigDTO = new OneAgentGlobalConfigDTO();
            oneAgentGlobalConfigDTO.setSessionServicePort(sessionPortConfigDTO.getSessionServicePort());
            oneAgentGlobalConfigDTO.setEstConnectPortBegin(sessionPortConfigDTO.getEstConnectPortBegin());
            oneAgentGlobalConfigDTO.setEstConnectPortEnd(sessionPortConfigDTO.getEstConnectPortEnd());
            oneAgentGlobalConfigDTO.setServerTime(System.currentTimeMillis());
            notifyConfigChangeDTO.setConfig(oneAgentGlobalConfigDTO);
            notifyConfigChangeDTO.setNotifyComponentScope(RcaEnum.NotifyComponentScope.ONE_AGENT);
            notifyConfigChangeDTO.setNotifyTargetList(NotifyTargetDTO.addGlobalTarget(Lists.newArrayList()));
            notifyConfigChangeDTO.setApiAction(RcaApiActionConstant.CDC_NOTIFY_OA_GLOBAL_CONFIG);
            try {
                rcaNotifyAPI.notifyConfigChange(notifyConfigChangeDTO);
            } catch (Exception e) {
                LOGGER.error("通知oa会话端口配置失败", e);
            }
            return DefaultWebResponse.Builder.success(
                    com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_UPDATE_SESSION_PORT_CONFIG_SUCCESS,
                    new String[]{});
        } else {
            return DefaultWebResponse.Builder.success(
                    com.ruijie.rcos.rcdc.clouddesktop.module.def.BusinessKey.RCDC_CLOUDDESKTOP_UPDATE_SESSION_PORT_CONFIG_NOT_NECESSARY,
                    new String[]{});
        }
    }
}
