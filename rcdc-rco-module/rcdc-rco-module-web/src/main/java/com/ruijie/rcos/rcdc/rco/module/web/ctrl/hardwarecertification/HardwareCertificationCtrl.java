package com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request.HardwareDTO;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacHardwareCertificationAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.certification.hardware.IacHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.GlobalStrategyBussinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.hardwarecertification.request.HardwareCertificationWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 *
 * Description: 硬件特征码认证策略配置
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年03月29日
 *
 * @author linke
 */
@Api(tags = "硬件特征码认证策略配置")
@Controller
@RequestMapping("/rco/hardwareCertification")
public class HardwareCertificationCtrl {

    private static final Logger LOGGER = LoggerFactory.getLogger(HardwareCertificationCtrl.class);

    @Autowired
    private IacHardwareCertificationAPI hardwareCertificationAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;


    /**
     * 编辑硬件特征码
     *
     * @param request 请求参数
     * @return DefaultWebResponse 响应参数
     */
    @ApiOperation(value = "编辑硬件特征码")
    @ApiVersions({@ApiVersion(value = Version.V3_1_1, descriptions = {"编辑硬件特征码"})})
    @RequestMapping(value = "edit", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse configureCertificationStrategy(HardwareCertificationWebRequest request) {
        Assert.notNull(request, "HardwareStrategyWebRequest is null");
        // 开启多集群统一登录功能，不允许变更硬件特征码策略
        if (!rccmManageAPI.canModifyGlobalSettings()) {
            return CommonWebResponse.fail(GlobalStrategyBussinessKey.RCCM_OPEN_UNIFIED_LOGIN_CAN_NOT_EDIT_SETTING);
        }
        IacHardwareCertificationDTO hardwareDTO = webRequestToDto(request);
        try {
            hardwareCertificationAPI.updateHardwareCertification(hardwareDTO);
            auditLogAPI.recordLog(HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CONFIG_SUCCESS);
            return CommonWebResponse.success(HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CONFIG_SUCCESS, new String[] {});
        } catch (Exception e) {
            LOGGER.error("编辑认证策略配置", e);
            auditLogAPI.recordLog(HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CONFIG_FAIL, e.getMessage());
            return CommonWebResponse.fail(HardwareCertificationBusinessKey.RCDC_HARDWARE_CERTIFICATION_CONFIG_FAIL, new String[] {e.getMessage()});
        }
    }

    /**
     * webRequestToDto
     *
     * @param request HardwareCertificationWebRequest
     * @return HardwareCertificationDTO
     */
    private IacHardwareCertificationDTO webRequestToDto(HardwareCertificationWebRequest request) {
        IacHardwareCertificationDTO dto = new IacHardwareCertificationDTO();
        HardwareDTO hardwareStrategy = request.getHardwareStrategy();
        dto.setOpenHardware(hardwareStrategy.getOpenHardware());
        dto.setAutoApprove(BooleanUtils.isTrue(hardwareStrategy.getAutoApprove()));
        dto.setEnableTerminalApproved(BooleanUtils.isTrue(hardwareStrategy.getEnableTerminalApproved()));
        return dto;
    }
}
