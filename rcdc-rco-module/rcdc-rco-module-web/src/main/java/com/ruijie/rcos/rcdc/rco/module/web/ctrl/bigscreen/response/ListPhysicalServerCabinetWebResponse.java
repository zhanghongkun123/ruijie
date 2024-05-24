package com.ruijie.rcos.rcdc.rco.module.web.ctrl.bigscreen.response;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.bigscreen.PhysicalServerInfoDTO;

/**
 * Description: 获取物理服务器列表web响应
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/9/16 16:05
 *
 * @author zhangyichi
 */
public class ListPhysicalServerCabinetWebResponse {

    private PhysicalServerInfoDTO[] itemArr;

    public PhysicalServerInfoDTO[] getItemArr() {
        return itemArr;
    }

    public void setItemArr(PhysicalServerInfoDTO[] itemArr) {
        this.itemArr = itemArr;
    }
}
