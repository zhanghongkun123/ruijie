package com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.response;

import com.ruijie.rcos.rcdc.rco.module.web.ctrl.terminal.vo.PackageComponentVO;

/**
 * Description: 获取组件独立升级包组件列表响应
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/11/23
 *
 * @author lyb
 */
public class PackComponentsWebResponse {

    private PackageComponentVO[] itemArr;

    public PackageComponentVO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(PackageComponentVO[] itemArr) {
        this.itemArr = itemArr;
    }
}
