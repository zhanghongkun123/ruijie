package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 *
 * Description: 批量编辑云桌面标签
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年6月28日
 *
 * @author linrenjian
 */
public class EditVDIDeskRemarkBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    /**
     * 云桌面标签
     */
    private String remark;

    public EditVDIDeskRemarkBatchTaskItem() {
    }

    public EditVDIDeskRemarkBatchTaskItem(UUID itemId, String itemName, String remark) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.remark = remark;
    }

    public UUID getItemId() {
        return itemId;
    }

    /**
     *
     * @param itemId 云桌面UUID
     * @return EditVDIDeskRemarkBatchTaskItem
     */

    public EditVDIDeskRemarkBatchTaskItem setItemId(UUID itemId) {
        this.itemId = itemId;
        return this;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
