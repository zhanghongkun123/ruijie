package com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.base.alarm.module.def.api.BaseAlarmAPI;
import com.ruijie.rcos.base.alarm.module.def.api.request.ListAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.request.ReleaseAlarmRequest;
import com.ruijie.rcos.base.alarm.module.def.api.response.QueryAlarmResponse;
import com.ruijie.rcos.base.alarm.module.def.dto.AlarmDTO;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmLevel;
import com.ruijie.rcos.base.alarm.module.def.enums.AlarmStatus;
import com.ruijie.rcos.rcdc.rco.module.def.api.AuditApplyMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseListAlarmPageWebRequest;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.alarm.request.BaseReleaseAlarmWebRequest;
import com.ruijie.rcos.sk.base.I18nKey;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskBuilder;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskSubmitResult;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultPageResponse;
import com.ruijie.rcos.sk.webmvc.api.response.DefaultWebResponse;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2018年12月17日
 *
 * @author xgx
 */
@Controller
@RequestMapping("rco/alarm")
public class AlarmCtrl {
    private static final Logger LOGGER = LoggerFactory.getLogger(AlarmCtrl.class);

    @Autowired
    private BaseAlarmAPI alarmAPI;
    
    @Autowired
    private AuditApplyMgmtAPI auditApplyMgmtAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    private static final String TRUE = "true";

    private static final String FALSE = "false";

    /**
     * 获取告警列表
     * 
     * @param pageWebRequest 告警列表请求
     * @throws BusinessException 业务异常
     * @return 告警列表
     */
    @RequestMapping(value = "/list")
    public DefaultWebResponse list(BaseListAlarmPageWebRequest pageWebRequest) throws BusinessException {
        Assert.notNull(pageWebRequest, "请求对象不能为空");

        Map<String, String[]> argsMap = pageWebRequest.toExactMatchMap();
        Boolean enableQueryHistory = resolveQueryHistory(argsMap);

        AlarmLevel[] alarmLevelArr = getAlarmLevelArr(argsMap);
        AlarmStatus[] alarmStatusArr = getAlarmStatusArr(argsMap);
        ListAlarmRequest listAlarmRequest = new ListAlarmRequest();
        BeanUtils.copyProperties(pageWebRequest, listAlarmRequest);
        listAlarmRequest.setEnableQueryHistory(enableQueryHistory);
        listAlarmRequest.setAlarmLevelArr(alarmLevelArr);
        listAlarmRequest.setAlarmStatusArr(alarmStatusArr);
        listAlarmRequest.setSort(pageWebRequest.getSort());

        // 通过告警关联的业务Id进行过滤查询
        resolveBusinessIdArr(listAlarmRequest, argsMap);

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警】转换API请求参数结束，参数：{}", JSON.toJSONString(listAlarmRequest));
        }

        DefaultPageResponse<AlarmDTO> defaultPageResponse = alarmAPI.listAlarmList(listAlarmRequest);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("【告警】获取告警列表成功，响应：{}", JSON.toJSONString(defaultPageResponse));
        }

        return DefaultWebResponse.Builder.success(defaultPageResponse);
    }

    private void resolveBusinessIdArr(ListAlarmRequest listAlarmRequest, Map<String, String[]> argsMap) {
        String[] valueArr = argsMap.get("businessIdArr");
        if (!ObjectUtils.isEmpty(valueArr)) {
            listAlarmRequest.setBusinessIdArr(valueArr);
        }
    }

    /**
     * 解除告警
     * 
     * @param baseReleaseAlarmWebRequest 解除告警请求对象
     * @param batchTaskBuilder 批量任务建造者
     * @return 响应
     * @throws BusinessException 业务异常
     */
    @RequestMapping(value = "/release")
    public DefaultWebResponse releaseAlarm(BaseReleaseAlarmWebRequest baseReleaseAlarmWebRequest,
                                           BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        Assert.notNull(baseReleaseAlarmWebRequest, "请求对象不能为空");
        Assert.notNull(batchTaskBuilder, "批量任务建造者不能为空");

        LOGGER.debug("【告警】解除告警开始");
        boolean isBatch = baseReleaseAlarmWebRequest.getIdArr().length > 1;
        if (isBatch) {
            return batchReleaseAlarm(baseReleaseAlarmWebRequest, batchTaskBuilder);
        } else {
            // 单条告警解除不适用异步任务
            return releaseOneAlarm(baseReleaseAlarmWebRequest);
        }
    }

    private DefaultWebResponse batchReleaseAlarm(BaseReleaseAlarmWebRequest baseReleaseAlarmWebRequest,
                                                 BatchTaskBuilder batchTaskBuilder) throws BusinessException {
        String itemName = LocaleI18nResolver.resolve(AlarmBusinessKey.BASE_ALARM_BATCH_RELEASE_ALARM_TASK_ITEM_NAME);
        List<BatchTaskItem> batchTaskItemList = Stream.of(baseReleaseAlarmWebRequest.getIdArr()) //
                .map(id -> DefaultBatchTaskItem.builder() //
                        .itemId(id) //
                        .itemName(itemName) //
                        .build()) //
                .collect(Collectors.toList());

        BatchDeleteTask batchDeleteTask = new BatchDeleteTask(batchTaskItemList, auditLogAPI);

        BatchTaskSubmitResult batchTaskSubmitResult = batchTaskBuilder //
                .setTaskName(AlarmBusinessKey.BASE_ALARM_BATCH_RELEASE_ALARM_TASK_TITLE) //
                .setTaskDesc(AlarmBusinessKey.BASE_ALARM_BATCH_RELEASE_ALARM_TASK_DESC) //
                .registerHandler(batchDeleteTask) //
                .start();

        return DefaultWebResponse.Builder.success(batchTaskSubmitResult);
    }

    private DefaultWebResponse releaseOneAlarm(BaseReleaseAlarmWebRequest baseReleaseAlarmWebRequest) throws BusinessException {
        UUID id = baseReleaseAlarmWebRequest.getIdArr()[0];
        QueryAlarmResponse queryAlarmResponse = null;
        try {
            ReleaseAlarmRequest releaseAlarmRequest = new ReleaseAlarmRequest(id, AlarmStatus.MANUAL_RELEASED);
            queryAlarmResponse = alarmAPI.queryAlarm(id);
            alarmAPI.releaseAlarm(releaseAlarmRequest);
            auditApplyMgmtAPI.applyRelease(id, queryAlarmResponse.getBusinessId());
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_SUCCESS, queryAlarmResponse.getAlarmContent());
            return DefaultWebResponse.Builder.success(AlarmBusinessKey.BASE_ALARM_OPERATOR_SUCCEED, StringUtils.EMPTY);
        } catch (BusinessException e) {
            String alarmContent = queryAlarmResponse == null ? id.toString() : queryAlarmResponse.getAlarmContent();
            auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_FAIL, alarmContent, e.getI18nMessage());
            throw e;
        }
    }


    private Boolean resolveQueryHistory(Map<String, String[]> argsMap) throws BusinessException {
        String[] valueArr = argsMap.get("enableQueryHistory");
        if (ObjectUtils.isEmpty(valueArr)) {
            throw new BusinessException(I18nKey.SK_VALIDATION_NOTEMPTY, "enableQueryHistory");
        }
        String value = valueArr[0];
        if (!TRUE.equals(value) && !FALSE.equals(value)) {
            throw new BusinessException(AlarmBusinessKey.BASE_ALARM_ARG_VALUE_ILLEGAL, value);
        }
        return Boolean.valueOf(value);
    }

    private AlarmLevel[] getAlarmLevelArr(Map<String, String[]> argsMap) {
        String[] alarmLevelArr = argsMap.get("alarmLevel");
        if (ObjectUtils.isEmpty(alarmLevelArr)) {
            return new AlarmLevel[0];
        }
        return Arrays.stream(alarmLevelArr).map(AlarmLevel::valueOf).toArray(AlarmLevel[]::new);
    }

    private AlarmStatus[] getAlarmStatusArr(Map<String, String[]> argsMap) {
        String[] alarmStatusArr = argsMap.get("alarmStatus");
        if (ObjectUtils.isEmpty(alarmStatusArr)) {
            return new AlarmStatus[0];
        }
        return Arrays.stream(alarmStatusArr).map(AlarmStatus::valueOf).toArray(AlarmStatus[]::new);
    }

    /**
     * 批量删除任务类
     */
    class BatchDeleteTask extends AbstractBatchTaskHandler {

        private BaseAuditLogAPI auditLogAPI;

        BatchDeleteTask(Collection<BatchTaskItem> batchTaskItemList, BaseAuditLogAPI auditLogAPI) {
            super(batchTaskItemList);
            this.auditLogAPI = auditLogAPI;
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
            ReleaseAlarmRequest releaseAlarmRequest = new ReleaseAlarmRequest(item.getItemID(), AlarmStatus.MANUAL_RELEASED);
            QueryAlarmResponse queryAlarmResponse = null;
            try {
                queryAlarmResponse = alarmAPI.queryAlarm(item.getItemID());
                alarmAPI.releaseAlarm(releaseAlarmRequest);
                LOGGER.debug("解除告警成功：{}", item.getItemID());
                auditApplyMgmtAPI.applyRelease(item.getItemID(), queryAlarmResponse.getBusinessId());
                auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_SUCCESS, queryAlarmResponse.getAlarmContent());
                return DefaultBatchTaskItemResult.success(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_SUCCESS, queryAlarmResponse.getAlarmContent());
            } catch (BusinessException ex) {
                LOGGER.error("解除告警失败：[" + item.getItemID() + "]", ex);
                String alarmContent = queryAlarmResponse == null ? item.getItemID().toString() : queryAlarmResponse.getAlarmContent();
                auditLogAPI.recordLog(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_FAIL, alarmContent, ex.getI18nMessage());
                return DefaultBatchTaskItemResult.fail(AlarmBusinessKey.BASE_ALARM_RELEASE_ALARM_FAIL, alarmContent,
                        ex.getI18nMessage());
            }
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            return buildDefaultFinishResult(successCount, failCount, AlarmBusinessKey.BASE_ALARM_BATCH_RELEASE_ALARM_TASK_RESULT);
        }
    }
}
