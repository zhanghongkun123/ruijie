package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.UserListDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionCreateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.dto.DesktopTempPermissionUpdateDTO;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.UUID;

/**
 * Description: 临时权限API
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
public interface DesktopTempPermissionAPI {

    /**
     * 分页查询临时权限
     *
     * @param request 参数
     * @return PageQueryResponse<DesktopTempPermissionDetailDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DesktopTempPermissionDetailDTO> pageQuery(PageQueryRequest request) throws BusinessException;

    /**
     * 根据对象ID和对象类型查询关联的临时权限信息
     *
     * @param relatedId   对象ID
     * @param relatedType 对象类型
     * @return CbbDesktopTempPermissionDTO
     * @throws BusinessException 业务异常
     */
    CbbDesktopTempPermissionDTO getPermissionByRelatedIdAndType(UUID relatedId, DesktopTempPermissionRelatedType relatedType)
            throws BusinessException;

    /**
     * 获取临时权限详情
     *
     * @param id 临时权限ID
     * @return DesktopTempPermissionDetailDTO
     * @throws BusinessException 业务异常
     */
    DesktopTempPermissionDetailDTO getDetailInfo(UUID id) throws BusinessException;

    /**
     * 分页查询关联的用户
     *
     * @param desktopTempPermissionId 临时权限ID
     * @param request                 查询条件
     * @return DefaultPageResponse<UserListDTO>
     */
    DefaultPageResponse<UserListDTO> pageBindUser(UUID desktopTempPermissionId, PageSearchRequest request);

    /**
     * 分页查询关联的云桌面
     *
     * @param desktopTempPermissionId 临时权限ID
     * @param request                 查询条件
     * @return DefaultPageResponse<CloudDesktopDTO>
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<CloudDesktopDTO> pageBindDesktop(UUID desktopTempPermissionId, PageSearchRequest request) throws BusinessException;

    /**
     * 创建临时权限
     *
     * @param desktopTempPermissionCreateDTO DesktopTempPermissionCreateDTO
     * @throws BusinessException 业务异常
     */
    void createDesktopTempPermission(DesktopTempPermissionCreateDTO desktopTempPermissionCreateDTO) throws BusinessException;

    /**
     * 删除临时权限
     *
     * @param id 临时权限ID
     * @param isByManager 是否由管理员删除
     * @throws BusinessException 业务异常
     */
    void deleteById(UUID id, Boolean isByManager) throws BusinessException;

    /**
     * 编辑临时权限
     *
     * @param updateDTO DesktopTempPermissionUpdateDTO
     * @throws BusinessException 业务异常
     */
    void updateDesktopTempPermission(DesktopTempPermissionUpdateDTO updateDTO) throws BusinessException;

    /**
     * 发送生效消息给运行中的桌面
     *
     * @param permissionId permissionId
     * @throws BusinessException 业务异常
     */
    void sendEffectMessageByPermissionId(UUID permissionId) throws BusinessException;

    /**
     * 创建快到期的用户消息
     *
     * @param permissionId permissionId
     * @throws BusinessException 业务异常
     */
    void createUserExpireNoticeMsg(UUID permissionId) throws BusinessException;

    /**
     * 删除用户临时权限
     *
     * @param desktopTempPermissionId desktopTempPermissionId
     */
    void deleteDesktopTempPermissionByUserId(UUID desktopTempPermissionId);


    /**
     * 删除桌面临时权限
     *
     * @param desktopTempPermissionId desktopTempPermissionId
     */
    void deleteDesktopTempPermissionByDeskId(UUID desktopTempPermissionId);
}
