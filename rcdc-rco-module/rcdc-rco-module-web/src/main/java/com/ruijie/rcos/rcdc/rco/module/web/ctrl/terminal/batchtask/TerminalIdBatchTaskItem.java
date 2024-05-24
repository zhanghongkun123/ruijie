package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import java.util.UUID;

import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

/**
 * 
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月28日
 * 
 * @author nt
 */
public class TerminalIdBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private String terminalId;

    public TerminalIdBatchTaskItem(UUID itemId, String itemName, String terminalId) {
        Assert.notNull(itemId, "itemId can not be null");
        Assert.hasText(itemName, "itemName can not be null");
        Assert.hasText(terminalId, "terminalId can not be null");

        this.itemId = itemId;
        this.itemName = itemName;
        this.terminalId = terminalId;
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public String getTerminalId() {
        return terminalId;
    }

}
