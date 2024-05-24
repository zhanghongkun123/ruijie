package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import java.util.Iterator;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbSetIDVDeskNetworkDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
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

/**
 * Description: 批量修改IDV云桌面DNS批任务处理器
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月16日
 *
 * @author heruiyuan1
 */
public class EditIDVDesktopDnsBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditIDVDesktopDnsBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    public EditIDVDesktopDnsBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public EditIDVDesktopDnsBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public EditIDVDesktopDnsBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public EditIDVDesktopDnsBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopOperateAPI, "cloudDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public EditIDVDesktopDnsBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cbbIDVDeskOperateAPI, "Param [cbbIDVDeskOperateAPI] must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    @Override
    public EditIDVDesktopDnsBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        Assert.notNull(cbbIDVDeskMgmtAPI, "Param [cbbIDVDeskMgmtAPI] must not be null");
        this.cbbIDVDeskMgmtAPI = cbbIDVDeskMgmtAPI;
        return this;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem is not null");
        Assert.notNull(batchTaskItem.getItemID(), "taskItemId is not null");

        EditIDVDesktopDnsBatchTaskItem taskItem = (EditIDVDesktopDnsBatchTaskItem) batchTaskItem;
        UUID deskId = taskItem.getItemID();
        CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
        String tmpDesktopName = cloudDesktopDetailDTO.getDesktopName();

        try {
            LOGGER.info("准备下发编辑IDV云桌面[id={}]DNS命令", deskId);
            CbbSetIDVDeskNetworkDTO req = new CbbSetIDVDeskNetworkDTO();
            req.setAutoDns(taskItem.getAutoDns());
            req.setDns(taskItem.getDns());
            req.setDnsBack(taskItem.getDnsBack());
            // req剩余参数默认为空(autoDhcp,ipAddr,mask,gateway)
            cbbIDVDeskMgmtAPI.setIDVDeskNetwork(deskId, req);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SUC_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SUC_DESC)
                    .msgArgs(new String[]{tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送设置IDV云桌面DNS命令失败，云桌面id=" + deskId, e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_FAIL_LOG, e, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_FAIL_DESC, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_RESULT_SUC)
                        .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_SINGLE_RESULT_FAIL)
                        .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_DNS_BATCH_RESULT);
        }
    }

}
