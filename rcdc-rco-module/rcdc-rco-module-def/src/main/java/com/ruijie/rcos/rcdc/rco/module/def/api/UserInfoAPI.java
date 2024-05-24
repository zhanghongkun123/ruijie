package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacImportUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserCertificationDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ApiCallerTypeEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.bigscreen.UserDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.cas.qrcode.dto.UserIdentityConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.constants.FunctionCallback;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 用户信息API
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/29 23:13
 * @author yxq
 */
public interface UserInfoAPI {
    /**
     * 通过用户ID获取用户名
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     * @return 用户名
     */
    String getUserNameById(UUID userId) throws BusinessException;

    /**
     * 根据用户名获取用户ID
     *
     * @param userName userName
     * @throws BusinessException 业务异常
     * @return 用户ID
     */
    UUID getUserIdByUserName(String userName) throws BusinessException;

    /**
     * 获取用户总数
     * @return 数量
     */
    long findUserCount();

    /**
     * 根据用户ID，查询用户认证开启情况
     *
     * @param userId 用户ID
     * @return 用户认证开启情况
     */
    UserCertificationDTO getUserCertificationDTO(UUID userId);

    /**
     * 校验是否可以删除用户
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    void validateDeleteUser(UUID userId) throws BusinessException;

    /**
     * 校验导入用户信息是否正确
     * @param importUser 用户信息
     * @param callback 回调
     * @throws BusinessException 业务异常
     */
    void validateImportUser(IacImportUserDTO importUser, FunctionCallback callback) throws BusinessException;

    /**
     * 校验用户组是否存在，多级组用“/”分隔
     * @param userGroupName 用户组名称
     * @throws BusinessException 业务异常
     */
    void validateUserGroupName(String userGroupName) throws BusinessException;

    /**
     * 获取系统用户组的保留名称集合
     * @param isNewUserGroup 是否获取AD和LDAP用户组
     * @return 用户组保留名称集合
     */
    List<String> getReserveGroupName(Boolean isNewUserGroup);

    /**
     * 获取系统用户的保留名称集合
     * @return 用户保留名称集合
     */
    List<String> getReserveUserName();

    /**
     * 通知shine退出登录
     * @param userId 用户id
     * @throws BusinessException 业务异常
     */
    void userLogout(UUID userId);

    /**
     * 统计用户类型
     * @return  用户数量、拥有云桌面的用户数量
     * @throws BusinessException 业务异常
     */
    UserDesktopResponse statisticsUserDeskTop() throws BusinessException;

    /**
     * 通知web客户端用户退出登录
     * @param userId 用户id
     */
    void notifyWebUserLogout(UUID userId);

    /**
     * 批量更新用户信息
     *
     * @param dto        请求参数
     * @param userIdList 用户列表
     * @throws BusinessException 业务异常
     */
    void updateBatchUserInfo(UserIdentityConfigDTO dto, List<UUID> userIdList) throws BusinessException;

    /**
     * 批量更新用户失效时间
     *  @param isNormalUser 是否普通用户
     * @param userIdList 用户Id列表
     * @param invalidTime 账号失效时间
     * @param accountExpireDate 账号过期时间
     * @throws BusinessException 业务异常
     */
    void updateBatchUserInvalidTime(Boolean isNormalUser, List<UUID> userIdList, Integer invalidTime, @Nullable Long accountExpireDate)
            throws BusinessException;

    /**
     * 获取用户加密密钥
     *
     * @param apiCallerTypeEnum  加密场景
     * @return 密钥
     */
    String getUserPasswordKey(ApiCallerTypeEnum apiCallerTypeEnum);


    /**
     * 通知oc用户修改密码
     * @param userId 用户id
     * @param sourceTerminalId 操作终端id
     */
    void sendUserPasswordChange(UUID userId, String sourceTerminalId);
}
