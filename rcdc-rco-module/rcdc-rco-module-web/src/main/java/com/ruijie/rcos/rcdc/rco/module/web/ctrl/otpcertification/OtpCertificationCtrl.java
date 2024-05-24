package com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.CloudPlatformManageAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.api.request.CloudPlatformBaseRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.NtpMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.NtpResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.base.iac.module.enums.SubSystem;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.OtpCertificationAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.otpcertification.dto.OtpCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.GlobalStrategyBussinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.request.OtpCertificationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.otpcertification.response.OtpCertificationWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import io.swagger.annotations.Api;

import java.util.UUID;

/**
 *
 * Description: 动态口令认证策略配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author lihengjing
 */
@Api(tags = "动态口令认证策略配置")
@Controller
@RequestMapping("/rco/otpCertification")
public class OtpCertificationCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(OtpCertificationCtrl.class);

    @Autowired
    private OtpCertificationAPI otpCertificationAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    @Autowired
    private NtpMgmtAPI ntpMgmtAPI;

    public static final String NEGATIVE = "-";


    /**
     * 获取认证策略配置
     *
     * @return DefaultWebResponse 响应参数
     */
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"获取辅助认证策略配置"})})
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<OtpCertificationWebResponse> qryCertificationStrategy() {
        OtpCertificationDTO otpDTO = otpCertificationAPI.getOtpCertification();
        LOGGER.info("动态口令全局配置信息为[{0}]", JSON.toJSONString(otpDTO));
        OtpCertificationWebResponse response = new OtpCertificationWebResponse();
        response.setOpenMfaLogin(otpDTO.getHasOtpCodeTab());
        response.setEnableMfa(otpDTO.getOpenOtp());
        response.setRefreshSeconds(otpDTO.getPeriod());
        response.setSubSystem(SubSystem.CDC);
        response.setOtpType(otpDTO.getOtpType());
        response.setSystemHost(otpDTO.getSystemHost());
        response.setSystemName(otpDTO.getSystemName());
        response.setEnableAdmin(otpDTO.getEnableAdmin());
        RccmServerConfigDTO rccmServerConfig = rccmManageAPI.getRccmServerConfig();
        response.setHasUnifiedLogin(Boolean.FALSE);
        if (rccmServerConfig != null && rccmServerConfig.enableAssistAuth()) {
            response.setHasUnifiedLogin(rccmServerConfig.getHasUnifiedLogin());
        }
        return CommonWebResponse.success(response);
    }

    /**
     * 编辑动态口令
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑动态口令"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse configureCertificationStrategy(OtpCertificationWebRequest request) {
        Assert.notNull(request, "OtpStrategyWebRequest is null");

        // 未开启NTP不能开启动态口令
        if (BooleanUtils.isTrue(request.getEnableMfa())) {
            try {
                NtpResponse ntpServer = ntpMgmtAPI.getNtpServer();
                LOGGER.info("设置动态口令从rccp获取到的ntp数据为：{}", JSON.toJSONString(ntpServer));
                if (BooleanUtils.isFalse(ntpServer.getExternal())) {
                    return CommonWebResponse.fail(OtpCertificationBusinessKey.RCDC_OPEN_OPT_NOT_OPEN_NTP_FAIL);
                }
            } catch (BusinessException ex) {
                return CommonWebResponse.fail(OtpCertificationBusinessKey.RCDC_OTP_CERTIFICATION_CONFIG_FAIL,
                        new String[] {ex.getMessage()});
            }
        }

        // 开启多集群统一登录功能，不允许变更动态口令策略
        if (!rccmManageAPI.canModifyGlobalSettings()) {
            return CommonWebResponse.fail(GlobalStrategyBussinessKey.RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING);
        }
        try {
            OtpCertificationDTO regionOtpDTO = otpCertificationAPI.getOtpCertification();
            regionOtpDTO.setHasOtpCodeTab(request.getOpenMfaLogin());
            regionOtpDTO.setOpenOtp(request.getEnableMfa());
            regionOtpDTO.setPeriod(request.getRefreshSeconds());
            regionOtpDTO.setEnableAdmin(request.getEnableAdmin());
            otpCertificationAPI.updateOtpCertification(regionOtpDTO);
            auditLogAPI.recordLog(OtpCertificationBusinessKey.RCDC_OTP_CERTIFICATION_CONFIG_SUCCESS);
            return CommonWebResponse.success(OtpCertificationBusinessKey.RCDC_OTP_CERTIFICATION_CONFIG_SUCCESS, new String[] {});
        } catch (Exception e) {
            LOGGER.error("编辑认证策略配置", e);
            auditLogAPI.recordLog(OtpCertificationBusinessKey.RCDC_OTP_CERTIFICATION_CONFIG_FAIL, e.getMessage());
            return CommonWebResponse.fail(OtpCertificationBusinessKey.RCDC_OTP_CERTIFICATION_CONFIG_FAIL, new String[] {"数据操作异常"});
        }
    }
}

