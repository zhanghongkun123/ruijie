package com.ruijie.rcos.rcdc.rco.module.impl.sms.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.rcdc.rco.module.def.sms.constnts.SmsAndScanCodeCheckConstants;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.def.sms.enums.MessageBusinessType;
import com.ruijie.rcos.rcdc.rco.module.impl.service.GlobalParameterService;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.auth.service.SmsCertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Description: SmsCertificationServiceImpl
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/15
 *
 * @author TD
 */
@Service
public class SmsCertificationServiceImpl implements SmsCertificationService {
    
    @Autowired
    private GlobalParameterService globalParameterService;
    
    @Override
    public SmsCertificationDTO getSmsCertificationStrategy() {
        return JSONObject.parseObject(globalParameterService.findParameter(SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG), 
                SmsCertificationDTO.class);
    }

    @Override
    public void editSmsCertificationStrategy(SmsCertificationDTO smsCertificationDTO) {
        Assert.notNull(smsCertificationDTO, "smsCertificationDTO can be not null");
        globalParameterService.updateParameter(SmsAndScanCodeCheckConstants.SMS_AUTH_CONFIG, JSON.toJSONString(smsCertificationDTO));
    }

    @Override
    public SmsPwdRecoverDTO getSmsPwdRecoverStrategy() {
        return JSONObject.parseObject(globalParameterService.findParameter(SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG),
                SmsPwdRecoverDTO.class);
    }

    @Override
    public void editSmsPwdRecoverStrategy(SmsPwdRecoverDTO pwdRecoverDTO) {
        Assert.notNull(pwdRecoverDTO, "pwdRecoverDTO can be not null");
        globalParameterService.updateParameter(SmsAndScanCodeCheckConstants.SMS_RECOVER_PWD_CONFIG, JSON.toJSONString(pwdRecoverDTO));
    }

}
