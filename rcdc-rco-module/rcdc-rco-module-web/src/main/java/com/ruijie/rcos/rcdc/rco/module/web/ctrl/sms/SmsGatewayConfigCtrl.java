package com.ruijie.rcos.rcdc.rco.module.web.ctrl.sms;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import com.ruijie.rcos.base.notify.module.def.api.BaseSmsNotifyAPI;
import com.ruijie.rcos.base.notify.module.def.dto.SmsNotifyConfig;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.Version;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.web.validation.SmsGatewayValidation;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.validation.EnableCustomValidate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Description: 短信配置控制层
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/19
 *
 * @author TD
 */
@Api(tags = "短信服务器配置管理")
@Controller
@RequestMapping("/rco/sms/gateway")
@EnableCustomValidate(validateClass = SmsGatewayValidation.class)
public class SmsGatewayConfigCtrl {
    
    @Autowired
    private BaseSmsNotifyAPI baseSmsNotifyAPI;

    /**
     * 获取短信服务器详情
     * @throws BusinessException 业务异常
     * @return 短信服务器信息
     */
    @ApiOperation("获取短信服务器详情")
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiVersions({@ApiVersion(value = Version.V3_2_101, descriptions = {"高级设置-短信服务器，获取短信服务器详情"})})
    @EnableAuthority
    public CommonWebResponse<SmsNotifyConfig> getSmsGatewayDetail() throws BusinessException {
        return CommonWebResponse.success(baseSmsNotifyAPI.detailConfig());
    }

}
