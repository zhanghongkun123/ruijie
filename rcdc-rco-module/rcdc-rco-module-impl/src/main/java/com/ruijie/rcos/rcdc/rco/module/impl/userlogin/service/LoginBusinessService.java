package com.ruijie.rcos.rcdc.rco.module.impl.userlogin.service;

import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacAuthUserResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.spi.dto.ShineLoginResponseDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.LoginPostAuthDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userlogin.dto.NameAndPwdCheckDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户登录业务处理
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/1/16
 *
 * @author linke
 */
public interface LoginBusinessService {

    /**
     * 登录处理类的唯一key
     *
     * @return String
     */
    String getKey();

    /**
     * 构造登入数据
     *
     * @param data data
     * @return ShineLoginDTO
     * @throws Exception Exception
     */
    ShineLoginDTO getShineLoginDTO(String data) throws Exception;


    /**
     * 校验用户名、密码
     *
     * @param nameAndPwdCheckDTO nameAndPwdCheckDTO
     * @return CbbAuthUserResultDTO
     */
    IacAuthUserResultDTO checkUserNameAndPassword(NameAndPwdCheckDTO nameAndPwdCheckDTO);

    /**
     * 校验用户名密码时，各个登录处理器各自逻辑处理
     *
     * @param terminalId terminalId
     * @param userDetailDTO 用户详细信息
     * @return 结果状态码
     * @throws BusinessException BusinessException
     */
    int processLogin(String terminalId, IacUserDetailDTO userDetailDTO) throws BusinessException;


    /**
     * 用户名、密码校验获取结果后处理事件：记录错误次数，是否需要锁定等
     *
     * @param loginPostAuthDTO loginPostAuthDTO
     * @param authUserResultDTO authUserResultDTO
     * @throws BusinessException BusinessException
     */
    void postAuth(LoginPostAuthDTO loginPostAuthDTO, IacAuthUserResultDTO authUserResultDTO) throws BusinessException;

    /**
     * 校验用户名和动态口令
     *
     * @param terminalId 终端ID
     * @param userName 用户名
     * @param otpCode 动态口令
     * @return 校验结果
     */
    IacAuthUserResultDTO checkUserNameAndOtpCode(String terminalId, String userName, String otpCode);

    /**
     * 登录类型
     *
     * @return 登录类型
     */
    String getLoginEvent();

    /**
     * 构建登录成功的结果
     *
     * @param userDetailDTO userDetailDTO
     * @param authUserResponse authUserResponse
     * @return BaseAuthUserResultDTO
     * @throws Exception 异常
     */
    ShineLoginResponseDTO responseSuccess(IacUserDetailDTO userDetailDTO, IacAuthUserResultDTO authUserResponse);

    /**
     * 处理响应结果
     *
     * @param userDetailDTO userDetailDTO
     * @return response
     */
    ShineLoginResponseDTO generateResponse(IacUserDetailDTO userDetailDTO);

    /**
     * 构建登录失败的结果
     *
     * @param resultDTO resultDTO
     * @return BaseAuthUserResultDTO
     * @throws Exception 异常
     */
    ShineLoginResponseDTO responseLoginFail(IacAuthUserResultDTO resultDTO);

    /**
     * 用户登录，保存用户信息报表
     * @param terminalId terminalId
     * @param userDetailDTO userDetailDTO
     */
    void saveUserLoginInfo(String terminalId, @Nullable IacUserDetailDTO userDetailDTO);
}
