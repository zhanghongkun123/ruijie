package com.ruijie.rcos.rcdc.rco.module.impl.service;


import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetUserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserLoginParam;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service.LoginBusinessService;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 集群管理接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/6/29
 *
 * @author zhiweiHong
 */
public interface RcoShineUserLoginService {

    /**
     * shine、oneclient用户登录逻辑
     * 
     * @param userLoginParam 请求信息
     * @return ShineLoginResponseDTO
     * @throws Exception 异常
     */
    ShineLoginResponseDTO userLogin(UserLoginParam userLoginParam) throws Exception;


    /**
     * 校验动态口令
     * @param terminalId 终端id
     * @param userOtpCodeDto 动态口令
     * @return CheckUserOtpCodeResponse
     */
    CheckUserOtpCodeResponse checkUserOtpCode(String terminalId, UserOtpCodeDTO userOtpCodeDto);


    /**
     *
     * @param terminalId 终端id
     * @param getUserOtpConfigDTO 获取用户动态口令信息请求
     * @return UserOtpConfigResponse 用户动态口令配置信息
     *
     */
    UserOtpConfigDTO getUserOtpConfig(String terminalId, GetUserOtpConfigDTO getUserOtpConfigDTO) ;

}
