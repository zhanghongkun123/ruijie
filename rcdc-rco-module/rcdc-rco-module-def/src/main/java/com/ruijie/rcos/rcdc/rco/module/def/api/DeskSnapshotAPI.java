package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskSnapshotUserType;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.MaxSnapshotsRangeResponse;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.pagekit.api.PageQueryRequest;
import com.ruijie.rcos.sk.pagekit.api.PageQueryResponse;

import java.util.List;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年10月10日
 *
 * @author luojianmo
 */
public interface DeskSnapshotAPI {

    /**
     * 生成快照名称
     * 
     * @param desktopName 云桌面名称
     * @return 桌面快照名称
     * @throws BusinessException 业务异常
     */
    String generateSnapshotNameByDesktopName(String desktopName) throws BusinessException;

    /**
     * 检查云桌面快照数量是否超配
     * 
     * @param deskId 云桌面ID
     * @return true：超配，false：未超配
     * @throws BusinessException 业务异常
     */
    Boolean checkSnapshotNumberOverByDeskId(UUID deskId) throws BusinessException;

    /**
     * 获取最早创建超过配置数量的快照
     * (例如当前限制8个快照，但是存在10个快照，会获得最早创建的3个快照)
     *根据快照类型：用户快照，管理员快照（管理员创建与系统创建）
     * @param deskId 云桌面ID
     * @return 第一个创建的快照信息
     * @throws BusinessException 业务异常
     */
    List<CbbDeskSnapshotDTO> getBeforeOverDeskSnapshotByDeskId(UUID deskId) throws BusinessException;

    /**
     * 检测快照名称是否重复， true 重复 false 可用
     *
     * @param name 快照名称
     * @return 响应 true 重复 false 可用
     */
    Boolean checkNameDuplication(String name);

    /**
     * 获取云桌面快照可配置的数量最大限制范围
     * （范围为：1到rccp限制快照数量）
     *
     * @return 云桌面快照数量最大限制数 1:10
     */
    MaxSnapshotsRangeResponse getMaxSnapshotsRange();

    /**
     * 获取云桌面快照已配置数量最大限制数
     *
     * @return 云桌面快照数量最大限制数
     */
    Integer getMaxSnapshots();

    /**
     * 更新云桌面允许创建的快照最大容量
     * @param maxSnapshots 桌面快照最大数量
     * @throws BusinessException 业务异常
     */
    void editMaxSnapshots(Integer maxSnapshots);

    /**
     * 获取第一个指定用户创建的快照
     *
     * @param deskId 云桌面ID
     * @param userId 用户ID
     * @return 第一个指定用户创建的快照信息
     * @throws BusinessException 业务异常
     */
    CbbDeskSnapshotDTO getFirstDeskSnapshotByDeskIdAndUserId(UUID deskId, UUID userId) throws BusinessException;

    /**
     * 获取第一个创建的快照
     *
     * @param deskId 云桌面ID
     * @param userType 用户类型 (管理员、普通用户)
     * @return 第一个创建的快照信息
     * @throws BusinessException 业务异常
     */
    CbbDeskSnapshotDTO getFirstDeskSnapshotByDeskIdAndUserType(UUID deskId, CbbDeskSnapshotUserType userType) throws BusinessException;

    /**
     * 分页查询桌面快照列表
     * @param request 请求对象
     * @return 桌面快照列表
     * @throws BusinessException 业务异常
     */
    PageQueryResponse<CbbDeskSnapshotDTO> pageQuery(PageQueryRequest request) throws BusinessException;
}
