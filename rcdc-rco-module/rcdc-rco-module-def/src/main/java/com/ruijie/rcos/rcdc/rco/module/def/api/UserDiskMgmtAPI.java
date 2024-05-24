package com.ruijie.rcos.rcdc.rco.module.def.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.ruijie.rcos.gss.sdk.iac.module.def.enums.IacConfigRelatedType;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.DesktopLockDiskResultDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserGroupBindDiskNumDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskApplySupportEnum;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 磁盘管理API接口
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/14
 *
 * @author TD
 */
public interface UserDiskMgmtAPI {

    /**
     * 根据磁盘Id查询
     * @param diskId 磁盘ID
     * @return ViewUserDiskDTO
     * @throws BusinessException 业务异常
     */
    UserDiskDetailDTO userDiskDetail(UUID diskId) throws BusinessException;

    /**
     * 根据用户Id查询
     * @param userId 用户ID
     * @return Optional<ViewUserDiskDTO>
     */
    Optional<UserDiskDetailDTO> diskDetailByUserId(UUID userId);

    /**
     * 根据磁盘池ID和用户Id集合查询磁盘列表
     * @param diskPoolId 磁盘池ID
     * @param relatedType 查询类型：USER,USERGROUP
     * @param idList 用户ID、用户组集合
     * @return List<UUID>
     * @throws BusinessException 业务异常
     */
    List<UUID> diskDetailByDiskPoolIdAndUserIdList(UUID diskPoolId, IacConfigRelatedType relatedType, List<UUID> idList) throws BusinessException;


    /**
     * 磁盘绑定/解绑用户
     * @param diskId 磁盘ID
     * @param userId 用户ID
     * @throws BusinessException 业务异常
     */
    void bindUserOrOn(UUID diskId, UUID userId) throws BusinessException;

    /**
     * 挂载桌面用户磁盘
     * @param deskId 桌面ID
     */
    void attachDesktopDisk(UUID deskId);

    /**
     * 删除磁盘
     * @param diskId 磁盘ID
     */
    void deleteUserDisk(UUID diskId);

    /**
     * 解绑用户与磁盘池关系
     * @param relatedId 关联ID
     * @param relatedType 关联类型
     * @throws BusinessException 业务异常
     */
    void unbindUserAllDiskPool(UUID relatedId, IacConfigRelatedType relatedType) throws BusinessException;

    /**
     * 获取磁盘支持的应用类型
     * @param diskId 磁盘ID
     * @return 磁盘支持的应用类型集合
     * @throws BusinessException 业务异常
     */
    List<DiskApplySupportEnum> getDiskUseApplySupport(UUID diskId)  throws BusinessException;

    /**
     * 获取故障磁盘桌面集合
     * @param desktopPoolId 桌面池ID
     * @return 桌面集合
     */
    Set<UUID> getFaultDiskDesktopSet(UUID desktopPoolId);

    /**
     * 获取指定磁盘池，以组进行统计绑定磁盘的数量
     * @param diskPoolId 磁盘池ID
     * @return 统计信息
     */
    List<UserGroupBindDiskNumDTO> countGroupBindDiskNumByDiskPoolId(UUID diskPoolId);


    /**
     * 检查桌面是否有配置UPM到个人盘
     * @param desktopId 桌面信息
     * @return true：就绪/false：未就绪
     * @throws BusinessException 业务异常
     */
    boolean checkDesktopUserProFile(UUID desktopId) throws BusinessException;

    /**
     * 桌面锁定磁盘
     * @param desktopId 桌面ID
     * @return DesktopLockDiskResultDTO
     */
    DesktopLockDiskResultDTO desktopLockDisk(UUID desktopId);

    /**
     * 桌面解绑磁盘
     * @param desktopId 桌面ID
     * @throws BusinessException 业务异常
     */
    void desktopUnlockDisk(UUID desktopId) throws BusinessException;

    /**
     * 回滚桌面-用户-磁盘关系
     * @param desktopId 桌面ID
     */
    void unbindUserAndDesktopAndDiskRelation(UUID desktopId);
}
