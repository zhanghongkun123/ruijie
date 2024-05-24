package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.sysmanage.module.def.api.BaseLicenseMgmtAPI;
import com.ruijie.rcos.base.sysmanage.module.def.api.request.license.BaseLicenseListRequest;
import com.ruijie.rcos.base.sysmanage.module.def.dto.license.BaseLicenseFeatureDTO;
import com.ruijie.rcos.base.sysmanage.module.def.enums.BaseFeatureType;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.maintenance.module.def.annotate.NoBusinessMaintenanceUrl;
import com.ruijie.rcos.rcdc.rco.module.def.annotation.ServerModel;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoTrialLicenseAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ConfigurationWizardAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesksoftUseConfigNotifyAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.GetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SetConfigurationWizardRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.GetConfigurationWizardResponse;
import com.ruijie.rcos.rcdc.rco.module.def.configuration.enums.GlobalConfigItem;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.GetConfigurationWizardWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rco.request.SetConfigurationWizardWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sysmanage.SysmanagerBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.CreateTempRcdcLicenseWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.request.RcdcAuthorizedDetailWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.response.RcdcAuthorizedDetailWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import com.ruijie.rcos.sk.webmvc.api.request.DefaultWebRequest;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;

/**
 * Description: 配置向导WEB接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年4月24日
 *
 * @author zouqi
 */
@Api(tags = "配置向导")
@Controller
@RequestMapping("/rco/configWizard")
public class ConfigWizardController {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigWizardController.class);

    private static final int PAGE_LIMIT = 300;

    private static final int PAGE = 0;

    @Autowired
    private BaseLicenseMgmtAPI baseLicenseMgmtAPI;

    @Autowired
    private AutoTrialLicenseAPI autoTrialLicenseAPI;

    @Autowired
    private ConfigurationWizardAPI configurationWizardAPI;

    @Autowired
    private AdminMgmtAPI adminMgmtAPI;

    @Autowired
    private AutoLogonAPI autoLogonAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private DesksoftUseConfigNotifyAPI desksoftUseConfigNotifyAPI;

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    /**
     * 创建Rcdc临时授权30天
     *
     * @param webRequest 参数
     * @return 结果
     * @throws BusinessException BusinessException
     */
    @ServerModel
    @ApiOperation("创建Rcdc临时授权30天")
    @RequestMapping(value = "createTempRcdcLicense", method = RequestMethod.POST)
    public CommonWebResponse createTempRcdcLicense(CreateTempRcdcLicenseWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        autoTrialLicenseAPI.autoTrialLicense();
        return CommonWebResponse.success();
    }

    /**
     * 创建IDV终端临时授权
     *
     * @param webRequest 参数
     * @return 结果
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "createIdvTempRcdcLicense")
    public DefaultWebResponse createIdvTempRcdcLicense(CreateTempRcdcLicenseWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is null");
        LOGGER.info("接收到自动授权请求，进行IDV授权 webRequest:[{}]", JSON.toJSONString(webRequest));
        autoTrialLicenseAPI.idvAutoTrialLicense();
        LOGGER.info("接收到自动授权请求，进行VOI授权 webRequest:[{}]", JSON.toJSONString(webRequest));
        autoTrialLicenseAPI.voiAutoTrialLicense();

        LOGGER.info("接收到自动授权请求，进行MINI服务器生成内置证书，webRequest:[{}]", JSON.toJSONString(webRequest));
        autoTrialLicenseAPI.generateMiniInternalLicense();
        return DefaultWebResponse.Builder.success();
    }

    /**
     * 判断系统是否授权授权
     *
     * @param webRequest 参数
     * @return 结果
     * @throws BusinessException BusinessException
     */
    @RequestMapping(value = "detail")
    public DefaultWebResponse isAuthorization(RcdcAuthorizedDetailWebRequest webRequest) throws BusinessException {

        Assert.notNull(webRequest, "RcdcAuthorizedDetailWebRequest is null");
        RcdcAuthorizedDetailWebResponse webResponse = new RcdcAuthorizedDetailWebResponse();

        BaseLicenseListRequest apiRequest = new BaseLicenseListRequest();
        apiRequest.setLimit(PAGE_LIMIT);
        apiRequest.setPage(PAGE);
        DefaultPageResponse<BaseLicenseFeatureDTO> response = baseLicenseMgmtAPI.listLicense(apiRequest);
        BaseLicenseFeatureDTO[] licenseArr = response.getItemArr();
        webResponse.setAuthorizationForPerpetual(false);
        boolean isAuthorization = Arrays.stream(licenseArr).anyMatch(dto -> dto.getFeatureType() == BaseFeatureType.PERPETUAL);
        webResponse.setAuthorizationForPerpetual(isAuthorization);

        //是否临时授权


        webResponse.setAuthorizationForTemporary(true);

        return DefaultWebResponse.Builder.success(webResponse);
    }

    /**
     * 获取当前配置向导信息
     *
     * @param webRequest webRequest
     * @return 配置信息
     */
    @RequestMapping("get")
    @NoBusinessMaintenanceUrl
    public DefaultWebResponse getConfigurationWizard(GetConfigurationWizardWebRequest webRequest) {
        Assert.notNull(webRequest, "webRequest is not null");
        final GetConfigurationWizardRequest request = new GetConfigurationWizardRequest();

        GetConfigurationWizardResponse configurationWizard = configurationWizardAPI.getConfigurationWizard(request);

        return DefaultWebResponse.Builder.success(configurationWizard);
    }

    /**
     * 修改配置向导
     *
     * @param webRequest webRequest
     * @return 空
     * @throws BusinessException 异常
     */
    @RequestMapping("set")
    public DefaultWebResponse setConfigurationWizard(SetConfigurationWizardWebRequest webRequest) throws BusinessException {
        Assert.notNull(webRequest, "webRequest is not null");

        // 查询数据库当前配置
        GetConfigurationWizardResponse configurationWizard = configurationWizardAPI.getConfigurationWizard(new GetConfigurationWizardRequest());
        // 已经部署完成过,返回操作失败
        if (Boolean.FALSE.equals(configurationWizard.getShow())) {
            throw new BusinessException(SysmanagerBusinessKey.BASE_SYS_MANAGE_OPERATOR_FAIL);
        }

        final SetConfigurationWizardRequest request = new SetConfigurationWizardRequest();
        request.setIndex(webRequest.getIndex());
        request.setShow(webRequest.getShow());
        request.setIsJoinUserExperiencePlan(webRequest.getIsJoinUserExperiencePlan());
        request.setCustomDataArr(webRequest.getCustomDataArr());
        configurationWizardAPI.setConfigurationWizard(request);

        // 用户协议和隐私协议更新阅读
        globalParameterAPI.updateParameter(GlobalConfigItem.USER_PROTOCOL.getValue(), String.valueOf(Boolean.TRUE));

        // 审计日志
        // 取消加入用户体验计划
        if (configurationWizard.getIsJoinUserExperiencePlan() && !webRequest.getIsJoinUserExperiencePlan()) {
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_CANCEL_JOIN_USER_EXPERIENCE_PLAN);
            // 关闭CMC配置并通知云桌面
            desksoftUseConfigNotifyAPI.updateConfig(Boolean.FALSE.toString());
        } else if (!configurationWizard.getIsJoinUserExperiencePlan() && webRequest.getIsJoinUserExperiencePlan()) {
            // 加入用户体验计划
            auditLogAPI.recordLog(RcoBusinessKey.RCDC_RCO_JOIN_USER_EXPERIENCE_PLAN);
        }

        return DefaultWebResponse.Builder.success();
    }


    /**
     * 初始化角色、管理员信息
     *
     * @param request request
     * @return 空
     * @throws BusinessException 异常
     */
    @RequestMapping("initAdminInfo")
    public DefaultWebResponse setConfigurationWizard(DefaultWebRequest request) throws BusinessException {
        Assert.notNull(request, "request is not null");

        // 前端可以不请求，由身份中心初始化
        return DefaultWebResponse.Builder.success();
    }

}
