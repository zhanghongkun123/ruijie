package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopRebootRequest;
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
 * Description: 云桌面重启批量任务处理器
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年01月11日
 *
 * @author guoyongxin
 */
public class RestartDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestartDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private String desktopType = Constants.CLOUD_DESKTOP;

    public RestartDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public AbstractDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public AbstractDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public AbstractDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public AbstractDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cbbIDVDeskOperateAPI, "cbbIDVDeskOperateAPI must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    @Override
    public AbstractDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
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

        try {
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
            LOGGER.info("准备下发重启{}类型云桌面[id={}]命令", deskType.name(), cloudDesktopDetailDTO.getId());
            if (deskType == CbbCloudDeskType.VDI) {
                if (cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.SLEEP.toString())) {
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_SLEEP_DESKTOP, deskType.name());
                }
                cloudDesktopOperateAPI.rebootDeskVDI(new CloudDesktopRebootRequest(id));
            } else if (deskType == CbbCloudDeskType.THIRD) {
                cloudDesktopOperateAPI.rebootDeskThird(id);
            } else if (deskType == CbbCloudDeskType.IDV) {
                cbbIDVDeskOperateAPI.rebootDeskIDV(id);
            } else {
                throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_DESKTOP_TYPE_BATCH_TASK
                        , deskType.name(), desktopType);
            }

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_SUC_LOG, tmpUserName, tmpDesktopName, desktopType);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_ITEM_SUC_DESC)
                    .msgArgs(new String[] {tmpUserName, tmpDesktopName, desktopType}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面重启命令失败，云桌面id= " + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_FAIL_LOG, e, tmpUserName, tmpDesktopName, e.getI18nMessage(), desktopType);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_ITEM_FAIL_DESC
                    , e, tmpUserName, tmpDesktopName, e.getI18nMessage(), desktopType);
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
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_APP_DESKTOP_RESTART_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_BATCH_RESULT);
            }
        }
    }
}
