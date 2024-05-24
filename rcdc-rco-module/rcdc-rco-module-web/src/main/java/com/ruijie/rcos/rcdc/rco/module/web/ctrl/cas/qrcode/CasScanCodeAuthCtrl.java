package com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.CasScanCodeAuthParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.CasScanCodeAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.request.CasScanCodeAuthWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.cas.qrcode.response.CasScanCodeAuthWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.validation.CasScanCodeValidation;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: CAS扫码认证Controller
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/23
 *
 * @author TD
 */
@Api("CAS扫码认证")
@Controller
@RequestMapping("/rco/cas/qrCode")
@EnableCustomValidate(validateClass = CasScanCodeValidation.class)
public class CasScanCodeAuthCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(CasScanCodeAuthCtrl.class);

    @Autowired
    private CasScanCodeAuthParameterAPI scanCodeAuthParameterAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 配置CAS扫码认证
     *
     * @param request 请求参数
     * @return CommonWebResponse 响应参数
     */
    @ApiOperation("配置CAS扫码认证")
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableCustomValidate(validateMethod = "updateCasServiceValidate")
    public CommonWebResponse<String> configureCasScanCodeAuth(CasScanCodeAuthWebRequest request) {
        Assert.notNull(request, "CasScanCodeAuthWebRequest is null");
        CasScanCodeAuthDTO scanCodeAuthDTO = new CasScanCodeAuthDTO();
        BeanUtils.copyProperties(request.getCasScanCodeAuth(), scanCodeAuthDTO);
        try {
            CasScanCodeAuthDTO codeAuthDTO = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo();
            boolean enableNewOpenOtp = BooleanUtils.toBoolean(scanCodeAuthDTO.getApplyOpen());
            boolean enableOldOpenOtp = BooleanUtils.toBoolean(codeAuthDTO.getApplyOpen());
            String message = "";
            if (enableNewOpenOtp == enableOldOpenOtp) {
                message = LocaleI18nResolver.resolve(CasScanCodeAuthBusinessKey.RCDC_RCO_EDIT_CAS);
            } else {
                if (enableNewOpenOtp) {
                    message = LocaleI18nResolver.resolve(CasScanCodeAuthBusinessKey.RCDC_RCO_OPEN_CAS);
                } else {
                    message = LocaleI18nResolver.resolve(CasScanCodeAuthBusinessKey.RCDC_RCO_CLOSE_CAS);
                }
            }
            scanCodeAuthParameterAPI.updateCasScanCodeAuthInfo(scanCodeAuthDTO);
            auditLogAPI.recordLog(CasScanCodeAuthBusinessKey.RCDC_CAS_SCAN_CODE_AUTH_CONFIG_SUCCESS, message);
            return CommonWebResponse.success(CasScanCodeAuthBusinessKey.RCDC_CAS_SCAN_CODE_AUTH_CONFIG_SUCCESS,
                    new String[] {message});
        } catch (Exception e) {
            LOGGER.error("编辑CAS扫码认证配置", e);
            auditLogAPI.recordLog(CasScanCodeAuthBusinessKey.RCDC_CAS_SCAN_CODE_AUTH_CONFIG_FAIL, e, e.getMessage());
            return CommonWebResponse.fail(CasScanCodeAuthBusinessKey.RCDC_CAS_SCAN_CODE_AUTH_CONFIG_FAIL,
                    new String[] {e.getMessage()});
        }
    }

    /**
     * 获取CAS扫码认证信息
     *
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation("获取CAS扫码认证信息")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public CommonWebResponse<CasScanCodeAuthWebResponse> qryCertificationStrategy() {
        CasScanCodeAuthDTO scanCodeAuthDTO = scanCodeAuthParameterAPI.getCasScanCodeAuthInfo();
        CasScanCodeAuthWebResponse webResponse = new CasScanCodeAuthWebResponse();
        BeanUtils.copyProperties(scanCodeAuthDTO, webResponse);
        return CommonWebResponse.success(webResponse);
    }
}
