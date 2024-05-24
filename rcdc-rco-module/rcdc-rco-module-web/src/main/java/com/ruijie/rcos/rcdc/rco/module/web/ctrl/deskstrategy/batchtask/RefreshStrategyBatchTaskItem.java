package com.ruijie.rcos.rcdc.rco.module.web.ctrl.deskstrategy.batchtask;

import com.ruijie.rcos.sk.base.batch.BatchTaskItem;

import java.util.UUID;

/**
 *
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019年1月28日
 *
 * @author nt
 */
public class RefreshStrategyBatchTaskItem implements BatchTaskItem {

    private UUID itemId;

    private String itemName;

    private UUID strategyId;

    public RefreshStrategyBatchTaskItem() { }

    public RefreshStrategyBatchTaskItem(UUID itemId, String itemName, UUID strategyId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.strategyId = strategyId;
    }

    @Override
    public UUID getItemID() {
        return itemId;
    }

    @Override
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param itemId 云桌面UUID
     * @return RefreshStrategyBatchTaskItem
     */
    public RefreshStrategyBatchTaskItem setItemId(UUID itemId) {
        this.itemId = itemId;
        return this;
    }

    /**
     *
     * @param itemName 子任务名
     * @return RefreshStrategyBatchTaskItem
     */
    public RefreshStrategyBatchTaskItem setItemName(String itemName) {
        this.itemName = itemName;
        return this;
    }

    public UUID getStrategyId() {
        return strategyId;
    }

    /**
     *
     * @param strategyId 策略ID
     * @return RefreshStrategyBatchTaskItem
     */
    public RefreshStrategyBatchTaskItem setStrategyId(UUID strategyId) {
        this.strategyId = strategyId;
        return this;
    }
}
