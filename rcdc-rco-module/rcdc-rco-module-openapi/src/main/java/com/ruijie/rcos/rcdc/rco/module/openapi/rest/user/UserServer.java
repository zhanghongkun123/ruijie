package com.ruijie.rcos.rcdc.rco.module.openapi.rest.user;

import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.sms.IacCheckSmsRestUserPwdRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.sms.IacUserUpdatePwdDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OpenApiHardwareCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.OpenApiUserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.UserDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.user.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.SyncUserIdentityConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.OtpCodeLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserInfoListRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserInfoRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.UserUpdatePwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.user.WebClientLoginRequest;
import com.ruijie.rcos.rcdc.rco.module.def.certificationstrategy.dto.PwdStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.dto.CertificationResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.SyncUserPwdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.user.request.ThirdPartyCertificationRequest;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response.AsyncTaskResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response.CommonRestServerResponse;
import com.ruijie.rcos.rcdc.rco.module.openapi.rest.user.request.*;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.plugin.openapi.OpenAPI;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

/**
 * Description: 用户相关openapi
 * <p>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-02-09
 *
 * @author linke
 */
@OpenAPI
@Path("/v1/user")
public interface UserServer {

    /**
     * 查询用户信息
     *
     * @param userInfoRequest 用户信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/21 10:52
     * @Author zjy
     **/
    @POST
    @Path("/detail")
    OpenApiUserInfoDTO getUserInfo(@NotNull UserInfoRequest userInfoRequest) throws BusinessException;

    /**
     * 根据用户id列表查询用户
     *
     * @param userInfoListRequest 用户id信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 14:00
     * @Author zjy
     **/
    @POST
    @Path("/listByIds")
    List<UserInfoDTO> getUserInfoListByIdList(@NotNull UserInfoListRequest userInfoListRequest) throws BusinessException;

    /**
     * 查询密码策略
     *
     * @param
     * @return 返回值
     *  @throws BusinessException 业务异常
     * @Date 2022/1/18 14:00
     * @Author zjy
     **/
    @GET
    @Path("/pwdStrategy")
    PwdStrategyDTO getPwdStrategy() throws BusinessException;

    /**
     * 用户登录
     *
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 14:01
     * @Author zjy
     **/
    @POST
    @Path("/login")
    UserLoginInfoDTO login(@NotNull UserLoginRequest request);

    /**
     * web客户端用户登录
     *
     * @param request 用户登录信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022-04-26
     * @Author zqj
     **/
    @POST
    @Path("/webClientLogin")
    WebClientLoginInfoDTO webClientLogin(@NotNull WebClientLoginRequest request);

    /**
     * 更新用户密码
     *
     * @param request 用户密码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 14:01
     * @Author zjy
     **/
    @POST
    @Path("/updatePwd")
    UserUpdatePwdDTO updatePwd(@NotNull UserUpdatePwdRequest request);

    /**
     * 更新用户密码(外部调用)
     *
     * @param request 用户密码信息
     * @return 返回值
     * @throws BusinessException 业务异常
     * @Date 2022/1/18 14:01
     * @Author zdc
     **/
    @POST
    @Path("/updateUserPwd")
    UserUpdatePwdDTO updateUserPwd(@NotNull UserUpdatePwdRequest request);

    /**
     * 根据用户名检查用户是否存在
     *
     * @param userExistRequest 参数
     * @return Map<String, Boolean>
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/exist")
    Map<String, Boolean> checkUserExist(UserExistRequest userExistRequest) throws BusinessException;

    /**
     * 增量同步Ad域用户
     *
     * @param syncAdUserArrRequest 参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/ad/sync")
    AsyncTaskResponse syncAdUser(SyncAdUserArrRequest syncAdUserArrRequest) throws BusinessException;

    /**
     * 管理员用户认证(域用户升级的，包含第三方用户)
     *
     * @param request 参数
     * @return 认证结果
     * @throws BusinessException 业务异常
     */
    @Deprecated
    @POST
    @Path("/domain/auth")
    CommonRestServerResponse domainAuth(@NotNull UserLoginRequest request) throws BusinessException;

    /**
     * 增量同步LDAP域用户
     *
     * @param syncAdUserArrRequest 参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/ldap/sync")
    AsyncTaskResponse syncLdapUser(SyncAdUserArrRequest syncAdUserArrRequest) throws BusinessException;

    /**
     * 创建本地用户
     *
     * @param createUserRequest 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/create")
    AsyncTaskResponse createUser(CreateUserRequest createUserRequest) throws BusinessException;

    /**
     * 删除本地用户
     *
     * @param deleteUserRequest 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @DELETE
    @Path("/delete")
    AsyncTaskResponse deleteUser(DeleteUserRequest deleteUserRequest) throws BusinessException;

    /**
     * 修改用户所属组
     *
     * @param modifyUserRequest 请求参数
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/modify")
    @ApiAction("usergroup")
    void modifyUserGroup(ModifyUserRequest modifyUserRequest) throws BusinessException;

    /**
     * 根据用户名获取桌面信息
     *
     * @param request 请求参数
     * @return 列表
     * @throws BusinessException 业务异常
     **/
    @POST
    @Path("/listDeskByUserName")
    List<UserDesktopInfoDTO> getDesktopInfoByUserName(UserDesktopRequest request) throws BusinessException;

    /**
     * 获取用户的动态口令配置
     *
     * @param request 入参
     * @return 用户的动态口令配置
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/getUserOtpConfig")
    UserOtpConfigDTO getUserOtpConfig(@NotNull RestUserOtpConfigRequest request) throws BusinessException;

    /**
     * 校验动态口令码
     *
     * @param request 请求
     * @return 校验结果
     */
    @POST
    @Path("/checkOtpCode")
    CheckUserOtpCodeResultDTO checkOtpCode(@NotNull CheckUserOtpCodeDTO request);

    /**
     * 动态口令码登录
     *
     * @param request 入参
     * @return 校验结果
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/otpCodeLogin")
    OtpCodeLoginResultDTO otpCodeLogin(@NotNull OtpCodeLoginRequest request) throws BusinessException;

    /**
     * 校验第三方辅助认证动态码
     * @param request 请求
     * @return 校验结果
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/checkThirdPartyCertificationCode")
    CertificationResultDTO checkThirdPartyCertificationCode(ThirdPartyCertificationRequest request) throws BusinessException;

    /**
     * 返回用户列表
     * @param request 请求参数
     * @return  响应内容
     * @throws BusinessException  业务异常
     */
    @POST
    @Path("/list")
    DefaultPageResponse<OpenApiUserListDTO> pageQuery(ListUserWebRequest request) throws BusinessException;

    /**
     * 获取密码AES加密秘钥KEY
     * @return  用户密码秘钥key
     * @throws BusinessException  业务异常
     */
    @GET
    @Path("/getPasswordKey")
    AesPasswordKeyDTO getPasswordKey() throws BusinessException;

    /**
     * 短信重置用户密码
     * @param restUserPwdRequest 请求参数
     * @return UserUpdatePwdDTO 返回值
     */
    @POST
    @Path("/sms/updatePwd")
    IacUserUpdatePwdDTO smsRestUserPwd(@NotNull IacCheckSmsRestUserPwdRequest restUserPwdRequest);

    /**
     * 批量启用用户账号
     *
     * @param batchEnableUserRequest 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/batch/enableUser")
    AsyncTaskResponse batchEnableUser(BatchEnableUserRequest batchEnableUserRequest) throws BusinessException;

    /**
     * 批量禁用用户账号
     *
     * @param batchDisableUserRequest 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/batch/disableUser")
    AsyncTaskResponse batchDisableUser(BatchDisableUserRequest batchDisableUserRequest) throws BusinessException;

    /**
     * 编辑用户信息
     *
     * @param editUserRequest 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/edit")
    AsyncTaskResponse editUser(EditUserRequest editUserRequest) throws BusinessException;

    /**
     * 同步密码
     * @param syncUserPwdRequest 请求参数
     * @return UserUpdatePwdDTO 返回值
     */
    @POST
    @Path("/syncUserPassword")
    UserUpdatePwdDTO syncUserPassword(@NotNull SyncUserPwdRequest syncUserPwdRequest);

    /**
     * 同步用户辅助认证信息
     * @param configRequest 请求参数
     * @return CommonRestServerResponse 结果
     */
    @POST
    @Path("/syncUserIdentityConfig")
    CommonRestServerResponse syncUserIdentityConfig(@NotNull SyncUserIdentityConfigRequest configRequest);

    /**
     * 登陆成功后回调
     *
     * @param request 入参
     * @return 登陆成功后回调结果
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/loginInfoChange")
    LoginInfoChangeDTO loginInfoChange(@NotNull LoginInfoChangeRequestDTO request) throws BusinessException;

    /**
     * 创建用户终端绑定关系
     *
     * @param request 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @POST
    @Path("/hardwareCertification/batch/create")
    AsyncTaskResponse batchCreateHardwareCertification(BatchUserMacBindingRequest request) throws BusinessException;

    /**
     * 删除用户终端绑定关系
     *
     * @param request 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @DELETE
    @Path("/hardwareCertification/batch/delete")
    AsyncTaskResponse batchDeleteHardwareCertification(BatchUserMacDeleteRequest request) throws BusinessException;

    /**
     * 返回用户列表
     * @param request 请求参数
     * @return  响应内容
     * @throws BusinessException  业务异常
     */
    @POST
    @Path("/hardwareCertification/list")
    DefaultPageResponse<OpenApiHardwareCertificationDTO> hardwareCertificationPageQuery(ListHardwareCertificationOpenapiRequest request)
            throws BusinessException;

    /**
     * 修改审批记录
     * @param request 请求参数
     * @return AsyncTaskResponse
     * @throws BusinessException 业务异常
     */
    @PUT
    @Path("/hardwareCertification/batch/update")
    AsyncTaskResponse updateHardwareCertification(BatchUpdateHardwareCertificationRequest request) throws BusinessException;
}
