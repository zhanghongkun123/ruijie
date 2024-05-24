package com.ruijie.rcos.rcdc.rco.module.web.ctrl.remember;

import com.ruijie.rcos.gss.base.iac.module.annotation.EnableAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.IacClientAuthSecurityConfigAPI;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.RccmManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.enums.OfflineAutoLockedEnum;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.remember.response.ConfigGlobalParameterResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.response.CommonWebResponse;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 认证安全相关Controller
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/30
 *
 * @author TD
 */
@Controller
@RequestMapping("/rco/certifiedSecurity")
public class CertifiedSecurityConfigController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertifiedSecurityConfigController.class);

    @Autowired
    IacClientAuthSecurityConfigAPI certifiedSecurityConfigAPI;

    @Autowired
    CbbTerminalOperatorAPI terminalOperatorAPI;

    @Autowired
    private RccmManageAPI rccmManageAPI;

    /**
     * 查询客户端认证安全相关配置
     * 
     * @return 认证安全相关配置
     * @throws BusinessException 异常
     */
    @RequestMapping(value = "clientDetail", method = RequestMethod.POST)
    @EnableAuthority
    public CommonWebResponse<ConfigGlobalParameterResponse> qryDisableRememberPasswordConfig() throws BusinessException {
        String offlineLoginSetting = terminalOperatorAPI.queryOfflineLoginSetting();
        IacClientAuthSecurityDTO securityDTO = certifiedSecurityConfigAPI.detail();
        ConfigGlobalParameterResponse response = new ConfigGlobalParameterResponse();
        response.setOfflineAutoLocked(changeStringToEnum(offlineLoginSetting));
        response.setEnableOfflineLogin(securityDTO.getEnableOfflineLogin());
        response.setRememberPassWord(securityDTO.getRememberPassWord());
        response.setChangePassword(securityDTO.getChangePassword());
        response.setNeedNotifyLoginTerminalChange(securityDTO.getNeedNotifyLoginTerminalChange());
        response.setRememberLastLoginMethod(securityDTO.getRememberLastLoginMethod());
        response.setHasUnifiedLogin(rccmManageAPI.getRccmServerHasUnifiedLoginConfig());
        return CommonWebResponse.success(response);
    }

    private OfflineAutoLockedEnum changeStringToEnum(String offlineLoginSetting) throws BusinessException {
        OfflineAutoLockedEnum offlineAutoLockedEnum = OfflineAutoLockedEnum.getByDays(Integer.valueOf(offlineLoginSetting));
        if (offlineAutoLockedEnum == null) {
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_CHANGE_OFFLINE_AUTO_LOCKED_ENUM_NOT_EXIST, offlineLoginSetting);
        }
        return offlineAutoLockedEnum;
    }
}

