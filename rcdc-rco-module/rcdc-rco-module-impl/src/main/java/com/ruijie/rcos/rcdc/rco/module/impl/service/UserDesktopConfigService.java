package com.ruijie.rcos.rcdc.rco.module.impl.service;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserGroupDesktopConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CreateUserGroupDesktopConfigRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author jarman
 */
public interface UserDesktopConfigService {

    /**
     * 创建用户桌面配置
     *
     * @param request request
     */
    void createOrUpdateUserDesktopConfig(CreateUserDesktopConfigRequest request);

    /**
     * 创建用户组桌面配置
     *
     * @param request request
     */
    void createOrUpdateUserGroupDesktopConfig(CreateUserGroupDesktopConfigRequest request);

    /**
     * 获取用户桌面配置
     *
     * @param userId userId
     * @param deskType deskType
     * @return UserDesktopConfigDTO
     */
    UserDesktopConfigDTO getUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType);

    /**
     * 删除用户桌面配置
     *
     * @param userId userId
     * @param deskType deskType
     */
    void deleteUserDesktopConfig(UUID userId, UserCloudDeskTypeEnum deskType);

    /**
     * 删除用户组桌面配置
     *
     * @param groupId groupId
     * @param deskType deskType
     */
    void deleteUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType);


    /**
     * 获取用户组桌面配置
     *
     * @param groupId groupId
     * @param deskType deskType
     * @return UserDesktopConfigDTO
     */
    UserGroupDesktopConfigDTO getUserGroupDesktopConfig(UUID groupId, UserCloudDeskTypeEnum deskType);

    /**
     * 删除访客用户云桌面配置
     *
     * @param userId userId
     */
    void deleteVisitorUserDesktopConfig(UUID userId);

    /**
     * 根据网络策略id获取配置列表
     *
     * @param networkId networkId
     * @return List<UserGroupDesktopConfigDTO>
     */
    List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigList(UUID networkId);

    /**
     * 判断策略是否绑定用户组
     * 
     * @param strategyId 策略Id
     * @return true - 已绑定 false - 未绑定
     */
    Boolean isStrategyBind(UUID strategyId);

    /**
     * 判断传入的镜像ID是否绑定用户组
     * 
     * @param imageId 镜像ID
     * @return true - 绑定
     */
    Boolean hasImageBindUserGroup(UUID imageId);

    /**
     * 根据桌面类型获取桌面配置列表
     *
     * @param deskType deskType
     * @return List<UserGroupDesktopConfigDTO>
     */
    List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByDeskType(UserCloudDeskTypeEnum deskType);

    /**
     * 根据桌面策略ID获取桌面配置列表
     *
     * @param strategyId strategyId
     * @return List<UserGroupDesktopConfigDTO>
     */
    List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByStrategyId(UUID strategyId);

    /**
     * 分页查询用户组信息
     * @param request 请求
     * @return 分页响应
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<UserGroupDesktopConfigDTO> pageQueryUserGroupDesktopConfigDTO(PageQueryRequest request) throws BusinessException;
}
