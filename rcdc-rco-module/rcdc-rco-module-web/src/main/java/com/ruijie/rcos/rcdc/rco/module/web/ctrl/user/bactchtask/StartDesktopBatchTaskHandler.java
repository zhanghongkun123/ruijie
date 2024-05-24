package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * 
 * Description: 云桌面开机批量任务处理器
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年9月11日
 * 
 * @author fyq
 */
public class StartDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean enableMountOldData;

    private String desktopType = Constants.CLOUD_DESKTOP;

    public void setEnableMountOldData(boolean enableMountOldData) {
        this.enableMountOldData = enableMountOldData;
    }

    public StartDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public StartDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public StartDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public StartDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public StartDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public StartDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

    public void setDesktopType(String desktopType) {
        this.desktopType = desktopType;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        UUID id = taskItem.getItemID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(id);
        String tmpUserName = cloudDesktopDetailDTO.getUserName();
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();
        desktopType = cloudDesktopMgmtAPI.getImageUsageByDeskId(id);

        try {
            CloudDesktopStartRequest cloudDesktopStartRequest = new CloudDesktopStartRequest(id, enableMountOldData);
            cloudDesktopStartRequest.setBatchTaskItem(taskItem);
            cloudDesktopOperateAPI.start(cloudDesktopStartRequest);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_START_SUC_LOG, tmpUserName, tmpDesktopName, desktopType);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_START_ITEM_SUC_DESC).msgArgs(tmpUserName, tmpDesktopName, desktopType)
                    .build();
        } catch (BusinessException e) {
            BusinessException businessException = e;
            LOGGER.error("发送云桌面开启命令失败，云桌面id= " + id.toString(), businessException);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_START_FAIL_LOG, e, tmpUserName, tmpDesktopName,
                    businessException.getI18nMessage(), desktopType);
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.
                    RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(e.getKey())) {

                if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                    businessException = new BusinessException(RcaBusinessKey.RCDC_RCO_APP_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY, e);
                } else {
                    businessException = new BusinessException(BusinessKey.RCDC_RCO_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY, e);
                }
            }
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_START_ITEM_FAIL_DESC, e, tmpUserName, tmpDesktopName,
                    businessException.getI18nMessage(), desktopType);
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
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_START_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_START_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_APP_DESKTOP_START_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_START_BATCH_RESULT);
            }
        }
    }

}
