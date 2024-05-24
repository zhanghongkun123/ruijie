package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 * Description: 设置开机模式
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月15日
 *
 * @author xwx
 */
public class ConfigStartModeBatchTaskItem implements BatchTaskItem {

    private UUID itemID;

    private String itermName;

    private String terminalId;


    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Override
    public UUID getItemID() {
        return itemID;
    }

    @Override
    public String getItemName() {
        return itermName;
    }

    public void setItemID(UUID itemID) {
        this.itemID = itemID;
    }

    public void setItermName(String itermName) {
        this.itermName = itermName;
    }
}
