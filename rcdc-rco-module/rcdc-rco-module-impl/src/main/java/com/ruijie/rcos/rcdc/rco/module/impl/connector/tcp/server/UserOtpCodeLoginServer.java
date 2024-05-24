package com.ruijie.rcos.rcdc.rco.module.impl.connector.tcp.server;

import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.CheckUserOtpCodeResponse;
import com.ruijie.rcos.rcdc.rco.module.impl.connector.dto.GetUserOtpConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.otpcertification.dto.UserOtpCodeDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.common.ShineAction;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.UserOtpConfigDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.SessionAlias;
import com.ruijie.rcos.sk.connectkit.api.annotation.tcp.Tcp;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年09月20日
 *
 * @author chenli
 */
@Tcp
public interface UserOtpCodeLoginServer {

    /**
     * 校验第三方认证动态口令
     *
     * @param terminalId     终端ID
     * @param userOtpCodeDto 请求
     * @throws BusinessException 业务异常
     * @return 认证结果
     */
    @ApiAction(ShineAction.OBTAIN_CHECK_USER_OTP_CODE)
    CheckUserOtpCodeResponse checkUserOtpCode(@SessionAlias String terminalId, UserOtpCodeDTO userOtpCodeDto) throws BusinessException;


    /**
     * 动态口令登录
     *
     * @param terminalId    别名
     * @param shineLoginDTO 请求
     * @throws Exception 异常
     * @return 登录响应
     */
    @ApiAction(ShineAction.OBTAIN_USER_OTP_CODE_LOGIN)
    ShineLoginResponseDTO userOtpCodeLogin(@SessionAlias String terminalId, ShineLoginDTO shineLoginDTO) throws Exception;


    /**
     * 获取动态口令配置信息
     *
     * @param terminalId 别名
     * @param request    请求
     * @throws BusinessException 业务异常
     * @return 响应
     */
    @ApiAction(ShineAction.OBTAIN_GET_USER_OTP_CONFIG)
    UserOtpConfigDTO getUserOtpConfig(@SessionAlias String terminalId, GetUserOtpConfigDTO request) throws BusinessException;

}
