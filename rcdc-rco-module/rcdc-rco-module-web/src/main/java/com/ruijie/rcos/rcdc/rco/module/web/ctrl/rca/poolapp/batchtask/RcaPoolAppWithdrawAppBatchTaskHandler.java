package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.poolapp.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaPoolAppAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaPoolAppDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Description: 下架应用批任务
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年01月15日
 *
 * @author zhengjingyong
 */
public class RcaPoolAppWithdrawAppBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaPoolAppPublishAppBatchTaskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private RcaPoolAppAPI rcaPoolAppAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private Set<String> terminalIdSet;

    private UUID poolId;

    public RcaPoolAppWithdrawAppBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator,
                                                 BaseAuditLogAPI auditLogAPI, RcaPoolAppAPI rcaPoolAppAPI,
                                                RcaAppPoolAPI rcaAppPoolAPI) {
        super(batchTaskItemIterator);
        this.auditLogAPI = auditLogAPI;
        this.rcaPoolAppAPI = rcaPoolAppAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.terminalIdSet = new HashSet<>();
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");

        String appName = "";
        String poolName = "";
        try {
            UUID appId = item.getItemID();
            RcaPoolAppDTO poolAppDTO = rcaPoolAppAPI.getDetail(appId);
            appName = poolAppDTO.getName();

            RcaAppPoolBaseDTO appPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolAppDTO.getPoolId());
            poolName = appPoolBaseDTO.getName();
            poolId = appPoolBaseDTO.getId();

            try {
                List<String> onlineTerminalByAppIdList = rcaPoolAppAPI.getOnlineTerminalByAppId(appId);
                terminalIdSet.addAll(onlineTerminalByAppIdList);
            } catch (Exception ex) {
                LOGGER.error("查询应用分配的在线终端发生异常，应用id: {} ex：", appId, ex);
            }

            rcaPoolAppAPI.withdrawnApp(appId);

            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM_SUCCESS, poolName, appName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM_SUCCESS)
                    .msgArgs(new String[]{poolName, appName}).build();
        } catch (BusinessException e) {
            LOGGER.error(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM_FAIL, poolName, appName, e);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM_FAIL,
                    poolName, appName, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().msgKey(RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_ITEM_FAIL)
                    .msgArgs(poolName, appName, e.getI18nMessage())
                    .batchTaskItemStatus(BatchTaskItemStatus.FAILURE).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int success, int fail) {
        try {
            rcaAppPoolAPI.updatePublishedAppBalance(poolId);
            if (!CollectionUtils.isEmpty(terminalIdSet)) {
                rcaPoolAppAPI.notifyAppWithdrawnToOc(new ArrayList<>(terminalIdSet));
            }
        } catch (BusinessException ex) {
            LOGGER.error("发送应用下架通知到OC失败，ex: ", ex);
        }
        return buildDefaultFinishResult(success, fail, RcaBusinessKey.RCDC_RCA_POOL_APP_BATCH_WITHDRAW_APP_TASK_RESULT);
    }
}
