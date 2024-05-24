package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.SyncUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户管理接口
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-01-14 16:57:00
 *
 * @author zjy
 */
public interface UserMgmtAPI {


    /**
     * 根据用户id获取用户基本信息
     *
     * @param userId 用户id
     * @return 返回值
     * @Date 2022/1/14 10:56
     * @Author zjy
     **/
    UserInfoDTO getUserInfoById(UUID userId);

    /**
     * 根据用户名获取用户基本信息
     *
     * @param userName 用户名
     * @return 返回值
     * @Date 2022/1/14 10:56
     * @Author zjy
     **/
    UserInfoDTO getUserInfoByName(String userName);

    /**
     * 根据用户名获取用户基本信息
     *
     * @param userIdList 用户id列表
     * @return 返回值
     * @Date 2022/1/14 10:56
     * @Author zjy
     **/
    List<UserInfoDTO> getUserInfoByIdList(List<UUID> userIdList);

    /**
     * 用户登录
     *
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 10:25
     * @Author zjy
     **/
    UserLoginInfoDTO userLogin(UserLoginRequest request) throws BusinessException;

    /**
     * web客户端用户登录
     *
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 10:25
     * @Author zqj
     **/
    UserLoginInfoDTO webClientLogin(WebClientLoginRequest request) throws BusinessException;

    /**
     * 根据用户名称获取用户登录信息
     * @param request request
     * @return 结果
     */
    UserLoginInfoDTO obtainUserLoginInfoByUserName(String request);

    /**
     *用户登录信息变更提示
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/9/18 10:25
     * @Author linrenjian
     **/
    LoginInfoChangeDTO loginInfoChangeTip(LoginInfoChangeRequestDTO request) throws BusinessException;


    /**
     * 用户修改密码
     *
     * @param request 修改密码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 11:25
     * @Author zjy
     **/
    UserUpdatePwdDTO updatePwd(UserUpdatePwdRequest request) throws BusinessException;


    /**
     * 获取云桌面列表
     *
     * @param userDesktopDTO 请求参数
     * @return 列表
     * @throws Exception 异常
     */
    List<UserDesktopInfoDTO> getDesktopInfoByUserName(UserDesktopDTO userDesktopDTO) throws Exception;

    /**
     * 校验用户动态口令码
     *
     * @param checkUserOtpCodeDTO 请求体
     * @return 结果
     */
    CheckUserOtpCodeResultDTO checkOtpCode(CheckUserOtpCodeDTO checkUserOtpCodeDTO);

    /**
     * 动态口令码登录
     *
     * @param request 入参
     * @return 结果
     * @throws BusinessException 业务异常
     */
    OtpCodeLoginResultDTO otpCodeLogin(OtpCodeLoginRequest request) throws BusinessException;

    /**
     * 校验动态码
     * @param checkDTO 校验请求
     * @return 结果
     * @throws BusinessException 业务异常
     */
    CertificationResultDTO checkThirdCertificationCode(ThirdPartyCertificationRequest checkDTO) throws BusinessException;

    /**
     * 根据用户ID列表查询出其所属的组ID列表
     *
     * @param userIdList 用户ID列表
     * @return 用户组ID列表
     */
    List<UUID> listGroupIdByUserIdList(List<UUID> userIdList);

    /**
     * 同步用户信息到终端
     * @param cbbUserDetailDTO 要推送的用户信息
     */
    void syncUserInfoToTerminal(IacUserDetailDTO cbbUserDetailDTO);

    /**
     * 同步用户密码
     * @param syncUserPwdRequest 请求参数
     * @return 结果
     * @throws BusinessException 业务异常
     */
    UserUpdatePwdDTO syncUserPassword(SyncUserPwdRequest syncUserPwdRequest) throws BusinessException;
}
