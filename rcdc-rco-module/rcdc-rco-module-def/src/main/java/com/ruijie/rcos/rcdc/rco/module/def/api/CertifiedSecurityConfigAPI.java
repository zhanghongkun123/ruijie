package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacClientAuthSecurityDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CertifiedSecurityDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: RememberMe 开关功能
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/30
 *
 * @author TD
 */
public interface CertifiedSecurityConfigAPI {

    String REMEMBER_KEY = "remember_password";

    String CHANGE_KEY = "change_password";


    /**
     * 修改安全配置
     *
     * @param request 记住密码请求参数
     * @throws BusinessException 配置错误
     */
    void updateCertifiedSecurityConfig(CertifiedSecurityDTO request) throws BusinessException;

    /**
     * 查询安全配置开关配置
     * 
     * @return 记住密码结果
     * @throws BusinessException BusinessException
     */
    IacClientAuthSecurityDTO queryCertifiedSecurityConfig() throws BusinessException;


    /**
     * 查询通知终端变更配置
     * @return Boolean
     * @throws BusinessException BusinessException
     */
    Boolean queryNotifyLoginTerminalChangeConfig() throws BusinessException;

    /**
     * 查询通知终端变更配置
     * @return Boolean
     * @throws BusinessException BusinessException
     */
    Boolean queryNotifyLoginTerminalChangeConfigByUnifiedLoginConfig() throws BusinessException;
}
