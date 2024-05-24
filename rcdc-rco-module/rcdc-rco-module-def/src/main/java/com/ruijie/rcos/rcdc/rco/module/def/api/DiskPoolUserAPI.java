package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.lang.Nullable;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.UserAssignmentPageRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UpdatePoolBindObjectDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupAssignedUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.UserGroupDisabledUserNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserWithAssignmentDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryAPI;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 磁盘池关联用户相关
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/11
 *
 * @author TD
 */
public interface DiskPoolUserAPI extends PageQueryAPI<DiskPoolUserWithAssignmentDTO> {

    /**
     * 分页查询桌面池关联的用户组下+分配的用户
     *
     * @param pageRequest 分页参数
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DiskPoolUserWithAssignmentDTO> pageUserWithAssignment(PageQueryRequest pageRequest) throws BusinessException;

    /**
     * 分页查询桌面池关联的用户组下+分配的用户
     *
     * @param pageRequest 分页参数
     * @return DefaultPageResponse<UserListDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageUserForDistribution(UserAssignmentPageRequest pageRequest) throws BusinessException;

    /**
     * 检查对象是否和磁盘池有绑定关系/对象是否绑定磁盘池
     *
     * @param diskPoolId 分页参数
     * @param relatedId 关联对象ID
     * @return true已绑定池，false未绑定
     */
    boolean checkUserInDiskPool(UUID diskPoolId, UUID relatedId);

    /**
     * 分页查询磁盘池关联的所有用户（用户组下的用户+分配的用户）
     *
     * @param diskPoolId 磁盘池ID
     * @param request 请求参数
     * @return 磁盘池关联的用户
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<UserListDTO> pageQueryDiskPoolBindUser(UUID diskPoolId, PageSearchRequest request) throws BusinessException;

    /**
     * 获取关联对象所在的池信息
     * 
     * @param relatedId 关联ID
     * @return 磁盘池关联的用户
     * @throws BusinessException 业务异常
     */
    Optional<DiskPoolUserDTO> listPoolIdByRelatedId(UUID relatedId) throws BusinessException;

    /**
     * 获取用户组下已分配的用户数量
     *
     * @param groupId       用户组ID
     * @param diskPoolId 池ID
     * @return 用户组下已分配的用户数量
     */
    Integer countAssignedUserNumByGroup(UUID groupId, UUID diskPoolId);

    /**
     * 根据池ID获取关联用户组下已分配的用户数量
     *
     * @param diskPoolId 池ID
     * @return List<UserGroupAssignedUserNumDTO> 用户组下已分配的用户数量
     */
    List<UserGroupAssignedUserNumDTO> countAssignedUserNumInGroupByDiskPoolId(UUID diskPoolId);

    /**
     * 根据关联类型查询
     *
     * @param relatedType 关联类型
     * @return List<DiskPoolUserDTO>
     */
    List<DiskPoolUserDTO> listDiskPoolUserByRelatedType(IacConfigRelatedType relatedType);

    /**
     * 修改池绑定对像关联关系
     *
     * @param updatePoolBindObjectDTO UpdatePoolBindObjectDTO
     * @throws BusinessException 业务异常
     */
    void updatePoolBindObject(UpdatePoolBindObjectDTO updatePoolBindObjectDTO) throws BusinessException;

    /**
     * 根据组ID获取不可选择的用户数量，组ID为空时返回所有不可选数量
     *
     * @param diskPoolId diskPoolId
     * @param groupId groupId
     * @return 不可选择的用户数量
     */
    long getDisabledUserNum(UUID diskPoolId, @Nullable UUID groupId);

    /**
     * 根据池ID获取关联用户组下不可选的用户数量
     *
     * @param diskPoolId 池ID
     * @return List<UserGroupDisabledUserNumDTO> 用户组下不可选的用户数量
     */
    List<UserGroupDisabledUserNumDTO> countDisabledUserNumInGroupByDiskPoolId(UUID diskPoolId);

    /**
     * 根据磁盘池ID获取有分配关系的用户组
     * @param diskPoolId 磁盘池ID
     * @return 用户组集合
     */
    Set<String> getDiskPoolRelationUserGroup(UUID diskPoolId);

    /**
     * 保存磁盘池分配失败的告警日志
     * @param userName 用户名称
     * @param diskPoolDTO 磁盘池
     */
    void saveDiskPoolAssignFailWarnLog(String userName, CbbDiskPoolDTO diskPoolDTO);

    /**
     * 磁盘池添加分配用户
     * @param diskPoolId 磁盘池ID
     * @param userId 用户ID
     */
    void saveDiskPoolRelatedUser(UUID diskPoolId, UUID userId);
}
