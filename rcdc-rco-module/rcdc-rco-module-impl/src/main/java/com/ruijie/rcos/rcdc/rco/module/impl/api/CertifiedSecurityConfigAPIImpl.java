package com.ruijie.rcos.rcdc.rco.module.impl.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacClientAuthSecurityConfigAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.CertifiedSecurityConfigAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CertifiedSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.impl.rccm.service.RccmManageService;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description:
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/30
 *
 * @author TD
 */
public class CertifiedSecurityConfigAPIImpl implements CertifiedSecurityConfigAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertifiedSecurityConfigAPIImpl.class);

    @Autowired
    private RccmManageService rccmManageService;

    @Autowired
    private IacClientAuthSecurityConfigAPI iacCertifiedSecurityConfigAPI;

    @Override
    public void updateCertifiedSecurityConfig(CertifiedSecurityDTO request) throws BusinessException {
        Assert.notNull(request, "request cannot be null");
        // 开启情况下 校验统一登陆是否开启，统一登陆开启则不允许将IP变更提示设置为开启
        if (Boolean.TRUE.equals(request.getNeedNotifyLoginTerminalChange()) && rccmManageService.getRccmServerHasUnifiedLoginConfig()) {
            throw new BusinessException(BusinessKey.RCDC_RCO_NOTIFY_LOGIN_TERMINAL_CHANGE_CAN_NOT_OPEN_BY_ISUNIFIEDLOGIN);
        }
        IacClientAuthSecurityDTO iacClientAuthSecurityDTO = new IacClientAuthSecurityDTO();
        iacClientAuthSecurityDTO.setChangePassword(request.getChangePassword());
        iacClientAuthSecurityDTO.setNeedNotifyLoginTerminalChange(request.getNeedNotifyLoginTerminalChange());
        iacClientAuthSecurityDTO.setEnableOfflineLogin(request.getEnableOfflineLogin());
        iacClientAuthSecurityDTO.setRememberPassWord(request.getRememberPassWord());
        iacCertifiedSecurityConfigAPI.updateConfig(iacClientAuthSecurityDTO);
    }

    @Override
    public IacClientAuthSecurityDTO queryCertifiedSecurityConfig() throws BusinessException {
        IacClientAuthSecurityDTO securityDTO = iacCertifiedSecurityConfigAPI.detail();
        securityDTO.setNeedNotifyLoginTerminalChange(queryNotifyLoginTerminalChangeConfigByUnifiedLoginConfig());
        return securityDTO;
    }

    @Override
    public Boolean queryNotifyLoginTerminalChangeConfig() throws BusinessException {
        return iacCertifiedSecurityConfigAPI.detail().getNeedNotifyLoginTerminalChange();
    }

    @Override
    public Boolean queryNotifyLoginTerminalChangeConfigByUnifiedLoginConfig() throws BusinessException {
        Boolean enable = queryNotifyLoginTerminalChangeConfig();
        // 如果开启情况下 检查发现统一登陆 开启 ，则不提示IP变更
        if (Boolean.TRUE.equals(enable)) {
            return !rccmManageService.getRccmServerHasUnifiedLoginConfig();

        }
        return enable;
    }
}
