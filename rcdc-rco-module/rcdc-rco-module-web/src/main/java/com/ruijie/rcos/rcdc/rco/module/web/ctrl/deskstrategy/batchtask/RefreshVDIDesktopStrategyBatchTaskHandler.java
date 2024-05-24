package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskStrategyMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyVDIDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskType;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskStrategyState;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskSpecAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserDesktopMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.EditDesktopStrategyRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.CloudDesktopBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
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
 *
 * Description: 修改VDI策略的CPU、内存、系统盘或个人盘后，批量修改关联的云桌面的策略
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年02月01日
 *
 * @author lk
 */
public class RefreshVDIDesktopStrategyBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RefreshVDIDesktopStrategyBatchTaskHandler.class);

    private CbbVDIDeskStrategyMgmtAPI cbbVDIDeskStrategyMgmtAPI;

    private UserDesktopMgmtAPI userDesktopMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private DeskSpecAPI deskSpecAPI;

    private String desktopName;

    /** 处理的记录数累计值 */
    private AtomicInteger processItemCount = new AtomicInteger(0);

    public RefreshVDIDesktopStrategyBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItems,
                                                     BaseAuditLogAPI auditLogAPI) {
        super(batchTaskItems);
        Assert.notNull(batchTaskItems, "batchTaskItems is not null");
        Assert.notNull(auditLogAPI, "auditLogAPI is not null");

        this.auditLogAPI = auditLogAPI;
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.cbbVDIDeskStrategyMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskStrategyMgmtAPI.class);
        this.userDesktopMgmtAPI = SpringBeanHelper.getBean(UserDesktopMgmtAPI.class);
        this.deskSpecAPI = SpringBeanHelper.getBean(DeskSpecAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem is not null");
        RefreshStrategyBatchTaskItem taskItem = (RefreshStrategyBatchTaskItem) batchTaskItem;
        Assert.notNull(taskItem.getStrategyId(), "strategyId is not null");
        UUID strategyId = taskItem.getStrategyId();

        CbbDeskDTO cbbDeskDTO = cbbVDIDeskMgmtAPI.getDeskVDI(taskItem.getItemID());
        String tmpDesktopName = cbbDeskDTO.getName();

        LOGGER.info("准备编辑修改云桌面[id:{}]策略", cbbDeskDTO.getDeskId());
        try {

            // 获取现在的策略信息详情
            CbbDeskStrategyVDIDTO deskStrategyVDIDTO = cbbVDIDeskStrategyMgmtAPI.getDeskStrategyVDI(strategyId);

            CbbCloudDeskType deskType = cbbDeskDTO.getDeskType();
            if (CbbCloudDeskType.VDI != deskType) {
                throw new BusinessException("不支持的云桌面类型:{0}", deskType.name());
            }

            if (deskStrategyVDIDTO.getState() != CbbDeskStrategyState.AVAILABLE) {
                throw new BusinessException(CloudDesktopBusinessKey.RCDC_CLOUDDESKTOP_STRATEGY_NOT_AVAILABLE);
            }

            configStrategy(cbbDeskDTO, deskStrategyVDIDTO);


            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SUC_LOG, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_SUC_DESC).msgArgs(new String[] {tmpDesktopName}).build();
        } catch (BusinessException e) {
            LOGGER.error("编辑云桌面的桌面策略失败，云桌面id=" + taskItem.getItemID().toString(), e);
            auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_FAIL_LOG, tmpDesktopName, e.getI18nMessage());
            throw new BusinessException(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_ITEM_FAIL_DESC, e, tmpDesktopName, e.getI18nMessage());
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    private void configStrategy(CbbDeskDTO cbbDeskDTO, CbbDeskStrategyVDIDTO deskStrategyVDIDTO) throws BusinessException {
        // 做个比对是否需要刷新策略
        EditDesktopStrategyRequest request = new EditDesktopStrategyRequest(cbbDeskDTO.getDeskId(), deskStrategyVDIDTO.getId());
        userDesktopMgmtAPI.configStrategy(request);
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_RESULT_SUC)
                        .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[] {desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, UserBusinessKey.RCDC_RCO_DESKTOP_REFRESH_STRATEGY_BATCH_RESULT);
    }
}
