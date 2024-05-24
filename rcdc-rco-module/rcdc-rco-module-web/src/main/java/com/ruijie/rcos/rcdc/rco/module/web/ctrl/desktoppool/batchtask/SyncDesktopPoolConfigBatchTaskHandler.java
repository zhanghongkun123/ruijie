package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopPoolMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.desktoppool.dto.SyncConfigResultDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.DesktopPoolBusinessKey;
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
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;

/**
 * Description: 应用池的相关策略到vdi云桌面批处理handler
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/10/28
 *
 * @author linke
 */
public class SyncDesktopPoolConfigBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDesktopPoolConfigBatchTaskHandler.class);

    private static final String STRATEGY = "strategy";

    private static final String DESK_SPEC = "deskSpec";

    private static final String IMAGE_TEMPLATE = "imageTemplate";

    private static final String NETWORK = "network";

    private static final String SOFTWARE = "software";

    private static final String UPM = "upm";

    /** 处理的记录数累计值 */
    private AtomicInteger processItemCount = new AtomicInteger(0);

    private BaseAuditLogAPI auditLogAPI;

    private DesktopPoolMgmtAPI desktopPoolMgmtAPI;

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private String desktopName;

    private String desktopPoolName;

    public SyncDesktopPoolConfigBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator) {
        super(iterator);
        this.auditLogAPI = SpringBeanHelper.getBean(BaseAuditLogAPI.class);
        this.desktopPoolMgmtAPI = SpringBeanHelper.getBean(DesktopPoolMgmtAPI.class);
        this.cbbVDIDeskMgmtAPI = SpringBeanHelper.getBean(CbbVDIDeskMgmtAPI.class);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
        Assert.notNull(taskItem, "taskItem can not be null");

        SyncConfigBatchTaskItem syncConfigTaskItem = (SyncConfigBatchTaskItem) taskItem;
        CbbDesktopPoolDTO desktopPool = syncConfigTaskItem.getDesktopPool();
        CbbDeskDTO desktopInfo = cbbVDIDeskMgmtAPI.getDeskVDI(syncConfigTaskItem.getItemID());
        if (Objects.isNull(desktopPool) || Objects.isNull(desktopInfo)) {
            processItemCount.incrementAndGet();
            LOGGER.error("应用策略失败信息不完整，syncConfigTaskItem: {}, pool：{}， desktopInfo：{}", JSON.toJSONString(syncConfigTaskItem),
                    JSON.toJSONString(desktopPool), JSON.toJSONString(desktopInfo));
            throw new BusinessException(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_ITEM_FAIL_INFO_EMPTY);
        }

        desktopPoolName = desktopPool.getName();
        String tmpDesktopName = desktopInfo.getName();

        Map<String, SyncConfigResultDTO> resultMap = new HashMap<>();
        try {
            // 应用策略
            resultMap.put(STRATEGY, desktopPoolMgmtAPI.syncStrategy(desktopPool, desktopInfo));

            // 应用规格
            resultMap.put(DESK_SPEC, desktopPoolMgmtAPI.syncDeskSpec(desktopPool, desktopInfo));

            // 应用镜像模板
            resultMap.put(IMAGE_TEMPLATE, desktopPoolMgmtAPI.syncImageTemplate(desktopPool, desktopInfo, taskItem));

            // 应用网络策略
            resultMap.put(NETWORK, desktopPoolMgmtAPI.syncNetworkStrategy(desktopPool, desktopInfo));

            // 软件管控策略变更
            resultMap.put(SOFTWARE, desktopPoolMgmtAPI.syncSoftwareStrategy(desktopPool, desktopInfo));

            // UPM策略变更
            resultMap.put(UPM, desktopPoolMgmtAPI.syncUserProfileStrategy(desktopPool, desktopInfo));

            if (!resultMap.isEmpty() && isAnyError(resultMap)) {
                String errorMsg = buildResultMsg(resultMap, tmpDesktopName);
                LOGGER.error(errorMsg);
                auditLogAPI.recordI18nLog(errorMsg);
                return DefaultBatchTaskItemResult.failWithI18nMessage(errorMsg);
            }

            auditLogAPI.recordLog(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_ITEM_SUC_DESC, desktopPoolName, tmpDesktopName);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_ITEM_SUC_DESC)
                    .msgArgs(new String[]{desktopPoolName, tmpDesktopName}).build();
        } catch (Exception e) {
            String errorMsg = buildResultMsg(resultMap, tmpDesktopName);
            LOGGER.error(errorMsg, e);
            auditLogAPI.recordI18nLog(errorMsg);
            return DefaultBatchTaskItemResult.failWithI18nMessage(errorMsg);
        } finally {
            desktopName = tmpDesktopName;
            processItemCount.incrementAndGet();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int sucCount, int failCount) {
        if (processItemCount.get() == 1) {
            if (sucCount == 1) {
                return DefaultBatchTaskFinishResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SINGLE_RESULT_SUC)
                        .msgArgs(new String[]{desktopPoolName, desktopName}).batchTaskStatus(BatchTaskStatus.SUCCESS).build();
            }
            return DefaultBatchTaskFinishResult.builder().msgKey(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_SINGLE_RESULT_FAIL)
                    .msgArgs(new String[]{desktopPoolName, desktopName}).batchTaskStatus(BatchTaskStatus.FAILURE).build();
        }
        return buildDefaultFinishResult(sucCount, failCount, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_BATCH_RESULT);
    }

    private boolean isAnyError(Map<String, SyncConfigResultDTO> resultMap) {
        SyncConfigResultDTO resultDTO;
        for (Map.Entry<String, SyncConfigResultDTO> entry : resultMap.entrySet()) {
            resultDTO = entry.getValue();
            if (!resultDTO.getIsSuccess()) {
                return true;
            }
        }
        return false;
    }

    private String buildResultMsg(Map<String, SyncConfigResultDTO> resultMap, String tmpDesktopName) {
        StringBuilder msg = new StringBuilder(LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_DEFAULT,
                desktopPoolName, tmpDesktopName));
        SyncConfigResultDTO configResult = resultMap.get(STRATEGY);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_STRATEGY, msg);
        configResult = resultMap.get(DESK_SPEC);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_DESK_SPEC, msg);
        configResult = resultMap.get(IMAGE_TEMPLATE);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_IMAGE, msg);
        configResult = resultMap.get(NETWORK);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_NETWORK, msg);
        configResult = resultMap.get(SOFTWARE);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_SOFTWARE, msg);
        configResult = resultMap.get(UPM);
        appendMsg(configResult, DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_UPM, msg);
        return msg.toString();
    }

    private void appendMsg(SyncConfigResultDTO configResult, String key, StringBuilder msg) {
        if (Objects.isNull(configResult)) {
            LOGGER.warn("桌面池[{}]批量应用策略时configResult为null", desktopPoolName);
            return;
        }
        if (configResult.getIsSuccess()) {
            // 成功
            msg.append(LocaleI18nResolver.resolve(key,
                    LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_SUC)));
            return;
        }
        // 失败
        String errMsg = StringUtils.isEmpty(configResult.getMessage()) ? "" : configResult.getMessage();
        errMsg = LocaleI18nResolver.resolve(DesktopPoolBusinessKey.RCDC_RCO_DESKTOP_POOL_SYNC_CONFIG_RESULT_FAIL, errMsg);
        msg.append(LocaleI18nResolver.resolve(key, errMsg));
    }
}
