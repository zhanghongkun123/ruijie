package com.ruijie.rcos.rcdc.rco.module.web.ctrl.user.bactchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 * Description: 批量修改IDV云桌面DNS批任务
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年1月16日
 *
 * @author heruiyuan1
 */
public class EditIDVDesktopDnsBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private Boolean autoDns;

    private String dns;

    private String dnsBack;

    @Override
    public UUID getItemID() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Boolean getAutoDns() {
        return autoDns;
    }

    public void setAutoDns(Boolean autoDns) {
        this.autoDns = autoDns;
    }

    public String getDns() {
        return dns;
    }

    public void setDns(String dns) {
        this.dns = dns;
    }

    public String getDnsBack() {
        return dnsBack;
    }

    public void setDnsBack(String dnsBack) {
        this.dnsBack = dnsBack;
    }
}
