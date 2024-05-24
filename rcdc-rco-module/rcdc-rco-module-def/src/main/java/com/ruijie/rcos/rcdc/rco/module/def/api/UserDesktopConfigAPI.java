package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.UserWithAssignmentPageResponse;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.enums.UserCloudDeskTypeEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 用户桌面配置api
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/7/22
 *
 * @author Jarman
 */
public interface UserDesktopConfigAPI {

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
     * 获取用户列表
     *
     * @param request 请求参数对象
     * @return 返回查询
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageQuery(PageSearchRequest request) throws BusinessException;

    /**
     * 根据用户ID列表获取用户信息
     *
     * @param idArr 用户id列表
     * @return 返回查询
     * @throws BusinessException 业务异常
     */
    List<ExportUserViewDTO> getUserList(List<UUID> idArr) throws BusinessException;

    /**
     * 获取所有用户信息
     *
     * @return 返回查询
     * @throws BusinessException 业务异常
     */
    List<ExportUserViewDTO> getAllUserList() throws BusinessException;

    /**
     * 判断策略是否绑定
     * 
     * @param strategyId 策略id
     * @return true 绑定用户或者用户组 false 未绑定
     */
    boolean isStrategyBind(UUID strategyId);

    /**
     * 判断传入的镜像Id是否有绑定用户组
     * 
     * @param imageId 镜像ID
     * @return true - 有绑定 false - 未绑定
     */
    boolean hasImageBindUserGroup(UUID imageId);

    /**
     * 导出用户数据，
     *
     * @param request 查询条件
     * @param cacheKey 数据缓存键
     * @return 返回查询结果
     * @throws BusinessException 业务异常
     */
    ExportUserPageDTO getExportUserList(UserPageSearchRequest request, String cacheKey) throws BusinessException;

    /**
     * 根据参数，获取用户列表及其分配信息
     *
     * @param pageRequest 请求参数对象
     * @return 返回查询
     * @throws BusinessException 业务异常
     */
    UserWithAssignmentPageResponse pageQueryWithAssignment(UserAssignmentPageRequest pageRequest) throws BusinessException;

    /**
     * 根据桌面类型获取桌面配置列表
     *
     * @param deskType deskType
     * @return List<UserGroupDesktopConfigDTO>
     */
    List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByDeskType(UserCloudDeskTypeEnum deskType);

    /**
     * 根据云桌面策略id获取配置列表
     *
     * @param strategyId strategyId
     * @return List<UserGroupDesktopConfigDTO>
     */
    List<UserGroupDesktopConfigDTO> getUserGroupDesktopConfigListByStrategyId(UUID strategyId);

    /**
     * 分页查询用户组信息
     *
     * @param request 请求
     * @return 分页响应
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<UserGroupDesktopConfigDTO> pageQueryUserGroupDesktopConfigDTO(PageQueryRequest request) throws BusinessException;
}
