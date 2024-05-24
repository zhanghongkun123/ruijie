package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolModel;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.AdminDataPermissionAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolUserMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.PoolDesktopInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.rco.module.web.util.WebBatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Description: 删除桌面池批处理handler
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/25 4:07 下午
 *
 * @author linke
 */
public class DeleteDesktopPoolBatchHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteDesktopPoolBatchHandler.class);

    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private DesktopPoolUserMgmtAPI desktopPoolUserMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private String singlePoolName = "";

    private boolean isSingleTask;

    private AdminDataPermissionAPI adminDataPermissionAPI;

    private Boolean shouldOnlyDeleteDataFromDb;

    private String prefix;

    public DeleteDesktopPoolBatchHandler(List<DefaultBatchTaskItem> taskItemList, Boolean shouldOnlyDeleteDataFromDb) {
        super(taskItemList);
        this.desktopPoolMgmtAPI = SpringBeanHelper.getBean(DesktopPoolMgmtAPI.class);
        this.desktopPoolUserMgmtAPI = SpringBeanHelper.getBean(DesktopPoolUserMgmtAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.isSingleTask = taskItemList.size() == 1;
        this.adminDataPermissionAPI = SpringBeanHelper.getBean(AdminDataPermissionAPI.class);
        this.shouldOnlyDeleteDataFromDb = shouldOnlyDeleteDataFromDb;
        this.prefix = WebBatchTaskUtils.getDeletePrefix(shouldOnlyDeleteDataFromDb);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");



        UUID desktopPoolId = taskItem.getItemID();
        CbbDesktopPoolDTO desktopPoolDTO;
        String tempName = desktopPoolId.toString();
        try {
            desktopPoolDTO = cbbDesktopPoolMgmtAPI.getDesktopPoolDetail(desktopPoolId);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_FAIL_DESC, e, tempName, e.getI18nMessage(), prefix);
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_FAIL_DESC, e, tempName, e.getI18nMessage(), prefix);
        }
        tempName = desktopPoolDTO.getName();
        singlePoolName = desktopPoolDTO.getName();
        try {
            checkDesktopPoolBeforeDelete(desktopPoolDTO);
            if (Boolean.TRUE.equals(shouldOnlyDeleteDataFromDb)) {
                desktopPoolMgmtAPI.deleteDesktopPoolFromDb(desktopPoolId);
            } else {
                desktopPoolMgmtAPI.deleteDesktopPool(desktopPoolId);
            }

            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_SUCCESS_DESC, tempName, prefix);
            // 删除数据权限
            adminDataPermissionAPI.deleteByPermissionDataId(desktopPoolId.toString());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_SUCCESS_DESC)
                    .msgArgs(new String[]{tempName, prefix}).build();
        } catch (BusinessException e) {
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_FAIL_DESC, e, tempName, e.getI18nMessage(), prefix);
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DESKTOP_POOL_DELETE_ITEM_FAIL_DESC, e, tempName, e.getI18nMessage(), prefix);
        }
    }

    private void checkDesktopPoolBeforeDelete(CbbDesktopPoolDTO desktopPool) throws BusinessException {

        if (desktopPool.getPoolState() != CbbDesktopPoolState.AVAILABLE) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_UNAVAILABLE_DELETE_FAIL,
                    desktopPool.getId().toString(), desktopPool.getPoolState().name());
        }

        List<PoolDesktopInfoDTO> desktopList = desktopPoolMgmtAPI.listAllDeskInfoByDesktopPoolId(desktopPool.getId());
        desktopList = desktopList.stream().filter(desktop -> Boolean.FALSE.equals(desktop.getIsDelete()) &&
                desktop.getDeskState() != CbbCloudDeskState.RECYCLE_BIN).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(desktopList)) {
            LOGGER.info("桌面池[{}]下没有桌面，直接删除", desktopPool.getName());
            return;
        }

        if (desktopPool.getPoolModel() == CbbDesktopPoolModel.STATIC && CollectionUtils.isNotEmpty(desktopList)) {
            // 静态池有桌面不允许删除
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_STATIC_HAS_DESKTOP_DELETE_FAIL, desktopPool.getName());
        }

        if (desktopPool.getPoolType() == CbbDesktopPoolType.THIRD && CollectionUtils.isNotEmpty(desktopList)) {
            // 第三方桌面池有桌面不允许删除
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_THIRD_PARTY_HAS_DESKTOP_DELETE_FAIL, desktopPool.getName());
        }

        if (desktopList.stream().anyMatch(desktop -> desktop.getDeskState() != CbbCloudDeskState.CLOSE)) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_STATE_NOT_READY_DELETE_FAIL, desktopPool.getName());
        }
        boolean isInUse = desktopPoolUserMgmtAPI.checkIsDesktopInUse(desktopPool.getId());
        if (isInUse) {
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_IN_USE_DELETE_FAIL, desktopPool.getName());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        if (isSingleTask) {
            if (failCount == 0) {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                        .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL_SINGLE_TASK_SUCCESS)
                        .msgArgs(new String[]{singlePoolName, prefix}).build();
            } else {
                return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                        .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL_SINGLE_TASK_FAIL)
                        .msgArgs(new String[]{singlePoolName, prefix}).build();
            }
        }
        if (failCount == 0) {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL_TASK_SUCCESS)
                    .msgArgs(new String[]{prefix}).build();
        } else {
            return DefaultBatchTaskFinishResult.builder().batchTaskStatus(BatchTaskStatus.FAILURE)
                    .msgKey(DesktopPoolBusinessKey.RCDC_DESKTOP_POOL_DELETE_DESKTOP_POOL_TASK_FAIL)
                    .msgArgs(new String[]{prefix}).build();
        }
    }
}
