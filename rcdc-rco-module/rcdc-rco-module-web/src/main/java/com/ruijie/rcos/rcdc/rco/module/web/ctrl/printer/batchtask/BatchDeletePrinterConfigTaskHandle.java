package com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer.batchtask;

import com.ruijie.rcos.gss.log.module.def.api.BaseAuditLogAPI;
import com.ruijie.rcos.rcdc.rco.module.def.api.PrinterManageServiceAPI;
import com.ruijie.rcos.rcdc.rco.module.def.printer.dto.PrinterConfigDTO;
import com.ruijie.rcos.rcdc.rco.module.web.ctrl.printer.PrinterBusinessKey;
import com.ruijie.rcos.sk.base.batch.*;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/9/17
 *
 * @author chenjiehui
 */
public class BatchDeletePrinterConfigTaskHandle extends AbstractBatchTaskHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(BatchDeletePrinterConfigTaskHandle.class);

    private final BaseAuditLogAPI auditLogAPI;

    private PrinterManageServiceAPI printerManageServiceAPI;

    private boolean isBatch = true;

    private String printerConfigName;

    public boolean isBatch() {
        return isBatch;
    }

    public void setBatch(boolean batch) {
        isBatch = batch;
    }


    public String getPrinterConfigName() {
        return printerConfigName;
    }

    public void setPrinterConfigName(String printerConfigName) {
        this.printerConfigName = printerConfigName;
    }

    public BatchDeletePrinterConfigTaskHandle(Iterator<? extends BatchTaskItem> iterator,
                                              BaseAuditLogAPI auditLogAPI, PrinterManageServiceAPI printerManageServiceAPI) {
        super(iterator);
        Assert.notNull(auditLogAPI, "auditLogAPI can not be null");
        Assert.notNull(printerManageServiceAPI, "scheduleTaskAPI can not be null");
        this.auditLogAPI = auditLogAPI;
        this.printerManageServiceAPI = printerManageServiceAPI;
    }

    public void setIsBatch(boolean isBatch) {
        this.isBatch = isBatch;
    }

    @Override
    public BatchTaskItemResult processItem(BatchTaskItem item) throws BusinessException {
        Assert.notNull(item, "item can not be null");
        UUID id = item.getItemID();
        PrinterConfigDTO printerConfigDTO = printerManageServiceAPI.getPrinterConfigById(id);
        printerManageServiceAPI.deletePrinterConfigById(id);
        auditLogAPI.recordLog(PrinterBusinessKey.BASE_RCO_PRINTER_DELETE_PRINGER_CONFIG_SUCCESS, printerConfigDTO.getConfigName());
        return DefaultBatchTaskItemResult.builder().batchTaskItemStatus(BatchTaskItemStatus.SUCCESS)
                .msgKey(PrinterBusinessKey.BASE_RCO_PRINTER_DELETE_PRINGER_CONFIG_SUCCESS).msgArgs(printerConfigDTO.getConfigName()).build();

    }

    @Override
    public BatchTaskFinishResult onFinish(int successCount, int failCount) {
        // 删除单条
        if (successCount == 1) {
            return DefaultBatchTaskFinishResult.builder()
                    .msgKey(PrinterBusinessKey.BASE_RCO_PRINTER_DELETE_PRINGER_CONFIG_SUCCESS).msgArgs(new String[] {printerConfigName})
                    .batchTaskStatus(BatchTaskStatus.SUCCESS).build();
        }

        return buildDefaultFinishResult(successCount, failCount, PrinterBusinessKey.BASE_RCO_BATCH_DELETE_PRINTER_CONFIG_RESULT);

    }

}
