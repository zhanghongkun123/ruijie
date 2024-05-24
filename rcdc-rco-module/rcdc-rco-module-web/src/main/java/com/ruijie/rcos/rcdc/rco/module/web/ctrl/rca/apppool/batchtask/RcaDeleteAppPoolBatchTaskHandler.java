package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.apppool.batchtask;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;

import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppGroupAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaAppPoolAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaOneClientNotifyAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaAppPoolBaseDTO;
import com.ruijie.rcos.rcdc.rca.module.def.api.dto.RcaGroupDTO;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rco.module.def.api.enums.AdminDataPermissionType;
import com.ruijie.rcos.rcdc.rco.module.def.utils.PermissionHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量删除融合应用池
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月26日
 *
 * @author zhengjingyong
 */
public class RcaDeleteAppPoolBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RcaEditAppPoolBatchTaskHandler.class);

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    private BaseAuditLogAPI auditLogAPI;

    private RcaAppPoolAPI rcaAppPoolAPI;

    private RcaHostAPI rcaHostAPI;

    private RcaAppGroupAPI rcaAppGroupAPI;

    private RcaOneClientNotifyAPI rcaOneClientNotifyAPI;

    private List<String> terminalIdList;

    private String appPoolName = "";

    private PermissionHelper permissionHelper;
    
    private UUID adminId;

    public RcaDeleteAppPoolBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI, RcaAppPoolAPI rcaAppPoolAPI,
            RcaHostAPI rcaHostAPI, UUID adminId) {
        super(iterator);
        this.rcaHostAPI = rcaHostAPI;
        this.rcaAppPoolAPI = rcaAppPoolAPI;
        this.auditLogAPI = auditLogAPI;
        this.terminalIdList = new ArrayList<>();
        this.adminId = adminId;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UUID poolId = batchTaskItem.getItemID();
        try {
            RcaAppPoolBaseDTO rcaAppPoolBaseDTO = rcaAppPoolAPI.getAppPoolById(poolId);
            appPoolName = rcaAppPoolBaseDTO.getName();

            checkPoolBeforeDelete(rcaAppPoolBaseDTO);

            List<RcaGroupDTO> groupDTOList = rcaAppGroupAPI.findAllGroupByPoolId(poolId);
            List<String> onlineTerminalList = rcaOneClientNotifyAPI.getAllOnlineTerminalByAppGroupId(
                    groupDTOList.stream().map(RcaGroupDTO::getId).distinct().collect(Collectors.toList()));
            if (!CollectionUtils.isEmpty(onlineTerminalList)) {
                terminalIdList.addAll(onlineTerminalList);
            }

            rcaAppPoolAPI.deleteAppPool(poolId, shouldOnlyDeleteDataFromDb);
            permissionHelper.deleteByPermissionDataId(String.valueOf(poolId));
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_ITEM_SUCCESS, appPoolName, prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_ITEM_SUCCESS).msgArgs(new String[]{appPoolName, prefix}).build();
        } catch (Exception e) {
            LOGGER.error("删除应用池[{}]发生异常，ex :", appPoolName, e);
            String errorMsg = e.getMessage();
            if (e instanceof BusinessException) {
                errorMsg = ((BusinessException) e).getI18nMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_ITEM_FAIL, appPoolName, errorMsg, prefix);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_ITEM_FAIL)
                    .msgArgs(new String[]{appPoolName, errorMsg, prefix}).build();

        }
    }

    private void checkPoolBeforeDelete(RcaAppPoolBaseDTO rcaAppPoolBaseDTO) throws BusinessException {

        if (!permissionHelper.isAllGroupPermission(adminId)
                && !permissionHelper.hasDataPermission(adminId, String.valueOf(rcaAppPoolBaseDTO.getId()), AdminDataPermissionType.APP_POOL)) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCO_RCA_HAS_NO_DATA_PERMISSION);
        }
        
        if (rcaAppPoolBaseDTO.getPoolState() != RcaEnum.PoolState.AVAILABLE) {
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_UNAVAILABLE_UPDATE_FORBID, rcaAppPoolBaseDTO.getName());
        }

        List<RcaHostDTO> hostDTOList = rcaHostAPI.findAllByPoolIdIn(Lists.newArrayList(rcaAppPoolBaseDTO.getId()));
        if (CollectionUtils.isEmpty(hostDTOList)) {
            LOGGER.info("应用池[{}]下没有主机，直接删除", rcaAppPoolBaseDTO.getName());
            return;
        }

        if (rcaAppPoolBaseDTO.getPoolType() == RcaEnum.PoolType.STATIC && CollectionUtils.isNotEmpty(hostDTOList)) {
            // 静态池有桌面不允许删除
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_DELETE_FAILURE_STATIC_HAS_DESKTOP, rcaAppPoolBaseDTO.getName());
        }

        if (hostDTOList.stream().filter(desktop -> desktop.getHostSourceType() == RcaEnum.HostSourceType.VDI).
                anyMatch(desktop -> desktop.getStatus() != RcaEnum.HostStatus.OFFLINE)) {
            // 只有VDI应用主机需要有桌面状态限制
            throw new BusinessException(RcaBusinessKey.RCDC_RCA_APP_POOL_DELETE_FAILURE_DESKTOP_STATE_NOT_READY, rcaAppPoolBaseDTO.getName());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (successCount > 0 && !CollectionUtils.isEmpty(terminalIdList)) {
            try {
                rcaOneClientNotifyAPI.sendAppChangeToTerminal(terminalIdList);
            } catch (BusinessException e) {
                LOGGER.info("发送应用变更通知到终端发生异常，异常信息为：", e);
            }
        }

        return WebBatchTaskUtils.buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_APP_POOL_BATCH_DELETE_TASK_RESULT, prefix);
    }

    public PermissionHelper getPermissionHelper() {
        return permissionHelper;
    }

    public void setPermissionHelper(PermissionHelper permissionHelper) {
        this.permissionHelper = permissionHelper;
    }

    public void setRcaAppGroupAPI(RcaAppGroupAPI rcaAppGroupAPI) {
        this.rcaAppGroupAPI = rcaAppGroupAPI;
    }

    public void setRcaOneClientNotifyAPI(RcaOneClientNotifyAPI rcaOneClientNotifyAPI) {
        this.rcaOneClientNotifyAPI = rcaOneClientNotifyAPI;
    }

    /**
     * 设置是否是从库表删除
     *
     * @param shouldOnlyDeleteDataFromDb ture说明从库表强制删除
     */
    public void setShouldOnlyDeleteDataFromDb(@Nullable Boolean shouldOnlyDeleteDataFromDb) {
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }
}
