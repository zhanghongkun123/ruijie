package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserRcaGroupDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.UUID;

/**
 * Description: 应用分组分配关系API, 与用户信息强相关，单独放置在RCO层，其余功能在RCA-API中处理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月12日
 *
 * @author zhengjingyong
 */
public interface RcoGroupMemberAPI {

    /**
     * 分页查询应用分组关联的所有用户（用户组下的用户+分配的用户）
     *
     * @param appGroupId  应用分组id
     * @param pageRequest   分页参数
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageQueryRealBindUser(UUID appGroupId, PageSearchRequest pageRequest) throws BusinessException;

    /**
     * 根据PageQueryRequest分页查询
     *
     * @param request UserAssignmentPageRequest
     * @return PageQueryResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageUserWithAssignment(UserAssignmentPageRequest request) throws BusinessException;

    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param desktopPoolId 桌面池ID
     * @return 用户组下已分配的用户数量
     */
    Integer countAssignedUserNumByGroup(UUID groupId, UUID desktopPoolId);


    /**
     * 分页查询AD域组关联的应用分组列表
     *
     * @param adGroupId adGroupId
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserRcaGroupDTO> pageQueryByAdGroup(UUID adGroupId, PageSearchRequest request) throws BusinessException;

    /**
     * 分页查询用户关联的应用分组列表
     *
     * @param userId userId
     * @param request 页面请求
     * @return DefaultPageResponse
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserRcaGroupDTO> pageQueryByUser(UUID userId, PageSearchRequest request) throws BusinessException;

}
