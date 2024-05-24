package com.ruijie.rcos.rcdc.rco.module.impl.sms.spi;

import com.ruijie.rcos.base.notify.module.def.dto.SmsNotifyConfig;
import com.ruijie.rcos.base.notify.module.def.spi.BaseSmsNotifySPI;
import com.ruijie.rcos.rcdc.rco.module.def.sms.dto.SmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.auth.service.SmsCertificationService;
import com.ruijie.rcos.rcdc.rco.module.impl.sms.service.SmsManageService;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 接受SK的通知spi请求
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/9/26
 *
 * @author zhiweiHong
 */
public class BaseSmsNotifySPIImpl implements BaseSmsNotifySPI {


    @Autowired
    private SmsCertificationService smsCertificationService;

    @Autowired
    private SmsManageService smsManageService;

    @Override
    public void onConfigChanged(SmsNotifyConfig smsNotifyConfig) {
        Assert.notNull(smsNotifyConfig, "smsNotifyConfig must not be null");
        // 密码找回开启，修改短信服务器需要通知在线客户端
        SmsPwdRecoverDTO pwdRecoverDTO = smsCertificationService.getSmsPwdRecoverStrategy();
        if (BooleanUtils.toBoolean(pwdRecoverDTO.getEnable())) {
            // 以短信网关的开关为主
            pwdRecoverDTO.setEnable(smsNotifyConfig.getEnable());
            // 通知在线客户端
            smsManageService.notifyOnlineClient(pwdRecoverDTO);
        }
    }
}
