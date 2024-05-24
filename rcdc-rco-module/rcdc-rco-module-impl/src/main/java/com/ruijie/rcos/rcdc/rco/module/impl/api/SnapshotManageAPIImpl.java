package com.ruijie.rcos.rcdc.rco.module.impl.api;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskSnapshotAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbCreateDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDeleteSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskRestoreSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskSnapshotDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpEvent;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DesktopOpType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopOpLogMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.SnapshotManageAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.DesktopOpLogDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.usersnapshot.CreateDeskSnapshotTaskDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.usersnapshot.UserSnapshotBusinessKey;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.UUID;

/**
 * Description: SnapshotManageAPI 快照相关api
 * Copyright: Copyright (c)
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/3/3 18:42
 *
 * @author lihengjing
 */
public class SnapshotManageAPIImpl implements SnapshotManageAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(SnapshotManageAPIImpl.class);

    @Autowired
    private CbbVDIDeskSnapshotAPI cbbVDIDeskSnapshotAPI;

    @Autowired
    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    @Autowired
    private UserDesktopOperateAPI userDesktopOperateAPI;

    @Autowired
    private DesktopOpLogMgmtAPI desktopOpLogMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Override
    public UUID createDeskSnapshotByBatchTask(CreateDeskSnapshotTaskDTO createDeskSnapshotTaskDTO) throws BusinessException {

        Assert.notNull(createDeskSnapshotTaskDTO, "createDeskSnapshotTaskDTO is need not null");
        CbbCreateDeskSnapshotDTO cbbCreateDeskSnapshotDTO = new CbbCreateDeskSnapshotDTO();
        UUID taskId = UUID.randomUUID();
        UUID deskSnapshotId = UUID.randomUUID();
        cbbCreateDeskSnapshotDTO.setTaskId(taskId);
        cbbCreateDeskSnapshotDTO.setDeskSnapshotId(deskSnapshotId);
        cbbCreateDeskSnapshotDTO.setDeskId(createDeskSnapshotTaskDTO.getDeskId());
        cbbCreateDeskSnapshotDTO.setName(createDeskSnapshotTaskDTO.getDeskSnapshotName());
        cbbCreateDeskSnapshotDTO.setUserId(createDeskSnapshotTaskDTO.getUserId());
        cbbCreateDeskSnapshotDTO.setUserType(createDeskSnapshotTaskDTO.getUserType());

        cbbVDIDeskSnapshotAPI.createDeskSnapshot(cbbCreateDeskSnapshotDTO);
        return taskId;
    }

    @Override
    public UUID deleteDeskSnapshotByBatchTask(UUID snapshotId) throws BusinessException {
        Assert.notNull(snapshotId, "snapshotId is need not null");
        UUID taskId = UUID.randomUUID();
        CbbDeskDeleteSnapshotDTO cbbDeskDeleteSnapshotDTO = new CbbDeskDeleteSnapshotDTO();
        cbbDeskDeleteSnapshotDTO.setDeskSnapshotId(snapshotId);
        cbbDeskDeleteSnapshotDTO.setTaskId(taskId);
        cbbVDIDeskSnapshotAPI.deleteDeskSnapshot(cbbDeskDeleteSnapshotDTO);
        return taskId;
    }

    @Override
    public UUID recoverDeskSnapshotByBatchTask(UUID deskSnapshotId, Boolean isNeedShutdown, Boolean isForceShutdown) throws BusinessException {
        Assert.notNull(deskSnapshotId, "deskSnapshotId is need not null");
        Assert.notNull(isNeedShutdown, "isNeedShutdown is need not null");
        Assert.notNull(isForceShutdown, "isForceShutdown is need not null");

        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = findDeskSnapshotInfoById(deskSnapshotId);

        if (isNeedShutdown) {
            // 用户自定义快照
            CloudDesktopDetailDTO cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(cbbDeskSnapshotDTO.getDeskId());
            if (!CbbCloudDeskState.CLOSE.name().equals(cloudDesktopDetailDTO.getDesktopState())) {
                // 尝试关机 同步方法
                userDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(cbbDeskSnapshotDTO.getDeskId(), isForceShutdown));
                cloudDesktopDetailDTO = userDesktopMgmtAPI.getDesktopDetailById(cbbDeskSnapshotDTO.getDeskId());
            }
            if (!CbbCloudDeskState.CLOSE.name().equals(cloudDesktopDetailDTO.getDesktopState())) {
                // 直接停止任务 抛出异常 上层处理
                throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_RECOVER_FAIL_LOG, cloudDesktopDetailDTO.getUserName(),
                        cloudDesktopDetailDTO.getDesktopName(), cbbDeskSnapshotDTO.getName(), "云桌面非关机状态");
            }
        }

        UUID taskId = UUID.randomUUID();
        CbbDeskRestoreSnapshotDTO cbbDeskRestoreSnapshotDTO = new CbbDeskRestoreSnapshotDTO();
        cbbDeskRestoreSnapshotDTO.setDeskSnapshotId(deskSnapshotId);
        cbbDeskRestoreSnapshotDTO.setTaskId(taskId);
        cbbDeskRestoreSnapshotDTO.setDeskId(cbbDeskSnapshotDTO.getDeskId());
        cbbVDIDeskSnapshotAPI.restoreDeskSnapshot(cbbDeskRestoreSnapshotDTO);

        return taskId;
    }

    @Override
    public CbbDeskSnapshotDTO findDeskSnapshotInfoById(UUID deskSnapshotId) throws BusinessException {
        Assert.notNull(deskSnapshotId, "deskSnapshotId is need not null");
        CbbDeskSnapshotDTO cbbDeskSnapshotDTO = cbbVDIDeskSnapshotAPI.findDeskSnapshotInfoById(deskSnapshotId);
        if (cbbDeskSnapshotDTO == null) {
            throw new BusinessException(UserSnapshotBusinessKey.RCDC_USER_SNAPSHOT_IS_NULL, deskSnapshotId.toString());
        }
        return cbbDeskSnapshotDTO;
    }

    @Override
    public CloudDesktopDetailDTO getDesktopDetailById(UUID deskId) throws BusinessException {
        Assert.notNull(deskId, "deskId is need not null");
        return userDesktopMgmtAPI.getDesktopDetailById(deskId);
    }

    @Override
    public void saveOpLog(Boolean isUser, CloudDesktopDetailDTO cloudDesktopDetailDTO, String logDetail) {
        Assert.notNull(isUser, "isUser is need not null");
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO is need not null");
        Assert.notNull(logDetail, "logDetail is need not null");
        if (isUser) {
            DesktopOpLogDTO req = new DesktopOpLogDTO();
            req.setDesktopId(cloudDesktopDetailDTO.getId());
            req.setDesktopName(cloudDesktopDetailDTO.getDesktopName());
            req.setEventName(DesktopOpEvent.USER_SNAPSHOT);
            req.setOperatorType(DesktopOpType.USER_FROM_TERMINAL);
            req.setOperatorId(cloudDesktopDetailDTO.getUserId());
            req.setOperatorName(cloudDesktopDetailDTO.getUserName());
            req.setSourceIp(cloudDesktopDetailDTO.getTerminalIp());
            req.setDetail(logDetail);
            req.setTerminalId(cloudDesktopDetailDTO.getTerminalId());
            desktopOpLogMgmtAPI.saveOperateLog(req);
        } else {
            auditLogAPI.recordI18nLog(logDetail);
        }
    }


}
