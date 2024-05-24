package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.Iterator;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import com.ruijie.rcos.rcdc.task.ext.module.def.batch.AbstractProgressAwareBatchTaskHandler;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDeskSpecAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbDesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.CbbDesktopPoolState;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.desktoppool.DesktopPoolType;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.CreateDesktopResponse;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolBasicDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.DesktopPoolConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.request.CreatePoolDesktopRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/20 15:27
 *
 * @author yanlin
 */
public class CreatePoolDesktopBatchTaskHandler extends AbstractProgressAwareBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatePoolDesktopBatchTaskHandler.class);

    private static final String NAME_FORMAT = "%s_%s";

    private BaseAuditLogAPI auditLogAPI;

    private DesktopPoolConfigDTO desktopPoolConfigDTO;

    private DesktopPoolBasicDTO desktopPoolDTO;

    private AtomicInteger startIndex;

    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private CbbDesktopPoolMgmtAPI cbbDesktopPoolMgmtAPI;

    private CbbDeskMgmtAPI cbbDeskMgmtAPI;

    private CbbDeskSpecAPI cbbDeskSpecAPI;

    public CreatePoolDesktopBatchTaskHandler(DesktopPoolBasicDTO desktopPoolDTO, int startIndex,
                                             Iterator<? extends BatchTaskItem> iterator, DesktopPoolConfigDTO desktopPoolConfigDTO) {
        super(iterator);
        this.desktopPoolConfigDTO = desktopPoolConfigDTO;
        this.desktopPoolDTO = desktopPoolDTO;
        this.startIndex = new AtomicInteger(startIndex);

        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.desktopPoolMgmtAPI = SpringBeanHelper.getBean(DesktopPoolMgmtAPI.class);
        this.cbbDesktopPoolMgmtAPI = SpringBeanHelper.getBean(CbbDesktopPoolMgmtAPI.class);
        this.cbbDeskMgmtAPI = SpringBeanHelper.getBean(CbbDeskMgmtAPI.class);
        this.cbbDeskSpecAPI = SpringBeanHelper.getBean(CbbDeskSpecAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem cannot be null");

        UUID deskId = batchTaskItem.getItemID();
        String poolName = desktopPoolDTO.getName();
        LOGGER.info("开始创建池[{}]中的桌面, deskId:{}", poolName, deskId);
        String deskNamePrefix = desktopPoolDTO.getDesktopNamePrefix();
        try {
            String desktopName = buildDesktopName(deskNamePrefix);
            LOGGER.info("开始创建池桌面, 云桌面名称{}", desktopName);
            CreateDesktopResponse createResponse = createPoolDesktop(deskId, desktopName, desktopPoolDTO, batchTaskItem);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_CREATE_SUCCESS_LOG,
                    poolName, createResponse.getDesktopName());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_CREATE_SUCCESS_LOG)
                    .msgArgs(new String[]{poolName, createResponse.getDesktopName()}).build();
        } catch (Exception e) {
            String message;
            if (e instanceof BusinessException) {
                BusinessException ex = (BusinessException) e;
                message = ex.getI18nMessage();
            } else {
                message = e.getMessage();
            }
            LOGGER.error(String.format("桌面池[%s]创建云桌面[%s]失败", poolName, deskId), e);
            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_CREATE_FAIL_LOG, poolName, message);
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_CREATE_FAIL_LOG, e, poolName, message);
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        try {
            cbbDesktopPoolMgmtAPI.updateState(desktopPoolDTO.getId(), CbbDesktopPoolState.AVAILABLE);
        } catch (Exception e) {
            LOGGER.error(String.format("更新桌面池[%s]信息失败", desktopPoolDTO.getId()), e);
        }
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_DESKTOP_BATCH_CREATE_TASK_RESULT);
    }

    private synchronized String buildDesktopName(String deskNamePrefix) throws BusinessException {
        String name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        while (cbbDeskMgmtAPI.checkDesktopNameExist(name)) {
            LOGGER.warn("已存在云桌面名称[{}]，重新生成", name);
            name = String.format(NAME_FORMAT, deskNamePrefix, startIndex.incrementAndGet());
        }
        return name;
    }

    private CreateDesktopResponse createPoolDesktop(UUID deskId, String desktopName,
                                                    DesktopPoolBasicDTO desktopPoolDTO, BatchTaskItem batchTaskItem) throws BusinessException {
        CreatePoolDesktopRequest request = new CreatePoolDesktopRequest();
        request.setDesktopId(deskId);
        request.setDesktopName(desktopName);
        request.setPoolId(desktopPoolDTO.getId());
        request.setPoolName(desktopPoolDTO.getName());
        request.setImageTemplateId(desktopPoolDTO.getImageTemplateId());
        request.setStrategyId(desktopPoolDTO.getStrategyId());
        request.setNetworkId(desktopPoolDTO.getNetworkId());
        request.setPoolDeskType(DesktopPoolType.convertToPoolDeskType(desktopPoolDTO.getPoolModel()));
        if (Objects.nonNull(desktopPoolConfigDTO)) {
            request.setSoftwareStrategyId(desktopPoolConfigDTO.getSoftwareStrategyId());
            request.setUserProfileStrategyId(desktopPoolConfigDTO.getUserProfileStrategyId());
        }
        request.setClusterId(desktopPoolDTO.getClusterId());
        request.setPlatformId(desktopPoolDTO.getPlatformId());
        request.setDeskSpec(cbbDeskSpecAPI.getById(desktopPoolDTO.getDeskSpecId()));
        request.setItem(batchTaskItem);
        return desktopPoolMgmtAPI.createDesktop(request);
    }
}
