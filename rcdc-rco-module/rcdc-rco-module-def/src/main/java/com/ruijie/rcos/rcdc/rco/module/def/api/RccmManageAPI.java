package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.dto.IacAdminDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.RccmServerConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageClusterVersionInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm.UnifiedManageForMasterClusterAllDataDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.rccm.UnifiedManageFunctionKeyEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.rccm.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ForwardRcdcResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.rccm.RccmManageResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月8日
 *
 * @author lihengjing
 */
public interface RccmManageAPI {

    /**
     * rccm 发起加入纳管请求
     *
     * @param rccmManageRequest 入参
     * @return 响应 code 0 成功
     * @throws BusinessException 业务异常
     */
    RccmManageResponse joinManage(RccmManageRequest rccmManageRequest) throws BusinessException;

    /**
     * rccm 更新统一登入开关
     *
     * @param rccmManageRequest 入参
     * @return 响应 code 0 成功
     * @throws BusinessException 业务异常
     */
    RccmManageResponse updateUnifiedLogin(RccmManageRequest rccmManageRequest) throws BusinessException;

    /**
     * rccm 发起退出纳管请求
     *
     * @param rccmManageRequest 入参
     * @return 响应 code 0 成功
     * @throws BusinessException 业务异常
     */
    RccmManageResponse exitManage(RccmManageRequest rccmManageRequest) throws BusinessException;


    /**
     * CDC主动退出纳管
     *
     * @throws BusinessException 业务异常
     */
    void activeExitManage() throws BusinessException;

    /**
     * 是否存在纳管
     *
     * @param clusterId 集群id
     * @return true 存在
     */
    RccmManageResponse existingManage(String clusterId);

    /**
     * 推送指定用户列表信息到rccm
     *
     * @param userNameList userNameList
     * @param isDirectPush 是否直接推送不判断
     */
    void pushUserToRccm(List<String> userNameList, Boolean isDirectPush);

    /**
     * 推送所有有桌面资源的用户信息到rccm
     *
     * @throws BusinessException 业务异常
     */
    void pushAllUserToRccm() throws BusinessException;

    /**
     * 是否能够修改辅助认证策略
     *
     * @return true 存在
     */
    boolean canModifyGlobalSettings();

    /**
     * 删除rccm用户集群关系缓存
     *
     * @param usernameList 用户名列表
     * @param force        true:不判断,直接推送  false:判断用户是否需要推送
     * @throws BusinessException 业务异常
     */
    void delRccmUserCLuster(List<String> usernameList, Boolean force);

    /**
     * RCCM服务是否就绪
     *
     * @return true 已就绪，false 未就绪
     */
    boolean isReadyToPush();

    /**
     * 检查多集群 统一登入是否可用
     *
     * @return 响应 统一登入是否可用
     */
    boolean isUnifiedLogin();

    /**
     * 获取rccm配置信息
     *
     * @return 配置信息
     */
    RccmServerConfigDTO getRccmServerConfig();

    /**
     * 获取rccm配置信息 统一登陆是否开启，空为不开启，
     *
     * @return 配置信息
     */
    boolean getRccmServerHasUnifiedLoginConfig();

    /**
     * 是否存在用户
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existUser(String username);

    /**
     * 测试健康状态
     *
     * @param request 集群id
     * @return 相关操作码
     */
    RccmManageResponse testHealthState(RccmManageStateRequest request);

    /**
     * 更新rccmvip
     *
     * @param clusterId 集群id
     * @param serverIp  vip
     */
    void updateRccmVIP(UUID clusterId, String serverIp);

    /**
     * 校验管理员账号密码
     *
     * @param request 入参
     * @return 结果
     * @throws BusinessException 业务异常
     */
    IacAdminDTO validateAdminPwd(VerifyAdminRequest request) throws BusinessException;

    /**
     * 登录认证
     *
     * @param request 入参
     * @return 结果
     * @throws BusinessException 业务异常
     */
    IacAdminDTO loginAdminAuth(VerifyAdminRequest request) throws BusinessException;

    /**
     * 是否其他集群请求
     *
     * @param webClientRequest webClient请求（ClusterId有值且不为本集群时，需要通过RCenter转发请求到对应集群）
     * @return boolean
     */
    boolean isOtherClusterRequest(@Nullable WebClientRequest webClientRequest);

    /**
     * 通过RCenter转发Rcdc请求到真实集群
     *
     * @param requestData 请求信息
     * @return JSONObject
     * @throws BusinessException 业务异常
     */
    ForwardRcdcResponse forwardRequestByClusterId(ForwardRcdcRequest requestData) throws BusinessException;

    /**
     * 通过RCenter广播所有集群
     *
     * @param webclientNotifyRequest 请求信息
     */
    void broadcastWebclientNotify(WebclientNotifyRequest webclientNotifyRequest);

    /**
     * 推送ad域安全组消息
     */
    void pushAdGroupToRccm();

    /**
     * 集群版本、统一管理支持功能信息获取接口
     *
     * @return 版本信息
     * @throws BusinessException 业务异常
     */
    UnifiedManageClusterVersionInfoDTO getVersionInfo() throws BusinessException;

    /**
     * 通知开启/关闭同步模式
     *
     * @param unifiedManageNotifyConfigRequest 统一管理配置请求体
     * @return 响应体
     */
    RccmManageResponse notifyStrategy(RccmUnifiedManageNotifyConfigRequest unifiedManageNotifyConfigRequest);

    /**
     * 统一管理是否需要刷新数据
     *
     * @param functionKey 功能key
     * @return false:不需要  true:需要
     */
    boolean isClusterUnifiedManageNeedRefresh(UnifiedManageFunctionKeyEnum functionKey);

    /**
     * 获取统一管理策略列表
     * @return 策略列表
     */
    List<RccmClusterUnifiedManageStrategyDTO> strategyList();

    /**
     * 保存统一管理数据
     * @param request 请求
     */
    void saveUnifiedManage(UnifiedManageDataRequest request);

    /**
     * 删除统一管理数据
     * @param request 请求
     */
    void deleteUnifiedManage(UnifiedManageDataRequest request);

    /**
     * 统一管理数据是否需要被锁定
     *
     * @param functionKey 功能key
     * @return false:不需要  true:需要
     */
    boolean needUnifiedManageLock(UnifiedManageFunctionKeyEnum functionKey);

    /**
     * 根据类型统一管理全量数据收集
     *
     * @param relateTypeArr 功能key，如果传空则查询所有类型
     * @return 主集群全量数据ID集合
     */
    List<UnifiedManageForMasterClusterAllDataDTO> collectAllDataByType(@Nullable UnifiedManageFunctionKeyEnum... relateTypeArr);

    /**
     * 统一管理数据查询
     * @param unifiedManageDataId  管理ID
     * @return 统一管理数据
     */
    UnifiedManageDataDTO findByUnifiedManageDataId(UUID unifiedManageDataId);

    /**
     * 策略创建通知
     * @param deskStrategyId 策略ID
     * @throws BusinessException 业务异常
     */
    void createNotify(UUID deskStrategyId) throws BusinessException;

    /**
     * 策略修订通知
     * @param deskStrategyId 策略ID
     * @throws BusinessException 业务异常
     */
    void updateNotify(UUID deskStrategyId) throws BusinessException;

    /**
     * 根据关联id和资源类型查找统一管理数据唯一ID
     *
     * @param relatedId 关联id
     * @param type 类型
     * @return 统一管理数据唯一ID
     */
    UUID getUnifiedManageDataId(UUID relatedId, UnifiedManageFunctionKeyEnum type);

    /**
     * 判断资源是否存在
     *
     * @param relatedId 关联id
     * @param relatedType      类型
     * @return 是否存在
     */
    boolean existsUnifiedData(UUID relatedId, UnifiedManageFunctionKeyEnum relatedType);

    /**
     * 是否属于主集群
     * @return true 主集群
     */
    boolean isMaster();

    /**
     * 是否属于从集群
     * @return false 从集群
     */
    boolean isSlave();

    /**
     * 根据关联ID列表和类型查询
     *
     * @param relatedType 类型
     * @param relatedIdList 关联id列表
     * @return 结果集
     */
    List<UnifiedManageDataDTO> findByRelatedTypeAndRelatedIdIn(UnifiedManageFunctionKeyEnum relatedType , List<UUID> relatedIdList);

    /**
     * 校验RCenter上管理员配置的CDC的VIP是否正确
     *
     * @param clusterVip 管理员配置的CDC的VIP
     * @throws BusinessException 业务异常
     */
    void validateRcdcVip(String clusterVip) throws BusinessException;

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
     * 推送用户
     * @param userIdList userId列表
     */
    void pushUserByUserIdList(List<UUID> userIdList);
}
