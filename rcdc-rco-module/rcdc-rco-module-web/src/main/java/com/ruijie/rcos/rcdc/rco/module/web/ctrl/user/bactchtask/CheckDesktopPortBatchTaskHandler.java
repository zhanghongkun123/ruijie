package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbVDIDeskMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskInfoDTO;
import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.api.DesktopAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.desk.CheckDesktopPortResultDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.clouddesktop.batchtask.BatchTaskUtils;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.base.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Description: 检测端口批任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd
 * Create Time: 2023/8/7
 *
 * @author chenjuan
 */
public class CheckDesktopPortBatchTaskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckDesktopPortBatchTaskHandler.class);

    private CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI;

    private DesktopAPI desktopAPI;

    private BaseAuditLogAPI auditLogAPI;

    private List<Integer> validPortList;

    public CheckDesktopPortBatchTaskHandler(Iterator<? extends BatchTaskItem> batchTaskItemIterator) {
        super(batchTaskItemIterator);
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");

        UUID desktopId = batchTaskItem.getItemID();
        CbbDeskInfoDTO cbbDeskInfoDTO = null;
        try {
            // 获取云桌面信息
            cbbDeskInfoDTO = cbbVDIDeskMgmtAPI.getByDeskId(desktopId);
        } catch (BusinessException e) {
            LOGGER.error("获取云桌面[{}]信息异常", desktopId, e);
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_FAIL, e, String.valueOf(desktopId), e.getI18nMessage());
            throw new BusinessException(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_FAIL, e, String.valueOf(desktopId), e.getI18nMessage());
        }

        // 云桌面IP为空时，提示错误信息
        if (StringUtils.isEmpty(cbbDeskInfoDTO.getDeskIp())) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_FAIL_LOG, cbbDeskInfoDTO.getName(), String.valueOf(validPortList));
            throw new BusinessException(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_IP_NULL_FAIL_LOG, cbbDeskInfoDTO.getName(),
                    String.valueOf(validPortList));
        }

        // 检测端口,errorPortList不为空时，表示有端口检测异常，提示检测失败，同时记录哪些端口成功，哪些端口异常
        CheckDesktopPortResultDTO checkDesktopPortResultDTO = desktopAPI.checkDesktopPort(cbbDeskInfoDTO.getDeskIp(), validPortList);
        // 端口全部检测失败
        if (CollectionUtils.isNotEmpty(checkDesktopPortResultDTO.getErrorPortList()) &&
                CollectionUtils.isEmpty(checkDesktopPortResultDTO.getSuccessPortList())) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_FAIL_LOG,
                    cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getErrorPortList()));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_FAIL_LOG)
                    .msgArgs(cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getErrorPortList())).build();
        }
        // 部分成功部分失败
        if (CollectionUtils.isNotEmpty(checkDesktopPortResultDTO.getErrorPortList()) &&
                CollectionUtils.isNotEmpty(checkDesktopPortResultDTO.getSuccessPortList())) {
            auditLogAPI.recordLog(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_SUCCESS_AND_FAIL_LOG,
                    cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getSuccessPortList()),
                    String.valueOf(checkDesktopPortResultDTO.getErrorPortList()));
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_SUCCESS_AND_FAIL_LOG)
                    .msgArgs(cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getSuccessPortList()),
                            String.valueOf(checkDesktopPortResultDTO.getErrorPortList())).build();
        }

        auditLogAPI.recordLog(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_SUCCESS_LOG,
                cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getSuccessPortList()));
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_SUCCESS_LOG)
                .msgArgs(cbbDeskInfoDTO.getName(), String.valueOf(checkDesktopPortResultDTO.getSuccessPortList())).build();
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return BatchTaskUtils.buildBatchTaskFinishResult(successCount, failCount,
                BusinessKey.RCDC_RCO_CHECK_DESKTOP_PORT_BATCH_TASK_RESULT);
    }

    public void setCbbVDIDeskMgmtAPI(CbbVDIDeskMgmtAPI cbbVDIDeskMgmtAPI) {
        this.cbbVDIDeskMgmtAPI = cbbVDIDeskMgmtAPI;
    }

    public void setDesktopAPI(DesktopAPI desktopAPI) {
        this.desktopAPI = desktopAPI;
    }

    public void setValidPortList(List<Integer> validPortList) {
        this.validPortList = validPortList;
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }
}
