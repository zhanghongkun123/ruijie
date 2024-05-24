package com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.service;


import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDesktopTempPermissionDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.deskstrategy.enums.DesktopTempPermissionRelatedType;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelatedInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.deskstrategy.dto.DesktopTempPermissionRelationDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.UUID;

/**
 * Description: 虚机临时权限Service
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023-04-26
 *
 * @author linke
 */
public interface DesktopTempPermissionService {

    /**
     * 根据临时权限ID，获取开通对象列表
     *
     * @param desktopTempPermissionId 临时权限ID
     * @return List<DesktopTempPermissionRelatedInfoDTO>
     */
    List<DesktopTempPermissionRelatedInfoDTO> listRelatedInfo(UUID desktopTempPermissionId);

    /**
     * 检查关联对象是否存在，是否已绑定其他临时权限
     *
     * @param relatedIdList 关联对象ID列表
     * @param relatedType   关联对象类型
     * @param permissionId  权限ID
     * @throws BusinessException 业务异常
     */
    void checkRelatedObject(List<UUID> relatedIdList, DesktopTempPermissionRelatedType relatedType, @Nullable UUID permissionId)
            throws BusinessException;

    /**
     * 根据对象id和类型查询临时权限
     *
     * @param relatedId 对象id
     * @param type      类型
     * @return CbbDesktopTempPermissionDTO
     * @throws BusinessException 业务异常
     */
    CbbDesktopTempPermissionDTO getPermissionDTOByRelatedObj(UUID relatedId, DesktopTempPermissionRelatedType type)
            throws BusinessException;

    /**
     * 根据临时权限ID和桌面状态查询绑定对象类型为"USER"的运行中桌面ID列表
     *
     * @param permissionId 临时权限ID
     * @param state        桌面状态
     * @return List<UUID>   桌面ID列表
     */
    List<UUID> listDesktopIdByRelatedUserAndDeskState(UUID permissionId, CbbCloudDeskState state);

    /**
     * 根据临时权限ID和桌面状态查询绑定对象类型为"DESKTOP"的运行中桌面ID列表
     *
     * @param permissionId 临时权限ID
     * @param state        桌面状态
     * @return List<UUID>   桌面ID列表
     */
    List<UUID> listDesktopIdByRelatedDesktopAndState(UUID permissionId, CbbCloudDeskState state);

    /**
     * 是否已绑定生效中的临时权限
     *
     * @param relatedId 对象id
     * @param type      类型
     * @return true已绑定生效中的临时权限；false未绑定生效中的临时权限。
     */
    boolean existInEffectPermission(UUID relatedId, DesktopTempPermissionRelatedType type);

    /**
     * 根据对象ID和对象类型查询生效中的临时权限ID
     *
     * @param relatedId 对象id
     * @param type      类型
     * @return List<UUID>  临时权限ID列表
     */
    List<UUID> listInEffectPermissionId(UUID relatedId, DesktopTempPermissionRelatedType type);

    /**
     * 根据临时权限ID，查询其关联的所有用户ID列表（包含对象为桌面，其关联的用户ID）
     *
     * @param permissionId 对象id
     * @return List<UUID>  用户ID列表
     */
    List<UUID> listAllUserIdByPermissionId(UUID permissionId);

    /**
     * 根据临时权限ID和关联类型查询其关联的ID列表
     *
     * @param permissionId 对象id
     * @param type         关联类型
     * @return List<UUID>  ID列表
     */
    List<UUID> listRelatedIdByPermissionIdAndRelatedType(UUID permissionId, DesktopTempPermissionRelatedType type);


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

    /**
     * 根据临时权限ID，修改hasSendExpireNotice
     *
     * @param permissionId        对象id
     * @param hasSendExpireNotice hasSendExpireNotice
     */
    void updateHasSendExpireNotice(UUID permissionId, Boolean hasSendExpireNotice);

    /**
     * 根据关联对象ID列表、关联对象类型查询关联表记录信息
     *
     * @param relatedIdList 关联对象ID列表
     * @param relatedType   关联对象类型
     * @return List<DesktopTempPermissionRelationDTO>
     */
    List<DesktopTempPermissionRelationDTO> listRelationByRelatedIdsAndRelatedType(List<UUID> relatedIdList,
                                                                                  DesktopTempPermissionRelatedType relatedType);
}
