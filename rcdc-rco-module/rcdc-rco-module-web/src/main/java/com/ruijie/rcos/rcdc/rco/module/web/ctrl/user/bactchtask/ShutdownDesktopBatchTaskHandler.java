package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.ImageUsageTypeEnum;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
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
import java.util.concurrent.TimeUnit;

/**
 * 
 * Description: 云桌面关机批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 * 
 * @author artom
 */
public class ShutdownDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutdownDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private String desktopType = Constants.CLOUD_DESKTOP;


    public ShutdownDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public ShutdownDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public ShutdownDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public ShutdownDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }

    @Override
    public ShutdownDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cbbIDVDeskOperateAPI must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
    }

    @Override
    public ShutdownDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
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

        if (cloudDesktopDetailDTO.getImageUsage() == ImageUsageTypeEnum.APP) {
            desktopType = Constants.APP_CLOUD_DESKTOP;
        }

        try {
            if (!cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.RUNNING.toString())
                    && !cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.SLEEP.toString())) {
                LOGGER.info("只能关闭处于运行或休眠状态的桌面，当前桌面运行状态为[{}]", cloudDesktopDetailDTO.getDesktopState());
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_SUC_DESC).msgArgs(tmpUserName, desktopType, tmpDesktopName)
                        .build();
            }
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(cloudDesktopDetailDTO.getDesktopCategory());
            LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), cloudDesktopDetailDTO.getId());
            switch (deskType) {
                case IDV:
                    CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                    shutdownDeskIDVDTO.setId(id);
                    shutdownDeskIDVDTO.setIsForce(Boolean.FALSE);
                    shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                    cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                    break;
                case VDI:
                case THIRD:
                    if (cloudDesktopDetailDTO.getDesktopState().equals(CbbCloudDeskState.SLEEP.toString())) {
                        LOGGER.info("唤醒云桌面[{}]", tmpDesktopName);
                        cloudDesktopOperateAPI.start(new CloudDesktopStartRequest(id));
                    }
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(id, Boolean.FALSE));
                    break;
                default:
                    throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
            }

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_SUC_LOG, tmpUserName, desktopType, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_SUC_DESC).msgArgs(tmpUserName, desktopType, tmpDesktopName)
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面关闭命令失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_FAIL_LOG, e, tmpUserName, desktopType,
                    tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_ITEM_FAIL_DESC, e, tmpUserName, desktopType,
                    tmpDesktopName, e.getI18nMessage());
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
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopType, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopType, desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_APP_DESKTOP_SHUTDOWN_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_SHUTDOWN_BATCH_RESULT);
            }
        }
    }

}
