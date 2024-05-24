package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.UserDesktopBindUserRequest;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 桌面池与用户/用户组关联
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/20 14:49
 *
 * @author linke
 */
public interface DesktopPoolUserService {

    /**
     * 检查对象是否和桌面池有绑定关系
     *
     * @param desktopPoolId 分页参数
     * @param userId        用户ID
     * @return true已绑定池，false未绑定
     */
    boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId);

    /**
     * 根据桌面池id和关联对象ID查询是否存在
     *
     * @param desktopPoolId 桌面池id
     * @param relatedId     关联对象ID
     * @return true 存在, false不存在
     */
    boolean checkRelatedIdExist(UUID desktopPoolId, UUID relatedId);

    /**
     * 根据关联对象ID查询桌面池ID列表
     *
     * @param relatedId 关联对象ID
     * @return List<UUID> 桌面池ID列表
     */
    List<UUID> listPoolIdByRelatedId(UUID relatedId);

    /**
     * 根据关联对象ID查询桌面池ID列表
     *
     * @param relatedIdList 关联对象ID列表
     * @return List<UUID> 桌面池ID列表
     */
    List<UUID> listPoolIdByRelatedIdIn(List<UUID> relatedIdList);

    /**
     * 查询桌面池关联用户、用户组或AD域组列表
     *
     * @param desktopPoolId 桌面池Id
     * @param relatedType   关联类型
     * @return List<DesktopPoolUserDTO>
     */
    List<DesktopPoolUserDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable IacConfigRelatedType relatedType);

    /**
     * 解除所有与该用户或用户组的绑定的桌面池
     *
     * @param relatedId relatedId
     */
    void deleteByRelatedId(UUID relatedId);

    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId);

    /**
     * 根据桌面池ID获取关联用户组下已分配的用户数量
     *
     * @param desktopPoolId 桌面池ID
     * @return List<UserGroupAssignedUserNumDTO> 用户组下已分配的用户数量
     */
    List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID删除所有分配对象记录
     *
     * @param desktopPoolId 桌面池ID
     */
    void deleteByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID和用户组ID获取related_type=USER的对象ID列表
     *
     * @param desktopPoolId   桌面池ID
     * @param userGroupIdList 用户组ID列表
     * @return List<UUID> 用户组下已分配的用户数量
     */
    List<UUID> listBindUserIdByUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList);

    /**
     * 查询所有桌面池关联的用户信息,去重
     *
     * @return List<DesktopPoolUserDTO>
     */
    List<DesktopPoolUserDTO> listDesktopPoolAllUser();

    /**
     * 根据用户ID列表查询已经绑定了桌面池的用户名列表
     *
     * @param userIdList 用户ID列表
     * @return List<String> 用户名列表
     */
    List<String> listBindDesktopPoolUserName(List<UUID> userIdList);

    /**
     * 根据池ID获取有分配关系的用户组
     * @param desktopPoolId 池ID
     * @return 用户组集合
     */
    Set<String> getDesktopPoolRelationUserGroup(UUID desktopPoolId);

    /**
     * 获取分配关系的数量
     * @param relatedType 关联类型
     * @return 数量
     */
    int countByRelatedType(IacConfigRelatedType relatedType);

    /**
     * 根据关联对象ID集合查询桌面池ID列表
     *
     * @param relatedIdList 关联对象ID集合
     * @param relatedType 关联类型
     * @return List<UUID> 桌面池ID列表
     */
    List<UUID> listPoolIdByRelatedIdList(List<UUID> relatedIdList, IacConfigRelatedType relatedType);

    /**
     * 池中云桌面绑定用户同时修改云桌面名称
     *
     * @param request request
     * @throws BusinessException 业务异常
     */
    void poolDesktopBindUser(UserDesktopBindUserRequest request) throws BusinessException;

    /**
     * 池中云桌面绑定用户同时修改云桌面名称
     *
     * @param desktopInfoDTO desktopInfoDTO
     * @param userId userId
     * @throws BusinessException 业务异常
     */
    void poolDesktopBindUser(PoolDesktopInfoDTO desktopInfoDTO, UUID userId);

    /**
     * 检查用户在桌面池中是否有绑定的非回收站的桌面
     *
     * @param desktopPoolId desktopPoolId
     * @param userId        userId
     * @return true存在；false不存在
     */
    boolean checkUserBindDesktopInPool(UUID desktopPoolId, UUID userId);

    /**
     * 检查用户列表在桌面池中是否有绑定的非回收站的桌面
     *
     * @param desktopPoolId desktopPoolId
     * @param userIdList        userIdList
     * @return true存在；false不存在
     */
    boolean checkUserIdListBindDesktopInPool(UUID desktopPoolId, List<UUID> userIdList);

    /**
     * 将用户加入桌面池
     *
     * @param desktopPoolId desktopPoolId
     * @param userId        userId
     */
    void addUserToDesktopPool(UUID desktopPoolId, UUID userId);

    /**
     * 用户编辑后，处理用户与桌面池关联记录
     *
     * @param userId 用户ID
     */
    void dealRelationAfterUpdateUser(UUID userId);

    /**
     * 获取池中指定用户的运行桌面ID
     * @param desktopPoolId 桌面池ID
     * @param userId 用户ID
     * @return 运行桌面ID
     */
    UUID findDeskRunningByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId);

    /**
     * 根据池ID，关联对象类型，关联对象ID列表查询已存在的关联对象ID列表
     *
     * @param desktopPoolId   池ID
     * @param relatedType     关联对象类型
     * @param inRelatedIdList 关联对象ID列表
     * @return 已存在的关联对象ID列表
     */
    List<UUID> listRelatedIdByPoolIdAndRelatedObj(UUID desktopPoolId, IacConfigRelatedType relatedType, List<UUID> inRelatedIdList);

    /**
     * 检查用户是否已添加到池，如果为安全组用户，添加记录到DesktopPoolUserEntity
     *
     * @param desktopPoolId 池ID
     * @param userId        用户ID
     * @throws BusinessException 业务异常
     */
    void checkAndSaveAdGroupUser(UUID desktopPoolId, UUID userId) throws BusinessException;

    /**
     * 第三方桌面池的桌面绑定用户
     * @param request request
     * @throws BusinessException 业务异常
     */
    void thirdPartyPoolDesktopBindUser(UserDesktopBindUserRequest request) throws BusinessException;
}
