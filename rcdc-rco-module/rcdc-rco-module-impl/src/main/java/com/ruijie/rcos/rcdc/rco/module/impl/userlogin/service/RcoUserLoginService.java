package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;

/**
 *
 * Description: 多集群统一登录Service
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年09月27日
 *
 * @author linke
 */
public interface RcoUserLoginService {


    /**
     * r-center发起的登录验证
     *
     * @param dispatcherRequest dispatcherRequest
     * @return  ShineLoginResponseDTO
     */
    ShineLoginResponseDTO userLoginValidate(CbbDispatcherRequest dispatcherRequest);

    /**
     * r-center发起的登录验证-新版接口
     *
     * @param dispatcherRequest dispatcherRequest
     * @return  ShineLoginResponseDTO
     */
    ShineLoginResponseDTO collectUserLoginValidate(CbbDispatcherRequest dispatcherRequest);
}
