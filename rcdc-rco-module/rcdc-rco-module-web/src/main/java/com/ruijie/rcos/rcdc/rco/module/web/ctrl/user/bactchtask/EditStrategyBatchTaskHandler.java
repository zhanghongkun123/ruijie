package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;


import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbIDVDeskOperateAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.request.desk.CbbUpdateIDVDeskStrategyIdRequest;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDesktopSessionType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.*;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.CloudDesktopDetailDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.CloudDesktopWebService;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;

/**
 * Description: 批量修改策略
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月16日
 *
 * @author artom
 */
public class EditStrategyBatchTaskHandler extends AbstractDesktopBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditStrategyBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UUID strategyId;

    private DeskStrategyAPI deskStrategyAPI;

    private UUID desktopPoolId;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    private AppCenterHelper appCenterHelper;

    private AppDeliveryMgmtAPI appDeliveryMgmtAPI;

    private UamAppTestAPI uamAppTestAPI;

    public EditStrategyBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, UUID strategyId, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        Assert.notNull(iterator, "iterator is not null");
        Assert.notNull(strategyId, "strategyId is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");

        this.strategyId = strategyId;
        this.auditLogAPI = auditLogAPI;
        this.deskStrategyAPI = SpringBeanHelper.getBean(DeskStrategyAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.appCenterHelper = SpringBeanHelper.getBean(AppCenterHelper.class);
        this.appDeliveryMgmtAPI = SpringBeanHelper.getBean(AppDeliveryMgmtAPI.class);
        this.uamAppTestAPI = SpringBeanHelper.getBean(UamAppTestAPI.class);
    }

    @Override
    public EditStrategyBatchTaskHandler setCloudDesktopWebService(CloudDesktopWebService cloudDesktopWebService) {
        Assert.notNull(cloudDesktopWebService, "cloudDesktopWebService must not be null");
        this.cloudDesktopWebService = cloudDesktopWebService;
        return this;
    }

    @Override
    public EditStrategyBatchTaskHandler setCbbUserDesktopMgmtAPI(UserDesktopMgmtAPI cloudDesktopMgmtAPI) {
        Assert.notNull(cloudDesktopMgmtAPI, "cloudDesktopMgmtAPI must not be null");
        this.cloudDesktopMgmtAPI = cloudDesktopMgmtAPI;
        return this;
    }

    @Override
    public EditStrategyBatchTaskHandler setCbbUserDesktopOperateAPI(UserDesktopOperateAPI cloudDesktopOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public EditStrategyBatchTaskHandler setCbbIDVDeskOperateAPI(CbbIDVDeskOperateAPI cbbIDVDeskOperateAPI) {
        // 不需要设置
        return null;
    }

    @Override
    public EditStrategyBatchTaskHandler setcbbIDVDeskMgmtAPI(CbbIDVDeskMgmtAPI cbbIDVDeskMgmtAPI) {
        Assert.notNull(cbbIDVDeskMgmtAPI, "Param [cbbIDVDeskMgmtAPI] must not be null");
        this.cbbIDVDeskMgmtAPI = cbbIDVDeskMgmtAPI;
        return this;
    }

    /**
     * 设置桌面策略service
     *
     * @param deskStrategyAPI deskStrategyAPI
     * @return EditStrategyBatchTaskHandler
     */
    public EditStrategyBatchTaskHandler setDeskStrategyAPI(DeskStrategyAPI deskStrategyAPI) {
        Assert.notNull(deskStrategyAPI, "Param [DeskStrategyAPI] must not be null");
        this.deskStrategyAPI = deskStrategyAPI;
        return this;
    }

    /**
     * deskStrategyTciNotifyAPI
     *
     * @param deskStrategyTciNotifyAPI deskStrategyTciNotifyAPI
     * @return deskStrategyTciNotifyAPI
     */
    public EditStrategyBatchTaskHandler setDeskStrategyTciNotifyAPI(DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI) {
        this.deskStrategyTciNotifyAPI = deskStrategyTciNotifyAPI;
        return this;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem is not null");

        UUID deskId = taskItem.getItemID();
        CloudDesktopDetailDTO desktopDTO = cloudDesktopWebService.obtainCloudDesktopResponse(deskId);
        CbbCloudDeskPattern targetPattern = deskStrategyAPI.findPatternById(strategyId);
        CbbDesktopSessionType sessionType = deskStrategyAPI.findSessionTypeById(strategyId);
        // 校验将要变更的桌面模式和会话类型是否符合要求
        checkPatternChangeBeforeUpdate(desktopDTO, targetPattern, sessionType);
        String tmpDesktopName = desktopDTO.getDesktopName();
        try {
            CbbCloudDeskType deskType = CbbCloudDeskType.valueOf(desktopDTO.getDesktopCategory());
            LOGGER.info("准备编辑修改云桌面[id:{}]策略", desktopDTO.getId());
            switch (deskType) {
                case IDV:
                    cbbIDVDeskMgmtAPI.updateDeskStrategyIDV(new CbbUpdateIDVDeskStrategyIdRequest(taskItem.getItemID(), strategyId));
                    // update后的处理
                    afterIDVUpdateDeskStrategy(desktopDTO, targetPattern);
                    break;
                case VDI:
                    // VDI变更前检查
                    checkVDIConfigBeforeUpdate(desktopDTO);
                    cloudDesktopMgmtAPI.configStrategy(new EditDesktopStrategyRequest(taskItem.getItemID(), strategyId));
                    break;
                case THIRD:
                    // 第三方桌面也需要检查是否动态桌面池桌面
                    checkVDIConfigBeforeUpdate(desktopDTO);
                    cloudDesktopMgmtAPI.updateDeskStrategyThirdParty(new EditDesktopStrategyRequest(taskItem.getItemID(), strategyId));
                    break;
                default:
                    throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
            }

            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_SUC_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_SUC_DESC).msgArgs(new String[]{tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面的桌面策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_FAIL_LOG, e, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_ITEM_FAIL_DESC, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    /**
     * 校验将要变更的桌面模式和会话类型是否符合要求
     *
     * @param desktopDTO desktopDTO
     * @throws BusinessException 业务异常
     */
    private void checkPatternChangeBeforeUpdate(CloudDesktopDetailDTO desktopDTO, CbbCloudDeskPattern targetPattern,
                                                CbbDesktopSessionType sessionType) throws BusinessException {
        if (desktopDTO.getUserProfileStrategyId() != null && CbbCloudDeskPattern.RECOVERABLE != targetPattern) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_CONFLICT_WITH_UPM, desktopDTO.getDesktopName());
        }

        if (desktopDTO.getSessionType() != sessionType) {
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_CHECK_SESSION_FAIL, desktopDTO.getDesktopName());
        }

        // 个性切还原/应用分层需要校验桌面测试信息
        if (isPatternPersonalToOther(desktopDTO, targetPattern)) {
            appCenterHelper.checkTestingDesk(desktopDTO.getId());
        }
    }

    private void checkVDIConfigBeforeUpdate(CloudDesktopDetailDTO desktopDTO) throws BusinessException {
        if (Objects.isNull(desktopPoolId) && Objects.equals(desktopDTO.getDesktopPoolType(), DesktopPoolType.DYNAMIC.name())) {
            // 没传池ID是针对单个桌面的操作，同时桌面因为是动态池桌面不支持单独修改
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_DYNAMIC_POOL_NOT_SUPPORT, desktopDTO.getDesktopName());
        }
    }

    private void afterIDVUpdateDeskStrategy(CloudDesktopDetailDTO desktopDTO, CbbCloudDeskPattern targetPattern) {
        // TCI公共终端策略变更下发：
        if (deskStrategyTciNotifyAPI != null) {
            deskStrategyTciNotifyAPI.notifyDeskFetchStartParams(desktopDTO.getId());
        }
        // 发送水印配置
        desktopDTO = cloudDesktopWebService.obtainCloudDesktopResponse(desktopDTO.getId());
        deskStrategyAPI.sendDesktopStrategyWatermark(desktopDTO);

        if (Boolean.TRUE.equals(isPatternPersonalToOther(desktopDTO, targetPattern))) {
            appDeliveryMgmtAPI.deleteDeliveryObjectWhenStrategyModify(desktopDTO.getId());
            try {
                uamAppTestAPI.deleteCompletedTestDeskWhenStrategyModify(desktopDTO.getId());
            } catch (BusinessException e) {
                LOGGER.error("删除桌面[{}]测试组异常", desktopDTO.getId(), e);
            }
        }
    }

    private boolean isPatternPersonalToOther(CloudDesktopDetailDTO desktopDTO, CbbCloudDeskPattern targetPattern) {
        return CbbCloudDeskPattern.PERSONAL.name().equals(desktopDTO.getDesktopType()) && targetPattern != CbbCloudDeskPattern.PERSONAL;
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (Objects.isNull(desktopPoolId)) {
            return normalDesktopResult(sucCount, failCount);
        } else {
            // 桌面池编辑策略结果
            return poolDesktopResult(sucCount, failCount);
        }
    }

    private BatchTaskFinishResult normalDesktopResult(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_RESULT_SUC)
                        .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[]{desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_STRATEGY_BATCH_RESULT);
    }

    private BatchTaskFinishResult poolDesktopResult(int sucCount, int failCount) {
        updateDesktopPoolState();
        // 桌面池批量修改云桌面策略，都是绿字
        if (failCount > 0) {
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_POOL_STRATEGY_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[]{String.valueOf(sucCount), String.valueOf(failCount)}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }
        return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_EDIT_POOL_STRATEGY_SINGLE_RESULT_SUC)
                .msgArgs(new String[]{String.valueOf(sucCount)}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
    }

    private void updateDesktopPoolState() {
        if (Objects.isNull(desktopPoolId)) {
            return;
        }
        try {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolId, CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新桌面池[%s]信息失败", desktopPoolId), e);
        }
    }

    public void setDesktopPoolId(UUID desktopPoolId) {
        this.desktopPoolId = desktopPoolId;
    }
}
