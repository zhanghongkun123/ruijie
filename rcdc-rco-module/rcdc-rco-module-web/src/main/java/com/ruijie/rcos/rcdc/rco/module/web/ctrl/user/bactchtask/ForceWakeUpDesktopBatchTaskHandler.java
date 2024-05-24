package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.BatchTaskStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 *
 * Description: 云桌面强制唤醒批量任务处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年4月26日
 *
 * @author zjy
 */
public class ForceWakeUpDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForceWakeUpDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean enableMountOldData;

    public void setEnableMountOldData(boolean enableMountOldData) {
        this.enableMountOldData = enableMountOldData;
    }

    public ForceWakeUpDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public ForceWakeUpDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public ForceWakeUpDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public ForceWakeUpDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public ForceWakeUpDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public ForceWakeUpDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        UUID id = taskItem.getItemID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(id);
        String tmpUserName = cloudDesktopDetailDTO.getUserName();
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        try {
            cloudDesktopWebService.checkThirdPartyDesktop(id, UserBusinessKey.RCDC_RCO_DESKTOP_WAKE_UP_THIRD_PARTY);
            CloudDesktopStartRequest request = new CloudDesktopStartRequest(id, enableMountOldData);
            request.setSupportCrossCpuVendor(Boolean.TRUE);
            cloudDesktopOperateAPI.start(request);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SUC_LOG, tmpUserName, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_SUC_DESC).msgArgs(new String[] {tmpUserName, tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面强制唤醒命令失败，云桌面id= " + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_FAIL_LOG, e, tmpUserName, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_ITEM_FAIL_DESC, e, tmpUserName, tmpDesktopName,
                    e.getI18nMessage());
        } finally {
            userName = tmpUserName;
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_FORCE_WAKE_UP_BATCH_RESULT);
        }
    }

}
