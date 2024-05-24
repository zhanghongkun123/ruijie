package com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.rco.module.common.query.ConditionQueryRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.PoolModelOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.desktoppool.dto.UserBindDesktopNumDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;

import java.util.List;
import java.util.UUID;

/**
 *
 * Description: 池桌面信息查询接口
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年10月12日
 *
 * @author linke
 */
public interface DesktopPoolService {

    /**
     * 查询桌面池的非回收站和删除中的云桌面列表
     *
     * @param desktopPoolId 桌面池Id
     * @return List<PoolDesktopInfoDTO> 云桌面列表
     */
    List<PoolDesktopInfoDTO> listNormalDeskInfoByDesktopPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池ID列表查询云桌面列表
     *
     * @param desktopPoolIdList 桌面池Id列表
     * @return List<PoolDesktopInfoDTO> 云桌面列表
     */
    List<PoolDesktopInfoDTO> listDesktopByDesktopPoolIds(List<UUID> desktopPoolIdList);

    /**
     * 根据桌面ID列表和池类型查询按桌面池ID和用户ID进行分组统计绑定的桌面数量
     *
     * @param desktopIdList 桌面Id列表
     * @param poolModel     池类型
     * @return List<UserBindDesktopNumDTO> 统计列表
     */
    List<UserBindDesktopNumDTO> listUserBindPoolDesktopNum(List<UUID> desktopIdList, CbbDesktopPoolModel poolModel);

    /**
     * 获取桌面池ID和用户组ID列表查询非回收站的云桌面列表
     *
     * @param desktopPoolId   桌面池Id列表
     * @param userGroupIdList 用户组Id列表
     * @return List<PoolDesktopInfoDTO> 云桌面列表
     */
    List<PoolDesktopInfoDTO> listNormalDesktopByPoolIdAndUserGroupIds(UUID desktopPoolId, List<UUID> userGroupIdList);

    /**
     * 根据桌面池列表
     *
     * @param request PageSearchRequest
     * @return DefaultPageResponse<DesktopPoolBasicDTO>
     */
    DefaultPageResponse<DesktopPoolBasicDTO> pageDesktopPool(PageSearchRequest request);

    /**
     * 根据桌面池模式查询列表
     *
     * @param poolModelList poolModelList
     * @return List<DesktopPoolBasicDTO>
     */
    List<DesktopPoolBasicDTO> listDesktopPoolByPoolModel(List<CbbDesktopPoolModel> poolModelList);

    /**
     * 根据桌面池ID获取信息
     *
     * @param id 桌面池ID
     * @return DesktopPoolBasicDTO
     * @throws BusinessException BusinessException
     */
    DesktopPoolBasicDTO getDesktopPoolBasicById(UUID id) throws BusinessException;

    /**
     * 查询桌面池列表
     *
     * @param request 请求对象
     * @return 桌面池列表
     * @throws BusinessException 业务异常
     */
    List<DesktopPoolBasicDTO> listByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 查询桌面池数量
     *
     * @param request 请求对象
     * @return 数量
     * @throws BusinessException 业务异常
     */
    long countByConditions(ConditionQueryRequest request) throws BusinessException;

    /**
     * 根据upm策略ID查询桌面池列表
     *
     * @param userProfileStrategyId upm策略ID
     * @return 桌面池列表
     */
    List<DesktopPoolBasicDTO> listByUserProfileStrategyId(UUID userProfileStrategyId);

    /**
     * 根据镜像ID查询桌面池列表
     *
     * @param imageTemplateId 镜像ID
     * @return 桌面池列表
     */
    List<DesktopPoolBasicDTO> listByImageId(UUID imageTemplateId);

    /**
     * 根据桌面池，查询池内已绑定用户的桌面列表
     *
     * @param desktopPoolId 桌面池ID
     * @return List<PoolDesktopInfoDTO>
     */
    List<PoolDesktopInfoDTO> listBindUserDesktopByPoolId(UUID desktopPoolId);

    /**
     * 根据桌面池和用户ID，查询已绑定的桌面
     *
     * @param userId 用户ID
     * @param desktopPoolId 桌面池ID
     * @return PoolDesktopInfoDTO
     */
    PoolDesktopInfoDTO getUserBindPoolDesktop(UUID userId, UUID desktopPoolId);

    /**
     * 根据池类型列表查询池的统计数据
     *
     * @param poolModelList     池类型
     * @return List<PoolModelOverviewDTO>
     */
    List<PoolModelOverviewDTO> countPoolOverviewByModel(List<CbbDesktopPoolModel> poolModelList);

    /**
     * 根据池ID列表查询池的统计数据
     *
     * @param poolIdList 池ID
     * @return List<PoolModelOverviewDTO>
     */
    List<PoolModelOverviewDTO> countPoolOverviewByIds(List<UUID> poolIdList);

    /**
     * 根据桌面ID，查询桌面
     *
     * @param deskId 桌面ID
     * @return PoolDesktopInfoDTO
     */
    PoolDesktopInfoDTO getPoolDeskInfoByDeskId(UUID deskId);
}
