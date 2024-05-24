package com.ruijie.rcos.rcdc.rco.module.web.ctrl.assistcertification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSecurityStrategyAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacThirdPartyCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacMfaConfigDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.request.bigscreen.response.AssistCertificationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: 辅助认证策略配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author lihengjing
 */
@Api(tags = "动态口令认证策略配置")
@Controller
@RequestMapping("/rco/assistCertification")
public class AssistCertificationCtrl {

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacThirdPartyCertificationAPI thirdPartyCertificationAPI;

    @Autowired
    private IacSecurityStrategyAPI iacSecurityPolicyAPI;

    /**
     * 获取认证策略配置
     * 
     * @throws BusinessException 业务异常
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation(value = "获取辅助认证策略配置")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取辅助认证策略配置"})})
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<AssistCertificationWebResponse> qryCertificationStrategy() throws BusinessException {
        IacMfaConfigDTO mfaConfigDTO = iacSecurityPolicyAPI.getMfaConfig(SubSystem.CDC);
        IacHardwareCertificationDTO hardwareDTO = hardwareCertificationAPI.getHardwareCertification();
        IacSmsCertificationDTO busSmsCertificationStrategyDTO = smsCertificationAPI.getBusSmsCertificationStrategy();
        Boolean enableRadius = thirdPartyCertificationAPI.getThirdPartyCertificationConfig(SubSystem.CDC).getEnable();
        AssistCertificationWebResponse response = new AssistCertificationWebResponse();
        response.setHasOtpCodeTab(mfaConfigDTO.getEnableMfaLogin());
        response.setOpenOtp(mfaConfigDTO.getEnableMfa());
        response.setOpenHardware(hardwareDTO.getOpenHardware());
        response.setOpenSmsCertification(busSmsCertificationStrategyDTO.getEnable());
        response.setOpenRadiusCertification(enableRadius);
        return CommonWebResponse.success(response);
    }

}
