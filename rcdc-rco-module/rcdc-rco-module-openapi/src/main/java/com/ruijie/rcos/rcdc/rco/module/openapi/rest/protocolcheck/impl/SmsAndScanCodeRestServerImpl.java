package com.ruijie.rcos.rcdc.rco.module.openapi.rest.protocolcheck.impl;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.halodetect.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.protocolcheck.SmsAndScanCodeRestServer;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.protocolcheck.response.SmsAndScanCodeConfigResponse;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年07月16日
 *
 * @author zhanghongkun
 */
@Service
public class SmsAndScanCodeRestServerImpl implements SmsAndScanCodeRestServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsAndScanCodeRestServerImpl.class);

    @Autowired
    RcoGlobalParameterAPI rcoGlobalParameterAPI;

    @Override
    public CommonWebResponse<SmsAndScanCodeConfigResponse> checkSMSAndScanCode() {
        FindParameterRequest findParameterRequest = new FindParameterRequest(SmsAndScanCodeCheckConstants.SMS_AND_SCANCODE_SWITCH);
        FindParameterResponse parameter = rcoGlobalParameterAPI.findParameter(findParameterRequest);
        SmsAndScanCodeConfigResponse response = JSONObject.parseObject(parameter.getValue(), SmsAndScanCodeConfigResponse.class);
        LOGGER.info("短信和扫码的全局配置为:{}", parameter.getValue());
        return CommonWebResponse.success(response);
    }
}
