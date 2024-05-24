package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.batchtask;

import java.util.Iterator;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rca.module.def.api.RcaHostAPI;
import com.ruijie.rcos.rcdc.rca.module.def.dto.RcaHostDTO;
import com.ruijie.rcos.rcdc.rca.module.def.enums.RcaEnum;
import com.ruijie.rcos.rcdc.rca.module.def.response.AddOneAgentHostResponse;
import com.ruijie.rcos.rcdc.rca.module.def.RcaBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.rcaapphost.dto.CreateThirdPartRcaHostBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.i18n.LocaleI18nResolver;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 纳管三方应用主机批量任务Handler
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年12月28日
 *
 * @author liuwc
 */
public class CreateThirdPartRcaHostBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateThirdPartRcaHostBatchTaskHandler.class);

    private RcaHostAPI rcaHostAPI;

    private BaseAuditLogAPI auditLogAPI;

    private static final int DEFAULT_TIMEOUT = 3000;

    public CreateThirdPartRcaHostBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, BaseAuditLogAPI auditLogAPI) {
        super(iterator);
        this.auditLogAPI = auditLogAPI;
    }

    public void setRcaHostAPI(RcaHostAPI rcaHostAPI) {
        this.rcaHostAPI = rcaHostAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem taskItem) {
        Assert.notNull(taskItem, "taskItem is not null");
        CreateThirdPartRcaHostBatchTaskItem item = (CreateThirdPartRcaHostBatchTaskItem) taskItem;

        RcaHostDTO createAppHostDTO = item.getCreateHostAppDTO();
        String ip = createAppHostDTO.getIp();
        RcaHostDTO hostInfoByIp = rcaHostAPI.getRcaHostInfoByIp(createAppHostDTO.getIp());
        if (!ObjectUtils.isEmpty(hostInfoByIp)) {
            LOGGER.warn("添加应用主机失败, 该主机已存在, 主机IP={}", ip);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_FAIL, ip);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_FAIL).msgArgs(new String[]{ip}).build();
        }
        if (createAppHostDTO.getHostSessionType() == RcaEnum.HostSessionType.SINGLE) {
            if (StringUtils.isEmpty(createAppHostDTO.getHostAuthName())) {
                LOGGER.warn("添加应用主机失败, 单会话必须填写账号, 主机IP={}", ip);
                auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_FAIL_AUTH_NULL, ip);
                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                        .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_FAIL_AUTH_NULL).msgArgs(new String[]{ip}).build();
            }
        }

        AddOneAgentHostResponse addHostResponse;
        try {
            addHostResponse = rcaHostAPI.joinHost(createAppHostDTO);
        } catch (BusinessException e) {
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_FAIL, ip, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_FAIL).msgArgs(new String[]{ip, e.getI18nMessage()}).build();
        }
        if (ObjectUtils.isEmpty(addHostResponse)) {
            LOGGER.warn("添加应用主机失败,无法访问该主机, 主机IP={}",  ip);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_LINK_FAIL, ip);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_LINK_FAIL).msgArgs(new String[]{ip}).build();
        }
        Integer code = addHostResponse.getCode() == null ? 0 : addHostResponse.getCode();
        if (code != 0) {
            LOGGER.warn("应用主机返回报错，添加主机失败，ip:{}", createAppHostDTO.getIp());
            String errorMsg = addHostResponse.getMsg();
            if (StringUtils.isEmpty(errorMsg)) {
                // 若msg为空，说明可能是旧的OA返回，此时异常在message字段，若都为空返回[未知异常]错误
                errorMsg = StringUtils.isEmpty(addHostResponse.getMessage())
                        ? LocaleI18nResolver.resolve(RcaBusinessKey.RCDC_RCA_HOST_ADD_LINK_FAIL_UNKNOW_REASON)
                        : addHostResponse.getMessage();
            }
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_HOST_ADD_LINK_FAIL,
                    ip, errorMsg);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_HOST_ADD_LINK_FAIL)
                    .msgArgs(new String[]{ip, errorMsg}).build();
        }
        try {
            LOGGER.warn("添加应用主机成功,主机IP={}", ip);
            rcaHostAPI.createThirdPartyHost(createAppHostDTO);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_LOG, ip);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_LOG).msgArgs(new String[]{ip}).build();
        } catch (BusinessException e) {
            LOGGER.error("添加应用主机失败,主机IP={}", ip);
            auditLogAPI.recordLog(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_FAIL, ip, e.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.FAILURE)
                    .msgKey(RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_FAIL).msgArgs(new String[]{ip, e.getI18nMessage()}).build();
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, RcaBusinessKey.RCDC_RCA_TRUSTEESHIP_APP_VM_ADD_TASK_RESULT);
    }
}
