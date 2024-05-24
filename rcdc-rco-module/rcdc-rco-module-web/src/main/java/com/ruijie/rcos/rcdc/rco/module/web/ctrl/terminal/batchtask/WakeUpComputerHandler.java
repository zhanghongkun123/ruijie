package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.ComputerBusinessAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.request.ComputerIdRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.response.ComputerInfoResponse;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.ComputerBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

/**
 * Description: 唤醒PC终端批处理
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/1/20
 *
 * @author zqj
 */
public class WakeUpComputerHandler extends AbstractBatchTaskHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(EditFullSystemDiskHandler.class);

    private BaseAuditLogAPI auditLogAPI;

    private ComputerBusinessAPI computerBusinessAPI;

    private Map<UUID, String> idMap;


    public WakeUpComputerHandler(Map<UUID, String> idMap, Iterator<DefaultBatchTaskItem> iterator) {
        super(iterator);
        this.idMap = idMap;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem batchTaskItem) throws BusinessException {
        Assert.notNull(batchTaskItem, "batchTaskItem must not be null");

        String id = idMap.get(batchTaskItem.getItemID());
        String logFlag = "";

        try {
            ComputerIdRequest computerIdRequest = new ComputerIdRequest();
            computerIdRequest.setComputerId(UUID.fromString(id));
            ComputerInfoResponse computerInfoResponse = computerBusinessAPI.getComputerInfoByComputerId(computerIdRequest);
            //获取终端操作LOG 用于打印日志
            logFlag = StringUtils.hasText(computerInfoResponse.getMac()) ? computerInfoResponse.getMac() : computerInfoResponse.getIp();
            computerBusinessAPI.wakeUpComputer(computerInfoResponse.getId(), Boolean.FALSE);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_SUCCESS, logFlag);
            return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                    .msgKey(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_SUCCESS).msgArgs(logFlag).build();
        } catch (BusinessException e) {
            LOGGER.error("唤醒终端[{}]失败，失败原因：", logFlag, e);
            auditLogAPI.recordLog(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_FAIL, logFlag, e.getI18nMessage());
            throw new BusinessException(ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_FAIL, e, logFlag, e.getI18nMessage());
        }
    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        return buildDefaultFinishResult(successCount, failCount, ComputerBusinessKey.RCDC_COMPUTER_WAKE_UP_RESULT);
    }


    public void setAuditLogAPI(BaseAuditLogAPI auditLogAPI) {
        this.auditLogAPI = auditLogAPI;
    }

    public BaseAuditLogAPI getAuditLogAPI() {
        return auditLogAPI;
    }

    public ComputerBusinessAPI getComputerBusinessAPI() {
        return computerBusinessAPI;
    }

    public void setComputerBusinessAPI(ComputerBusinessAPI computerBusinessAPI) {
        this.computerBusinessAPI = computerBusinessAPI;
    }

    public Map<UUID, String> getIdMap() {
        return idMap;
    }

    public void setIdMap(Map<UUID, String> idMap) {
        this.idMap = idMap;
    }
}
