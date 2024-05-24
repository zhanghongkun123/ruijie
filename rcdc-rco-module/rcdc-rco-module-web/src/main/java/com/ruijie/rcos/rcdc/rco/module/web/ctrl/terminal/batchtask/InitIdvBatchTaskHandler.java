package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.TerminalDTO;
import com.ruijie.rcos.rcdc.rco.module.def.terminaloperate.exception.TerminalOperateSuccessBusinessException;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.rco.module.web.service.AppCenterHelper;
import com.ruijie.rcos.rcdc.rco.module.web.service.SpringBeanHelper;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * Description: 批量初始化IDV处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020-09-08
 *
 * @author zhiweiHong
 */
public class InitIdvBatchTaskHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(InitIdvBatchTaskHandler.class);

    private final Map<UUID, String> idMap;

    private final UserTerminalMgmtAPI userTerminalMgmtAPI;

    private BaseAuditLogAPI auditLogAPI;

    private Boolean retainImage;

    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private AppCenterHelper appCenterHelper;


    public InitIdvBatchTaskHandler(Iterator<? extends BatchTaskItem> iterator, Map<UUID, String> idMap, UserTerminalMgmtAPI userTerminalMgmtAPI,
                                   BaseAuditLogAPI auditLogAPI, Boolean retainImage) {
        super(iterator);
        Assert.notNull(idMap, "idMap can not be null");
        Assert.notNull(userTerminalMgmtAPI, "userTerminalMgmtAPI can not be null");
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");

        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
        this.idMap = idMap;
        this.auditLogAPI = auditLogAPI;
        this.retainImage = retainImage;
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
        this.appCenterHelper = SpringBeanHelper.getBean(AppCenterHelper.class);
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem can not be null");
        String terminalId = idMap.get(batchTaskItem.getItemID());

        String terminalAddr = terminalId;
        try {
            CbbTerminalBasicInfoDTO terminalBasicInfoDTO = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            terminalAddr = terminalBasicInfoDTO.getUpperMacAddrOrTerminalId();
        } catch (BusinessException e) {
            LOGGER.debug("查询终端信息发生异常，终端id: [{}]", terminalId);
        }

        final TerminalDTO terminalDTO = userTerminalMgmtAPI.findByTerminalId(terminalId);
        if (terminalDTO.getBindDeskId() != null) {
            appCenterHelper.checkTestingDesk(terminalDTO.getBindDeskId());
        }
        try {
            userTerminalMgmtAPI.initialize(terminalId, retainImage);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_SUCCESS, terminalAddr);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_SUCCESS).msgArgs(new String[] {terminalAddr}).build();
        } catch (TerminalOperateSuccessBusinessException se) {
            LOGGER.warn("发送终端初始化命令给终端[{}]成功，温馨提示：[{}]", terminalId, se.getI18nMessage());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_SUCCESS_HAS_WARN, terminalAddr, se.getI18nMessage());
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_SUCCESS_HAS_WARN)
                    .msgArgs(new String[] {terminalAddr, se.getI18nMessage()}).build();
        } catch (BusinessException e) {
            LOGGER.error("发送终端初始化命令给终端[{}]失败，失败原因：[{}]", terminalId, e.getI18nMessage());
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_FAIL, terminalAddr, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_FAIL, e, terminalAddr, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_INIT_TERMINAL_RESULT);
    }
}
