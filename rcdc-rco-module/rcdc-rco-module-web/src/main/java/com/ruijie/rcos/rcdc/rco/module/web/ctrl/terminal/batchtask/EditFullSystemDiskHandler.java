package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.UserTerminalMgmtAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.dto.ShineConfigFullSystemDiskDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.TerminalBusinessKey;
import com.ruijie.rcos.rcdc.terminal.module.def.api.CbbTerminalOperatorAPI;
import com.ruijie.rcos.rcdc.terminal.module.def.api.dto.CbbTerminalBasicInfoDTO;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 发送自动扩容命令批量处理
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/6/27 12:31
 *
 * @author yxq
 */
public class EditFullSystemDiskHandler extends AbstractBatchTaskHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EditFullSystemDiskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private UserTerminalMgmtAPI userTerminalMgmtAPI;

    /**
     * 终端操作API类
     */
    private CbbTerminalOperatorAPI cbbTerminalOperatorAPI;

    private Map<UUID, String> idMap;

    private ShineConfigFullSystemDiskDTO configFullSystemDiskDTO;

    public EditFullSystemDiskHandler(Map<UUID, String> idMap, Iterator<DefaultBatchTaskItem> iterator) {
        super(iterator);
        this.idMap = idMap;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem must not be null");

        String terminalId = idMap.get(batchTaskItem.getItemID());
        String terminalIdentification = terminalId;

        try {
            CbbTerminalBasicInfoDTO response = cbbTerminalOperatorAPI.findBasicInfoByTerminalId(terminalId);
            //获取终端操作LOG 用于打印日志
            terminalIdentification = response.getUpperMacAddrOrTerminalId();
            userTerminalMgmtAPI.configTerminalFullSystemDisk(terminalId, configFullSystemDiskDTO);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_SUCCESS, terminalIdentification);

            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_SUCCESS).msgArgs(terminalIdentification).build();
        } catch (BusinessException e) {
            LOGGER.error("发送开启系统盘自动扩容给终端[" + terminalIdentification + "]失败，失败原因：", e);
            auditLogAPI.recordLog(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_FAIL, terminalIdentification, e.getI18nMessage());
            throw new BusinessException(TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_FAIL, e, terminalIdentification, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, TerminalBusinessKey.RCDC_TERMINAL_EDIT_FULL_SYSTEM_DISK_RESULT);
    }

    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public void setUserTerminalMgmtAPI(UserTerminalMgmtAPI userTerminalMgmtAPI) {
        this.userTerminalMgmtAPI = userTerminalMgmtAPI;
    }

    public void setConfigFullSystemDiskDTO(ShineConfigFullSystemDiskDTO configFullSystemDiskDTO) {
        this.configFullSystemDiskDTO = configFullSystemDiskDTO;
    }

    public void setCbbTerminalOperatorAPI(CbbTerminalOperatorAPI cbbTerminalOperatorAPI) {
        this.cbbTerminalOperatorAPI = cbbTerminalOperatorAPI;
    }
}
