package com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.batchtask;

import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.appcenter.key.UamDeliveryObjectBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年04月03日
 *
 * @author xgx
 */
@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class UnbindUamAppPrePublishVersionRelateDeskTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnbindUamAppPrePublishVersionRelateDeskTaskHandler.class);


    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Autowired
    private CloudDesktopWebService cloudDesktopWebService;

    @Autowired
    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    private String appName;

    private static final long RETRY_MAX_COUNT = 3;

    private static final long DEFAULT_RETRY_TIME = TimeUnit.SECONDS.toMillis(3);

    private static final long DEFAULT_WAIT_TIME = TimeUnit.SECONDS.toMillis(10);

    protected UnbindUamAppPrePublishVersionRelateDeskTaskHandler(Collection<? extends BatchTaskItem> batchTaskItemCollection, String appName) {
        super(batchTaskItemCollection);
        Assert.notNull(appName, "appName can not be null");
        this.appName = appName;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        UUID deskId = batchTaskItem.getItemID();

        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
        if (CbbCloudDeskState.SLEEP.toString().equals(cloudDesktopDetailDTO.getDesktopState())) {
            // 如果云桌面处于休眠状态则先唤醒云桌面
            wakeDesktop(deskId, batchTaskItem.getItemName());
        }


        try {
            cbbAppSoftwarePackageMgmtAPI.forceRefreshDeskAppDisk(deskId);
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SUCCESS_LOG, appName,
                    batchTaskItem.getItemName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_SUCCESS_RESULT)
                    .msgArgs(appName, batchTaskItem.getItemName()).build();
        } catch (BusinessException ex) {
            auditLogAPI.recordLog(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_FAIL_LOG, appName, batchTaskItem.getItemName(),
                    ex.getI18nMessage());
            throw new BusinessException(UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SINGLE_FAIL_RESULT, ex, appName,
                    batchTaskItem.getItemName(), ex.getI18nMessage());
        }

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failureCount) {

        return buildDefaultFinishResult(successCount, failureCount, UamDeliveryObjectBusinessKey.RCDC_UAM_UNBIND_APP_DISK_RELATE_DESK_SUCCESS_RESULT);
    }

    private void wakeDesktop(UUID deskId, String desktopName) throws BusinessException {
        LOGGER.info("唤醒云桌面[{}]", desktopName);
        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(deskId));
        waitForDesktopRunning(deskId);
    }

    private void waitForDesktopRunning(UUID deskId) {
        int retryCount = 0;
        while (retryCount < RETRY_MAX_COUNT) {
            // 每隔3秒查询状态
            try {
                Thread.sleep(DEFAULT_RETRY_TIME);
            } catch (InterruptedException e) {
                LOGGER.error("忽略异常", e);
            }
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
            if (CbbCloudDeskState.RUNNING.toString().equals(cloudDesktopDetailDTO.getDesktopState())) {
                retryCount ++;
                continue;
            }
            // 桌面状态为RUNNING的时候，等待10秒GT就绪
            try {
                Thread.sleep(DEFAULT_WAIT_TIME);
            } catch (InterruptedException e) {
                LOGGER.error("忽略异常", e);
            }
            break;
        }
    }

}
