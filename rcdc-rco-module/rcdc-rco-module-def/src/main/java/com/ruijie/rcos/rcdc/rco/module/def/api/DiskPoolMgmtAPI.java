package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.PageSearchRequest;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolOverviewDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolStatisticDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DiskPoolUserDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

/**
 * Description: 磁盘池管理API接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/6
 *
 * @author TD
 */
public interface DiskPoolMgmtAPI {

    /**
     * 获取磁盘池列表
     * @param request 分页获取请求
     *
     * @return CbbDiskPoolDTOList 磁盘池列表信息
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<DiskPoolStatisticDTO> pageDiskPool(PageQueryRequest request) throws BusinessException;

    /**
     * 根据用户ID获取磁盘池列表
     * @param userId 用户ID
     * @param request 分页请求
     * @return CbbDiskPoolDTOList 磁盘池列表信息
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DiskPoolStatisticDTO> pageDiskPoolByUserId(UUID userId, PageSearchRequest request) throws BusinessException;

    /**
     * 查询磁盘池统计信息
     *
     * @param diskPoolId 磁盘池ID
     * @return String
     * @throws BusinessException 业务异常
     */
    DiskPoolStatisticDTO diskPoolStatistic(UUID diskPoolId) throws BusinessException;

    /**
     * 获取磁盘池的总览信息
     *
     * @return DiskPoolOverviewDTO
     */
    DiskPoolOverviewDTO getDiskPoolOverview();

    /**
     * 删除磁盘池
     *
     * @param diskPoolId 磁盘池id
     * @throws BusinessException ex
     */
    void deleteDiskPool(UUID diskPoolId) throws BusinessException;

    /**
     * 查询磁盘池关联用户或用户组列表
     *
     * @param diskPoolId   磁盘池Id
     * @param relatedType 关联类型
     * @return   List<DesktopPoolUserDTO>
     */
    List<DiskPoolUserDTO> listDiskPoolUser(UUID diskPoolId, IacConfigRelatedType relatedType);

    /**
     * 计算磁盘名称最大后缀
     *
     * @param diskPoolId 磁盘池ID
     * @return 最大后缀数字
     */
    int getMaxIndexNumWhenAddDisk(UUID diskPoolId);

    /**
     * 分页查询磁盘池中磁盘列表
     *
     * @param pageQueryRequest 分页参数
     * @return PageQueryResponse<UserDiskDetailDTO>
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<UserDiskDetailDTO> pagePoolDiskUser(PageQueryRequest pageQueryRequest) throws BusinessException;

    /**
     * 根据磁盘池ID查询可分配的磁盘列表
     * @param diskPoolId 磁盘池ID
     * @return 可分配的磁盘列表
     */
    List<UserDiskDetailDTO> queryAssignableDiskByDiskPoolId(UUID diskPoolId);

    /**
     * 根据磁盘池ID查询可创建快照的磁盘列表
     *
     * @param diskPoolId 磁盘池ID
     * @return 可创建快照的磁盘列表
     */
    List<UserDiskDetailDTO> querySnapshotCapableDiskByDiskPoolId(UUID diskPoolId);

    /**
     * 根据AD域组ID获取磁盘池列表
     * @param adGroupId AD域组ID
     * @param request 分页请求
     * @return CbbDiskPoolDTOList 磁盘池列表信息
     * @throws BusinessException 业务异常
     */
    DefaultPageResponse<DiskPoolStatisticDTO> pageDiskPoolByAdGroupId(UUID adGroupId, PageSearchRequest request) throws BusinessException;
}
