package com.ruijie.rcos.rcdc.rco.module.web.ctrl.desktoppool.batchtask;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.desktoppool.CbbDesktopPoolDTO;
import com.ruijie.rcos.sk.base.batch.DefaultBatchTaskItem;

import java.util.UUID;

/**
 *
 * Description: 应用池策略批处理任务项
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年12月29日
 *
 * @author linke
 */
public class SyncConfigBatchTaskItem extends DefaultBatchTaskItem {

    private CbbDesktopPoolDTO desktopPool;

    public SyncConfigBatchTaskItem(UUID itemId, String itemName) {
        super(itemId, itemName);
    }

    public CbbDesktopPoolDTO getDesktopPool() {
        return desktopPool;
    }

    public void setDesktopPool(CbbDesktopPoolDTO desktopPool) {
        this.desktopPool = desktopPool;
    }
}
