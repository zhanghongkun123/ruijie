package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacClientAuthSecurityConfigAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacSmsCertificationMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacSmsPwdRecoverDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.RcoGlobalParameterAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CertifiedSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.FindParameterRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.FindParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.CommonMessageCode;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年08月09日
 *
 * @author zhanghongkun
 */
@DispatcherImplemetion(ShineAction.GET_CERTIFIED_SECURITY_CONFIG)
public class GetCertifiedSecurityConfigSPIImpl implements CbbDispatcherHandlerSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetCertifiedSecurityConfigSPIImpl.class);

    @Autowired
    RcoGlobalParameterAPI rcoGlobalParameterAPI;

    private static final String REMEMBER_KEY = "remember_password";

    private static final String CHANGE_KEY = "change_password";

    private static final String ENABLE_OFFLINE_LOGIN = "enable_offline_login";

    @Autowired
    private ShineMessageHandler shineMessageHandler;
    
    @Autowired
    private IacSmsCertificationMgmtAPI smsCertificationAPI;

    @Autowired
    private IacClientAuthSecurityConfigAPI iacCertifiedSecurityConfigAPI;

    @Override
    public void dispatch(CbbDispatcherRequest cbbDispatcherRequest) {
        Assert.notNull(cbbDispatcherRequest, "CbbDispatcherRequest can not null");

        CertifiedSecurityDTO responseDTO = new CertifiedSecurityDTO();
        try {
            IacClientAuthSecurityDTO detail = iacCertifiedSecurityConfigAPI.detail();
            responseDTO.setRememberPassWord(detail.getRememberPassWord());
            responseDTO.setChangePassword(detail.getChangePassword());
            responseDTO.setEnableOfflineLogin(detail.getEnableOfflineLogin());
            IacSmsPwdRecoverDTO smsPwdRecoverStrategy = smsCertificationAPI.getClientSmsPwdRecoverStrategy();
            responseDTO.setEnablePwdRecover(smsPwdRecoverStrategy.getEnable());
            responseDTO.setInterval(smsPwdRecoverStrategy.getInterval());
            responseDTO.setPeriod(smsPwdRecoverStrategy.getPeriod());
            LOGGER.info("返回客户端安全策略数据为：{}", JSON.toJSONString(responseDTO));
            // 返回消息给shine
            shineMessageHandler.responseContent(cbbDispatcherRequest, CommonMessageCode.SUCCESS, responseDTO);
        } catch (Exception e) {
            LOGGER.error(String.format("终端:%s获取:%s配置信息失败", cbbDispatcherRequest.getTerminalId(), JSON.toJSONString(responseDTO)), e);
        }
    }
}
