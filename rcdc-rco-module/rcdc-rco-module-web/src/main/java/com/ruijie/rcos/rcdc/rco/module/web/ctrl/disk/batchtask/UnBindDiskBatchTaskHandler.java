package com.ruijie.rcos.rcdc.rco.module.web.ctrl.disk.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDiskMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.constants.DiskPoolConstants;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.dto.UserDiskDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.diskpool.enums.DiskBusinessKeyEnums;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.diskpool.DiskPoolBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 磁盘批量解绑用户handler
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/7/16
 *
 * @author TD
 */
public class UnBindDiskBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnBindDiskBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserDiskMgmtAPI userDiskMgmtAPI;

    private String diskName;

    public UnBindDiskBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator, BaseAuditLogAPI auditLogAPI,
            UserDiskMgmtAPI userDiskMgmtAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.userDiskMgmtAPI = userDiskMgmtAPI;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID diskId = batchTaskItem.getItemID();
        UserDiskDetailDTO viewUserDisk = null;
        try {
            viewUserDisk = userDiskMgmtAPI.userDiskDetail(diskId);
            // 用户为空说明磁盘未绑定用户，无需进行解绑
            if (Objects.isNull(viewUserDisk.getUserId())) {
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_NOT_BIND_LOG).msgArgs(viewUserDisk.getDiskName()).build();
            }
            checkDiskForUnBind(viewUserDisk);
            userDiskMgmtAPI.bindUserOrOn(diskId, null);
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_SUCCESS_LOG, viewUserDisk.getDiskName(),
                    viewUserDisk.getUserName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_SUCCESS_LOG)
                    .msgArgs(viewUserDisk.getDiskName(), viewUserDisk.getUserName()).build();
        } catch (Exception e) {
            String signName = Objects.nonNull(viewUserDisk) ? viewUserDisk.getDiskName() : diskId.toString();
            LOGGER.error("磁盘[{}]解绑失败：", signName, e);
            String message = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_FAIL_LOG, e, signName, message);
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_COVER_USER_UNBIND_FAIL_LOG, e, signName, message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (StringUtils.isNotBlank(diskName)) {
            if (successCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_UNBIND_SINGLE_SUCCESS)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(DiskPoolBusinessKey.RCDC_RCO_DISK_UNBIND_SINGLE_FAIL)
                        .msgArgs(new String[] {diskName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(successCount, failCount, DiskPoolBusinessKey.RCDC_RCO_BATCH_DISK_UNBIND_TASK_RESULT);
        }
    }

    private void checkDiskForUnBind(UserDiskDetailDTO userDiskDetailDTO) throws BusinessException {
        // 非可用，禁用，故障状态磁盘不能进行解绑
        if (!DiskPoolConstants.BIND_DISK_STATUS.contains(userDiskDetailDTO.getDiskState())) {
            throw new BusinessException(DiskPoolBusinessKey.RCDC_RCO_DISK_STATUS_OPERATION_UNALLOWED, userDiskDetailDTO.getDiskName(),
                    DiskBusinessKeyEnums.obtainResolve(userDiskDetailDTO.getDiskState().name()),
                    LocaleI18nResolver.resolve(DiskPoolBusinessKey.RCDC_RCO_USER_UNBIND_DISK_LOG));
        }
    }
}
