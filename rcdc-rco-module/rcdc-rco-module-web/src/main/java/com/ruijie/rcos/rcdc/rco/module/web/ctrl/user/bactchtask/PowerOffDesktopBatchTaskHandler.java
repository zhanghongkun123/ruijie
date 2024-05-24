package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desk.CbbShutdownDeskIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
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
 * Description: 云桌面强制关机批量任务处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年4月16日
 *
 * @author zhuangchenwu
 */
public class PowerOffDesktopBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PowerOffDesktopBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    protected String desktopType = Constants.CLOUD_DESKTOP;

    public PowerOffDesktopBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    @Override
    public PowerOffDesktopBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public PowerOffDesktopBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public PowerOffDesktopBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "CbbUserDesktopOperateAPI must not be null");
        this.cloudDesktopOperateAPI = cloudDesktopOperateAPI;
        return this;
    }
    
    @Override
    public PowerOffDesktopBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cbbIDVDeskOperateAPI must not be null");
        this.cbbIDVDeskOperateAPI = cbbIDVDeskOperateAPI;
        return this;
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
            LOGGER.info("准备下发关闭{}类型云桌面[id={}]命令", deskType.name(), cloudDesktopDetailDTO.getId());
            switch (deskType) {
                case IDV:
                    CbbShutdownDeskIDVDTO shutdownDeskIDVDTO = new CbbShutdownDeskIDVDTO();
                    shutdownDeskIDVDTO.setId(id);
                    shutdownDeskIDVDTO.setIsForce(Boolean.TRUE);
                    shutdownDeskIDVDTO.setTimeout(TimeUnit.MINUTES.toMillis(5));
                    cbbIDVDeskOperateAPI.shutdownDeskIDV(shutdownDeskIDVDTO);
                    break;
                case VDI:
                    cloudDesktopOperateAPI.shutdown(new CloudDesktopShutdownRequest(id, Boolean.TRUE));
                    break;
                case THIRD:
                    throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_FAIL_THIRD_PARTY, cloudDesktopDetailDTO.getDesktopName());
                default:
                    throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
            }
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_SUC_LOG, tmpUserName, tmpDesktopName, desktopType);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_ITEM_SUC_DESC).msgArgs(tmpUserName, tmpDesktopName, desktopType)
                    .build();
        } catch (BusinessException e) {
            LOGGER.error("发送云桌面强制关闭命令失败，云桌面id=" + id.toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_FAIL_LOG, e, tmpUserName, tmpDesktopName,
                    e.getI18nMessage(), desktopType);
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_ITEM_FAIL_DESC, e, tmpUserName, tmpDesktopName,
                    e.getI18nMessage(), desktopType);
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
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_SUC)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_SINGLE_FAIL)
                        .msgArgs(new String[] {userName, desktopName, desktopType}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
            }
        } else {
            if (desktopType.equals(Constants.APP_CLOUD_DESKTOP)) {
                return buildDefaultFinishResult(sucCount, failCount, RcaBusinessKey.RCDC_RCO_APP_DESKTOP_POWER_OFF_BATCH_RESULT);
            } else {
                return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_POWER_OFF_BATCH_RESULT);
            }
        }
    }

    @Override
    public AbstractDesktopBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        // 不需要设置
        return null;
    }

}
