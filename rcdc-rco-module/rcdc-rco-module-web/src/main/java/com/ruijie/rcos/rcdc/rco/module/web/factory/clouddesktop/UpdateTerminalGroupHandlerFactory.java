package com.ruijie.rcos.rcdc.rco.module.web.factory.clouddesktop;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.WifiWhitelistAPI;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.common.utils.TerminalIdMappingUtils;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.UserBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbModifyTerminalDTO;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.AbstractBatchTaskHandler;
import com.ruijie.rcos.sk.base.batch.BatchTaskFinishResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItem;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemResult;
import com.ruijie.rcos.sk.base.batch.BatchTaskItemStatus;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItemResult;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年5月26日
 * 
 * @author yinfeng
 */
@Service
public class UpdateTerminalGroupHandlerFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateTerminalGroupHandlerFactory.class);

    @Autowired
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    @Autowired
    private WifiWhitelistAPI wifiWhitelistAPI;

    @Autowired
    private BaseAuditLogAPI auditLogAPI;

    /**
     * 创建 handler
     * 
     * @param terminalIdArr 终端 id 数组
     * @param terminalGroupId 组 id
     * @return handler
     */
    public UpdateTerminalGroupHandler createHandler(String[] terminalIdArr, UUID terminalGroupId) {
        Assert.notNull(terminalIdArr, "terminalIdArr must not be null");
        Assert.notNull(terminalGroupId, "terminalGroupId must not be null");
        terminalIdArr = Stream.of(terminalIdArr).distinct().toArray(String[]::new);

        Map<UUID, String> idMap = TerminalIdMappingUtils.mapping(terminalIdArr);
        UUID[] idArr = TerminalIdMappingUtils.extractUUID(idMap);
        final Iterator<? extends BatchTaskItem> iterator = Stream.of(idArr).distinct().map(id -> DefaultBatchTaskItem.builder().itemId(id)
                .itemName(UserBusinessKey.RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_ITEM_NAME, new String[] {}).build()).iterator();

        return new UpdateTerminalGroupHandler(iterator, idMap, terminalGroupId, auditLogAPI);
    }

    /**
     * 
     * Description: Function Description
     * Copyright: Copyright (c) 2018
     * Company: Ruijie Co., Ltd.
     * Create Time: 2020年5月26日
     * 
     * @author yinfeng
     */
    private class UpdateTerminalGroupHandler extends AbstractBatchTaskHandler {

        private Map<UUID, String> idMap;

        private BaseAuditLogAPI auditLogAPI;

        private UUID terminalGroupId;

        UpdateTerminalGroupHandler(Iterator<? extends BatchTaskItem> iterator, Map<UUID, String> idMap, UUID terminalGroupId,
                                   BaseAuditLogAPI auditLogAPI) {
            super(iterator);

            Assert.notNull(idMap, "idMap must not be null");
            Assert.notNull(terminalGroupId, "terminalGroupId must not be null");
            Assert.notNull(auditLogAPI, "auditLogAPI must not be null");

            this.terminalGroupId = terminalGroupId;
            this.auditLogAPI = auditLogAPI;
            this.idMap = idMap;
        }

        @Override
        public BatchTaskFinishResult onFinish(int successCount, int failCount) {
            return buildDefaultFinishResult(successCount, failCount, UserBusinessKey.RCDC_RCO_TERMINAL_BATCH_UPDATE_TERMINAL_GROUP_RESULT);
        }

        @Override
        public BatchTaskItemResult processItem(BatchTaskItem taskItem) throws BusinessException {
            Assert.notNull(taskItem, "taskItem can not be null");

            String terminalId = idMap.get(taskItem.getItemID());
            return updateGroupOfTerminalAddOptLog(terminalId, terminalGroupId);
        }


        private BatchTaskItemResult updateGroupOfTerminalAddOptLog(String terminalId, UUID terminalGroupId) throws BusinessException {
            String terminalIdentification = terminalId;
            try {
                CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
                terminalIdentification = response.getUpperMacAddrOrTerminalId();
                updateTerminalInfo(terminalId, terminalGroupId, response.getTerminalName());
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_SUCCESS_LOG, terminalIdentification);

                return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                        .msgKey(UserBusinessKey.RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_RESULT_SUCCESS).msgArgs(new String[] {terminalIdentification})
                        .build();
            } catch (BusinessException e) {
                LOGGER.error("编辑终端" + terminalIdentification + "所属分组失败", e);
                auditLogAPI.recordLog(UserBusinessKey.RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_FAIL_LOG, e, terminalIdentification,
                        e.getI18nMessage());
                throw new BusinessException(UserBusinessKey.RCDC_RCO_TERMINAL_UPDATE_TERMINAL_GROUP_RESULT_FAIL, e, terminalIdentification,
                        e.getI18nMessage());
            }
        }

        private void updateTerminalInfo(String terminalId, UUID terminalGroupId, String terminalName) throws BusinessException {
            CbbModifyTerminalDTO terminalRequest = new CbbModifyTerminalDTO();
            terminalRequest.setCbbTerminalId(terminalId);
            terminalRequest.setGroupId(terminalGroupId);
            terminalRequest.setTerminalName(terminalName);
            cbbTerminalOperatorAPI.modifyTerminal(terminalRequest);
            wifiWhitelistAPI.notifyIdvIfTargetGroupHasWhitelist(terminalGroupId, terminalId);
        }
    }
}
