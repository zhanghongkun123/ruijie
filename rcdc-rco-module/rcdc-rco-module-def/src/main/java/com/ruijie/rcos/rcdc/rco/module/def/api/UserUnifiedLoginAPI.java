package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedChangePwdResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userlogin.dto.UnifiedLoginResultDTO;

import java.util.UUID;

/**
 *
 * Description: rcdc统一登录API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public interface UserUnifiedLoginAPI {

    /**
     * 统一登录校验
     *
     * @param dispatcherRequest  登录请求信息
     * @return  UnifiedLoginResultDTO
     * @throws Exception 异常
     */
    UnifiedLoginResultDTO unifiedUserLoginValidate(CbbDispatcherRequest dispatcherRequest) throws Exception;

    /**
     * 新版统一登录校验
     *
     * @param dispatcherRequest  登录请求信息
     * @return  UnifiedLoginResultDTO
     * @throws Exception 异常
     */
    UnifiedLoginResultDTO collectUserLoginValidate(CbbDispatcherRequest dispatcherRequest) throws Exception;

    /**
     * 校验clusterId，rcdc是否开启统一纳管，统一登录
     * @param clusterId clusterId
     * @return Boolean
     */
    Boolean validateUnifiedLoginAuth(UUID clusterId);

    /**
     * 统一修改密码校验
     * @param dispatcherRequest 修改密码请求
     * @return 修改密码结果
     * @throws Exception 异常
     */
    UnifiedChangePwdResultDTO unifiedChangePwdValidate(CbbDispatcherRequest dispatcherRequest) throws Exception;
}
