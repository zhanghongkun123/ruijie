package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolAssignResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Description: 桌面池用户相关API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/07/06
 *
 * @author linke
 */
public interface DesktopPoolUserMgmtAPI {

    /**
     * 检查是否有用户、终端在使用桌面池中的桌面
     *
     * @param desktopPoolId 桌面池id
     * @return true-在使用；false-不在使用
     */
    boolean checkIsDesktopInUse(UUID desktopPoolId);

    /**
     * 查询桌面池关联用户或用户组列表
     *
     * @param desktopPoolId 桌面池Id
     * @param relatedType   关联类型
     * @return List<DesktopPoolUserDTO>
     */
    List<DesktopPoolUserDTO> listDesktopPoolUser(UUID desktopPoolId, @Nullable IacConfigRelatedType relatedType);

    /**
     * 解除所有与该用户或用户组的绑定的桌面池
     *
     * @param userId 分页参数
     */
    void unbindUserAllDesktopPool(UUID userId);

    /**
     * 分配池中的桌面给用户
     *
     * @param userId        用户ID
     * @param desktopPoolId 桌面池ID
     * @return AllotDesktopResultDTO
     * @throws BusinessException 业务异常
     */
    DesktopPoolAssignResultDTO assignDesktop(UUID userId, UUID desktopPoolId) throws BusinessException;

    /**
     * 检查对象是否和桌面池有绑定关系
     *
     * @param desktopPoolId 分页参数
     * @param userId        用户ID
     * @return true已绑定池，false未绑定
     */
    boolean checkUserInDesktopPool(UUID desktopPoolId, UUID userId);

    /**
     * 分页查询桌面池关联的所有用户（用户组下的用户+分配的用户）
     *
     * @param desktopPoolId 桌面池ID
     * @param pageRequest   分页参数
     * @return DefaultPageResponse<UserListDTO>
     */
    DefaultPageResponse<UserListDTO> pageQueryRealBindUser(UUID desktopPoolId, PageSearchRequest pageRequest);

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
     * 修改桌面池绑定对像关联关系
     *
     * @param updatePoolBindObjectDTO UpdatePoolBindObjectDTO
     * @throws BusinessException 业务异常
     */
    void updatePoolBindObject(UpdatePoolBindObjectDTO updatePoolBindObjectDTO) throws BusinessException;

    /**
     * 根据PageQueryRequest分页查询
     *
     * @param request UserAssignmentPageRequest
     * @return PageQueryResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageUserWithAssignment(UserAssignmentPageRequest request) throws BusinessException;

    /**
     * 记录分配资源失败的警告日志
     *
     * @param userName       userName
     * @param desktopPoolDTO desktopPoolDTO
     * @throws BusinessException 业务异常
     */
    void saveAssignFailWarnLog(String userName, CbbDesktopPoolDTO desktopPoolDTO) throws BusinessException;

    /**
     * 查询所有桌面池关联的用户,返回用户名列表
     *
     * @return 用户名列表
     * @throws BusinessException 业务异常
     */
    List<String> listDesktopPoolAllUserName() throws BusinessException;


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
     * 移动桌面
     *
     * @param desktopDetailDTO     桌面信息
     * @param targetDesktopPoolDTO 目标桌面池
     * @throws BusinessException ex
     */
    void moveDesktop(CloudDesktopDetailDTO desktopDetailDTO, CbbDesktopPoolDTO targetDesktopPoolDTO) throws BusinessException;

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
}
