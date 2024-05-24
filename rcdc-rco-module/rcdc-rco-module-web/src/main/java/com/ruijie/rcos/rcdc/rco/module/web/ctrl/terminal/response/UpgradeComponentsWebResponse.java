package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.UpgradeComponentVO;

/**
 * Description: 获取组件独立升级任务组件列表响应
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/3
 *
 * @author lyb
 */
public class UpgradeComponentsWebResponse {

    private UpgradeComponentVO[] itemArr;

    public UpgradeComponentVO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(UpgradeComponentVO[] itemArr) {
        this.itemArr = itemArr;
    }
}
