package com.ruijie.rcos.rcdc.rco.module.impl.userprofile.tx;

import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfilePathGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.userprofile.dto.UserProfileStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfilePathGroupEntity;
import com.ruijie.rcos.rcdc.rco.module.impl.userprofile.entity.UserProfileStrategyEntity;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.List;
import java.util.UUID;

/**
 * Description: 用户配置管理事务API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/12
 *
 * @author WuShengQiang
 */
public interface UserProfileServiceTx {

    /**
     * 删除路径组
     *
     * @param entity 组对象
     * @throws BusinessException 业务异常
     */
    void deleteUserProfilePathGroup(UserProfilePathGroupEntity entity) throws BusinessException;

    /**
     * 移动路径
     *
     * @param idList        路径id列表
     * @param targetGroupId 组id
     * @throws BusinessException 业务异常
     */
    void moveUserProfilePath(List<UUID> idList, UUID targetGroupId) throws BusinessException;

    /**
     * 根据路径ID删除路径
     *
     * @param id 路径id
     * @throws BusinessException 业务异常
     */
    void deleteUserProfilePath(UUID id) throws BusinessException;

    /**
     * 创建用户配置策略
     *
     * @param userProfileStrategyDTO 策略对象
     * @throws BusinessException 业务异常
     */
    void createUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException;

    /**
     * 编辑用户配置策略
     *
     * @param userProfileStrategyDTO 策略对象
     * @throws BusinessException 业务异常
     */
    void editUserProfileStrategy(UserProfileStrategyDTO userProfileStrategyDTO) throws BusinessException;

    /**
     * 删除用户配置策略
     *
     * @param entity 策略对象
     * @throws BusinessException 业务异常
     */
    void deleteUserProfileStrategy(UserProfileStrategyEntity entity) throws BusinessException;

    /**
     * 获取路径组ID 不存在则创建
     *
     * @param groupDTO 组对象
     * @return 组ID
     */
    UUID getPathGroupIdIfNotExistCreate(UserProfilePathGroupDTO groupDTO);
}
