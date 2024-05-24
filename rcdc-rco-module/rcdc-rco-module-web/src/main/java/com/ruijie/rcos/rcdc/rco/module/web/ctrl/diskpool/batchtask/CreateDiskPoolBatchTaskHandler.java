package com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDiskPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskDiskAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.disk.CbbVDICreatePoolDiskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.diskpool.CbbDiskPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDiskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.DiskPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description: 创建磁盘池批处理任务
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/8
 *
 * @author TD
 */
public class CreateDiskPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateDiskPoolBatchTaskHandler.class);

    private CbbDiskPoolDTO diskPoolDTO;

    private AtomicInteger startIndex;

    private CbbDiskPoolMgmtAPI cbbdiskPoolMgmtAPI;

    private CbbVDIDeskDiskAPI deskDiskAPI;

    private BaseAuditLogAPI auditLogAPI;

    public CreateDiskPoolBatchTaskHandler(CbbDiskPoolDTO diskPoolDTO, int startIndex, Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
        this.diskPoolDTO = diskPoolDTO;
        this.startIndex = new AtomicInteger(startIndex);
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        try {
            cbbdiskPoolMgmtAPI.updateState(diskPoolDTO.getId(), CbbDiskPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新磁盘池[%s]信息失败", diskPoolDTO.getName()), e);
        }
        return buildDefaultFinishResult(successCount, failCount, DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_BATCH_CREATE_DISK_TASK_RESULT);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskId = batchTaskItem.getItemID();
        String poolName = diskPoolDTO.getName();
        LOGGER.info("[{}]磁盘池开始创建磁盘, diskId:{}", poolName, diskId);
        String diskNamePrefix = diskPoolDTO.getDiskNamePrefix();
        try {
            String diskName = buildDiskName(diskNamePrefix);
            CbbVDICreatePoolDiskDTO createDiskDTO = new CbbVDICreatePoolDiskDTO();
            createDiskDTO.setDiskId(diskId);
            createDiskDTO.setTaskId(UUID.randomUUID());
            createDiskDTO.setDiskName(diskName);
            createDiskDTO.setDiskPoolId(diskPoolDTO.getId());
            createDiskDTO.setCapacity(diskPoolDTO.getDiskSize());
            createDiskDTO.setDiskType(CbbDiskType.DATA);
            createDiskDTO.setDiskPoolType(DiskPoolType.POOL);
            createDiskDTO.setClusterId(diskPoolDTO.getClusterId());
            createDiskDTO.setAssignedStoragePoolId(diskPoolDTO.getStoragePoolId());
            createDiskDTO.setPlatformId(diskPoolDTO.getPlatformId());
            deskDiskAPI.createPoolDisk(createDiskDTO);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_SUCCESS_LOG, poolName, diskName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_SUCCESS_LOG).msgArgs(poolName, diskName).build();
        } catch (Exception e) {
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            LOGGER.error(String.format("磁盘池[%s]创建磁盘失败", poolName), e);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_FAIL_LOG, poolName, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_POOL_CREATE_DISK_FAIL_LOG, e, poolName, message);
        }
    }

    /**
     * setCbbDiskPoolMgmtAPI
     *
     * @param diskPoolMgmtAPI 操作类
     * @return CreateDiskPoolBatchTaskHandler
     */
    public CreateDiskPoolBatchTaskHandler setCbbDiskPoolMgmtAPI(CbbDiskPoolMgmtAPI diskPoolMgmtAPI) {
        this.cbbdiskPoolMgmtAPI = diskPoolMgmtAPI;
        return this;
    }

    /**
     * setDeskDiskAPI
     *
     * @param deskDiskAPI 操作
     * @return CreateDiskPoolBatchTaskHandler
     */
    public CreateDiskPoolBatchTaskHandler setDeskDiskAPI(CbbVDIDeskDiskAPI deskDiskAPI) {
        this.deskDiskAPI = deskDiskAPI;
        return this;
    }

    /**
     * setAuditLogAPI
     *
     * @param auditLogAPI 操作类
     * @return CreateDiskPoolBatchTaskHandler
     */
    public CreateDiskPoolBatchTaskHandler setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
        return this;
    }

    private synchronized String buildDiskName(String diskNamePrefix) {
        String name = String.format(DiskPoolConstants.DISK_NAME_FORMAT, diskNamePrefix, startIndex.incrementAndGet());
        while (BooleanUtils.toBoolean(deskDiskAPI.checkDiskNameExist(name))) {
            LOGGER.warn("已存在磁盘名称[{}]，重新生成", name);
            name = String.format(DiskPoolConstants.DISK_NAME_FORMAT, diskNamePrefix, startIndex.incrementAndGet());
        }
        return name;
    }
}
