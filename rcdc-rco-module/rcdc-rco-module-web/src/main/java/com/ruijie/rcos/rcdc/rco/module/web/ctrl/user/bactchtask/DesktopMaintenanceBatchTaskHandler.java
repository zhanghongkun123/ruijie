package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.constants.Constants;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * 
 * Description: 云桌面关机批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class DesktopMaintenanceBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DesktopMaintenanceBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private boolean isOpen;

    public DesktopMaintenanceBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, boolean isOpen) {
        super(iterator);
        this.isOpen = isOpen;
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
    }

    @Override
    public DesktopMaintenanceBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public DesktopMaintenanceBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public DesktopMaintenanceBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public DesktopMaintenanceBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cbbIDVDeskOperateAPI must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    @Override
    public DesktopMaintenanceBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        Assert.notNull(cbbIDVDeskMgmtAPI, "cbbIDVDeskMgmtAPI must not be null");
        this.cbbIDVDeskMgmtAPI = cbbIDVDeskMgmtAPI;
        return this;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");
        Assert.notNull(taskItem.getItemID(), "taskItem id is not null");
        UUID id = taskItem.getItemID();
        String desktopName = StringUtils.EMPTY;
        try {
            CloudDesktopDetailDTO cloudDesktopDetailDTO = cloudDesktopWebService.obtainCloudDesktopResponse(id);
            checkDeskSupportMaintenance(cloudDesktopDetailDTO);
            desktopName = cloudDesktopDetailDTO.getDesktopName();
            if (isOpen) {
                shutdownDesktop(cloudDesktopDetailDTO);
            }
            cloudDesktopOperateAPI.changeDeskMaintenanceModel(Lists.newArrayList(id), isOpen);
            auditLogAPI.recordLog(getSuccessDescI18nKey(isOpen), desktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS).msgKey(getSuccessDescI18nKey(isOpen))
                    .msgArgs(new String[]{desktopName}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(getFailDescI18nKey(isOpen), desktopName, e.getI18nMessage());
            throw new BusinessException(getFailDescI18nKey(isOpen), e, desktopName, e.getI18nMessage());
        }
    }

    private void checkDeskSupportMaintenance(CloudDesktopDetailDTO cloudDesktopDetailDTO) throws BusinessException {
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO not allow null");
        if (CbbCloudDeskType.VDI.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType()) ||
                CbbCloudDeskType.THIRD.name().equalsIgnoreCase(cloudDesktopDetailDTO.getDeskType())) {
            return;
        }
        throw new BusinessException(UserBusinessKey.RCDC_DESKTOP_MAINTENANCE_NOT_SUPPORT_IDV_OR_DESKTOPPOOL);

    }

    private void shutdownDesktop(CloudDesktopDetailDTO cloudDesktopDetailDTO) {
        Assert.notNull(cloudDesktopDetailDTO, "cloudDesktopDetailDTO is not null");
        if (!cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.RUNNING.toString())
                && !cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.SLEEP.toString())) {
            LOGGER.info("只能关闭处于运行或休眠状态的桌面，当前桌面运行状态为[{}]", cloudDesktopDetailDTO.getDesktopState());
            return;
        }

        try {
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
            LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), cloudDesktopDetailDTO.getId());
            switch (deskType) {
                case VDI:
                case THIRD:
                    if (cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.SLEEP.toString())) {
                        LOGGER.info("唤醒云桌面[{}]", cloudDesktopDetailDTO.getDesktopName());
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(cloudDesktopDetailDTO.getId()));
                    }
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(cloudDesktopDetailDTO.getId(), Boolean.FALSE));
                    break;
                default:
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_RESTART_FAIL_NOT_SUPPORT_DESKTOP_TYPE_BATCH_TASK, deskType.name());
            }
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面关闭命令失败，云桌面id=" + cloudDesktopDetailDTO.getId().toString(), e);
            if (com.ruijie.rcos.rcdc.hciadapter.module.def.BusinessKey.
                    RCDC_HCIADAPTER_VM_WAKE_ERROR_BY_RESOURCE_INSUFFICIENTLY.equals(e.getKey())) {
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_WAKE_UP_FAIL_LOG, cloudDesktopDetailDTO.getUserName(),
                        cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
                return;
            }
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_FAIL_LOG, cloudDesktopDetailDTO.getUserName(), 
                    Constants.CLOUD_DESKTOP, cloudDesktopDetailDTO.getDesktopName(), e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (failCount == 0) {
            return buildTaskSuccessResult();
        }
        return buildTaskFailResult();
    }

    private DefaultBatchTaskFinishResult buildTaskSuccessResult() {
        if (processItemCount.get() == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(getFinishSuccessDescI18nKey(isOpen, true)).msgArgs(new String[]{desktopName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                .msgKey(getFinishSuccessDescI18nKey(isOpen, false)).msgArgs(new String[]{}).build();
    }

    private DefaultBatchTaskFinishResult buildTaskFailResult() {
        if (processItemCount.get() == 1) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(getFinishFailDescI18nKey(isOpen, true)).msgArgs(new String[]{desktopName}).build();
        }
        return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                .msgKey(getFinishFailDescI18nKey(isOpen, false)).msgArgs(new String[]{}).build();
    }

    private String getFinishSuccessDescI18nKey(boolean isOpen, boolean isSingle) {
        if (isOpen) {
            return isSingle ? UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_SUC :
                    UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_ITEM_SUC;
        } else {
            return isSingle ? UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_SUC :
                    UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_ITEM_SUC;
        }
    }

    private String getFinishFailDescI18nKey(boolean isOpen, boolean isSingle) {
        if (isOpen) {
            return isSingle ? UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_FAIL :
                    UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_ITEM_FAIL;
        } else {
            return isSingle ? UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_FAIL :
                    UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_ITEM_FAIL;
        }
    }

    private String getSuccessDescI18nKey(boolean isOpen) {
        if (isOpen) {
            return UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_SUC;
        }
        return  UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_SUC;
    }

    private String getFailDescI18nKey(boolean isOpen) {
        if (isOpen) {
            return UserBusinessKey.RCDC_DESKTOP_OPEN_MAINTENANCE_SINGLE_FAIL;
        }
        return  UserBusinessKey.RCDC_DESKTOP_CLOSE_MAINTENANCE_SINGLE_FAIL;
    }

}
