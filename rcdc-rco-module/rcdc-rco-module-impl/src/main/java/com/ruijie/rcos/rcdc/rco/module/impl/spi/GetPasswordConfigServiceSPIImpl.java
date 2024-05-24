package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSONObject;
import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacAdMgmtAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacDomainConfigDetailDTO;
import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.codec.adapter.def.spi.CbbDispatcherHandlerSPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.AutoLogonAPI;
import com.ruijie.rcos.rcdc.rco.module.def.autologon.enums.AutoLogonEnum;
import com.ruijie.rcos.rcdc.rco.module.impl.autologon.constant.AutoLogonConstants;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineMessageHandler;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DispatcherImplemetion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * Description: 获取全局密码配置开关
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/01
 *
 * @author TD
 */
@DispatcherImplemetion(ShineAction.GET_WINDOWS_PASSWORD_CONFIG_STATUS)
public class GetPasswordConfigServiceSPIImpl implements CbbDispatcherHandlerSPI {

    public static final Logger LOGGER = LoggerFactory.getLogger(GetPasswordConfigServiceSPIImpl.class);

    @Autowired
    private AutoLogonAPI autoLogonAPI;

    @Autowired
    private ShineMessageHandler shineMessageHandler;

    @Autowired
    private IacAdMgmtAPI iacAdMgmtAPI;

    @Override
    public void dispatch(CbbDispatcherRequest request) {
        Assert.notNull(request, "request can not be null");

        JSONObject resultObject = new JSONObject();
        resultObject.put(AutoLogonEnum.AUTO_LOGON.getAutoLogonKey(),
                autoLogonAPI.getGlobalAutoLogonStrategy(AutoLogonEnum.AUTO_LOGON) ? AutoLogonConstants.WINDOWS_AUTO_LOGON_ON
                        : AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);
        Boolean enableAdAutoLogon = Boolean.FALSE;
        try {
            IacDomainConfigDetailDTO adConfig = iacAdMgmtAPI.getAdConfig();
            enableAdAutoLogon = adConfig == null ? Boolean.FALSE : adConfig.getAdAutoLogon();
        } catch (BusinessException e) {
            LOGGER.error("从身份中心获取AD域配置发生异常", e);
        }
        resultObject.put(AutoLogonEnum.AD_AUTO_LOGON.getAutoLogonKey(), enableAdAutoLogon ? AutoLogonConstants.WINDOWS_AUTO_LOGON_ON
                : AutoLogonConstants.WINDOWS_AUTO_LOGON_OFF);

        response(request, AutoLogonConstants.WINDOWS_AUTO_LOGON_SUCCESS, resultObject);
    }


    private void response(CbbDispatcherRequest request, Integer code, Object content) {
        try {
            // 返回消息给shine
            shineMessageHandler.responseContent(request, code, content);
        } catch (Exception e) {
            LOGGER.error("终端{}获取全局密码信息失败，e={}", request.getTerminalId(), e);
        }
    }
}
