package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDiskSnapshotDetailDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 磁盘快照相关操作API
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年7月22日
 *
 * @author lyb
 */
public interface DiskSnapshotAPI {

    /**
     * 生成快照名称
     * 
     * @param diskName 磁盘名称
     * @return 磁盘快照名称
     * @throws BusinessException 业务异常
     */
    String generateSnapshotNameByDiskName(String diskName) throws BusinessException;

    /**
     * 检查磁盘快照数量是否超配
     * 
     * @param diskId 磁盘ID
     * @return true：超配，false：未超配
     * @throws BusinessException 业务异常
     */
    Boolean checkSnapshotNumberOverByDiskId(UUID diskId) throws BusinessException;

    /**
     * 删除最早创建超过配置数量的磁盘快照
     * (例如当前限制8个快照，但是存在10个快照，会将最早创建的3个快照进行删除)
     *
     * @param deskId 磁盘ID
     * @throws BusinessException 业务异常
     */
    void deleteBeforeOverDiskSnapshotByDiskId(UUID deskId) throws BusinessException;

    /**
     * 获取最早创建超过配置数量的快照
     * (例如当前限制8个快照，但是存在10个快照，会获得最早创建的3个快照)
     *
     * @param deskId 磁盘ID
     * @return 第一个创建的快照信息
     * @throws BusinessException 业务异常
     */
    List<CbbDiskSnapshotDetailDTO> getBeforeOverDiskSnapshotByDiskId(UUID deskId) throws BusinessException;

    /**
     * 检测快照名称是否重复， true 重复 false 可用
     *
     * @param name 快照名称
     * @return 响应 true 重复 false 可用
     */
    Boolean checkNameDuplication(String name);

    /**
     * 获取磁盘快照已配置数量最大限制数
     *
     * @return 磁盘快照数量最大限制数
     */
    Integer getMaxSnapshots();

}
