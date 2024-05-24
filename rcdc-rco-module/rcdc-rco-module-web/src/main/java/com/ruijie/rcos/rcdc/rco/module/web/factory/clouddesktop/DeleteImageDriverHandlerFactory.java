package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import java.util.Iterator;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbImageDriverMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbImageDriverDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/12/26
 *
 * @author songxiang
 */
@Service
public class DeleteImageDriverHandlerFactory {

    @Autowired
    private CbbImageDriverMgmtAPI cbbImageDriverMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建批任务处理器:
     * 
     * @param iterator 批任务项
     * @return 删除驱动批任务Handler
     */
    public DeleteImageDriverBatchHandler createHandler(Iterator<? extends BatchTaskItem> iterator) {
        Assert.notNull(iterator, "batchTaskItem must not be null");

        return new DeleteImageDriverBatchHandler(iterator, auditLogAPI);
    }

    /**
     * 批任务处理类
     */
    protected class DeleteImageDriverBatchHandler extends AbstractBatchTaskHandler {


        private BaseAuditLogAPI auditLogAPI;

        private String driverName = StringUtils.EMPTY;

        private String errMessage;

        private DeleteImageDriverBatchHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
            super(iterator);
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
            Assert.notNull(batchTaskItem, "batchTaskItem must not be null");
            try {
                UUID driverId = batchTaskItem.getItemID();
                CbbImageDriverDTO driverDTO = cbbImageDriverMgmtAPI.findImageDriverInfo(driverId);
                this.driverName = driverDTO.getDriverName();
                cbbImageDriverMgmtAPI.deleteImageDriverFile(driverId);
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_SUCCESS, this.driverName);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_SUCCESS).msgArgs(new String[] {this.driverName})
                        .build();
            } catch (BusinessException e) {
                this.errMessage = e.getI18nMessage();
                auditLogAPI.recordLog(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_FAIL, e, this.driverName,
                        this.errMessage);
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT_FAIL, e, this.driverName,
                        e.getI18nMessage());
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int success, int failCount) {
            // 批量删除
            if (success + failCount > 1) {
                return buildDefaultFinishResult(success, failCount, CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_RESULT);
            }

            // 删除单条驱动
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_SUCCESS_RESULT).msgArgs(new String[] {this.driverName})
                        .build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_IMAGE_DRIVER_DELETE_FAIL_RESULT).msgArgs(new String[] {this.driverName})
                        .build();
            }
        }
    }
}
