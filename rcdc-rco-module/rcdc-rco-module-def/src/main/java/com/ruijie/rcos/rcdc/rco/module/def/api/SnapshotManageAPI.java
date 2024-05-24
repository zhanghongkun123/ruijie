package com.ruijie.rcos.rcdc.rco.module.def.api;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.CreateDeskSnapshotTaskDTO;
import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.lang.NonNull;

import java.util.UUID;

/**
 * Description: SnapshotManageAPI 快照相关api
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/3 18:42
 *
 * @author lihengjing
 */
public interface SnapshotManageAPI {


    /**
     * 批量处理任务创建云桌面快照
     *
     *
     * @param createDeskSnapshotTaskDTO 请求参数
     * @return 任务taskId
     * @throws BusinessException ex
     */
    UUID createDeskSnapshotByBatchTask(CreateDeskSnapshotTaskDTO createDeskSnapshotTaskDTO) throws BusinessException;

    /**
     * 批量处理任务删除云桌面快照
     *
     * @param snapshotId 快照UUID（不允许为空）
     * @return 任务taskId
     * @throws BusinessException ex
     */
    UUID deleteDeskSnapshotByBatchTask(@NotNull UUID snapshotId) throws BusinessException;

    /**
     * 批量处理任务恢复云桌面快照
     *
     * @param deskSnapshotId 快照UUID（不允许为空）
     * @param isNeedShutdown 是否需要关机（用户自定义快照为true）
     * @param isForceShutdown 是否强制关机（用户自定义快照使用）
     * @return 任务taskId
     * @throws BusinessException ex
     */
    UUID recoverDeskSnapshotByBatchTask(@NotNull UUID deskSnapshotId, Boolean isNeedShutdown, Boolean isForceShutdown) throws BusinessException;

    /**
     * 通过快照ID查询快照信息对象
     *
     * @param deskSnapshotId 快照UUID（不允许为空）
     * @return CbbDeskSnapshotDTO 快照对象
     * @throws BusinessException ex
     */
    CbbDeskSnapshotDTO findDeskSnapshotInfoById(@NotNull UUID deskSnapshotId) throws BusinessException;

    /**
     * 通过云桌面ID查询快照信息对象
     *
     * @param deskId 云桌面UUID（不允许为空）
     * @return CloudDesktopDetailDTO 云桌面信息对象
     * @throws BusinessException ex
     */
    CloudDesktopDetailDTO getDesktopDetailById(@NonNull UUID deskId) throws BusinessException;

    /**
     * 保存相关快照操作日志
     * 
     * @param isUser 是否用户操作快照（用户自定义快照 为true）
     * @param cloudDesktopDetailDTO 云桌面信息对象（管理员直接传一个new的对象进来）
     * @param logDetail （记录的日志翻译后信息）
     */
    void saveOpLog(Boolean isUser, CloudDesktopDetailDTO cloudDesktopDetailDTO, String logDetail);
}
