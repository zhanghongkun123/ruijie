package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktopsession.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopSessionServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopOperateAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopShutdownRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.CloudDesktopStartRequest;
import com.ruijie.rcos.rcdc.rco.module.def.desktopsession.DesktopSessionDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;

/**
 * Description: 注销会话批量任务处理器
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年02月23日
 *
 * @author wangjie9
 */
public class DeleteDesktopUserSessionBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDesktopUserSessionBatchTaskHandler.class);

    private static final int SHUTDOWN_RETRY_NUM = 3;

    private final DesktopSessionServiceAPI desktopSessionService;

    private BaseAuditLogAPI auditLogAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private UserDesktopOperateAPI cloudDesktopOperateAPI;

    public DeleteDesktopUserSessionBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.desktopSessionService = SpringBeanHelper.getBean(DesktopSessionServiceAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.cbbDeskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.cloudDesktopOperateAPI = SpringBeanHelper.getBean(UserDesktopOperateAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "taskItem can not be null");

        try {
            DesktopSessionDTO sessionDTO = desktopSessionService.findById(batchTaskItem.getItemID());
            // 下发命令通知oa前，判断桌面是否离线，离线则删除会话信息
            Boolean isOffLine = desktopSessionService.isOffLine(sessionDTO.getDeskId());
            if (isOffLine) {
                desktopSessionService.deleteSessionById(batchTaskItem.getItemID());
            } else {
                // 将对应会话状态修改为注销中
                desktopSessionService.updateSessionStatus(batchTaskItem.getItemID());
                // 通知OA进行注销会话
                desktopSessionService.destroySessionInDestroying(batchTaskItem.getItemID());
            }
            // VDI动态池单会话,需要关机
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(sessionDTO.getDeskId());
            if (CbbCloudDeskType.VDI == deskDTO.getDeskType() && DesktopPoolType.DYNAMIC == deskDTO.getDesktopPoolType()
                    && CbbDesktopSessionType.SINGLE == deskDTO.getSessionType()) {
                // 先gt关机，尝试失败后再强制关机
                int retryNum = 0;
                while (retryNum < SHUTDOWN_RETRY_NUM) {
                    retryNum++;
                    LOGGER.info("动态池单会话桌面:{} 注销会话时自动关机,尝试第{}次关机", deskDTO.getDeskId(), retryNum);
                    if (shutdownDesktop(deskDTO, retryNum == SHUTDOWN_RETRY_NUM)) {
                        break;
                    }
                }
            }

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_LOG, sessionDTO.getUserName(), sessionDTO.getDesktopName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_FAIL_LOG, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_TASK_DESC_FAIL).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_DESTROY_SESSION_RESULT);
    }

    private boolean shutdownDesktop(CbbDeskDTO desktopDTO, boolean force) {
        try {
            CbbDeskDTO deskDTO = cbbDeskMgmtAPI.getDeskById(desktopDTO.getDeskId());
            if (CbbCloudDeskState.SLEEP == deskDTO.getDeskState()) {
                CloudDesktopStartRequest startRequest = new CloudDesktopStartRequest();
                startRequest.setId(deskDTO.getDeskId());
                cloudDesktopOperateAPI.start(startRequest);
                return false;
            }
            if (CbbCloudDeskState.RUNNING == deskDTO.getDeskState()) {
                CloudDesktopShutdownRequest shutdownRequest = new CloudDesktopShutdownRequest();
                shutdownRequest.setId(deskDTO.getDeskId());
                shutdownRequest.setForce(force);
                cloudDesktopOperateAPI.shutdown(shutdownRequest);
                if (force) {
                    // 强制关机记录日志
                    auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_FORCE_SHUTDOWN, deskDTO.getName());
                }
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("管理员注销会话,VDI动态池单会话桌面[{}]，尝试关机失败, force:{}", desktopDTO.getDeskId(), force, e);
            String msg = e instanceof BusinessException ? ((BusinessException) e).getI18nMessage() : e.getMessage();
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_USER_DESTROY_SESSION_SHUTDOWN_FAIL, desktopDTO.getName(), msg);
        }
        return false;
    }
}
