package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsCertificationDTO;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.GlobalStrategyBussinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request.SmsCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms.request.SmsPwdRecoverStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Description: 短信认证控制层
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
@Api(tags = "短信认证认证策略配置")
@Controller
@RequestMapping("/rco/smsCertification")
public class SmsCertificationCtrl {

    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;


    /**
     * 编辑短信认证策略
     * 
     * @param request 请求参数
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑短信认证策略")
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"全局策略-登录认证配置-辅助认证-短信认证，编辑短信认证策略"})})
    @EnableAuthority
    public CommonWebResponse<?> editSmsCertification(SmsCertificationRequest request) throws BusinessException {
        Assert.notNull(request, "request can be not null");
        // 开启多集群统一登录功能，不允许变更动态口令策略
        if (!rccmManageAPI.canModifyGlobalSettings()) {
            return CommonWebResponse.fail(GlobalStrategyBussinessKey.RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING);
        }
        IacSmsCertificationDTO smsCertificationDTO = new IacSmsCertificationDTO();
        BeanUtils.copyProperties(request, smsCertificationDTO);
        smsCertificationAPI.editSmsCertificationStrategy(smsCertificationDTO);
        auditLogAPI.recordLog(SmsBusinessKey.RCDC_RCO_UPDATE_SMS_CERTIFICATION_SUCCESS);
        return CommonWebResponse.success(SmsBusinessKey.RCDC_RCO_UPDATE_SMS_CERTIFICATION_SUCCESS, new String[] {});
    }

    /**
     * 编辑密码找回策略
     * 
     * @param request 请求参数
     * @return CommonWebResponse
     * @throws BusinessException 业务异常
     */
    @ApiOperation("编辑密码找回策略")
    @RequestMapping(value = "/pwdRecover/edit", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"全局策略-登录认证配置-辅助认证-短信认证，编辑密码找回策略"})})
    @EnableAuthority
    public CommonWebResponse<?> editPwdRecoverStrategy(SmsPwdRecoverStrategyRequest request) throws BusinessException {
        Assert.notNull(request, "request can be not null");
        // 开启多集群统一登录功能，不允许变更动态口令策略
        if (!rccmManageAPI.canModifyGlobalSettings()) {
            return CommonWebResponse.fail(GlobalStrategyBussinessKey.RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING);
        }
        IacSmsPwdRecoverDTO pwdRecoverDTO = new IacSmsPwdRecoverDTO();
        BeanUtils.copyProperties(request, pwdRecoverDTO);
        smsCertificationAPI.editSmsPwdRecoverStrategy(pwdRecoverDTO);
        auditLogAPI.recordLog(SmsBusinessKey.RCDC_RCO_UPDATE_SMS_PWD_RECOVER_SUCCESS);
        return CommonWebResponse.success(SmsBusinessKey.RCDC_RCO_UPDATE_SMS_PWD_RECOVER_SUCCESS, new String[] {});
    }
}
