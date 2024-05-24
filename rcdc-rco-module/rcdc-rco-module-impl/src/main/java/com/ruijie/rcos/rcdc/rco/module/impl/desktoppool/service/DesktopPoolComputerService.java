package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.ComputerRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolComputerDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.entity.DesktopPoolComputerEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 桌面池与PC终端/PC终端组关联
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/30
 *
 * @author zqj
 */
public interface DesktopPoolComputerService {

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
     * 根据桌面池id和关联对象ID查询是否存在
     *
     * @param relatedId     关联对象ID
     * @return true 存在, false不存在
     */
    boolean checkRelatedIdExist(UUID relatedId);

    /**
     * 根据关联对象ID查询桌面池ID列表
     *
     * @param relatedId 关联对象ID
     * @return List<UUID> 桌面池ID列表
     */
    List<UUID> listPoolIdByRelatedId(UUID relatedId);

    /**
     * 查询桌面池关联用户、用户组或AD域组列表
     *
     * @param desktopPoolId 桌面池Id
     * @param relatedType   关联类型
     * @return List<DesktopPoolComputerDTO>
     */
    List<DesktopPoolComputerDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable ComputerRelatedType relatedType);

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
     * @return List<DesktopPoolComputerDTO>
     */
    List<DesktopPoolComputerDTO> listDesktopPoolAllUser();

    /**
     * 根据用户ID列表查询已经绑定了桌面池的用户名列表
     *
     * @param userIdList 用户ID列表
     * @return List<String> 用户名列表
     */
    List<String> listBindDesktopPoolUserName(List<UUID> userIdList);

    /**
     * 根据池ID获取有分配关系的终端组
     *
     * @param desktopPoolId 池ID
     * @return 用户组集合
     */
    Set<String> getDesktopPoolRelationComputerGroup(UUID desktopPoolId);


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
     * 获取池中指定用户的运行桌面ID
     * @param desktopPoolId 桌面池ID
     * @param userId 用户ID
     * @return 运行桌面ID
     */
    UUID findDeskRunningByDesktopPoolIdAndUserId(UUID desktopPoolId, UUID userId);


    /**
     * 检查用户是否已添加到池，如果为安全组用户，添加记录到DesktopPoolUserEntity
     *
     * @param desktopPoolId 池ID
     * @param userId        用户ID
     * @throws BusinessException 业务异常
     */
    void checkAndSaveAdGroupUser(UUID desktopPoolId, UUID userId) throws BusinessException;

    /**
     * 统计桌面池的PC终端已存在数量
     * @param computerIdArr PC终端Id数组
     * @param desktopPoolId desktopPoolId
     * @return 数量
     */
    int countAssignedComputerNumInGroupByDesktopPoolId(UUID[] computerIdArr, UUID desktopPoolId);

    /**
     * 根据组id获取列表
     *
     * @param groupIdArr 组id数组
     * @return list
     */
    List<DesktopPoolComputerEntity> findDesktopPoolRelationComputerGroupList(List<UUID> groupIdArr);

    /**
     * 获取实体
     * @param relatedId relatedId
     * @return DesktopPoolComputerEntity
     */
    DesktopPoolComputerEntity findByRelatedId(UUID relatedId);

    /**
     * 获取关联PC终端组列表
     * @param computerRelatedType computerRelatedType
     * @return list
     */
    List<DesktopPoolComputerDTO> listDeskPoolComputerByRelatedType(ComputerRelatedType computerRelatedType);

    /**
     * 获取关联PC终端列表
     * @param computerIdArr computerIdArr
     * @return list
     */
    List<DesktopPoolComputerEntity> findDesktopPoolRelationComputerList(UUID[] computerIdArr);

    /**
     * 移除关系
     * @param poolId poolId
     * @param relatedId relatedId
     */
    void removeByPoolIdAndRelatedId(UUID poolId, UUID relatedId);
}
