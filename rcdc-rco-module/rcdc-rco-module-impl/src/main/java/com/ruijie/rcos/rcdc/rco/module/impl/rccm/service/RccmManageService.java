package com.ruijie.rcos.rcdc.rco.module.impl.rccm.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.api.request.user.IacUserIdentityConfigRequest;
import com.ruijie.rcos.gss.sdk.iac.module.def.dto.user.IacUserDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserSyncDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.RccmClusterUnifiedManageStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.UnifiedManageMasterRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: RccmManageService
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/9
 *
 * @author lihengjing
 */
public interface RccmManageService {

    /**
     * 通知开始建立RCCM反向连接通道
     *
     * @throws BusinessException 业务异常
     */
    void notifyListenRccmServer() throws BusinessException;

    /**
     * 保存或修改RCCM服务器配置
     *
     * @param rccmServerConfigDTO RCCM服务器配置对象
     */
    void saveOrUpdateRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO);

    /**
     * 保存RCCM服务器配置
     *
     * @param rccmServerConfigDTO RCCM服务器配置对象
     */
    void saveRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO);

    /**
     * 更新RCCM服务器配置(RCCM服务器IP不允许变更)
     *
     * @param rccmServerConfigDTO RCCM服务器配置对象
     */
    void updateRccmServerConfig(RccmServerConfigDTO rccmServerConfigDTO);

    /**
     * 获取当前保存的RCCM服务器配置信息对象
     *
     * @return 响应RCCM服务器配置信息对象
     */
    RccmServerConfigDTO getRccmServerConfig();

    /**
     * 获取rccm配置信息 统一登陆是否开启，空为不开启，
     *
     * @return 配置信息
     */
    boolean getRccmServerHasUnifiedLoginConfig();

    /**
     * 检查多集群 统一登入是否可用
     *
     * @return 响应 统一登入是否可用
     */
    boolean isUnifiedLogin();

    /**
     * 检查多集群开启统一登入和未开启辅助认证
     *
     * @return 响应 统一登入是否可用
     */
    boolean isUnifiedLoginAndNotAssistAuth();

    /**
     * 检查是否可以编辑辅助认证策略（RCCM被纳管且开启统一登录功能）
     *
     * @return 响应 是否可以编辑辅助认证策略
     */
    boolean canModifyGlobalSettings();

    /**
     * 更新 统一登入开关
     *
     * @param hasUnifiedLogin 开关
     * @param enableAssistAuth 辅助认证开关
     * @throws BusinessException BusinessException
     */
    void updateUnifiedLogin(Boolean hasUnifiedLogin, Boolean enableAssistAuth) throws BusinessException;

    /**
     * 通知退出RCCM纳管
     *
     * @throws BusinessException 业务异常
     */
    void exitJoinRccmServer() throws BusinessException;

    /**
     * 推送用户信息到RCCM
     *
     * @param usernameList 用户名列表
     * @param force true:不判断,直接推送 false:判断用户是否需要推送
     */
    void pushUser(List<String> usernameList, boolean force);

    /**
     * 推送ad域安全组消息
     *
     * @param hasAdGroup 是否有安全组
     */
    void pushAdGroupToRccm(Boolean hasAdGroup);


    /**
     * 主集群同步用户组数据
     *
     * @param userGroupSyncDataDTO 是否有安全组
     * @throws BusinessException 业务异常
     */
    void syncUserGroup(UserGroupSyncDataDTO userGroupSyncDataDTO) throws BusinessException;


    /**
     * 主集群同步用户数据
     *
     * @param userSyncDataDTO 用户数据
     * @throws BusinessException 业务异常
     */
    void syncUser(UserSyncDataDTO userSyncDataDTO) throws BusinessException;

    /**
     * 保存统一管理策略配置
     *
     * @param strategyList 统一管理策略配置列表
     */
    void saveClusterUnifiedManageStrategy(List<RccmClusterUnifiedManageStrategyDTO> strategyList);

    /**
     * 获取统一管理策略配置
     *
     * @return 统一管理策略配置列表
     */
    List<RccmClusterUnifiedManageStrategyDTO> getClusterUnifiedManageStrategy();

    /**
     * 推送云桌面策略给RCCM
     *
     * @param masterRequest 请求参数
     * @throws BusinessException 业务异常
     */
    void pushDeskStrategyToRccm(UnifiedManageMasterRequest<?> masterRequest) throws BusinessException;


    /**
     * 是否属于主集群
     *
     * @return true 主集群
     */
    boolean isMaster();


    /**
     * 是否属于从集群
     *
     * @return false 从集群
     */
    boolean isSlave();

    /**
     * 通知rcenter 同步密码
     * @param userDetail 用户信息
     */
    void notifyUserUpdatePwd(IacUserDetailDTO userDetail);

    /**
     * 添加rest接口同步密码标识
     * @param userName 用户名
     */
    void addSyncUserPasswordCache(String userName);

    /**
     * 删除rest接口同步密码标识
     * @param userName 用户名
     */
    void delSyncUserPasswordCache(String userName);

    /**
     * 通知rcenter更新用户辅助认证配置信息
     * @param configRequest  请求参数
     */
    void notifyUpdateUserIdentityConfig(IacUserIdentityConfigRequest configRequest);

    /**
     * 是否支持动态口令直接登录
     * @return  是否
     */
    Boolean hasOtpCodeTab();

    /**
     * 推送用户
     *
     * @param userIdList 用户id列表
     */
    void pushUserByUserIdList(List<UUID> userIdList);
}
