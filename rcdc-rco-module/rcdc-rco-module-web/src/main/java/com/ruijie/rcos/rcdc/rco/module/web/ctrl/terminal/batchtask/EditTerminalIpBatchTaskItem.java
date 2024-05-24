package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/16
 *
 * @author hry
 */
public class EditTerminalIpBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;


    private Boolean autoDhcp;

    private String mask;

    private String gateway;

    private String terminalId;

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getAutoDhcp() {
        return autoDhcp;
    }

    public void setAutoDhcp(Boolean autoDhcp) {
        this.autoDhcp = autoDhcp;
    }

    public String getMask() {
        return mask;
    }

    public void setMask(String mask) {
        this.mask = mask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
