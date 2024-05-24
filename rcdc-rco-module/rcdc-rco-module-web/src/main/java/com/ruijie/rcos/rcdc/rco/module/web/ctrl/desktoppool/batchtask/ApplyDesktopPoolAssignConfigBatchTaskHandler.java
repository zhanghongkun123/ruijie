package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.SyncConfigResultDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Objects;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/4/10
 *
 * @author TD
 */
public class ApplyDesktopPoolAssignConfigBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplyDesktopPoolAssignConfigBatchTaskHandler.class);

    private final DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private final CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private final String desktopName;
    
    private final String desktopPoolName;
    
    public ApplyDesktopPoolAssignConfigBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, String desktopPoolName, String desktopName) {
        super(iterator);
        this.desktopPoolMgmtAPI = SpringBeanHelper.getBean(DesktopPoolMgmtAPI.class);
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
        this.desktopPoolName = desktopPoolName;
        this.desktopName = desktopName;
    }
    
    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can be not null");
        SyncConfigBatchTaskItem syncConfigTaskItem = (SyncConfigBatchTaskItem) batchTaskItem;
        CbbDesktopPoolDTO desktopPool = syncConfigTaskItem.getDesktopPool();
        CbbDeskDTO desktopInfo = cbbVDIDeskMgmtAPI.getDeskVDI(syncConfigTaskItem.getItemID());
        if (Objects.isNull(desktopPool) || Objects.isNull(desktopInfo)) {
            LOGGER.error("应用镜像模板失败信息不完整，syncConfigTaskItem: {}, pool：{}， desktopInfo：{}", JSON.toJSONString(syncConfigTaskItem),
                    JSON.toJSONString(desktopPool), JSON.toJSONString(desktopInfo));
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_ITEM_FAIL_INFO_EMPTY);
        }
        
        SyncConfigResultDTO syncConfigResultDTO = desktopPoolMgmtAPI.syncImageTemplate(desktopPool, desktopInfo, batchTaskItem);
        if (!syncConfigResultDTO.getIsSuccess()) {
            return DefaultBatchTaskItemResult.failWithI18nMessage(syncConfigResultDTO.getMessage());
        }
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_SUC_DESC)
                .msgArgs(desktopPool.getName(), desktopInfo.getName()).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (Objects.nonNull(desktopName)) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder()
                        .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_SINGLE_RESULT_SUC)
                        .msgArgs(new String[]{desktopPoolName, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder()
                    .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[]{desktopPoolName, desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_STATIC_POOL_APPLY_IMAGE_TEMPLATE_BATCH_RESULT);
    }
}
